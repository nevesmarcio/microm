package pt.me.navigator.controller;

import pt.me.navigator.model.base.CameraModel;
import pt.me.navigator.model.base.WorldModel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class MyGestureListener implements GestureListener {
	private static final String TAG = MyGestureListener.class.getSimpleName();
	
	private CameraModel camModel;
	private WorldModel worldModel;
	
	public MyGestureListener(CameraModel camModel, WorldModel wm) {
		this.camModel = camModel;
		this.worldModel = wm;
	}
	
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		Gdx.app.debug(TAG, "touchDown");
		
		return false;
	}


	@Override
	public boolean tap(float x, float y, int count, int button) {
		Gdx.app.debug(TAG, "tap");
		
		return false;
	}


	@Override
	public boolean longPress(float x, float y) {
		Gdx.app.debug(TAG, "longPress");
		
		return false;
	}


	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		Gdx.app.debug(TAG, "fling");
		
		return false;
	}


	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		Gdx.app.debug(TAG, "pan");
		
		return false;
	}
	

	@Override
	public boolean zoom(float originalDistance, float currentDistance) {
		Gdx.app.debug(TAG, "zoom");

		return false;
	}

	@Override
	public boolean pinch(Vector2 initialFirstPointer,
			Vector2 initialSecondPointer, Vector2 firstPointer,
			Vector2 secondPointer) {
		Gdx.app.debug(TAG,  "pinch");

		return false;
	}



}
