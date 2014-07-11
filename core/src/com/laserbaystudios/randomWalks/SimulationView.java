package com.laserbaystudios.randomWalks;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class SimulationView implements IViewSwitch {

	Camera camera;
	ShapeRenderer shapeRenderer;
	
	Random rand;
	
	ArrayList<Walker> susceptible;
	ArrayList<Walker> infected;
	ArrayList<Walker> recovered;
	
	final int TOTAL_POINTS;
    final int INIT_INFECTED;
	final Color INFECTED_COLOR = Color.RED;
	final Color SUSCEPTIBLE_COLOR = Color.BLACK;
	final Color RECOVERED_COLOR = Color.GREEN;
	final Color INFECT_RADIUS_COLOR = Color.GRAY;
	
	float displayCircleRadius = 5.0f;
    float runAwayCircleRadius = 15.0f;
	float infectRadius = 15.0f;
    float turningRadius = .01f;
	float travelDistance = 4.0f;
	float infectProbability = 0.1f;
	float recoverProbability = 0.002f;
	
	int arenaHeight;
	int arenaWidth;
	int leftBound;
	int topBound;
	int rightBound;
	int bottomBound;
	
	boolean drawUpArrow;
	boolean playing;
	boolean zombieMode;
	
	randomWalks parent;

	

	private class Walker
	{
		float x, y;
        float direction;

        public void setLocation(float inX, float inY)
        {
            setLocationAndDirection(inX, inY, 0f);
        }

		public void setLocationAndDirection(float inX, float inY, float inDirection)
		{
			x = inX;
			y = inY;
            setDirection(inDirection);
		}

        public void setDirection(float inDirection)
        {
            /* TODO: turning radii
            if(inDirection - direction < turningRadius)
                direction = inDirection;
            else if(inDirection < turningRadius)
                direction -= turningRadius;
            else
                direction += turningRadius;
                */
            direction = inDirection;
        }

        public void stepForward(boolean randomDistance)
        {
            x += Math.cos(direction);
            y += Math.sin(direction);
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

        public Walker(float inX, float inY, float inDirection)
        {
            //TODO: use the other constructor
            x = inX;
            y = inY;
            direction = inDirection;
        }
	}
	
	@Override
	public void dispose()
	{
		// TODO: Do we need to clean up the ArrayLists?
		// TODO: How about the ShapeRenderer?
	}
	
	
	public SimulationView(randomWalks app, int totalPoints, int initInfected,
                          boolean isZombieMode)
	{

        ///////// incoming options ///////////
        TOTAL_POINTS = totalPoints;
        INIT_INFECTED = initInfected;
        zombieMode = isZombieMode;


       // GestureDetector gd = new GestureDetector(this);
       // Gdx.input.setInputProcessor(gd);

        playing = false;
		drawUpArrow = false;
		
		arenaHeight = Gdx.graphics.getHeight();
		arenaWidth = Gdx.graphics.getWidth();
		topBound = arenaHeight/2;
		bottomBound = -topBound;
		rightBound = arenaWidth/2;
		leftBound = -rightBound;
		
		camera = new OrthographicCamera(arenaWidth, arenaHeight);
		
		rand = new Random(System.currentTimeMillis());
		
		shapeRenderer = new ShapeRenderer();

		
		
		susceptible = new ArrayList<Walker>();
		infected = new ArrayList<Walker>();
		recovered = new ArrayList<Walker>();
		
		for(int i = 0; i<TOTAL_POINTS; i++)
			susceptible.add(new Walker(
					rand.nextFloat() * arenaWidth - (arenaWidth/2),
					rand.nextFloat() * arenaHeight - (arenaHeight/2),
                    rand.nextFloat() * 2 * (float)Math.PI)
			);

        for(int i = 0; i < INIT_INFECTED; i++)
            infected.add(new Walker(
                    rand.nextFloat() * arenaWidth - (arenaWidth/2),
                    rand.nextFloat() * arenaHeight - (arenaHeight/2),
                    rand.nextFloat() * 2 * (float)Math.PI)
            );
		
		
		
		parent = app;
	}
	
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {

		
		if((infected.size() == 0  ||  susceptible.size() == 0))
		{
            // TODO: Options button?
			parent.currentView = new OptionsView(parent);	// TODO: is this going to adequately destroy everything, or become a memory leak?
		}
		
		playing = !playing;
		
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

	@Override
	public void render() {
		
		

		camera.update();
		shapeRenderer.setProjectionMatrix(camera.combined);
		
		
//////////Draw the simulation ////////////
		

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		shapeRenderer.begin(ShapeType.Line);
		
		if(drawUpArrow)
		{
			shapeRenderer.setColor(Color.GRAY);
			shapeRenderer.line(-5, 0, 0, 10);
			shapeRenderer.line(0, 10, 5, 0);
			shapeRenderer.line(0, -10, 0, 10);
		}
		
		shapeRenderer.setColor(SUSCEPTIBLE_COLOR);
		for (Walker s : susceptible) {
            shapeRenderer.circle(s.x, s.y, displayCircleRadius);
            if(zombieMode)
                shapeRenderer.line(s.x, s.y,
                        (float)(s.x + Math.cos(s.direction) * infectRadius * .65),
                        (float)(s.y + Math.sin(s.direction) * infectRadius * .65));
        }
		
		shapeRenderer.setColor(INFECTED_COLOR);
		for (Walker i : infected) {
            shapeRenderer.circle(i.x, i.y, displayCircleRadius);
            if(zombieMode)
                shapeRenderer.line(i.x, i.y,
                    (float)(i.x + Math.cos(i.direction) * infectRadius),
                    (float)(i.y + Math.sin(i.direction) * infectRadius));
        }

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
				for (Walker s : susceptible)
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
				susceptible.remove(n);
				infected.add(n);
			}

            //////////////////////////// do the moving /////////////////////////////////
            moveWalkers(zombieMode);


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
			if ((infected.size() == 0  ||  susceptible.size() == 0))
				playing = false;
		}

	}

    private void moveWalkers(boolean zombieMode) {

        // move the healthy
        for(Walker s : susceptible) {


            if(zombieMode)
            {
                // find the closest zombie; don't perform check like below because we recover after we move
                Walker closest = infected.get(0);
                float minDistance = s.distance(closest);
                for (Walker i : infected) {
                    if (s.distance(i) < minDistance) {
                        closest = i;
                        minDistance = s.distance(i);
                    }
                }

                float denom = closest.x - s.x;
                if(denom == 0)
                    denom = 0.0000001f;

                // invert x and y; run away!
                s.setDirection((float) Math.atan2( -(closest.y - s.y), -denom));
                s.stepForward(true);
            }
            else
                s.setLocation(
                        s.x + (float)rand.nextGaussian() * travelDistance,
                        s.y + (float)rand.nextGaussian() * travelDistance
                );


        // wrap around the screen
            if(!zombieMode) {
                if (s.x < leftBound)
                    s.x = rightBound - (leftBound - s.x);
                else if (s.x > rightBound)
                    s.x = leftBound + (s.x - rightBound);

                if (s.y < bottomBound)
                    s.y = topBound - (bottomBound - s.y);
                else if (s.y > topBound)
                    s.y = bottomBound + (s.y - topBound);
            } else  // in zombie mode, trap them in a box
            {
                if (s.x < leftBound)
                    s.x = (leftBound);
                else if (s.x > rightBound)
                    s.x = rightBound;

                if (s.y < bottomBound)
                    s.y = bottomBound;
                else if (s.y > topBound)
                    s.y = topBound;
            }
        }

        // move the infected
        for(Walker i : infected)
        {
            // find the closest healthy walker
            if(susceptible.size() > 0)
            {

                if(zombieMode) {

                    Walker closest = susceptible.get(0);
                    float minDistance = i.distance(closest);
                    for (Walker s : susceptible) {
                        if (i.distance(s) < minDistance) {
                            closest = s;
                            minDistance = i.distance(s);
                        }
                    }

                    float denom = closest.x - i.x;
                    if(denom == 0)
                        denom = 0.0000001f;

                    i.setDirection((float) Math.atan2((closest.y - i.y), denom));
                    i.stepForward(true);
                }
                else

                    i.setLocation(
                        i.x + (float)rand.nextGaussian() * travelDistance,
                        i.y + (float)rand.nextGaussian() * travelDistance
                    );
            }

        // wrap around the screen if in random mode
            if(!zombieMode) {
                if (i.x < leftBound)
                    i.x = rightBound - (leftBound - i.x);
                else if (i.x > rightBound)
                    i.x = leftBound + (i.x - rightBound);

                if (i.y < bottomBound)
                    i.y = topBound - (bottomBound - i.y);
                else if (i.y > topBound)
                    i.y = bottomBound + (i.y - topBound);
            } else  // in zombie mode, trap them in a box
            {
                if (i.x < leftBound)
                    i.x = (leftBound);
                else if (i.x > rightBound)
                    i.x = rightBound;

                if (i.y < bottomBound)
                    i.y = bottomBound;
                else if (i.y > topBound)
                    i.y = topBound;
            }
        }

        // move the recovered
        // in zombie mode, they have died and do not move
        if(!zombieMode) {
            for (Walker r : recovered) {
                r.setLocation(
                        r.x + (float) rand.nextGaussian() * travelDistance,
                        r.y + (float) rand.nextGaussian() * travelDistance
                );

                // wrap around the screen
                if (!zombieMode) {
                    if (r.x < leftBound)
                        r.x = rightBound - (leftBound - r.x);
                    else if (r.x > rightBound)
                        r.x = leftBound + (r.x - rightBound);

                    if (r.y < bottomBound)
                        r.y = topBound - (bottomBound - r.y);
                    else if (r.y > topBound)
                        r.y = bottomBound + (r.y - topBound);
                } else  // in zombie mode, trap them in a box
                {
                    if (r.x < leftBound)
                        r.x = (leftBound);
                    else if (r.x > rightBound)
                        r.x = rightBound;

                    if (r.y < bottomBound)
                        r.y = bottomBound;
                    else if (r.y > topBound)
                        r.y = topBound;
                }

            }
        }
    }

}
