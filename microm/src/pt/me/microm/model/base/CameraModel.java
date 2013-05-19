package pt.me.microm.model.base;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import pt.me.microm.GameMicroM;
import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Logger;

public class CameraModel extends AbstractModel implements InputProcessor {
	private static final String TAG = CameraModel.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private OrthographicCamera uiCamera; 	// camera dedicada ao UI
	private PerspectiveCamera gameCamera;	// game camera
	
	private static int CAM_MOVE_LEFT = 		0x00000001;
	private static int CAM_MOVE_RIGHT = 	0x00000002;
	private static int CAM_MOVE_UP = 		0x00000004;
	private static int CAM_MOVE_DOWN = 		0x00000008;
	
	private static int CAM_ZOOM_OUT = 		0x00000010;
	private static int CAM_ZOOM_IN = 		0x00000020;

	private static int CAM_LEAN_FWD = 		0x00000040;
	private static int CAM_LEAN_BCK = 		0x00000080;
	
	private static int CAM_TILT_LEFT = 		0x00000100;
	private static int CAM_TILT_RIGHT = 	0x00000200;
	
	private static int CAM_ROTATE_LEFT = 	0x00000400;
	private static int CAM_ROTATE_RIGHT = 	0x00000800;
	
	private int flags = 0x00000000;

	private float fovy;
	private float theta;
	private float phi;
	private Vector3 camCenter;
	private float camRadius;
	
	public CameraModel() {
	
		//## UI CAMERA STUFF
		uiCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		
		//## GAME CAMERA STUFF
		fovy = 67; // mudando o fov, muda imenso a distância da camera ao viewport - calculo em camRadius
		gameCamera = new PerspectiveCamera(fovy, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		// Sinaliza os subscritores de que a construção do modelo terminou.
		this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));
	}
	
	
//	@Override
//	public Body getBody() {
//		return null;
//	}
//
//	@Override
//	public Vector2 getPosition() {
//		return null;
//	}
	
	/**
	 * 
	 * @param width the canvas width
	 * @param height the canvas height
	 */
	public float cameraXposition = 0.0f;							// default values
	public float cameraYposition = 0.0f;							// default values
	public float camWidth = Gdx.graphics.getWidth();				// default values
	public float camHeight = Gdx.graphics.getHeight();				// default values
	public void Resize(int width, int height) {
		//## UI CAMERA STUFF
		uiCamera.viewportWidth = width;
		uiCamera.viewportHeight = height;
		
		//## GAME CAMERA STUFF
		float screen_ratio = (float)width / (float)height;
		float camera_ratio = (float)camWidth / (float)camHeight;
		
		float real_w;
		float real_h;
		
		if ((camera_ratio >= 1.0) && screen_ratio < camera_ratio) { // FIT WIDTH
			real_w = camWidth;
			real_h = camWidth / screen_ratio;
			
		} else { 													// FIT HEIGHT
			real_w = camHeight * screen_ratio;
			real_h = camHeight;
		}
			
		gameCamera.viewportWidth = real_w;
		gameCamera.viewportHeight = real_h;
		
		
		theta = 0;
		phi = (float)Math.PI/2;

		camCenter = new Vector3(cameraXposition, cameraYposition, 0.0f);
		
		camRadius = (float)( (real_h/2f) / Math.tan(Math.toRadians(fovy/2f)));  // tendo em conta o fov, a que distância está a camara do "near clipping plan" - viewport	
		
		gameCamera.position.set(camCenter.x + (float)(camRadius*Math.cos(theta)*Math.cos(phi)),
							camCenter.y + (float)(camRadius*Math.sin(theta)*Math.cos(phi)),
							camCenter.z + (float)(camRadius*Math.sin(phi)));
		
		gameCamera.lookAt(camCenter.x, camCenter.y, camCenter.z);
	
		gameCamera.near = 0.1f; //10cm
		gameCamera.far = 2000.0f;//2km

		printCameraValues();
		gameCamera.update();
	}
	
	public OrthographicCamera getUiCamera() {
		return uiCamera;
	}
	public PerspectiveCamera getGameCamera() {
		return gameCamera;
	}

	public void printCameraValues() {
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
		otherSymbols.setDecimalSeparator('.');
		otherSymbols.setGroupingSeparator(' '); 
		DecimalFormat fmt = new DecimalFormat("0.00", otherSymbols);
		
		logger.info("cam position: " + fmt.format(gameCamera.position.x) + ", " + fmt.format(gameCamera.position.y) + ", " + fmt.format(gameCamera.position.z));
		logger.info("cam direction: " + fmt.format(gameCamera.direction.x) + ", " + fmt.format(gameCamera.direction.y) + ", " + fmt.format(gameCamera.direction.z));
		logger.info("cam up: " + fmt.format(gameCamera.up.x) + ", " + fmt.format(gameCamera.up.y) + ", " + fmt.format(gameCamera.up.z));	
	}
	
	
	public void startLeanFwd() {
		flags |= CAM_LEAN_FWD;
	}
	public void stopLeanFwd() {
		flags &= ~CAM_LEAN_FWD;
	}
	
	public void startLeanBck() {
		flags |= CAM_LEAN_BCK;
	}
	public void stopLeanBck() {
		flags &= ~CAM_LEAN_BCK;
	}
	
	public void startTiltLeft() {
		flags |= CAM_TILT_LEFT;
	}
	public void stopTiltLeft() {
		flags &= ~CAM_TILT_LEFT;
	}
	
	public void startTiltRight() {
		flags |= CAM_TILT_RIGHT;
	}
	public void stopTiltRight() {
		flags &= ~CAM_TILT_RIGHT;
	}
	
	public void startRotateLeft() {
		flags |= CAM_ROTATE_LEFT;
	}
	public void stopRotateLeft() {
		flags &= ~CAM_ROTATE_LEFT;
	}
	
	public void startRotateRight() {
		flags |= CAM_ROTATE_RIGHT;
	}
	public void stopRotateRight() {
		flags &= ~CAM_ROTATE_RIGHT;
	}
	
	public void startMoveCameraLeft() {
		flags |= CAM_MOVE_LEFT;
	}
	public void stopMoveCameraLeft() {
		flags &= ~CAM_MOVE_LEFT;
	}
	
	public void startMoveCameraRight() {
		flags |= CAM_MOVE_RIGHT;
	}
	public void stopMoveCameraRight() {
		flags &= ~CAM_MOVE_RIGHT;
	}
	
	public void startMoveCameraUp() {
		flags |= CAM_MOVE_UP;
	}
	public void stopMoveCameraUp() {
		flags &= ~CAM_MOVE_UP;
	}
	
	public void startMoveCameraDown() {
		flags |= CAM_MOVE_DOWN;
	}
	public void stopMoveCameraDown() {
		flags &= ~CAM_MOVE_DOWN;
	}
	
	public void startZoomOut() {
		flags |= CAM_ZOOM_OUT;
	}
	public void stopZoomOut() {
		flags &= ~CAM_ZOOM_OUT;
	}
	
	public void startZoomIn() {
		flags |= CAM_ZOOM_IN;
	}
	public void stopZoomIn() {
		flags &= ~CAM_ZOOM_IN;
	}
	
	
	private Vector3 temp;
	private static float MVSPD = 0.1f;
	private static float ROTSPD = 1.0f;
	@Override
	public void handleGameTick(GameTickEvent e) {

		if ((flags & CAM_LEAN_FWD) != 0) {
			temp = gameCamera.up.cpy();
			temp.crs(gameCamera.direction);
			gameCamera.rotate(temp, 1.0f*ROTSPD);
		}
		if ((flags & CAM_LEAN_BCK) != 0) {
			temp = gameCamera.up.cpy();
			temp.crs(gameCamera.direction);
			gameCamera.rotate(temp, -1.0f*ROTSPD);
		}		

		if ((flags & CAM_ROTATE_LEFT) != 0)
			gameCamera.rotate(gameCamera.up, -1.0f*ROTSPD);
			
		if ((flags & CAM_ROTATE_RIGHT) != 0)
			gameCamera.rotate(gameCamera.up, 1.0f*ROTSPD);
		
		
		if ((flags & CAM_TILT_LEFT) != 0){
			gameCamera.rotate(gameCamera.direction, -1.0f*ROTSPD);
		}
			
		if ((flags & CAM_TILT_RIGHT) != 0) {
			gameCamera.rotate(gameCamera.direction, 1.0f*ROTSPD);
		}
					
		if ((flags & CAM_MOVE_LEFT) != 0) {
			temp = gameCamera.up.cpy();
			temp.crs(gameCamera.direction); 
			temp.mul(1.0f*MVSPD);
			
			System.out.println(gameCamera);
			
			gameCamera.translate(temp);
		}
		if ((flags & CAM_MOVE_RIGHT) != 0) {
			temp = gameCamera.up.cpy();
			temp.crs(gameCamera.direction); 
			temp.mul(-1.0f*MVSPD);
			
			gameCamera.translate(temp);			
		}		
		
		if ((flags & CAM_MOVE_UP) != 0) {
			temp = gameCamera.up.cpy();
			temp.mul(1.0f*MVSPD);
			
			gameCamera.translate(temp);
		}		
		if ((flags & CAM_MOVE_DOWN) != 0) {
			temp = gameCamera.up.cpy();
			temp.mul(-1.0f*MVSPD);
			
			gameCamera.translate(temp);		
		}		
		
		
		if ((flags & CAM_ZOOM_OUT) != 0) {
			temp = gameCamera.direction.cpy();
			temp.mul(-1.0f*MVSPD);
			
			gameCamera.translate(temp);
		}
		if ((flags & CAM_ZOOM_IN) != 0) {
			temp = gameCamera.direction.cpy();
			temp.mul(1.0f*MVSPD);
			
			gameCamera.translate(temp);
		}

		//FIXME: n posso meter isto aqui senão o ecrã faz um "flick"
		if (GameMicroM.FLAG_DEV_ELEMENTS_B)
			gameCamera.update(); // faz update às matrizes da camera após os movimentos
		
	}

	
	// InputProcessor interface implementation
	@Override
	public boolean keyDown(int keycode) {

		if (logger.getLevel() >= Logger.DEBUG) logger.debug("[KEY]: " + Integer.toString(keycode));
		
		if (keycode == Keys.P)
			printCameraValues();
		
		if (keycode == Keys.Q)
			startZoomIn();
		if (keycode == Keys.A)
			startZoomOut();
		
		if (keycode == Keys.W)
			startLeanFwd();
		if (keycode == Keys.S)
			startLeanBck();		

		if (keycode == Keys.Z)
			startRotateLeft();
		if (keycode == Keys.X)
			startRotateRight();		

		if (keycode == Keys.E)
			startTiltLeft();
		if (keycode == Keys.R)
			startTiltRight();
		
		
		if (keycode == Keys.LEFT) {
			startMoveCameraLeft();
		}
		
		if (keycode == Keys.RIGHT) {
			startMoveCameraRight();

		}
		if (keycode == Keys.DOWN) {
			startMoveCameraDown();
		}
		if (keycode == Keys.UP) {
			startMoveCameraUp();
		}
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {

		if (keycode == Keys.Q)
			stopZoomIn();
		if (keycode == Keys.A)
			stopZoomOut();
		
		if (keycode == Keys.W)
			stopLeanFwd();
		if (keycode == Keys.S)
			stopLeanBck();
		
		if (keycode == Keys.Z)
			stopRotateLeft();
		if (keycode == Keys.X)
			stopRotateRight();		
		
		if (keycode == Keys.E)
			stopTiltLeft();
		if (keycode == Keys.R)
			stopTiltRight();		
		
		if (keycode == Keys.LEFT) {
			stopMoveCameraLeft();
		}
		if (keycode == Keys.RIGHT) {
			stopMoveCameraRight();
		}
		if (keycode == Keys.UP) {
			stopMoveCameraUp();
		}
		if (keycode == Keys.DOWN) {
			stopMoveCameraDown();
		}
		
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
