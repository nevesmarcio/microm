package pt.me.microm.model.stuff;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.events.GameTickEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.BodyInterface;
import pt.me.microm.model.PointerToFunction;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.model.events.SimpleEvent;
import pt.me.microm.model.phenomenon.CollisionModel;
import pt.me.microm.tools.levelloader.BasicShape;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Logger;

public class DaBoxModel extends AbstractModel implements BodyInterface {
	private static final String TAG = DaBoxModel.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private Color color = new Color(0.5f,0.5f,0.5f,0.5f);
	
	private Vector2[] silhouetteVertex;
	 
	private BodyDef daBoxBodyDef = new BodyDef();
	private PolygonShape daBoxShape;
	private Body daBoxBody;	

	private WorldModel wm;
	private BasicShape dabox;

	public ParticleEffect particleEffect;
	
	public void create(Vector2 pos) {
		daBoxBody.setTransform(pos, daBoxBody.getAngle());
		// FIXME:: Activar o daBox!
		daBoxBody.setActive(true);		
	}
	
	private DaBoxModel(final WorldModel wm, final BasicShape dabox) {
		this.wm = wm;
		this.dabox = dabox; 

		// FIXME:: fazer isto sem ser às cegas!
		// CCW vertices
		silhouetteVertex = dabox.getPointsArray();
		Vector2[] t = new Vector2[silhouetteVertex.length];
		for (int i = 0; i < silhouetteVertex.length; i++)
			t[silhouetteVertex.length - 1 - i] = silhouetteVertex[i];

		daBoxShape = new PolygonShape();
		daBoxShape.set(t);

		daBoxBodyDef.position.set(dabox.getCentroid());
		daBoxBodyDef.type = BodyType.DynamicBody;

		daBoxBody = wm.getPhysicsWorld().createBody(daBoxBodyDef);

		/* fixture */
		FixtureDef fixDef = new FixtureDef();
		fixDef.shape = daBoxShape;
		fixDef.density = 1.0f;
		fixDef.friction = 0.0f;
		fixDef.restitution = 0.0f;
		daBoxBody.createFixture(fixDef);

		daBoxBody.setUserData(DaBoxModel.this); // relacionar com o modelo

		daBoxBody.setSleepingAllowed(false);		
		
		daBoxBody.setActive(false);
		
		particleEffect = new ParticleEffect();
	    particleEffect.load(Gdx.files.internal("data/particles/fire.p"), Gdx.files.internal("data/particles"));
	    particleEffect.start();
	    
		// Sinaliza os subscritores de que a construção do modelo terminou.
		DaBoxModel.this.dispatchEvent(new SimpleEvent(EventType.ON_MODEL_INSTANTIATED));	    
	}

	public static DaBoxModel getNewInstance(WorldModel wm, BasicShape dabox){
		return new DaBoxModel(wm, dabox);
	}
	
	@Override
	public void handleGameTick(GameTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
		// internal engine
		daBoxBody.setLinearVelocity(4.6f, daBoxBody.getLinearVelocity().y);
	}

	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}

	public void jump() {
		//FIXME: É necessário escalar as forças mediante o timestep 
		float force_to_apply = 235f; //N
		daBoxBody.applyForceToCenter(0.0f, force_to_apply);
		daBoxBody.applyTorque(10.0f); //N.m
		
		CollisionModel.getNewInstance(getPosition());
	}

	// BodyInterface implementation
	@Override
	public BasicShape getBasicShape() {
		return dabox;
	}
	@Override
	public Vector2 getPosition() {
		return daBoxBody.getPosition();
	}
	@Override
	public float getAngle() {
		return daBoxBody.getAngle();
	}
	@Override
	public Body getBody() {
		return daBoxBody;
	}
	
	
	
}
