package pt.me.microm.model.trigger;

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


public class SimpleTriggerModel extends AbstractModel implements IActorBody {
	private static final String TAG = SimpleTriggerModel.class.getSimpleName();
	private static final Logger logger = LoggerFactory.getLogger(TAG);

	private Body triggerBody;
	
	private WorldModel wm;
	private BasicShape trigger;
	private String script;
	
	private SimpleTriggerModel(final WorldModel wm, final BasicShape trigger, final String name) {
		this.wm = wm;
		this.trigger = trigger;
		setName(name);
		
		triggerBody = wm.getWorldPhysicsManager().addBody(trigger, this);
		
		// Sinaliza os subscritores de que a construção do modelo terminou.
		SimpleTriggerModel.this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));	
		
	}
	
	public static SimpleTriggerModel getNewInstance(WorldModel wm, BasicShape wall, String name){
		return new SimpleTriggerModel(wm, wall, name);
	}

	
	@Override
	public void handleGameTick(GameTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
		if (triggerBody != null)
			if (logger.isDebugEnabled())
				logger.debug("[Physics-room]: Pos.x:" + String.format("%.2f", getBody().getPosition().x)
					+ " Pos.y:" + String.format("%.2f", getBody().getPosition().y) 
					+ " Angle:" + String.format("%.2f", getBody().getAngle())
					+ " Mass:" + getBody().getMass()
					+ " Type:" + getBody().getType());

	
	}

	
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
	
	
	// BodyInterface implementation
	@Override 
	public String getName() {
		return super.getName();
	}
	@Override
	public BasicShape getBasicShape() {
		return trigger;
	}
	@Override
	public Vector2 getPosition() {
		return triggerBody.getPosition();
	}
	@Override
	public float getAngle() {
		return triggerBody.getAngle();
	}
	@Override
	public Body getBody() {
		return triggerBody;
	}


	@Override
	public void beginContactWith(IActorBody oModel) {
		if (logger.isDebugEnabled()) logger.debug("da trigger has been hitted!");
	}
	
	@Override
	public void endContactWith(IActorBody oModel) {
		if (logger.isDebugEnabled()) logger.debug("da trigger has been cleared!");
	}

}
