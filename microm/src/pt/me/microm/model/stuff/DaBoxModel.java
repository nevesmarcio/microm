package pt.me.microm.model.stuff;

import pt.me.microm.infrastructure.events.GameTickEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.PointerToFunction;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.model.events.SimpleEvent;
import pt.me.microm.tools.levelloader.BasicShape;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class DaBoxModel extends AbstractModel {
	private static final String TAG = DaBoxModel.class.getSimpleName();
	
	private float side = 0.75f;
	
	private Color color = new Color(0.5f,0.5f,0.5f,0.5f);
	
	private Vector2[] silhouetteVertex;
	
	private BodyDef daBoxBodyDef = new BodyDef();
	private PolygonShape daBoxShape;
	public Body daBoxBody;	
	public PointerToFunction handler; 

	private WorldModel wm;
	private BasicShape dabox;

	public void create(Vector2 pos) {
		// deslocamento do centroid
		for (Vector2 v : dabox.getPoints()) {
			v.sub(dabox.getCentroid());
		}

		// FIXME:: fazer isto sem ser às cegas!
		// CCW vertices
		silhouetteVertex = dabox.getPoints().toArray(new Vector2[] {});
		Vector2[] t = new Vector2[silhouetteVertex.length];
		for (int i = 0; i < silhouetteVertex.length; i++)
			t[silhouetteVertex.length - 1 - i] = silhouetteVertex[i];

		daBoxShape = new PolygonShape();
		daBoxShape.set(t);

		daBoxBodyDef.position.set(pos);//dabox.getCentroid()
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

		// Sinaliza os subscritores de que a construção do modelo terminou.
		DaBoxModel.this.dispatchEvent(new SimpleEvent(
				EventType.ON_MODEL_INSTANTIATED));

	}
	
	private DaBoxModel(final WorldModel wm, final BasicShape dabox) {
		this.wm = wm;
		this.dabox = dabox; 
		
	}

	public static DaBoxModel getNewInstance(WorldModel wm, BasicShape dabox){
		return new DaBoxModel(wm, dabox);
	}
	
	@Override
	public void handleGameTick(GameTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
		//FIXME temporarily disabled
		daBoxBody.setLinearVelocity(4.0f, daBoxBody.getLinearVelocity().y);
	}

	public float getSide() {
		return side;
	}

	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}

	public void jump() {

		// É necessário escalar as forças mediante o timestep 
		float force_to_apply = 500f; //N
		daBoxBody.applyForceToCenter(0.0f, force_to_apply);
		daBoxBody.applyTorque(-10.0f); //N.m
	}
	
	@Override
	public Body getBody() {
		return daBoxBody;
	}
	
	@Override
	public Vector2 getPosition() {
		return daBoxBody.getPosition();
	}	
	
}
