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
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Logger;

public class GroundModel extends AbstractModel implements IActorBody {
	private static final String TAG = GroundModel.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private Vector2[] silhouetteVertex;
	
	private BodyDef groundBodyDef = new BodyDef();
	private ChainShape groundShape; // Fronteira do tabuleiro
	private Body groundBody;
	
	private WorldModel wm;
	private BasicShape ground;
	private String ground_name;
	
	private GroundModel(final WorldModel wm, final BasicShape ground, final String ground_name) {
		this.wm = wm;
		this.ground = ground;
		this.ground_name = ground_name;
		
		wm.wmManager.add(new ICommand() {
			
			@Override
			public Object handler(Object ... a) {

				silhouetteVertex = ground.getPointsArray();
				
				groundShape = new ChainShape();
				groundShape.createLoop(silhouetteVertex);
				
				groundBodyDef.position.set(ground.getCentroid()); // posição inicial do tabuleiro
				groundBodyDef.type = BodyType.StaticBody;
				
				groundBody = wm.getPhysicsWorld().createBody(groundBodyDef);

				FixtureDef fixDef = new FixtureDef();
				fixDef.shape = groundShape;
				fixDef.density = 1.0f;
				fixDef.friction = 0.9f;
				fixDef.restitution = 0.0f;		
				groundBody.createFixture(fixDef);
				getBody().createFixture(fixDef);
					
				getBody().setUserData(GroundModel.this); // relacionar com o modelo
				
				
				// Sinaliza os subscritores de que a construção do modelo terminou.
				GroundModel.this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));	
				
				return null;
			}
		});
	
	}
	
	public static GroundModel getNewInstance(WorldModel wm, BasicShape ground, String ground_name){
		return new GroundModel(wm, ground, ground_name);
	}

	
	@Override
	public void handleGameTick(GameTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
		if (groundBody != null)
			if (logger.getLevel() >= Logger.DEBUG)
				logger.debug("[Physics-room]: Pos.x:" + String.format("%.2f", groundBody.getPosition().x)
						+ " Pos.y:" + String.format("%.2f", groundBody.getPosition().y) 
						+ " Angle:" + String.format("%.2f", groundBody.getAngle())
						+ " Mass:" + getBody().getMass()
						+ " Type:" + getBody().getType());			
	}

	
	// BodyInterface implementation
	@Override
	public String getName() {
		return ground_name;
	}
	@Override
	public BasicShape getBasicShape() {
		return ground;
	}
	@Override
	public Vector2 getPosition() {
		return groundBody.getPosition();
	}
	@Override
	public float getAngle() {
		return groundBody.getAngle();
	}
	@Override
	public Body getBody() {
		return groundBody;
	}
	


}
