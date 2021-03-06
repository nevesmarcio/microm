package pt.me.microm.controller;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.model.base.CameraModel;
import pt.me.microm.model.base.WorldModel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Logger;

public class MyGestureListener implements GestureListener {
	private static final String TAG = MyGestureListener.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	public MyGestureListener() {

	}
	
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		if (logger.getLevel() >= Logger.DEBUG) logger.debug("touchDown");
		
		return false;
	}


	@Override
	public boolean tap(float x, float y, int count, int button) {
		if (logger.getLevel() >= Logger.DEBUG) logger.debug("tap");
		
		return false;
	}


	@Override
	public boolean longPress(float x, float y) {
		if (logger.getLevel() >= Logger.DEBUG) logger.debug("longPress");
		
		return false;
	}


	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		if (logger.getLevel() >= Logger.DEBUG) logger.debug("fling");
		
		return false;
	}


	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		if (logger.getLevel() >= Logger.DEBUG) logger.debug("pan");
		
		return false;
	}
	

	@Override
	public boolean zoom(float originalDistance, float currentDistance) {
		if (logger.getLevel() >= Logger.DEBUG) logger.debug("zoom");

		return false;
	}

	@Override
	public boolean pinch(Vector2 initialFirstPointer,
			Vector2 initialSecondPointer, Vector2 firstPointer,
			Vector2 secondPointer) {
		if (logger.getLevel() >= Logger.DEBUG) logger.debug("pinch");

		return false;
	}



}
