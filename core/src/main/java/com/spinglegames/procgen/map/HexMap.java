package com.spinglegames.procgen.map;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.ScreenUtils;
import com.spinglegames.procgen.util.HexExporter;

import java.util.Random;

public class HexMap extends InputAdapter implements ApplicationListener {

    private TiledMap map;
    private TiledMapRenderer renderer;
    private OrthographicCamera camera;
    private BitmapFont font;
    private SpriteBatch batch;

    private static final int TILE_W = 220;     // pixel width of hex bounding box
    private static final int TILE_H = 254;     // pixel height of hex bounding box
    private static final int HEX_SIDE = 110;   // pixel length of a hex side
    private static final int COLS = 240;       // map width in tiles
    private static final int ROWS = 160;       // map height in tiles

    @Override
    public void create () {

//        HexExporter.saveHexPng(
//            256,                      // canvas size (PNG will be 256x256)
//            Color.FOREST,   // fill
//            new Color(0f,0f,0f,0f),            // outline
//            2,                         // outline width in px
//            "hex.png"                  // output path (assets/local)
//        );

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, (w / h) * 1080, 1080);
        camera.update();

        font = new BitmapFont();
        batch = new SpriteBatch();


        map = new TiledMap();
        MapProperties p = map.getProperties();
        p.put("orientation", "hexagonal");
        p.put("staggeraxis", "y");     // <-- pointy-topped (use "x" for flat-topped)
        p.put("staggerindex", "even");  // or "even", whichever you prefer
        p.put("hexsidelength", HEX_SIDE);    // side length in pixels (tune to your art)
        p.put("tilewidth", TILE_W);    // bounding box width of one hex tile image
        p.put("tileheight", TILE_H);   // bounding box height of one hex tile image
        p.put("width", COLS);          // map size in tiles
        p.put("height", ROWS);

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("textures/textures.atlas"));
        StaticTiledMapTile WATER = new StaticTiledMapTile(atlas.findRegion("water"));
        StaticTiledMapTile GRASS = new StaticTiledMapTile(atlas.findRegion("grass"));
        StaticTiledMapTile DIRT  = new StaticTiledMapTile(atlas.findRegion("hextile"));

        StaticTiledMapTile BIGWATER = new StaticTiledMapTile(atlas.findRegion("bigwater"));
        StaticTiledMapTile BIGGRASS = new StaticTiledMapTile(atlas.findRegion("biggrass"));
        StaticTiledMapTile BIGDIRT = new StaticTiledMapTile(atlas.findRegion("bigdirt"));

        StaticTiledMapTile[] textures = {BIGWATER,BIGGRASS};

        Random rnd = new Random();

        TiledMapTileLayer layer = new TiledMapTileLayer(COLS, ROWS, TILE_W, TILE_H);
        MapLayers layers = map.getLayers();


        for (int x = 0; x < COLS+1; x++) {
            for (int y = 0; y < ROWS+1; y++) {
                Cell cell = new Cell();

                if (y==0 & x==0) {
                    cell.setTile(BIGDIRT);
                } else {
                    cell.setTile(textures[rnd.nextInt(textures.length)]);
                }
                layer.setCell(x, y, cell);
            }
        }

        layers.add(layer);

        renderer = new HexagonalTiledMapRenderer(map);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render () {
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.Q)) camera.zoom = Math.max(0.2f, camera.zoom + 0.1f);
        if (Gdx.input.isKeyPressed(Input.Keys.E)) camera.zoom -= 0.1f;

        float speed = (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? 2f : 1f) * 600f * delta * camera.zoom;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) camera.position.x += speed;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) camera.position.x -= speed;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) camera.position.y += speed;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) camera.position.y -= speed;

        ScreenUtils.clear(100/255f, 100/255f, 250/255f, 1f);
        camera.update();
        renderer.setView(camera);
        renderer.render();

        batch.begin();
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
        batch.end();
    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
