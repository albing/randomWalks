package com.laserbaystudios.randomWalks;

import java.util.ArrayList;

import java.util.Random;

import com.badlogic.gdx.scenes.scene2d.ui.*;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class randomWalks extends ApplicationAdapter implements GestureListener {

	
	/*    UI ELEMENTS    */
	Slider numWalkers;
	
	
	/*    SIMULATION ELEMENTS    */
	Camera camera;
	ShapeRenderer shapeRenderer;
	
	Random rand;
	
	ArrayList<Walker> succeptible;
	ArrayList<Walker> infected;
	ArrayList<Walker> recovered;
	
	final int TOTAL_POINTS = 100;
	final Color INFECTED_COLOR = Color.RED;
	final Color SUCCEPTIBLE_COLOR = Color.BLACK;
	final Color RECOVERED_COLOR = Color.GREEN;
	final Color INFECT_RADIUS_COLOR = Color.GRAY;
	
	float displayCircleRadius = 5.0f;
	float infectRadius = 15.0f;
	float travelDistance = 4.0f;
	float infectProbability = 0.1f;
	float recoverProbability = 0.002f;
	
	int arenaHeight;
	int arenaWidth;
	int leftBound;
	int topBound;
	int rightBound;
	int bottomBound;
	
	boolean playing;
	
	private class Walker
	{
		float x, y;
		
		public void setLocation(float inX, float inY)
		{
			x = inX;
			y = inY;
		}
		
		public float distance(Walker w)
		{
			return (float) Math.abs(Math.sqrt(Math.pow(w.x - x, 2) + Math.pow(w.y - y, 2)));
		}
		
		public Walker(float inX, float inY)
		{
			x = inX;
			y = inY;
		}
	}
	
	@Override
	public void create () {
		
		playing = false;
		
		arenaHeight = Gdx.graphics.getHeight();
		arenaWidth = Gdx.graphics.getWidth();
		topBound = arenaHeight/2;
		bottomBound = -topBound;
		rightBound = arenaWidth/2;
		leftBound = -rightBound;
		
		GestureDetector gd = new GestureDetector(this);
		Gdx.input.setInputProcessor(gd);
		
		rand = new Random(System.currentTimeMillis());
		
		shapeRenderer = new ShapeRenderer();
		camera = new OrthographicCamera(arenaWidth, arenaHeight);
		
		initializeSimulation();
	}

	private void initializeSimulation() {
		succeptible = new ArrayList<Walker>();
		infected = new ArrayList<Walker>();
		recovered = new ArrayList<Walker>();
		
		for(int i = 0; i<TOTAL_POINTS; i++)
			succeptible.add(new Walker(
					rand.nextFloat()* arenaWidth - (arenaWidth/2), 
					rand.nextFloat()* arenaHeight - (arenaHeight/2))
			);
		
		infected.add(new Walker(
				rand.nextFloat()* arenaWidth - (arenaWidth/2), 
				rand.nextFloat()* arenaHeight - (arenaHeight/2))
		);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		
		camera.update();
		shapeRenderer.setProjectionMatrix(camera.combined);
		
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.GRAY);
		shapeRenderer.line(-5, 0, 0, 10);
		shapeRenderer.line(0, 10, 5, 0);
		shapeRenderer.line(0, -10, 0, 10);
		
		
		shapeRenderer.setColor(SUCCEPTIBLE_COLOR);
		for (Walker s : succeptible)
			shapeRenderer.circle(s.x, s.y, displayCircleRadius);
		
		shapeRenderer.setColor(INFECTED_COLOR);
		for (Walker i : infected)
			shapeRenderer.circle(i.x, i.y, displayCircleRadius);

		shapeRenderer.setColor(INFECT_RADIUS_COLOR);
		for (Walker i : infected)
			shapeRenderer.circle(i.x, i.y, infectRadius);
		
		shapeRenderer.setColor(RECOVERED_COLOR);
		for (Walker r : recovered)
			shapeRenderer.circle(r.x, r.y, displayCircleRadius);
		
		shapeRenderer.end();
		
		
		if(playing == true)
		{
				
			/////////////////////////// CHECK FOR INFECTION /////////////////////////
			ArrayList<Walker> newlyInfected = new ArrayList<Walker>();
			
			for (Walker i : infected)
			{
				for (Walker s : succeptible)
				{
					if(i.distance(s) < displayCircleRadius + infectRadius)
					{
						if(rand.nextFloat() < infectProbability)
							newlyInfected.add(s);
					}
				}
			}
			
			// switch their groups
			for (Walker n : newlyInfected)
			{
				succeptible.remove(n);
				infected.add(n);
			}
			
			////////////////////////// jiggle everyone ///////////////////////////////
			
			for(Walker s : succeptible)
				s.setLocation(
					s.x + (float)rand.nextGaussian() * travelDistance,
					s.y + (float)rand.nextGaussian() * travelDistance
				);
			
			for(Walker w : succeptible)
			{
				if(w.x < leftBound)
					w.x = rightBound - (leftBound - w.x);
				else if(w.x > rightBound)
					w.x = leftBound + (w.x - rightBound);
				
				if(w.y < bottomBound)
					w.y = topBound - (bottomBound - w.y);
				else if (w.y > topBound)
					w.y = bottomBound + (w.y - topBound);
			}
			
			for(Walker i : infected)
				i.setLocation(
					i.x + (float)rand.nextGaussian() * travelDistance,
					i.y + (float)rand.nextGaussian() * travelDistance
				);

			for(Walker w : infected)
			{
				if(w.x < leftBound)
					w.x = rightBound - (leftBound - w.x);
				else if(w.x > rightBound)
					w.x = leftBound + (w.x - rightBound);
				
				if(w.y < bottomBound)
					w.y = topBound - (bottomBound - w.y);
				else if (w.y > topBound)
					w.y = bottomBound + (w.y - topBound);
			}
			
			for(Walker r : recovered)
				r.setLocation(
					r.x + (float)rand.nextGaussian() * travelDistance,
					r.y + (float)rand.nextGaussian() * travelDistance
				);

			for(Walker w : recovered)
			{
				if(w.x < leftBound)
					w.x = rightBound - (leftBound - w.x);
				else if(w.x > rightBound)
					w.x = leftBound + (w.x - rightBound);
				
				if(w.y < bottomBound)
					w.y = topBound - (bottomBound - w.y);
				else if (w.y > topBound)
					w.y = bottomBound + (w.y - topBound);
			}
			
			///////////////////////////// move to recovered ////////////////////////////
			
			ArrayList<Walker> newlyRecovered = new ArrayList<Walker>();
			for (Walker i : infected)
			{
				if(rand.nextFloat() < recoverProbability)
				{
					newlyRecovered.add(i);
				}
			}
			
			// switch their groups
			for (Walker r : newlyRecovered)
			{
				infected.remove(r);
				recovered.add(r);
			}
			
			
			/////////////////// decide if we're still good to go //////////////////////
			if ((infected.size() == 0  ||  succeptible.size() == 0))
				playing = false;
		}
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		
		if((infected.size() == 0  ||  succeptible.size() == 0))
		{
			initializeSimulation();
		}
		
		playing = !playing;
		
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}
}
