package pt.me.microm.model.ui;

import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.base.CameraModel;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.model.ui.utils.FlashMessageManagerModel;
import pt.me.microm.model.ui.utils.IDataSourceObject;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.utils.Logger;

public class UIModel extends AbstractModel implements InputProcessor{
	// Esta classe deverá ter um objecto visual independente da camera sobre o mundo.
	// O UI deverá permanecer inalterado independentemente do zoom/ pan, etc.
	private static final String TAG = UIModel.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private WorldModel wm;
	private CameraModel cam;
	
	////////// protected devido à innerclass do QueryCallback
	protected Body[] hitBody = null;
	
	protected BodyDef dummyBodyDef = new BodyDef(); // zero sized body to connect the joint to
	protected Body dummyBody = null;

	protected MouseJoint[] mouseJoint = null;
	
	private float ups;
	
	int pntr;


	public UIModel(CameraModel cam, WorldModel wm) {
		this.cam = cam;
		this.wm = wm;
		
		hitBody = new Body[GAME_CONSTANTS.MAX_TOUCH_POINTS];
		mouseJoint = new MouseJoint[GAME_CONSTANTS.MAX_TOUCH_POINTS];
		
		dummyBody = wm.getPhysicsWorld().createBody(dummyBodyDef);
		
		dummyBody.setUserData(this);
		
		// Sinaliza os subscritores de que a construção do modelo terminou.
		this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));
	}
	
	
	// variable used to memorize positions
	private Vector3[] testPoint = new Vector3[GAME_CONSTANTS.MAX_TOUCH_POINTS];
	private Vector3[] originalTestPoint = new Vector3[GAME_CONSTANTS.MAX_TOUCH_POINTS];
	QueryCallback callback = new QueryCallback() {
		@Override
		public boolean reportFixture (Fixture fixture) {
			// if the hit point is inside the fixture of the body
			// we report it
			if (fixture.testPoint(getTestPoint()[pntr].x, getTestPoint()[pntr].y)) {
				hitBody[pntr] = fixture.getBody();
				return false;
			} else
				return true;
		}
	};

	public void _touchDown (float positionX, float positionY, int pointer){

		getTestPoint()[pointer] = new Vector3(positionX, positionY, 0);
		getOriginalTestPoint()[pointer] = new Vector3(positionX, positionY, 0);

		logger.info("touchDown: " + getTestPoint()[pointer].x + ", " + getTestPoint()[pointer].y);
		
		FlashMessageManagerModel.getInstance(wm.tweenManager).addFlashMessage(new IDataSourceObject<String>() {

			@Override
			public void set(String obj) {

			}

			@Override
			public String get() {
				
				return "Hello World";
			}

		}, new Vector2(0.0f, 0.0f), 1.0f, false);
		

		
		
//		Vector3 vec = getTestPoint()[pointer];
//		vec.z = 0.0f;
//
//		cam.getCamera().near = cam.getCamera().position.len(); //10.0f
//		cam.getCamera().update();
//		cam.getCamera().unproject(vec);
//		
//		vec.z = 0.0f; // força a renderização ao plano certo.
//		
//		cam.getCamera().near = 0.10f;
//		cam.getCamera().update();
//		
		
		
		Ray rr = cam.getGameCamera().getPickRay(getOriginalTestPoint()[pointer].x, getOriginalTestPoint()[pointer].y);
		Vector3 v = getTestPoint()[pointer];
		Intersector.intersectRayPlane(rr, new Plane(new Vector3(0f,0f,1f), 0.0f), v);		
		
		
		// ask the world which bodies are within the given
		// bounding box around the mouse pointer
		hitBody[pointer] = null;
		this.pntr = pointer;		
		wm.getPhysicsWorld().QueryAABB(callback, getTestPoint()[pointer].x - 0.1f, getTestPoint()[pointer].y - 0.1f, getTestPoint()[pointer].x + 0.1f, getTestPoint()[pointer].y + 0.1f);

		if (hitBody[pointer] == dummyBody) hitBody[pointer] = null;

		// ignore kinematic bodies, they don't work with the mouse joint
		if (hitBody[pointer] != null && hitBody[pointer].getType() == BodyType.KinematicBody) return;

		// if we hit something we create a new mouse joint
		// and attach it to the hit body.
		if (hitBody[pointer] != null) {
			MouseJointDef def = new MouseJointDef();
			def.bodyA = dummyBody;
			def.bodyB = hitBody[pointer];
			def.collideConnected = true;
			def.target.set(getTestPoint()[pntr].x, getTestPoint()[pntr].y);
			def.maxForce = 1000.0f * hitBody[pointer].getMass();

			mouseJoint[pointer] = (MouseJoint)wm.getPhysicsWorld().createJoint(def);
			hitBody[pointer].setAwake(true);
		}
	}

	/** another temporary vector **/
	Vector2 target = new Vector2();
	public void _touchDragged(float positionX, float positionY, int pointer) {

		testPoint[pointer].x = positionX;
		testPoint[pointer].y = positionY;

		originalTestPoint[pointer].x = positionX;
		originalTestPoint[pointer].y = positionY;

//		Vector3 vec = getTestPoint()[pointer];
//		vec.z = 0.0f;
//
//		cam.getCamera().near = cam.getCamera().position.len();//10f;
//		cam.getCamera().update();
//		cam.getCamera().unproject(vec);
//		
//		vec.z = 0.0f; // força a renderização ao plano certo.
//		
//		cam.getCamera().near = 0.10f;
//		cam.getCamera().update();
		
		Ray rr = cam.getGameCamera().getPickRay(getOriginalTestPoint()[pointer].x, getOriginalTestPoint()[pointer].y);
		Vector3 v = getTestPoint()[pointer];
		Intersector.intersectRayPlane(rr, new Plane(new Vector3(0f,0f,1f), 0.0f), v);
		
		
		if (mouseJoint[pointer] != null) {
			mouseJoint[pointer].setTarget(target.set(getTestPoint()[pointer].x, getTestPoint()[pointer].y));
		}

	}
		
	public void _touchUp(float positionX, float positionY, int pointer){
		getTestPoint()[pointer] = null;
		getOriginalTestPoint()[pointer] = null;
		
		// if a mouse joint exists we simply destroy it
		if (mouseJoint[pointer] != null) {
			wm.getPhysicsWorld().destroyJoint(mouseJoint[pointer]);
			mouseJoint[pointer] = null;
		}
		
	}

	
	@Override
	public void handleGameTick(GameTickEvent e) {
		float elapsedNanoTime = e.getElapsedNanoTime();
		
		setUps((float) (1000.0 / (elapsedNanoTime / GAME_CONSTANTS.ONE_MILISECOND_TO_NANO)));		
		
	}

	// Updates Per Second (relativo à cadência dos cálculos físicos)
	public float getUps() {
		return ups;
	}
	public void setUps(float ups) {
		this.ups = ups;
	}	
	
	public Vector3[] getTestPoint() {
		return testPoint;
	}
	
	public Vector3[] getOriginalTestPoint() {
		return originalTestPoint;
	}
	

	
	
	
	
	@Override
	public void dispose() {
	
		wm = null;
		cam = null;
		
		super.dispose();
	}

	
	// InputProcessor interface implementation
	@Override
	public boolean keyDown(int keycode) {
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

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		this._touchDown(screenX, screenY, pointer);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		this._touchUp(screenX, screenY, pointer);
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		this._touchDragged(screenX, screenY, pointer);
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	
}
