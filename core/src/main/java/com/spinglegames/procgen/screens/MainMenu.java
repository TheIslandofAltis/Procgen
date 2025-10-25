package com.spinglegames.procgen.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;


public class MainMenu extends ProcgenScreen {
    private Skin skin;
    private Stage stage;

    public MainMenu(Game game) {
        super(game);
    }

    @Override
    public void show () {
        stage = new Stage(new FitViewport(1920, 1080));
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        Table root = new Table(); root.setFillParent(true); stage.addActor(root);

        Window window = new Window("Main Menu", skin);
        window.setMovable(false);
        window.pad(16f);
        window.defaults().pad(8f).width(260).height(48);

        TextButton play    = new TextButton("Play", skin);
        TextButton options = new TextButton("Options", skin);
        TextButton exit    = new TextButton("Exit", skin);

        window.add(play).row();
        window.add(options).row();
        window.add(exit).row();
        window.row();
        window.add(new Label("v0.1.0", skin)).expandX().right().padTop(4f);


        play.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeListener.ChangeEvent event, final Actor actor) {
                play.setText("Clicked.");
                game.setScreen(new GameScreen(game));
            }
        });

        options.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeListener.ChangeEvent event, final Actor actor) {
                options.setText("Clicked.");
            }
        });

        exit.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeListener.ChangeEvent event, final Actor actor) {
                hide();
            }
        });

        window.pack();
        root.add(window).center();

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

    @Override public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
}
