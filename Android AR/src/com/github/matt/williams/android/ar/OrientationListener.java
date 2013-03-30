package com.github.matt.williams.android.ar;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.github.matt.williams.android.gl.Projection;

public class OrientationListener extends Projection implements SensorEventListener {
    private final SensorManager mSensorManager;

    public OrientationListener(SensorManager sensorManager) {
        mSensorManager = sensorManager;
    }

    public void onResume() {
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void onPause() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Only process the event if it is reliable and the right type
        if ((event.accuracy != SensorManager.SENSOR_STATUS_UNRELIABLE) &&
            (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR)) {
            float[] matrix = new float[16];
            SensorManager.getRotationMatrixFromVector(matrix, event.values);
            this.setRotationMatrix(matrix);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}