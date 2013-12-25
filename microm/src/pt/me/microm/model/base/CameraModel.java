package pt.me.microm.model.base;

import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Logger;

public class CameraModel extends AbstractModel {
	private static final String TAG = CameraModel.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private OrthographicCamera uiCamera; 	// camera dedicada ao UI
	private PerspectiveCamera gameCamera;	// game camera
	
	private final float fovy;
	private float theta;
	private float phi;
	private Vector3 camCenter;
	private float camRadius;

//	private float windowWidth = screenWidth;
//	private float windowHeight = screenHeight;
	
	public CameraModel() {
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();
		
		
		//## UI CAMERA STUFF
		uiCamera = new OrthographicCamera(screenWidth, screenHeight);
		
		//## GAME CAMERA STUFF
		fovy = 67; // mudando o fov, muda imenso a distância da camera ao viewport - calculo em camRadius
		gameCamera = new PerspectiveCamera(fovy, 1, screenHeight/ screenWidth);

		gameCamera.near = 0.1f; //10cm
		gameCamera.far = 2000f;//2km		

		adjustCameraToDefaultPosition();
		
		// Sinaliza os subscritores de que a construção do modelo terminou.
		this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));
	}
	
	

	public void adjustCamera(float windowWidth, float windowHeight, float positionX, float positionY) {
		
		// calculo dos tamanhos que pervalecem atendendo aos aspect racios
		float screen_ratio = (float)gameCamera.viewportWidth / (float)gameCamera.viewportHeight;
		float camera_ratio = windowWidth/ windowHeight;
		
		float real_w;
		float real_h;
		
		if ((camera_ratio >= 1.0) && screen_ratio < camera_ratio) { // FIT WIDTH
			real_w = windowWidth;
			real_h = windowWidth / screen_ratio;
			
		} else { 													// FIT HEIGHT
			real_w = windowHeight * screen_ratio;
			real_h = windowHeight;
		}
		// a partir de agora o que interessa é o real_w e o real_h

		
		// calculo da posição da camera atendendo ao tamanho da window a representar e ao fovy
		theta = 0;
		phi = (float)Math.PI/2;

		camCenter = new Vector3(positionX, positionY, 0.0f);
		
		camRadius = (float)( (real_h/2f) / Math.tan(Math.toRadians(fovy/2f)));  // tendo em conta o fov, a que distância está a camara do "near clipping plan" - viewport	
		
		gameCamera.position.set(camCenter.x + (float)(camRadius*Math.cos(theta)*Math.cos(phi)),
							camCenter.y + (float)(camRadius*Math.sin(theta)*Math.cos(phi)),
							camCenter.z + (float)(camRadius*Math.sin(phi)));
		
		gameCamera.lookAt(camCenter.x, camCenter.y, camCenter.z);
			
		gameCamera.update();		
		
	}
	
	
	private void adjustCameraToDefaultPosition () {
		// calculo da posição da camera atendendo ao tamanho da window a representar e ao fovy
		theta = 0;
		phi = (float)Math.PI/2;

		camCenter = new Vector3(0.0f, 0.0f, 0.0f);
		
		camRadius = (float)( (Gdx.graphics.getHeight()/2f) / Math.tan(Math.toRadians(fovy/2f)));  // tendo em conta o fov, a que distância está a camara do "near clipping plan" - viewport	
		
		gameCamera.position.set(camCenter.x + (float)(camRadius*Math.cos(theta)*Math.cos(phi)),
							camCenter.y + (float)(camRadius*Math.sin(theta)*Math.cos(phi)),
							camCenter.z + (float)(camRadius*Math.sin(phi)));
		
		gameCamera.lookAt(camCenter.x, camCenter.y, camCenter.z);
		
		gameCamera.update();
		
	}
	
	
	/**
	 * 
	 * @param width the canvas width
	 * @param height the canvas height
	 */
	public void resize(float width, float height) {
		//## UI CAMERA STUFF
		uiCamera.viewportWidth = width;
		uiCamera.viewportHeight = height;
		uiCamera.update();
		
		//## GAME CAMERA STUFF
		gameCamera.viewportWidth = 1;
		gameCamera.viewportHeight = height/ width;
		gameCamera.update();

	}
	
	public OrthographicCamera getUiCamera() {
		return uiCamera;
	}
	public PerspectiveCamera getGameCamera() {
		return gameCamera;
	}

	
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
	
	private Vector3 temp;
	private static float MVSPD = 0.1f*10f;
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

		
		if (flags != 0) {
			gameCamera.update(); // faz update às matrizes da camera após os movimentos
		}

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


	

//	@Override
//	public Body getBody() {
//		return null;
//	}
//
//	@Override
//	public Vector2 getPosition() {
//		return null;
//	}	
	
}
