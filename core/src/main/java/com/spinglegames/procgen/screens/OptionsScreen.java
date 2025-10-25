package com.spinglegames.procgen.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class OptionsScreen extends ProcgenScreen {
    private Skin skin;
    private Stage stage;

    public OptionsScreen(Game game) {
        super(game);
    }

    @Override
    public void show () {
        stage = new Stage(new FitViewport(1920, 1080));
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        Table root = new Table(); root.setFillParent(true);
        stage.addActor(root);

        root.setDebug(true);

        Window window = new Window("Options", skin, "border");
        window.setMovable(false);
        window.pad(16f);
        window.defaults().pad(8f).width(130).height(24);


        String[] displayOpts = {"Windowed","Fullscreen"};

        Table selectSection = new Table();

        Label displayLbl = new Label("Display:",skin);
        SelectBox<String> displaySlct = new SelectBox<>(skin);
        displaySlct.setItems(displayOpts);


        TextButton apply = new TextButton("Apply", skin);
        TextButton back = new TextButton("Back", skin);


        window.add(displayLbl);
        window.add(displaySlct);
        window.row();
        window.add(back);
        window.add(apply);

        window.pack();
        root.add(window).center();


        apply.addListener(click(() -> System.out.println("Settings applied")));
        back.addListener(click(() -> game.setScreen(new MainMenu(game))));

        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.GRAY);
        stage.act(delta);
        stage.draw();
    }

    @Override public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    private static InputListener click(Runnable r) {
        return new InputListener() {
            @Override public boolean touchDown(InputEvent e, float x, float y, int p, int b) { return true; }
            @Override public void touchUp(InputEvent e, float x, float y, int p, int b) { r.run(); }
        };
    }
}
