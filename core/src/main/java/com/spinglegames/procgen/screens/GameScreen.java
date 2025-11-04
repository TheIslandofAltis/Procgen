package com.spinglegames.procgen.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.spinglegames.procgen.map.HexMap;

public class GameScreen extends ProcgenScreen {

    private Skin skin;
    private Stage stage;
    private Window pauseMenu;
    private Boolean paused = false;
    private HexMap hexMap;

    public GameScreen (Game game) {
        super(game);
    }


    @Override
    public void show() {

        hexMap = new HexMap(21);
        hexMap.create();

        stage = new Stage(new FitViewport(1920, 1080));
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        Table root = new Table(); root.setFillParent(true);
        stage.addActor(root);

        pauseMenu = createPauseMenu();


        root.add(pauseMenu).center();

        Gdx.input.setInputProcessor(stage);

    }

    public Window createPauseMenu() {

        Window window = new Window("Pause Menu", skin, "border");
//        window.debug();
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
            if (paused) pauseMenu.toFront();
        }

        ScreenUtils.clear(Color.GRAY);

        hexMap.render();

        stage.act(delta);
        stage.draw();
    }
}
