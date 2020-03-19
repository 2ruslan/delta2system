package delta2.system.wcaralarm.Accelerometer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import rock.delta2.carwatchdog.Helper;

public class Accelerometer  implements SensorEventListener {


    private SensorManager mSensorManager;

    public Accelerometer(Context context){
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST );
    }

    public void stop(){

        mSensorManager.unregisterListener (this);
        mSensorManager= null;
    }

    int pos = 0;
    int arrLen = 20;

    boolean isFull = false;
    boolean resetRes = true;

    float mX[] = new float[arrLen];
    float mY[] = new float[arrLen];
    float mZ[] = new float[arrLen];

    float mXDelta = 0;
    float mYDelta = 0;
    float mZDelta = 0;

    float mXAvg = 0;
    float mYAvg = 0;
    float mZAvg = 0;

    double maxAcceleration = 0;
    double kk = 1.2;


    @Override
    public void onSensorChanged(SensorEvent event) {

        try {


            mX[pos] = event.values[0];
            mY[pos] = event.values[1];
            mZ[pos] = event.values[2];

            double crrAcc = Math.sqrt(mX[pos] * mX[pos] + mY[pos] * mY[pos] + mZ[pos] * mZ[pos]) - SensorManager.GRAVITY_EARTH;

            if(resetRes)
                maxAcceleration = 0;

            if(maxAcceleration > crrAcc)
                return;

            pos++;

            if (pos == arrLen){
                pos = 0;
                isFull = true;
            }

            if(isFull) {
                mXAvg = mYAvg = mZAvg = 0;
                for (int i = 0; i < arrLen; i++){
                    mXAvg += mX[i];
                    mYAvg += mY[i];
                    mZAvg += mZ[i];
                }

                mXAvg /=  arrLen;
                mYAvg /=  arrLen;
                mZAvg /=  arrLen;

                for (int i = 0; i < arrLen; i++){
                    mXDelta += delta(mX[i], mXAvg);
                    mYDelta += delta(mY[i], mYAvg);
                    mZDelta += delta(mZ[i], mZAvg);
                }

                mXDelta /=  arrLen;
                mYDelta /=  arrLen;
                mZDelta /=  arrLen;

                if( delta(mX[pos], mXAvg) > mXDelta*kk
                        || delta(mY[pos], mYAvg) > mYDelta*kk
                        || delta(mZ[pos], mZAvg) > mZDelta*kk
                )
                    maxAcceleration = crrAcc;
                else
                    maxAcceleration = 0;

            }


        }catch (Exception ex) {
            Helper.Ex2Log(ex);
        }

    }

    private float delta(float a, float b){
        return Math.abs( a > b? a - b : b - a);
    }

    public Accelerometer_Result getResult(){

        Accelerometer_Result res = new Accelerometer_Result();

        res.acceleration = (float) maxAcceleration;
        resetRes = true;

        //Log.d("acc", String.valueOf(res.acceleration));

        return res;
    }

    private float getCorrect(float prev, float crr, float next, float delta){
        float dP = Math.abs(crr - prev);
        float dN = Math.abs(next - crr);

        if(
                dP < delta && dN < delta ||
                        dP >= delta && dN >= delta && (dP > 0 && dN > 0 || dP < 0 && dN < 0)
        )
            return crr;

        return (prev + next) / 2;
    }




    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



}
