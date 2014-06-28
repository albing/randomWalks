package com.laserbaystudios.randomWalks;

import com.badlogic.gdx.input.GestureDetector.GestureListener;

public interface IViewSwitch extends GestureListener {
	public void render();
	
	public void dispose();
}
