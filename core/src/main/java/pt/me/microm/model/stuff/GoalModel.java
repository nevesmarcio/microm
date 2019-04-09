package pt.me.microm.model.stuff;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.IActorBody;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.tools.levelloader.BasicShape;


public class GoalModel extends AbstractModel implements IActorBody {
	private static final String TAG = GoalModel.class.getSimpleName();
	private static final Logger logger = LoggerFactory.getLogger(TAG);
	
	private Body goalBody;
	
	private WorldModel wm;
	private BasicShape goal;
	
	private GoalModel(final WorldModel wm, final BasicShape goal, final String goal_name) {
		this.wm = wm;
		this.goal = goal;
		setName(goal_name);

		//todo: this cannot be added here - it is on a different thread where the game simulation occurs ,therefore prone to create problems
		goalBody = wm.getWorldPhysicsManager().addBody(goal, this);

		// Sinaliza os subscritores de que a construção do modelo terminou.
		GoalModel.this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));		

	}
	
	public static GoalModel getNewInstance(WorldModel wm, BasicShape goal, String goal_name){
		return new GoalModel(wm, goal, goal_name);
	}

	
	@Override
	public void handleGameTick(GameTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
		if (getBody() != null)
			if (logger.isDebugEnabled())
				logger.debug("[Physics-room]: Pos.x:" + String.format("%.2f", getBody().getPosition().x)
					+ " Pos.y:" + String.format("%.2f", getBody().getPosition().y) 
					+ " Angle:" + String.format("%.2f", getBody().getAngle())
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
		return goal;
	}
	@Override
	public Vector2 getPosition() {
		return goalBody.getPosition();
	}
	@Override
	public float getAngle() {
		return goalBody.getAngle();
	}
	@Override
	public Body getBody() {
		return goalBody;
	}
	@Override
	public void setBody(Body body) {

	}
	
	// ContactInterface implementation
	@Override
	public void beginContactWith(IActorBody oModel) {
		logger.info("Oh yeah! Elvis has entered the building!");
		wm.dispatchEvent(new SimpleEvent(WorldModel.EventType.ON_WORLD_COMPLETED));
	}
	
	@Override
	public void endContactWith(IActorBody oModel) {
	}
	
	

}
