package pt.me.microm.model.stuff;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.IActorBody;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.tools.levelloader.BasicShape;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;


public class WallModel extends AbstractModel implements IActorBody {
	private static final String TAG = WallModel.class.getSimpleName();
	private static final Logger logger = LoggerFactory.getLogger(TAG);

	private Body wallBody;
	
	private WorldModel wm;
	private BasicShape wall;
	
	private WallModel(final WorldModel wm, final BasicShape wall, final String wall_name) {
		this.wm = wm;
		this.wall = wall;
		setName(wall_name);
		
		wallBody = wm.getWorldPhysicsManager().addBody(wall, this);

		// Sinaliza os subscritores de que a construção do modelo terminou.
		WallModel.this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));
		
	}
	
	public static WallModel getNewInstance(WorldModel wm, BasicShape wall, String wall_name){
		return new WallModel(wm, wall, wall_name);
	}

	
	@Override
	public void handleGameTick(GameTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
		if (wallBody != null)
			if (logger.isDebugEnabled())
				logger.debug("[Physics-room]: Pos.x:" + String.format("%.2f", getBody().getPosition().x)
					+ " Pos.y:" + String.format("%.2f", getBody().getPosition().y) 
					+ " Angle:" + String.format("%.2f", getBody().getAngle())
					+ " Mass:" + getBody().getMass()
					+ " Type:" + getBody().getType());
		

		// Corre a lógica de teleportação
		if ((boxTouchMyTralala > 0) && (box!=null)) {
			box.getBody().setTransform(wm.getWaypoint(), box.getBody().getAngle());
			box = null;
		}

	}

	
	// BodyInterface implementation - delegation pattern
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
			if (logger.isInfoEnabled()) logger.info("daBox hit da wall!");
		boxTouchMyTralala +=1;
		box = oModel;
	}
	
	@Override
	public void endContactWith(IActorBody oModel) {
		boxTouchMyTralala -=1;
		if (boxTouchMyTralala == 0) 
			if (logger.isInfoEnabled()) logger.info("daBox left the wall!");
	}
	
}
