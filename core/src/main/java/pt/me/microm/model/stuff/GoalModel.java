package pt.me.microm.model.stuff;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.google.common.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.AbstractModelEvent;
import pt.me.microm.model.IBody;
import pt.me.microm.model.IBodyStatic;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.tools.levelloader.BasicShape;


public class GoalModel extends AbstractModel implements IBodyStatic {
    private static final String TAG = GoalModel.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(TAG);

    private Body goalBody;

    private BasicShape goal;

    private EventBus modelEventBus;

    private GoalModel(final EventBus modelEventBus, final BasicShape goal, final String goal_name) {
        this.modelEventBus = modelEventBus;
        this.goal = goal;
        setName(goal_name);

        // notify eventBus
        modelEventBus.post(new GoalModelEvent(this, GoalModelEvent.OnCreate.class));

        // Sinaliza os subscritores de que a construção do modelo terminou.
        GoalModel.this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));

    }

    public static GoalModel getNewInstance(EventBus modelEventBus, BasicShape goal, String goal_name) {
        return new GoalModel(modelEventBus, goal, goal_name);
    }


    @Override
    public void handleGameTick(GameTickEvent e) {
        long elapsedNanoTime = e.getElapsedNanoTime();

        if (goalBody != null) {
            if (getBody() != null)
                if (logger.isDebugEnabled())
                    logger.debug("[Physics-room]: Pos.x:" + String.format("%.2f", getBody().getPosition().x)
                            + " Pos.y:" + String.format("%.2f", getBody().getPosition().y)
                            + " Angle:" + String.format("%.2f", getBody().getAngle())
                            + " Mass:" + getBody().getMass()
                            + " Type:" + getBody().getType());

        }
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
        if (goalBody != null)
            return goalBody.getPosition();
        else
            return new Vector2();
    }

    @Override
    public float getAngle() {
        if (goalBody != null)
            return goalBody.getAngle();
        else
            return 0f;
    }

    @Override
    public Body getBody() {
        return goalBody;
    }

    @Override
    public void setBody(Body body) {
        this.goalBody = body;
    }

    // ContactInterface implementation
    @Override
    public void beginContactWith(IBody oModel) {
        logger.info("Oh yeah! Elvis has entered the building!");
        // notify eventBus
        modelEventBus.post(new GoalModelEvent(this, GoalModelEvent.OnTouch.class));
    }

    @Override
    public void endContactWith(IBody oModel) {
    }


}
