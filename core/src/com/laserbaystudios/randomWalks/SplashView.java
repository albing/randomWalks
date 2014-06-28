package com.laserbaystudios.randomWalks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class SplashView implements IViewSwitch {

	private SpriteBatch spriteBatch;
	private Sprite splash;
	private Texture splashTex;
	
	randomWalks parent;
	
	int arenaHeight;
	int arenaWidth;
	
	public SplashView(randomWalks app)
	{

		spriteBatch = new SpriteBatch();

		splashTex = new Texture(Gdx.files.internal("splash.png"));
		splash = new Sprite(splashTex);
		
		arenaHeight = Gdx.graphics.getHeight();
		arenaWidth = Gdx.graphics.getWidth();
		
		parent = app;
	}
	
	
	@Override
	public void dispose()
	{
		spriteBatch.dispose();
		splashTex.dispose();
	}
	
	@Override
	public void render() {
		Gdx.gl.glClearColor(0,0,0,0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		spriteBatch.begin();
		splash.setPosition((arenaWidth - splash.getWidth())/2,
				(arenaHeight - splash.getHeight())/2);
		splash.draw(spriteBatch);
		spriteBatch.end();
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
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
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		return false;
	}

}
