package com.github.matt.williams.android.ar;

import java.util.List;

import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;

public final class CameraUtils {

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

    public static double getAspectRatio(Camera camera) {
        Parameters params = camera.getParameters();
        Size previewSize = params.getPreviewSize();
        return (double)previewSize.width / previewSize.height;
    }
}
