package com.laserbaystudios.randomWalks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;

/**
 * Created by greg on 6/9/14.
 */
public class OptionsView implements IViewSwitch {

    randomWalks parent;
    Slider numWalkersSlider;
    //Window window;
    Stage stage;

    public OptionsView(randomWalks app) {
        parent = app;

        stage = new Stage();

        numWalkersSlider = new Slider(1f, 200f, 1f, false, new Slider.SliderStyle());
        //window = new Window("Dialog", new Skin());
        //window.add(numWalkersSlider).minWidth(100).fillX().colspan(3);
    }

    @Override
    public void render() {

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {

        // TODO: set ALL the options! here

        parent.currentView = new SimulationView(parent);

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
