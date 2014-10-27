package smartring.masterihm.enac.com.smartdring.service;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by David on 17/10/2014.
 * <p/>
 * Class used to access the sensors of the phone.
 */
public class SensorDetector implements SensorEventListener {

    private float[] accelerometerVector = new float[3];
    private float[] magneticVector = new float[3];
    private float[] resultMatrix = new float[9];
    private float[] computedValues = new float[3];
    private double xDegrees;
    private double yDegrees;

    private boolean isPhoneFlipped;

    private PhoneStateInterface mPhoneStateListener;

    private SensorManager mSensorManager;
    private Sensor acceleroSensor;
    private Sensor magneticSensor;

    public SensorDetector(Context context, PhoneStateInterface pListener) {
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        acceleroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mPhoneStateListener = pListener;
    }

    void startDetection() {
        mSensorManager.registerListener(this, acceleroSensor, 500);
        mSensorManager.registerListener(this, magneticSensor, 500);
    }

    void stopDetection() {
        mSensorManager.unregisterListener(this, acceleroSensor);
        mSensorManager.unregisterListener(this, magneticSensor);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerVector[0] = (accelerometerVector[0] * 2 + sensorEvent.values[0]) * 0.33334f;
            accelerometerVector[1] = (accelerometerVector[1] * 2 + sensorEvent.values[1]) * 0.33334f;
            accelerometerVector[2] = (accelerometerVector[2] * 2 + sensorEvent.values[2]) * 0.33334f;
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magneticVector[0] = (magneticVector[0] * 1 + sensorEvent.values[0]) * 0.5f;
            magneticVector[1] = (magneticVector[1] * 1 + sensorEvent.values[1]) * 0.5f;
            magneticVector[2] = (magneticVector[2] * 1 + sensorEvent.values[2]) * 0.5f;
        }

        SensorManager.getRotationMatrix(resultMatrix, null, accelerometerVector, magneticVector);
        SensorManager.getOrientation(resultMatrix, computedValues);

        xDegrees = Math.toDegrees(computedValues[1]);
        yDegrees = Math.toDegrees(computedValues[2]);

        if (isPhoneFlipped) {
            if (Math.abs(xDegrees) > 20 || Math.abs(yDegrees) < 160) {
                isPhoneFlipped = false;
                mPhoneStateListener.phoneFlippedStateChanged(false);
            }
        } else {
            if (Math.abs(xDegrees) < 5 && Math.abs(yDegrees) > 175) {
                isPhoneFlipped = true;
                mPhoneStateListener.phoneFlippedStateChanged(true);
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public interface PhoneStateInterface {
        void phoneFlippedStateChanged(boolean isPhoneFlipped);
    }
}
