package com.laserbaystudios.randomWalks;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class randomWalks extends ApplicationAdapter 
	implements GestureListener, ApplicationListener {

	
	public IViewSwitch currentView;
	
	
	
	@Override
	public void create () {

		GestureDetector gd = new GestureDetector(this);
		Gdx.input.setInputProcessor(gd);

		currentView = new SplashView(this);

	}

	@Override
	public void dispose()
	{
		currentView.dispose();
	}

	@Override
	public void render () {
		
		
		currentView.render();
		
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		
		return currentView.touchDown(x,y,pointer,button);
		
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
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		return false;
	}
}
