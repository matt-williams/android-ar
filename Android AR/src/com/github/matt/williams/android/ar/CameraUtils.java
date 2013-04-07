package com.github.matt.williams.android.ar;

import java.util.List;

import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;

import com.github.matt.williams.android.gl.Projection;

public final class CameraUtils {
    private static final float MINIMUM_HORIZONTAL_VIEW_ANGLE = 10.0f;
    private static final float MAXIMUM_HORIZONTAL_VIEW_ANGLE = 170.0f;
    private static final float DEFAULT_HORIZONTAL_VIEW_ANGLE = 60.0f;
    private static final float MINIMUM_VERTICAL_VIEW_ANGLE = 10.0f;
    private static final float MAXIMUM_VERTICAL_VIEW_ANGLE = 170.0f;
    private static final float DEFAULT_VERTICAL_VIEW_ANGLE = 48.0f;

    private CameraUtils() {}

    public static Camera getCamera(int facing) {
        CameraInfo info = new CameraInfo();
        for (int ii = 0; ii < Camera.getNumberOfCameras(); ii++) {
            Camera.getCameraInfo(ii, info);
            if (info.facing == facing) {
                return Camera.open(ii);
            }
        }
        return null;
    }

    public static void setMaxPreviewSize(Camera camera) {
        Parameters params = camera.getParameters();
        List<Size> supportedSizes = params.getSupportedPreviewSizes();
        Size previewSize = params.getPreviewSize();
        for (Size size : supportedSizes) {
            if (size.width * size.height > previewSize.width * previewSize.height) {
                previewSize = size;
            }
        }
        params.setPreviewSize(previewSize.width, previewSize.height);
        camera.setParameters(params);
    }

    public static double getPixelAspectRatio(Camera camera) {
        Size previewSize = camera.getParameters().getPreviewSize();
        return (double)previewSize.width / previewSize.height;
    }

    public static double getViewAngleAspectRatio(Camera camera) {
        return Math.tan(getSaneHorizontalViewAngle(camera) / 2 * Math.PI / 180) / Math.tan(getSaneVerticalViewAngle(camera) / 2 * Math.PI / 180);
    }

    public static void setProjection(Projection projection, Camera camera) {
        projection.setProjection(getSaneHorizontalViewAngle(camera), (float)(getSaneHorizontalViewAngle(camera) / getPixelAspectRatio(camera)));
    }

    public static float getSaneHorizontalViewAngle(Camera camera) {
        float horizViewAngle = camera.getParameters().getHorizontalViewAngle();
        return ((horizViewAngle >= MINIMUM_HORIZONTAL_VIEW_ANGLE) && (horizViewAngle <= MAXIMUM_HORIZONTAL_VIEW_ANGLE)) ? horizViewAngle : DEFAULT_HORIZONTAL_VIEW_ANGLE;
    }

    public static float getSaneVerticalViewAngle(Camera camera) {
        float vertViewAngle = camera.getParameters().getVerticalViewAngle();
        return ((vertViewAngle >= MINIMUM_VERTICAL_VIEW_ANGLE) && (vertViewAngle <= MAXIMUM_VERTICAL_VIEW_ANGLE)) ? vertViewAngle : DEFAULT_VERTICAL_VIEW_ANGLE;
    }
}
