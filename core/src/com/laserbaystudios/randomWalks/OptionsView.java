package com.laserbaystudios.randomWalks;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class OptionsView implements IViewSwitch, ApplicationListener {

    randomWalks parent;
    Stage stage;
    Skin skin;
    InputProcessor oldInputProcessor;

    // ApplicationListener
    @Override
    public void resume()
    {

    }
    @Override
    public void pause()
    {

    }
    @Override
    public void create()
    {
        stage = new Stage();
        oldInputProcessor = Gdx.input.getInputProcessor();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);


        Window window = new Window("Dialog", skin);

        final Label susceptibleSliderLabel = new Label("Number of susceptible", skin);
        table.add(susceptibleSliderLabel);

        final Slider numSusceptibleSlider = new Slider(1, 200, 1, false, skin);
        table.add(numSusceptibleSlider).minWidth(100).fillX().colspan(3);

        final Label numSusceptibleLabel = new Label("1", skin);
        table.add(numSusceptibleLabel);


        table.row();

        final Label infectedSliderLabel = new Label("Number of infected", skin);
        table.add(infectedSliderLabel);

        final Slider numInfectedSlider = new Slider(1, 200, 1, false, skin);
        table.add(numInfectedSlider).minWidth(100).fillX().colspan(3);

        final Label numInfectedLabel = new Label("1", skin);
        table.add(numInfectedLabel);


        table.row();

        final CheckBox zombieModeCheckBox = new CheckBox("Random Mode", skin);
        table.add(zombieModeCheckBox);

        table.row();

        final TextButton startSimulationButton = new TextButton("Release the Zombies!", skin);
        table.add(startSimulationButton);


        stage.addActor(table);

        numSusceptibleSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                numSusceptibleLabel.setText("" + (int) numSusceptibleSlider.getValue());
            }
        });

        numInfectedSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                numInfectedLabel.setText("" + (int) numInfectedSlider.getValue());
            }
        });

        zombieModeCheckBox.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                zombieModeCheckBox.setText("Zombie Mode");
            }
        });

        startSimulationButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                startSimulationButton.setText("Beginning...");
                parent.currentView = new SimulationView(parent, (int)numSusceptibleSlider.getValue(),
                        (int)numInfectedSlider.getValue(), zombieModeCheckBox.isChecked());
                Gdx.input.setInputProcessor(oldInputProcessor);
            }
        });



        // TODO: set ALL the options! here


    }

    @Override
    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose () {
        stage.dispose();
        skin.dispose();

        // TODO: is this ever getting called?  set a breakpoint in here...
    }



    public OptionsView(randomWalks app) {
        parent = app;

        if(stage == null)
            create();

    }

    @Override
    public void render() {

        Gdx.gl.glClearColor(.5f, .5f, .5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1/30f));
        stage.draw();
        //Table.drawDebug(stage);

    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {


        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
}
