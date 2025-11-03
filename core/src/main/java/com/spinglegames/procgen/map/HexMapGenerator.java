package com.spinglegames.procgen.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class HexMapGenerator {

    private enum Terrain {WATER, GRASS, DIRT}
    private int rows;
    private int cols;

    Texture dirt = new Texture(Gdx.files.internal("textures/hextile.png"));
    Texture water = new Texture(Gdx.files.internal("textures/water.png"));
    Texture grass = new Texture(Gdx.files.internal("textures/grass.png"));

    Texture[] textures = {dirt, water,grass};

    public HexMapGenerator(int rows, int cols) {
        rows = this.rows;
        cols = this.cols;
    }

    public void generate() {

    }
}
