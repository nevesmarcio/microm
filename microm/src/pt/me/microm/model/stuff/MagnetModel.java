package pt.me.microm.model.stuff;

import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.ICommand;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.IActorBody;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.tools.levelloader.BasicShape;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Logger;

public class MagnetModel extends AbstractModel implements IActorBody {
	private static final String TAG = MagnetModel.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);

	private Vector2[] silhouetteVertex;
	
	private BodyDef magnetBodyDef = new BodyDef();
	private ChainShape magnetShape; 
	private Body magnetBody;
	
	private WorldModel wm;
	private BasicShape magnet;
	
	private MagnetModel(final WorldModel wm, final BasicShape magnet, final String magnet_name) {
		this.wm = wm;
		this.magnet = magnet;
		setName(magnet_name);
		
		wm.wmManager.add(new ICommand() {
			
			@Override
			public Object handler(Object ... a) {
				
				silhouetteVertex = magnet.getPointsArray();
				
				magnetShape = new ChainShape();
				magnetShape.createLoop(silhouetteVertex);
								

				// não esquecer que é este start-position que fode com a porca toda!
				magnetBodyDef.position.set(magnet.getCentroid()); // aqui devia calcular a posicao do centro de massa
				magnetBodyDef.type = BodyType.DynamicBody;
				
				magnetBody = wm.getPhysicsWorld().createBody(magnetBodyDef);

				FixtureDef fixDef = new FixtureDef();
				fixDef.shape = magnetShape;
				fixDef.density = 1.0f;
				fixDef.friction = 0.0f;
				fixDef.restitution = 0.0f;		
				magnetBody.createFixture(fixDef);
				
				
				////////////////////////////////////////////////////////////////////////////////////
				BodyDef dummyBodyDef = new BodyDef(); // zero sized body to connect the joint to
				Body dummyBody = null;
				dummyBody = wm.getPhysicsWorld().createBody(dummyBodyDef);
				
				MouseJointDef def = new MouseJointDef();
				def.bodyA = dummyBody;
				def.bodyB = magnetBody;
				def.collideConnected = true;
				def.target.set(magnetBody.getPosition().x, magnetBody.getPosition().y);
				def.maxForce = 100.0f * magnetBody.getMass();

				MouseJoint mj = (MouseJoint)wm.getPhysicsWorld().createJoint(def);
				//magnetBody.setAwake(true);
				//////////////////////////////////////////////////////////////////////////////////////
				
				
				magnetBody.setUserData(MagnetModel.this); // relacionar com o modelo
				
				// Sinaliza os subscritores de que a construção do modelo terminou.
				MagnetModel.this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));		
				
				return null;
			}
		});
		
	}
	
	public static MagnetModel getNewInstance(WorldModel wm, BasicShape wall, String wall_name){
		return new MagnetModel(wm, wall, wall_name);
	}

	
	@Override
	public void handleGameTick(GameTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
		if (magnetBody != null)
			if (logger.getLevel() >= Logger.DEBUG)
				logger.debug("[Physics-room]: Pos.x:" + String.format("%.2f", getBody().getPosition().x)
					+ " Pos.y:" + String.format("%.2f", getBody().getPosition().y) 
					+ " Angle:" + String.format("%.2f", getBody().getAngle())
					+ " Mass:" + getBody().getMass()
					+ " Type:" + getBody().getType());
		
//		logger.info("distance: " + wm.player.getPosition().dst(getPosition()));
		Vector2 diff = wm.player.getPosition().sub(getPosition());
//		logger.info("vector: " + diff.toString());
//		logger.info("Uvector: " + diff.div(diff.len()).toString());
		
		float K = -400; //-500
		getBody().applyForceToCenter(diff.mul(K * getBody().getMass() * wm.player.getBody().getMass() / diff.len2()));

	}

	
	// BodyInterface implementation
	@Override
	public String getName() {
		return super.getName();
	}
	@Override
	public BasicShape getBasicShape() {
		return magnet;
	}
	@Override
	public Vector2 getPosition() {
		return magnetBody.getPosition();
	}
	@Override
	public float getAngle() {
		return magnetBody.getAngle();
	}
	@Override
	public Body getBody() {
		return magnetBody;
	}

	
}
