import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import java.lang.*;

public class MotionManager implements SensorEventListener {

    private final SensorManager mSensorManager;

    private final Sensor mAccelerometer;
    private final Sensor mGyroscope;

    private float mAccelerationX = 0;
    private float mAccelerationY = 0;
    private float mAccelerationZ = 0;

    private float mRotationX = 0;
    private float mRotationY = 0;
    private float mRotationZ = 0;

    MotionManager(SensorManager sensorManager)
    {
        mSensorManager = sensorManager;

        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        if (mAccelerometer != null) {
            Log.d("MonitorManager", "Detected " + mAccelerometer.getName());
        }

        if (mGyroscope != null) {
            Log.d("MonitorManager", "Detected " + mGyroscope.getName());
        }
    }

    public final void start() {
        if (mAccelerometer != null) {
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mGyroscope != null) {
            mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public final void stop() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public final void onSensorChanged(SensorEvent event) {

        final float currentX = event.values[0];
        final float currentY = event.values[1];
        final float currentZ = event.values[2];

        float previousX = 0.0F;
        float previousY = 0.0F;
        float previousZ = 0.0F;

        float threshold = 0.0F;

        boolean isUpdatedX = false;
        boolean isUpdatedY = false;
        boolean isUpdatedZ = false;

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            previousX = mAccelerationX;
            previousY = mAccelerationY;
            previousZ = mAccelerationZ;
            threshold = 0.25F;
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            previousX = mRotationX;
            previousY = mRotationY;
            previousZ = mRotationZ;
            threshold = 0.5F;
        }

        if (Math.abs(previousX - currentX) >= threshold) {
            isUpdatedX = true;
        }

        if (Math.abs(previousY - currentY) >= threshold) {
            isUpdatedY = true;
        }

        if (Math.abs(previousZ - currentZ) >= threshold) {
            isUpdatedZ = true;
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mAccelerationX = isUpdatedX ? currentX : mAccelerationX;
            mAccelerationY = isUpdatedY ? currentY : mAccelerationY;
            mAccelerationZ = isUpdatedZ ? currentZ : mAccelerationZ;
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            mRotationX = isUpdatedX ? currentX : mRotationX;
            mRotationY = isUpdatedY ? currentY : mRotationY;
            mRotationZ = isUpdatedZ ? currentZ : mRotationZ;
        }

        if (isUpdatedX || isUpdatedY || isUpdatedZ) {
            Log.d("MonitorManager", "X = " + Float.toString(mAccelerationX));
            Log.d("MonitorManager", "Y = " + Float.toString(mAccelerationY));
            Log.d("MonitorManager", "Z = " + Float.toString(mAccelerationZ));
        }
    }
}
