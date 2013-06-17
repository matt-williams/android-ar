package com.github.matt.williams.android.ar;

import android.content.Context;
import android.hardware.SensorManager;
import android.util.AttributeSet;

import com.github.matt.williams.android.gl.Projection;

public class CameraOrientationView extends CameraView {
    OrientationListener mOrientationListener;

    public CameraOrientationView(Context context) {
        super(context);
        Projection projection = new OrientationListener((SensorManager)context.getSystemService(Context.SENSOR_SERVICE));
        setProjection(projection);
        setCameraProjection(projection);
    }

    public CameraOrientationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Projection projection = new OrientationListener((SensorManager)context.getSystemService(Context.SENSOR_SERVICE));
        setProjection(projection);
        setCameraProjection(projection);
    }

    public CameraOrientationView(Context context, OrientationListener orientationListener) {
        super(context);
        setProjection(orientationListener);
        setCameraProjection(orientationListener);
    }

    public CameraOrientationView(Context context, AttributeSet attrs, OrientationListener orientationListener) {
        super(context, attrs);
        setProjection(orientationListener);
        setCameraProjection(orientationListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mOrientationListener != null) {
            mOrientationListener.onResume();
        }
    }

    @Override
    public void onPause() {
        if (mOrientationListener != null) {
            mOrientationListener.onPause();
        }
        super.onPause();
    }

    @Override
    public void setProjection(Projection projection) {
        if (projection instanceof OrientationListener) {
            this.mOrientationListener = (OrientationListener)projection;
        } else {
            this.mOrientationListener = null;
        }
        super.setProjection(projection);
    }
}
