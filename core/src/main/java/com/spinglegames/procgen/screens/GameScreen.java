package com.spinglegames.procgen.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen extends ProcgenScreen {

    private Skin skin;
    private Stage stage;
    private Window pauseMenu;
    private Boolean paused = false;

    public GameScreen (Game game) {
        super(game);
    }


    @Override
    public void show() {
        stage = new Stage(new FitViewport(1920, 1080));
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        Table root = new Table(); root.setFillParent(true);
        stage.addActor(root);

        pauseMenu = createPauseMenu();


        root.add(pauseMenu).center();

        Gdx.input.setInputProcessor(stage);

    }

    public void createMap() {

        Pixmap pixmap = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fillCircle(9,9,5);


        Texture solid = new Texture(pixmap);
        TextureRegion region = new TextureRegion(solid);




        TiledMap map = new TiledMap();
        MapLayers layers = map.getLayers();

        TiledMapTileLayer layer1 = new TiledMapTileLayer(400, 400, 50,50);
        Cell cell = new Cell();

        cell.setTile(new StaticTiledMapTile(region));








        Polygon hexagon = new Polygon(new float[] {1f,2f,3f,4f,5f,6f});

        PolygonMapObject tile = new PolygonMapObject(hexagon);
    }

    public Window createPauseMenu() {

        Window window = new Window("Pause Menu", skin, "border");
        window.debug();
        window.setMovable(false);
        window.pad(16f);
        window.defaults().pad(10f).width(260).height(48);

        TextButton menu = new TextButton("Menu", skin);
        TextButton exit    = new TextButton("Exit", skin);

        window.add(menu).row();
        window.add(exit).row();
        window.row();
        window.add(new Label("v0.1.0", skin)).expandX().right().padTop(4f);

        window.setVisible(false);

        window.pack();

        menu.addListener(click(() -> game.setScreen(new MainMenu(game))));
        exit.addListener(click(Gdx.app::exit));

        return window;
    }



    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            paused = !paused;
            pauseMenu.setVisible(paused);
        }


        ScreenUtils.clear(Color.GRAY);
        stage.act(delta);
        stage.draw();
    }
}
