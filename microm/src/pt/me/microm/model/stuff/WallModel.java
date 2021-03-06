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

public class WallModel extends AbstractModel implements IActorBody {
	private static final String TAG = WallModel.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);

	private Vector2[] silhouetteVertex;
	
	private BodyDef wallBodyDef = new BodyDef();
	private ChainShape wallShape; // Fronteira do tabuleiro
	private Body wallBody;
	
	private WorldModel wm;
	private BasicShape wall;
	
	private WallModel(final WorldModel wm, final BasicShape wall, final String wall_name) {
		this.wm = wm;
		this.wall = wall;
		setName(wall_name);
		
		wm.wmManager.add(new ICommand() {
			
			@Override
			public Object handler(Object ... a) {
				
				silhouetteVertex = wall.getPointsArray();
				
				wallShape = new ChainShape();
				wallShape.createLoop(silhouetteVertex);
								

				// não esquecer que é este start-position que fode com a porca toda!
				wallBodyDef.position.set(wall.getCentroid()); // aqui devia calcular a posicao do centro de massa
				wallBodyDef.type = BodyType.StaticBody;
				
				wallBody = wm.getPhysicsWorld().createBody(wallBodyDef);

				FixtureDef fixDef = new FixtureDef();
				fixDef.shape = wallShape;
				fixDef.density = 1.0f;
				fixDef.friction = 0.0f;
				fixDef.restitution = 0.0f;		
				wallBody.createFixture(fixDef);
				
				wallBody.setUserData(WallModel.this); // relacionar com o modelo
				
				// Sinaliza os subscritores de que a construção do modelo terminou.
				WallModel.this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));		
				
				return null;
			}
		});
		
	}
	
	public static WallModel getNewInstance(WorldModel wm, BasicShape wall, String wall_name){
		return new WallModel(wm, wall, wall_name);
	}

	
	@Override
	public void handleGameTick(GameTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
		if (wallBody != null)
			if (logger.getLevel() >= Logger.DEBUG)
				logger.debug("[Physics-room]: Pos.x:" + String.format("%.2f", getBody().getPosition().x)
					+ " Pos.y:" + String.format("%.2f", getBody().getPosition().y) 
					+ " Angle:" + String.format("%.2f", getBody().getAngle())
					+ " Mass:" + getBody().getMass()
					+ " Type:" + getBody().getType());
		

		// Corre a lógica de teleportação
		if ((boxTouchMyTralala > 0) && (box!=null)) {
			box.getBody().setTransform(wm.waypoint, box.getBody().getAngle());
			box = null;
		}
		
		
		
	}

	
	// BodyInterface implementation
	@Override
	public String getName() {
		return super.getName();
	}
	@Override
	public BasicShape getBasicShape() {
		return wall;
	}
	@Override
	public Vector2 getPosition() {
		return wallBody.getPosition();
	}
	@Override
	public float getAngle() {
		return wallBody.getAngle();
	}
	@Override
	public Body getBody() {
		return wallBody;
	}


	
	private int boxTouchMyTralala = 0;
	IActorBody box = null;
	@Override
	public void beginContactWith(IActorBody oModel) {
		if (boxTouchMyTralala == 0) 
			if (logger.getLevel() >= Logger.INFO) logger.info("daBox hit da wall!");
		boxTouchMyTralala +=1;
		box = oModel;
	}
	
	@Override
	public void endContactWith(IActorBody oModel) {
		boxTouchMyTralala -=1;
		if (boxTouchMyTralala == 0) 
			if (logger.getLevel() >= Logger.INFO) logger.info("daBox left the wall!");
	}
	
}
