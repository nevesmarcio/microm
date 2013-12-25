package pt.me.microm.model.stuff;

import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.IActorBody;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.tools.levelloader.BasicShape;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Logger;

public class GroundModel extends AbstractModel implements IActorBody {
	private static final String TAG = GroundModel.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private Body groundBody;
	
	private WorldModel wm;
	private BasicShape ground;
	
	private GroundModel(final WorldModel wm, final BasicShape ground, final String ground_name) {
		this.wm = wm;
		this.ground = ground;
		setName(ground_name);

		groundBody = wm.getWorldPhysicsManager().addBody(ground, this); 
				
		// Sinaliza os subscritores de que a construção do modelo terminou.
		GroundModel.this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));
	
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
		return super.getName();
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
