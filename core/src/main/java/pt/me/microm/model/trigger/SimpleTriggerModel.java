package pt.me.microm.model.trigger;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.google.common.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.IBody;
import pt.me.microm.model.IBodyStatic;
import pt.me.microm.tools.levelloader.BasicShape;


public class SimpleTriggerModel extends AbstractModel implements IBodyStatic {
    private static final String TAG = SimpleTriggerModel.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(TAG);

    private Body triggerBody;

    private BasicShape trigger;
    private String script;

    private EventBus modelEventBus;

    private SimpleTriggerModel(final EventBus modelEventBus, final BasicShape trigger, final String name) {
        this.modelEventBus = modelEventBus;
        this.trigger = trigger;
        setName(name);

        modelEventBus.post(new SimpleTriggerModelEvent(this, SimpleTriggerModelEvent.OnCreate.class));

        // Sinaliza os subscritores de que a construção do modelo terminou.
        SimpleTriggerModel.this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));

    }

    public static SimpleTriggerModel getNewInstance(final EventBus modelEventBus, BasicShape trigger, String name) {
        return new SimpleTriggerModel(modelEventBus, trigger, name);
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
        if (triggerBody != null)
            return triggerBody.getPosition();
        else
            return new Vector2();
    }

    @Override
    public float getAngle() {
        if (triggerBody != null)
            return triggerBody.getAngle();
        else
            return 0f;
    }

    @Override
    public Body getBody() {
        return triggerBody;
    }

    @Override
    public void setBody(Body body) {
        this.triggerBody = body;
    }

    @Override
    public void beginContactWith(IBody oModel) {
        if (logger.isDebugEnabled()) logger.debug("da trigger has been hit!");
    }

    @Override
    public void endContactWith(IBody oModel) {
        if (logger.isDebugEnabled()) logger.debug("da trigger has been cleared!");
    }

}
