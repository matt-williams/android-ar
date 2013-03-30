package com.github.matt.williams.android.ar;

import android.content.res.Resources;
import android.opengl.GLES20;

import com.github.matt.williams.android.ar.R;
import com.github.matt.williams.android.gl.FragmentShader;
import com.github.matt.williams.android.gl.Program;
import com.github.matt.williams.android.gl.Projection;
import com.github.matt.williams.android.gl.Texture;
import com.github.matt.williams.android.gl.Utils;
import com.github.matt.williams.android.gl.VertexShader;

public class CameraBillboard {
    private final Program mProgram;
    private final Texture mTexture;
    private final float[] PROJECTED_VERTICES = new float[] {-1, -1, 1, -1, -1, 1, 1, 1};
    private final float[] mVertices = new float[12];

    public CameraBillboard(Resources resources, Texture texture) {
        mTexture = texture;
        mProgram = new Program(new VertexShader(resources.getString(R.string.cameraBillboardVertexShader)),
                               new FragmentShader(resources.getString(R.string.cameraBillboardFragmentShader)));
    }

    // Camera is the projection of the camera itself (to which the texture is applied) and projection is the projection for rendering.
    public void render(Projection camera, Projection projection) {
        mProgram.setUniform("textureMatrix", mTexture.getTransformMatrix());
        mProgram.setUniform("projection", projection.getViewMatrix());
        camera.inverseView(mVertices, 0, PROJECTED_VERTICES, 0, PROJECTED_VERTICES.length / 2);
        mProgram.setVertexAttrib("pos", mVertices, 3);
        mProgram.setVertexAttrib("uv", new float[] {0, 0, 1, 0, 0, 1, 1, 1}, 2);
        mProgram.setUniform("texture", 0);
        mProgram.use();
        mTexture.use(GLES20.GL_TEXTURE0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        Utils.checkErrors("glDrawArrays");
    }

    public void render(Projection projection) {
        render(projection, projection);
    }
}
