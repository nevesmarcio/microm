package pt.me.microm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.model.base.CameraModel;
import pt.me.microm.model.base.WorldModel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class MyGestureListener implements GestureListener {
	private static final String TAG = MyGestureListener.class.getSimpleName();
	private static final Logger logger = LoggerFactory.getLogger(TAG);
	
	public MyGestureListener() {

	}
	
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		logger.debug("touchDown");
		
		return false;
	}


	@Override
	public boolean tap(float x, float y, int count, int button) {
		logger.debug("tap");
		
		return false;
	}


	@Override
	public boolean longPress(float x, float y) {
		logger.debug("longPress");
		
		return false;
	}


	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		logger.debug("fling");
		
		return false;
	}


	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		logger.debug("pan");
		
		return false;
	}
	

	@Override
	public boolean zoom(float originalDistance, float currentDistance) {
		logger.debug("zoom");

		return false;
	}

	@Override
	public boolean pinch(Vector2 initialFirstPointer,
			Vector2 initialSecondPointer, Vector2 firstPointer,
			Vector2 secondPointer) {
		logger.debug("pinch");

		return false;
	}



}
