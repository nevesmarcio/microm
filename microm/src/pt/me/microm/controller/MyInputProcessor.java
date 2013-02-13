package pt.me.microm.controller;

import pt.me.microm.model.base.CameraModel;
import pt.me.microm.model.base.WorldModel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Logger;

public class MyInputProcessor implements InputProcessor {
	private static final String TAG = MyInputProcessor.class.getSimpleName();
	private static final Logger logger = new Logger(TAG);
	
	private WorldModel worldModel;
	private CameraModel camModel;
	
	public MyInputProcessor(CameraModel camModel, WorldModel wm) {
		this.worldModel = wm;
		this.camModel = camModel;
	}
	


	@Override
	public boolean keyDown(int keycode) {

		if (logger.getLevel() == logger.DEBUG) logger.debug("[KEY]: " + Integer.toString(keycode));
		
		if (keycode == Keys.SPACE)
			worldModel.player.jump();
		
		if (keycode == Keys.P)
			camModel.printCameraValues();
		
		if (keycode == Keys.Q)
			camModel.startZoomIn();
		if (keycode == Keys.A)
			camModel.startZoomOut();
		
		if (keycode == Keys.W)
			camModel.startLeanFwd();
		if (keycode == Keys.S)
			camModel.startLeanBck();		

		if (keycode == Keys.Z)
			camModel.startRotateLeft();
		if (keycode == Keys.X)
			camModel.startRotateRight();		

		if (keycode == Keys.E)
			camModel.startTiltLeft();
		if (keycode == Keys.R)
			camModel.startTiltRight();
		
		
		if (keycode == Keys.LEFT) {
			camModel.startMoveCameraLeft();
		}
		
		if (keycode == Keys.RIGHT) {
			camModel.startMoveCameraRight();

		}
		if (keycode == Keys.DOWN) {
			camModel.startMoveCameraDown();
		}
		if (keycode == Keys.UP) {
			camModel.startMoveCameraUp();
		}
		
		if (keycode == Keys.P) {
			worldModel.setPauseSim(true);
		}
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {

		if (keycode == Keys.Q)
			camModel.stopZoomIn();
		if (keycode == Keys.A)
			camModel.stopZoomOut();
		
		if (keycode == Keys.W)
			camModel.stopLeanFwd();
		if (keycode == Keys.S)
			camModel.stopLeanBck();
		
		if (keycode == Keys.Z)
			camModel.stopRotateLeft();
		if (keycode == Keys.X)
			camModel.stopRotateRight();		
		
		if (keycode == Keys.E)
			camModel.stopTiltLeft();
		if (keycode == Keys.R)
			camModel.stopTiltRight();		
		
		
		if (keycode == Keys.LEFT) {
			camModel.stopMoveCameraLeft();
		}
		if (keycode == Keys.RIGHT) {
			camModel.stopMoveCameraRight();
		}
		if (keycode == Keys.UP) {
			camModel.stopMoveCameraUp();
		}
		if (keycode == Keys.DOWN) {
			camModel.stopMoveCameraDown();
		}
		
		if (keycode == Keys.P) {
			worldModel.setPauseSim(false);
		}		
		
		return false;
	}

	@Override
	public boolean keyTyped(char character) {

		return false;
	}

//	@Override
//	public boolean touchDown(int x, int y, int pointer, int button) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean touchUp(int x, int y, int pointer, int button) {
//		// TODO Auto-generated method stub		
//		return false;
//	}
//
//	@Override
//	public boolean touchDragged(int x, int y, int pointer) {
//		// TODO Auto-generated method stub
//		return false;
//	}

	
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
//		y+=80.0f;
		
		// translate the mouse coordinates to world coordinates
		
//		float positionX = (float)x * GAME_CONSTANTS.MODEL_SCREEN_WIDTH_CAPACITY / camModel.getCamera().viewportWidth;
//		float positionY = (float)(camModel.getCamera().viewportHeight-y) * (GAME_CONSTANTS.MODEL_SCREEN_WIDTH_CAPACITY*camModel.getCamera().viewportHeight/camModel.getCamera().viewportWidth) / camModel.getCamera().viewportHeight;
		
		worldModel.touchDown(camModel,x, y, pointer);

		return false;
	}



	@Override
	public boolean touchDragged (int x, int y, int pointer) {
//		y+=80.0f;
		
		// if a mouse joint exists we simply update
		// the target of the joint based on the new
		// mouse coordinates
//		float positionX = (float)x * GAME_CONSTANTS.MODEL_SCREEN_WIDTH_CAPACITY / camModel.getCamera().viewportWidth;
//		float positionY = (float)(camModel.getCamera().viewportHeight-y) * (GAME_CONSTANTS.MODEL_SCREEN_WIDTH_CAPACITY*camModel.getCamera().viewportHeight/camModel.getCamera().viewportWidth) / camModel.getCamera().viewportHeight;
		

		worldModel.touchDragged(camModel,x, y, pointer);
		
		return false;
	}

	@Override
	public boolean touchUp (int x, int y, int pointer, int button) {
//		float positionX = (float)x * GAME_CONSTANTS.MODEL_SCREEN_WIDTH_CAPACITY / camModel.getCamera().viewportWidth;
//		float positionY = (float)(camModel.getCamera().viewportHeight-y) * (GAME_CONSTANTS.MODEL_SCREEN_WIDTH_CAPACITY*camModel.getCamera().viewportHeight/camModel.getCamera().viewportWidth) / camModel.getCamera().viewportHeight;

		worldModel.touchUp(camModel, x, y, pointer);
		
		return false;
	}
	
	
	
	
		
	
	
	
	
	
	
}
