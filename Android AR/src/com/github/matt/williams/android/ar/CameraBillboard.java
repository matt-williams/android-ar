package com.github.matt.williams.android.ar;

import android.content.res.Resources;
import android.graphics.Rect;
import android.opengl.GLES20;

import com.github.matt.williams.android.gl.FragmentShader;
import com.github.matt.williams.android.gl.Program;
import com.github.matt.williams.android.gl.Projection;
import com.github.matt.williams.android.gl.Texture;
import com.github.matt.williams.android.gl.Utils;
import com.github.matt.williams.android.gl.VertexShader;

public class CameraBillboard implements Renderable {
    public final Program mProgram;
    public final Texture mTexture;
    public static final float[] PROJECTED_VERTICES = new float[] {-1, -1, 1, -1, -1, 1, 1, 1};

    public CameraBillboard(Program program, Texture texture) {
        mProgram = program;
        mTexture = texture;
    }

    public CameraBillboard(Resources resources, Texture texture) {
        this(new Program(new VertexShader(resources.getString(R.string.cameraBillboardVertexShader)),
                         new FragmentShader(resources.getString(R.string.cameraBillboardFragmentShader))),
             texture);
    }

    // Camera is the projection of the camera itself (to which the texture is applied) and projection is the projection for rendering.
    public static void render(Program program, Texture texture, Projection camera, Projection projection, Rect rect) {
        program.setUniform("textureMatrix", texture.getTransformMatrix());
        program.setUniform("projection", projection.getViewMatrix());
        final float[] mVertices = new float[12];
        camera.inverseView(mVertices, 0, PROJECTED_VERTICES, 0, PROJECTED_VERTICES.length / 2);
        program.setVertexAttrib("pos", mVertices, 3);
        program.setVertexAttrib("uv", new float[] {0, 0, 1, 0, 0, 1, 1, 1}, 2);
        program.setUniform("texture", 0);
        program.use();
        texture.use(GLES20.GL_TEXTURE0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        Utils.checkErrors("glDrawArrays");
    }

    @Override
    public void render(Projection projection, Projection camera, Rect rect) {
        render(mProgram, mTexture, projection, camera, rect);
    }
}
