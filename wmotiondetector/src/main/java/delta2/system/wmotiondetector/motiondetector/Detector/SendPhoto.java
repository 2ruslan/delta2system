package delta2.system.wmotiondetector.motiondetector.Detector;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import delta2.system.common.FileStructure;
import delta2.system.common.Helper;
import delta2.system.common.Log.L;
import delta2.system.wmotiondetector.motiondetector.Mediator.MediatorMD;

public class SendPhoto {
    private static final String _TAG = SendPhoto.class.getName();

    public static void Send(String msgId, Context contest, byte[] jpg, String info, int h, int w){

        try {
            L.log.info( _TAG, "Send photo - start");
            String fileName = FileStructure.getFilePath("pic", "jpg");
            FileOutputStream os = new FileOutputStream(fileName);
            os.write(jpg);
            os.close();
            L.log.info( _TAG, "Send photo - save data to file : " + fileName);

            MediatorMD.sendPhoto( msgId, fileName, Helper.getNowDT() + " " + info, h, w);

            L.log.info( _TAG, "Send photo end");

        } catch (Exception e) {
            Helper.Ex2Log(e);
        }
    }

}
