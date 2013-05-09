package pt.me.microm.controller;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.model.base.CameraModel;
import pt.me.microm.model.base.WorldModel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Logger;

public class MyInputProcessor implements InputProcessor {
	private static final String TAG = MyInputProcessor.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	public MyInputProcessor() {

	}

	@Override
	public boolean keyDown(int keycode) {
		if (logger.getLevel() >= Logger.DEBUG) logger.debug("[KEY]: " + Integer.toString(keycode));
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		
		return false;
	}

	@Override
	public boolean keyTyped(char character) {

		return false;
	}

	/*
	 * this method will not be called on android 
	 */
	@Override
	public boolean mouseMoved(int x, int y) {

		return false;
	}
	
	/*
	 * this method will not be called on android 
	 */
	@Override
	public boolean scrolled(int amount) {
		
		return false;
	}

	

	@Override
	public boolean touchDown (int x, int y, int pointer, int button) {
		
		return false;
	}



	@Override
	public boolean touchDragged (int x, int y, int pointer) {
		
		return false;
	}

	@Override
	public boolean touchUp (int x, int y, int pointer, int button) {
		
		return false;
	}
	
	
	
	
		
	
	
	
	
	
	
}
