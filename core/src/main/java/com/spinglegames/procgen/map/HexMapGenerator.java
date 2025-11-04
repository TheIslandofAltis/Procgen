package com.spinglegames.procgen.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.Arrays;
import java.util.Random;

public class HexMapGenerator {

    private enum Terrain {WATER, GRASS, DIRT}
    private int ROWS;
    private int COLS;
    float[] values;

    public HexMapGenerator(int rows, int cols) {
        ROWS = rows;
        COLS = cols;


        values = new float[COLS * ROWS];
    }

    public void generate() {

        Random random = new Random();


        for (int x=0; x<COLS; x++) {
            for (int y=0; y<ROWS; y++) {

                int min = -10;
                int max = 10;

//                if (y!=0) {
//                    float prev = getValue(x,y-1);
//                    if (prev < 0 ) {
//                        min = (int) prev;
//                    } else if (prev > 0) {
//                        max = (int) prev;
//                    }
//
//                }

                float randomNumber = random.nextInt(max - min + 1) + min;


                setValue(x,y,randomNumber);
            }
        }

    }

    void setValue(int x, int y, float v) { values[y * COLS + x] = v; }

    float getValue(int x, int y) { return values[y * COLS + x]; }


}
