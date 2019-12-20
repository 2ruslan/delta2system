package delta2.system.wmotiondetector.motiondetector.Detector;

import android.graphics.Color;

import delta2.system.common.Helper;
import delta2.system.wmotiondetector.motiondetector.Mediator.MediatorMD;

public class MD {


    private static final int _NO_USE = -1;
    private static final int _OUT_W = 40;
    private static final int _OUT_H = 40;
    private static final int _OUT_CNT= _OUT_H * _OUT_W;

    private static final int _AVG_WHITE = 0;
    private static final int _AVG_BLACK = 1;

    private static final int _COLOR_WHITE = Color.WHITE;
    private static final int _COLOR_BLACK = Color.DKGRAY;
    private static final int _COLOR_NO_USE = Color.YELLOW;
    private static final int _COLOR_DETECT = Color.RED;


    private static  long [] mNewHash;

    private static  long[] mPrevHash;

    private static long[] mArr0;

    private static int [] mapData2Hash;
    private static long [] mCounts;




    // int[] m_argb;

    private int mDelta;
    private boolean mIsNoFirst;

    MD(){
        mDelta = 0;
        mIsNoFirst = false;

        mNewHash = new long[_OUT_CNT];
        mPrevHash = new long[_OUT_CNT];
        mCounts = new long[_OUT_CNT];

        mArr0 = new long[_OUT_CNT];
        for (int i =0; i < _OUT_CNT; i++)
            mArr0[i]= 0;
    }

    public void onDestroy(){
        mNewHash = null;
        mPrevHash = null;
        mCounts = null;
    }

    int GetDelta(){
        return mDelta;
    }

    //region hash
    private static int prevH = -1;
    private static int prevW = -1;



    private int[] getMapHW(int mapQnt, int qntPoints, boolean isMultiple){

        float [] mRuller = new float[mapQnt];
        int [] mMap = new int[qntPoints];

        float step = (float)qntPoints / mapQnt;
        for (int i = 0; i < mapQnt; i++)
            mRuller[i] = (float)i * step;


        int mapPos = 0;

        for(int i=0; i<qntPoints; i++){

            mapPos = mapQnt - 1;
            for (int m = mapQnt-1; m > -1 ; m--){
                if (mRuller[m] <= i) {
                    mapPos = m;
                    break;
                }
            }

            mMap[i]=  isMultiple ? mapPos * mapQnt : mapPos;

        }

        return mMap;
    }

    private void initHashMeas(int w, int h){

      //  Helper.Log("initHashMeas", "w = " + w + " ; h = "+  h, true );

        int[] wConv = getMapHW(_OUT_W, w, false);
        int[] hConv = getMapHW(_OUT_H, h, true);

        int hw = h * w;
        mapData2Hash = new int[hw];

        int i = 0;
        for (int hh = 0; hh < h; hh++)
            for (int ww = 0; ww < w; ww++)
                mapData2Hash[i++] = hConv[hh] + wConv[ww];

    }

    public long[] getHash(byte[] yuv,  int width, int height){
        try {
            if(prevH != height || prevW != width)
                initHashMeas(width, height);

            prevH = height;
            prevW = width;

            System.arraycopy(mArr0,0,mNewHash, 0, _OUT_CNT);
            System.arraycopy(mArr0,0,mCounts, 0, _OUT_CNT);


            int qnt = width *height;

            for (int i = 0; i < qnt; i++) {
                int pos = mapData2Hash[i];
                mNewHash[pos] += (0xff & yuv[i]);
                mCounts[pos]++;
             }

            long sum = 0;
            int y;
            for (int p = 0; p < _OUT_CNT; p++) {

                if (mCounts[p] > 0)
                    y = (int) (mNewHash[p] / mCounts[p]);
                else
                    y = 0;

                sum += y;
                mNewHash[p] = y;
            }

            float avg = ((float)sum / (float) _OUT_CNT);

            for (int p = 0; p < _OUT_CNT; p++)
                if (mNewHash[p] > avg - 3 && mNewHash[p] < avg + 3)
                    mNewHash[p] = _NO_USE;
                else if (mNewHash[p] > avg)
                    mNewHash[p] = _AVG_WHITE;
                else
                    mNewHash[p] = _AVG_BLACK;


        }catch (Exception ex)
        {
            Helper.Ex2Log(ex);
        }
        return mNewHash;
    }

    //endregion hash

    int chkImg(byte[] data, int w, int h) {

        long[] ch;

        ch = getHash(data, w, h);

        int delta = 0;
        for (int i = 0; i < _OUT_CNT; i++){
            if (mPrevHash[i] != ch[i]  &&  ch[i] != _NO_USE && mPrevHash[i] != _NO_USE  && mIsNoFirst)
                delta++;
        }

        System.arraycopy(ch,0,mPrevHash, 0, _OUT_CNT);

        mIsNoFirst = true;
        mDelta = delta;

        MediatorMD.SetInfo("delta = " + String.valueOf(delta));

        return delta;
    }



}
