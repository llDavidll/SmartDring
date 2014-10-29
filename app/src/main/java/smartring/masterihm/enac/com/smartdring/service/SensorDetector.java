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

    // At which rate do we update the sensor data (ms)
    private static final int SENSOR_REFRESH_DELAY = 600;

    // Is the sensor detector running.
    private boolean isRunning;

    // Is the phone flipped.
    private boolean isPhoneFlipped;

    // Listener receiving sensor updates.
    private ContextChangeDetector.ContextChangeInterface mPhoneStateListener;

    // Sensors and sensor manager.
    private SensorManager mSensorManager;
    private Sensor acceleroSensor;
    private Sensor magneticSensor;

    // Objects used to retrieve the sensor values
    private float[] accelerometerVector = new float[3];
    private float[] magneticVector = new float[3];
    private float[] resultMatrix = new float[9];
    private float[] computedValues = new float[3];
    // Fields kept local to avoid allocation durent onSensorChanged
    @SuppressWarnings("FieldCanBeLocal")
    private double xDegrees;
    @SuppressWarnings("FieldCanBeLocal")
    private double yDegrees;

    /**
     * Create a new sensor detector.
     *
     * @param context   the context requesting the sensor detector.
     * @param pListener the listener requesting sensor updates.
     */
    public SensorDetector(Context context, ContextChangeDetector.ContextChangeInterface pListener) {
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        acceleroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mPhoneStateListener = pListener;
    }

    /**
     * Start the detection of the phone position.
     */
    void startDetection() {
        if (!isRunning) {
            mSensorManager.registerListener(this, acceleroSensor, SENSOR_REFRESH_DELAY);
            mSensorManager.registerListener(this, magneticSensor, SENSOR_REFRESH_DELAY);
            isRunning = true;
        }
    }

    /**
     * Stop the detection of the phone position.
     */
    void stopDetection() {
        if (isRunning) {
            mSensorManager.unregisterListener(this, acceleroSensor);
            mSensorManager.unregisterListener(this, magneticSensor);
            isRunning = false;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // Retrieve and filter sensor data.
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerVector[0] = (accelerometerVector[0] * 2 + sensorEvent.values[0]) * 0.33334f;
            accelerometerVector[1] = (accelerometerVector[1] * 2 + sensorEvent.values[1]) * 0.33334f;
            accelerometerVector[2] = (accelerometerVector[2] * 2 + sensorEvent.values[2]) * 0.33334f;
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magneticVector[0] = (magneticVector[0] * 1 + sensorEvent.values[0]) * 0.5f;
            magneticVector[1] = (magneticVector[1] * 1 + sensorEvent.values[1]) * 0.5f;
            magneticVector[2] = (magneticVector[2] * 1 + sensorEvent.values[2]) * 0.5f;
        }
        // Compute the sensor data.
        SensorManager.getRotationMatrix(resultMatrix, null, accelerometerVector, magneticVector);
        SensorManager.getOrientation(resultMatrix, computedValues);

        // Get the phone position on the X and Y axis in degrees.
        xDegrees = Math.toDegrees(computedValues[1]);
        yDegrees = Math.toDegrees(computedValues[2]);

        if (isPhoneFlipped) {
            // The phone was flipped before.
            if (Math.abs(xDegrees) > 20 || Math.abs(yDegrees) < 160) {
                // The phone is now "unflipped"
                isPhoneFlipped = false;
                mPhoneStateListener.phoneFlippedStateChanged(false);
            }
        } else {
            // The phone was not flipped before.
            if (Math.abs(xDegrees) < 5 && Math.abs(yDegrees) > 175) {
                // The phone is now flipped.
                isPhoneFlipped = true;
                mPhoneStateListener.phoneFlippedStateChanged(true);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}
