package com.github.matt.williams.android.ar;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.util.AttributeSet;

import com.github.matt.williams.android.gl.CameraTexture;
import com.github.matt.williams.android.gl.Projection;
import com.github.matt.williams.android.gl.ScreenTarget;
import com.github.matt.williams.android.gl.Texture;
import com.github.matt.williams.android.gl.Utils;

public class CameraView extends GLSurfaceView implements Renderer {
    private Camera mCamera;
    private final ScreenTarget mScreenTarget = new ScreenTarget();
    private Projection mProjection = new Projection();
    private Texture mTexture;
    private CameraBillboard mBillboard;

    public CameraView(Context context) {
        super(context);
        initialize();
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    private void initialize() {
        setEGLContextClientVersion(2);
        setEGLConfigChooser(8, 8, 8, 0, 16, 0);
        setRenderer(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mCamera = CameraUtils.getCamera(CameraInfo.CAMERA_FACING_BACK);
        if (mCamera != null) {
            CameraUtils.setMaxPreviewSize(mCamera);
            Parameters params = mCamera.getParameters();
            // TODO: Remove hack for Sony phones that seem to report nonsense viewing angles
            getProjection().setProjection(params.getHorizontalViewAngle() > 1.0f ? params.getHorizontalViewAngle() : 63.4f, params.getVerticalViewAngle() > 1.0f ? params.getVerticalViewAngle() : 49.6f);
        }
    }

    @Override
    public void onPause() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
        super.onPause();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        if (mCamera != null) {
            mTexture = new CameraTexture(mCamera);
            mCamera.startPreview();
        }
        mBillboard = new CameraBillboard(getContext().getResources(), mTexture);

        GLES20.glEnable(GLES20.GL_BLEND);
        Utils.checkErrors("glEnable(GLES20.GL_BLEND)");
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        Utils.checkErrors("glBlendFunc");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (mCamera != null) {
            mScreenTarget.set(width, height, CameraUtils.getAspectRatio(mCamera));
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        Utils.checkErrors("glDisable(GLES20.GL_DEPTH_TEST)");
        GLES20.glDepthMask(false);
        Utils.checkErrors("glDepthMask");

        mScreenTarget.renderTo();
        if (mTexture != null) {
            mBillboard.render(getProjection());
        }
    }

    public Projection getProjection() {
        return mProjection;
    }

    public void setProjection(Projection projection) {
        this.mProjection = projection;
    }

    public Texture getTexture() {
        return mTexture;
    }

    public ScreenTarget getScreenTarget() {
        return mScreenTarget;
    }

    public Camera getCamera() {
        return mCamera;
    }
}
