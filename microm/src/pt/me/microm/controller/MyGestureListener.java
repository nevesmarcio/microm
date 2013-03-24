package pt.me.microm.controller;

import pt.me.microm.model.base.CameraModel;
import pt.me.microm.model.base.WorldModel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Logger;

public class MyGestureListener implements GestureListener {
	private static final String TAG = MyGestureListener.class.getSimpleName();
	private static final Logger logger = new Logger(TAG);
	
	private CameraModel camModel;
	private WorldModel worldModel;
	
	public MyGestureListener(CameraModel camModel, WorldModel wm) {
		this.camModel = camModel;
		this.worldModel = wm;
	}
	
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		if (logger.getLevel() == logger.DEBUG) logger.debug("touchDown");
		
//		if (worldModel.player.daBoxBody != null)
//			worldModel.player.jump();
		
		return false;
	}


	@Override
	public boolean tap(float x, float y, int count, int button) {
		if (logger.getLevel() == logger.DEBUG) logger.debug("tap");
		
		return false;
	}


	@Override
	public boolean longPress(float x, float y) {
		if (logger.getLevel() == logger.DEBUG) logger.debug("longPress");
		
		return false;
	}


	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		if (logger.getLevel() == logger.DEBUG) logger.debug("fling");
		
		return false;
	}


	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		if (logger.getLevel() == logger.DEBUG) logger.debug("pan");
		
		return false;
	}
	

	@Override
	public boolean zoom(float originalDistance, float currentDistance) {
		if (logger.getLevel() == logger.DEBUG) logger.debug("zoom");

		return false;
	}

	@Override
	public boolean pinch(Vector2 initialFirstPointer,
			Vector2 initialSecondPointer, Vector2 firstPointer,
			Vector2 secondPointer) {
		if (logger.getLevel() == logger.DEBUG) logger.debug("pinch");

		return false;
	}



}
