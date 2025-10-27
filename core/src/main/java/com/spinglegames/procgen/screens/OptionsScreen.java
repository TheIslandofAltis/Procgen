package com.spinglegames.procgen.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Objects;

public class OptionsScreen extends ProcgenScreen {
    private Skin skin;
    private Stage stage;

    public OptionsScreen(Game game) {
        super(game);
    }

    private static final class DisplayResult {
        private final SelectBox<String> select;
        private final Label label;

        public DisplayResult(SelectBox<String> select, Label label) {
            this.select = select;
            this.label = label;
        }

        public SelectBox<String> getSelect() {
            return select;
        }

        public Label getLabel() {
            return label;
        }
    }


    @Override
    public void show () {
        stage = new Stage(new FitViewport(1920, 1080));
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        Table root = new Table(); root.setFillParent(true);
        stage.addActor(root);

//        root.setDebug(true);

        Window window = new Window("Options", skin, "border");
        window.setMovable(false);
        window.pad(16f);
        window.defaults().pad(8f).width(130).height(24);

        DisplayResult display = createDisplaySlct();

        SelectBox<String> displaySlct = display.getSelect();
        Label displayLbl = display.getLabel();

        TextButton apply = new TextButton("Apply", skin);
        TextButton back = new TextButton("Back", skin);


        window.add(displayLbl);
        window.add(displaySlct);
        window.row();
        window.add(back);
//        window.add(apply);

        window.pack();
        root.add(window).center();


        displaySlct.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String value = displaySlct.getSelected();
                if (Objects.equals(value, "Fullscreen")) {
                    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                } else {
                    Gdx.graphics.setWindowedMode(1900,1000);
                }
            }
        });

        apply.addListener(click(() -> System.out.println("Settings applied")));
        back.addListener(click(() -> game.setScreen(new MainMenu(game))));

        Gdx.input.setInputProcessor(stage);

    }

    private DisplayResult createDisplaySlct() {
        String defaultSlctd;

        if (Gdx.graphics.isFullscreen()) {
            defaultSlctd = "Fullscreen";
        } else {
            defaultSlctd = "Windowed";
        }

        String[] displayOpts = {"Windowed","Fullscreen"};

        Label displayLbl = new Label("Display",skin);
        SelectBox<String> displaySlct = new SelectBox<>(skin);
        displaySlct.setItems(displayOpts);
        displaySlct.setSelected(defaultSlctd);

        return new DisplayResult(displaySlct, displayLbl);
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

}
