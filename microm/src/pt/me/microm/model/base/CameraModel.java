package pt.me.microm.model.base;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.logging.SimpleFormatter;

import pt.me.microm.GameMicroM;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.events.GameTickEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.events.SimpleEvent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;

public class CameraModel extends AbstractModel {
	private static final String TAG = CameraModel.class.getSimpleName();
	
	private OrthographicCamera uiCamera; 	// camera dedicada ao UI
	private Camera gameCamera;				// game camera
	
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

	float theta;
	float phi;
	Vector3 camCenter;
	float camRadius;
	
	public CameraModel() {
		//FIXME:: se não colocar isto aqui tenho uma data de excepções. Analisar!!
		Resize();
		
		// Sinaliza os subscritores de que a construção do modelo terminou.
		this.dispatchEvent(new SimpleEvent(EventType.ON_MODEL_INSTANTIATED));
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
	
	
	public void Resize() {
		//## UI CAMERA STUFF
		uiCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		//## GAME CAMERA STUFF
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		float real_w;// = 1;
		float real_h;// = real_w*h/w;
		
		//quero encaixar da melhor maneira o board 15x15
		if (w<h) {
			real_w = GAME_CONSTANTS.MODEL_SCREEN_WIDTH_CAPACITY;
			real_h = real_w*h/w;
		} else {
			real_h = GAME_CONSTANTS.MODEL_SCREEN_WIDTH_CAPACITY;
			real_w = real_h*w/h;
		}
		
		float fovy = 67; // mudando o fov, muda imenso a distância da camera ao viewport - calculo em camRadius
		gameCamera = new PerspectiveCamera(fovy, real_w, real_h);
		
		theta = 0;
		phi = (float)Math.PI/2;
		if (real_w < real_h)
			camCenter = new Vector3(real_w/2, real_w/2, 0.0f);
		else
			camCenter = new Vector3(real_h/2, real_h/2, 0.0f);
		
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
	public Camera getGameCamera() {
		return gameCamera;
	}

	public void printCameraValues() {
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
		otherSymbols.setDecimalSeparator('.');
		otherSymbols.setGroupingSeparator(' '); 
		DecimalFormat fmt = new DecimalFormat("0.00", otherSymbols);
		
		Gdx.app.log(TAG, "cam position: " + fmt.format(gameCamera.position.x) + ", " + fmt.format(gameCamera.position.y) + ", " + fmt.format(gameCamera.position.z));
		Gdx.app.log(TAG, "cam direction: " + fmt.format(gameCamera.direction.x) + ", " + fmt.format(gameCamera.direction.y) + ", " + fmt.format(gameCamera.direction.z));
		Gdx.app.log(TAG, "cam up: " + fmt.format(gameCamera.up.x) + ", " + fmt.format(gameCamera.up.y) + ", " + fmt.format(gameCamera.up.z));	
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
		if (GameMicroM.FLAG_DEV_ELEMENTS)
			gameCamera.update(); // faz update às matrizes da camera após os movimentos
		
	}




}
