package com.spinglegames.procgen.map;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

public class HexMap extends InputAdapter implements ApplicationListener {

    private TiledMap map;
    private TiledMapRenderer renderer;
    private OrthographicCamera camera;
    private AssetManager assetManager;
    private Texture tiles;
    private Texture texture;
    private BitmapFont font;
    private SpriteBatch batch;
    private float cameraSpeed;

    private static final int TILE_W = 56;     // pixel width of hex bounding box
    private static final int TILE_H = 64;     // pixel height of hex bounding box
    private static final int HEX_SIDE = 24;   // pixel length of a hex side
    private static final int COLS = 60;       // map width in tiles
    private static final int ROWS = 40;       // map height in tiles

    @Override
    public void create () {
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
        p.put("staggerindex", "odd");  // or "even", whichever you prefer
        p.put("hexsidelength", 24);    // side length in pixels (tune to your art)
        p.put("tilewidth", TILE_W);    // bounding box width of one hex tile image
        p.put("tileheight", TILE_H);   // bounding box height of one hex tile image
        p.put("width", COLS);          // map size in tiles
        p.put("height", ROWS);

        {
            tiles = new Texture(Gdx.files.internal("textures/hextile.png"));
            TextureRegion[][] splitTiles = TextureRegion.split(tiles, TILE_W, TILE_H);
            MapLayers layers = map.getLayers();
            for (int l = 0; l < 20; l++) {
                TiledMapTileLayer layer = new TiledMapTileLayer(COLS, ROWS, TILE_W, TILE_H);
                for (int x = 0; x < 150; x++) {
                    for (int y = 0; y < 100; y++) {
                        int ty = (int)(Math.random() * splitTiles.length);
                        int tx = (int)(Math.random() * splitTiles[ty].length);
                        Cell cell = new Cell();
                        cell.setTile(new StaticTiledMapTile(splitTiles[ty][tx]));
                        layer.setCell(x, y, cell);
                    }
                }
                layers.add(layer);
            }
        }

        renderer = new HexagonalTiledMapRenderer(map);

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render () {
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {camera.zoom += 1f;}
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {camera.zoom -= 1f;}
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {cameraSpeed = 20f * camera.zoom;} else {cameraSpeed = 10f;}
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {camera.position.x += cameraSpeed;}
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {camera.position.x -= cameraSpeed;}
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {camera.position.y += cameraSpeed;}
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {camera.position.y -= cameraSpeed;}


        if (Gdx.input.getInputProcessor().scrolled(1,-1)) {
            camera.zoom += 1f;
        }

        ScreenUtils.clear(100f / 255f, 100f / 255f, 250f / 255f, 1f);
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
