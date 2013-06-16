package com.github.matt.williams.android.ar;

import android.graphics.Rect;

import com.github.matt.williams.android.gl.Projection;

public interface Renderable {
    public void render(Projection projection, Projection camera, Rect rect);
}
