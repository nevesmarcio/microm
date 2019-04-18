package pt.me.microm.model.stuff;

import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.google.common.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.AbstractModelEvent;
import pt.me.microm.model.IBodyStatic;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.tools.levelloader.BasicShape;


public class GroundModel extends AbstractModel implements IBodyStatic {
	private static final String TAG = GroundModel.class.getSimpleName();
	private static final Logger logger = LoggerFactory.getLogger(TAG);
	
	private Body groundBody;

	private BasicShape ground;

	private EventBus modelEventBus;
	
	private GroundModel(final EventBus modelEventBus, final BasicShape ground, final String ground_name) {
		this.modelEventBus = modelEventBus;
		this.ground = ground;
		setName(ground_name);

		// notify eventBus
		modelEventBus.post(new GroundModelEvent(this, GroundModelEvent.OnCreate.class));

		// Sinaliza os subscritores de que a construção do modelo terminou.
		GroundModel.this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));
	
	}
	
	public static GroundModel getNewInstance(EventBus modelEventBus, BasicShape ground, String ground_name){
		return new GroundModel(modelEventBus, ground, ground_name);
	}

	
	@Override
	public void handleGameTick(GameTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
		if (groundBody != null)
			if (logger.isDebugEnabled())
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
		if (groundBody!=null)
		return groundBody.getPosition();
		else
			return new Vector2();
	}
	@Override
	public float getAngle() {
		if (groundBody!=null) {
			return groundBody.getAngle();
		}
		else
			return 0f;
	}
	@Override
	public Body getBody() {
		return groundBody;
	}
	@Override
	public void setBody(Body body) {
		this.groundBody=body;

	}


}
