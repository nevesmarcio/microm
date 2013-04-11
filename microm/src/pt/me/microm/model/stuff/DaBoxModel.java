package pt.me.microm.model.stuff;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.events.GameTickEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.IBodyProperties;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.model.base.WorldModelManager.PointerToFunction;
import pt.me.microm.model.events.SimpleEvent;
import pt.me.microm.tools.levelloader.BasicShape;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Logger;

public class DaBoxModel extends AbstractModel implements IBodyProperties {
	private static final String TAG = DaBoxModel.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private Color color = new Color(0.5f,0.5f,0.5f,0.5f);
	
	private Vector2[] silhouetteVertex;
	 
	private BodyDef daBoxBodyDef = new BodyDef();
	private PolygonShape daBoxShape;
	private Body daBoxBody;	

	private WorldModel wm;
	private BasicShape dabox;
	
	public void create(Vector2 pos) {
		daBoxBody.setTransform(pos, daBoxBody.getAngle());
		// FIXME:: Activar o daBox!
		daBoxBody.setActive(true);		
	}
	
	private DaBoxModel(final WorldModel wm, final BasicShape dabox) {
		this.wm = wm;
		this.dabox = dabox; 

		wm.wmManager.add(new PointerToFunction() {
			
			@Override
			public Object handler(Object... a) {

				// FIXME:: fazer isto sem ser às cegas!
				// CCW vertices
				silhouetteVertex = dabox.getPointsArray();
				Vector2[] t = new Vector2[silhouetteVertex.length];
				for (int i = 0; i < silhouetteVertex.length; i++)
					t[silhouetteVertex.length - 1 - i] = silhouetteVertex[i];

				daBoxShape = new PolygonShape();
				daBoxShape.set(t);
				
//				float newRadius = 0.001f;
//				logger.debug(">>>>>>> setting radius of DaBox from ...... " + daBoxShape.getRadius() + " ....... to " + newRadius);
//				daBoxShape.setRadius(newRadius);
				
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

				daBoxBody.setSleepingAllowed(true);		
				
				daBoxBody.setBullet(false);
						
				daBoxBody.setActive(false);
			    
				// Sinaliza os subscritores de que a construção do modelo terminou.
				DaBoxModel.this.dispatchEvent(new SimpleEvent(EventType.ON_MODEL_INSTANTIATED));	    				
				
				return null;
			}
		});
		

	}

	public static DaBoxModel getNewInstance(WorldModel wm, BasicShape dabox){
		return new DaBoxModel(wm, dabox);
	}
	
	@Override
	public void handleGameTick(GameTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
		// internal engine
		//FIXME: julgo que este "set" à bruta do movimento provoca aqueles saltinhos estúpidos.
		// O setBullet = true não resolve o comportamento...
		// Com método do impulso o problema também se manifesta
		// Ao diminuir a espessura da skin, o problema parece manifestar-se menos frequentemente... Este comportamento dos saltinhos deve ter a ver com as aproximações do box2d
		// Ao fazer resize do screen aumenta a probabilidade de acontecer
		
		// Análise do link http://www.iforce2d.net/b2dtut/constant-speed
		
		// Método da velocidade linear
		//daBoxBody.setLinearVelocity(4.6f, daBoxBody.getLinearVelocity().y);
		
		// Método do impulso
		Vector2 vel = daBoxBody.getLinearVelocity();
		float desiredXvel = 4.6f;
		float velChange = desiredXvel - vel.x;
		float impulse = daBoxBody.getMass() * velChange; //disregard time factor
		
		daBoxBody.applyLinearImpulse(new Vector2(impulse, 0.0f), daBoxBody.getWorldCenter());

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
