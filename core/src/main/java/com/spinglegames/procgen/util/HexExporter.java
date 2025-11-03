package com.spinglegames.procgen.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.files.FileHandle;

public class HexExporter {

    // Save a pointy-topped hex, perfect (all sides equal), to hex.png with transparent background
    public static void saveHexPng(int sizePx, Color fill, Color outline, int outlinePx, String outPath) {
        // sizePx = canvas size (square). The hex will be centered and padded inside.
        FrameBuffer fbo = new FrameBuffer(Pixmap.Format.RGBA8888, sizePx, sizePx, false);
        ShapeRenderer shape = new ShapeRenderer();

        // Hex geometry
        float cx = sizePx / 2f;
        float cy = sizePx / 2f;

        // Choose radius R so the hex fits inside the square with a bit of padding
        float pad = Math.max(1, outlinePx);            // 1px padding minimum
        float R = (sizePx / 2f) - pad;                 // corner-to-center
        // For a pointy-topped regular hex, height = √3 * R. If that exceeds the square, clamp R:
        R = Math.min(R, (float)((sizePx - 2*pad) / Math.sqrt(3.0)));

        float[] pts = hexCornersPointy(cx, cy, R);

        fbo.begin();
        // Transparent clear
        Gdx.gl.glViewport(0, 0, sizePx, sizePx);
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Ortho 2D matching pixel space
        shape.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, sizePx, sizePx));

        // Fill (triangle fan)
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(fill);
        for (int i = 0; i < 6; i++) {
            int j = (i + 1) % 6;
            shape.triangle(cx, cy, pts[2*i], pts[2*i+1], pts[2*j], pts[2*j+1]);
        }
        shape.end();

        // Outline
        if (outlinePx > 0 && outline.a > 0f) {
            shape.begin(ShapeRenderer.ShapeType.Line);
            shape.setColor(outline);
            Gdx.gl.glLineWidth(outlinePx);
            for (int i = 0; i < 6; i++) {
                int j = (i + 1) % 6;
                shape.line(pts[2*i], pts[2*i+1], pts[2*j], pts[2*j+1]);
            }
            shape.end();
            Gdx.gl.glLineWidth(1f);
        }

        // Read back pixels & save
        Pixmap pm = Pixmap.createFromFrameBuffer(0, 0, sizePx, sizePx);
        fbo.end();

        FileHandle out = Gdx.files.local(outPath);         // or internal/external as you prefer
        PixmapIO.writePNG(out, pm);

        // Cleanup
        pm.dispose();
        shape.dispose();
        fbo.dispose();
    }

    // 6 corners for a pointy-topped hex centered at (cx,cy)
    private static float[] hexCornersPointy(float cx, float cy, float R) {
        float[] p = new float[12];
        // angles: 30°, 90°, 150°, 210°, 270°, 330°
        for (int i = 0; i < 6; i++) {
            double a = Math.toRadians(60 * i - 30);
            p[2*i]   = (float)(cx + R * Math.cos(a));
            p[2*i+1] = (float)(cy + R * Math.sin(a));
        }
        return p;
    }
}

