package pt.me.microm.model.dev;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.google.common.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.AbstractModelEvent;


public class DebugModel extends AbstractModel {
    private static final String TAG = DebugModel.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(TAG);

    private Vector2 position;
    private float radius = 0.075f;

    private Color color = new Color(1.0f, 0.0f, 0.0f, 1.0f);

    private DebugModel(EventBus modelEventBus, final float x, final float y) {
        this.position = new Vector2(x, y);

        // notify eventBus
        modelEventBus.post(new DebugModelEvent(this, AbstractModelEvent.OnCreate.class));

        // Sinaliza os subscritores de que a construção do modelo terminou.
        DebugModel.this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));
    }

    public static DebugModel getNewInstance(EventBus modelEventBus, float x, float y) {
        return new DebugModel(modelEventBus, x, y);
    }

    @Override
    public void handleGameTick(GameTickEvent e) {

    }

    public float getRadius() {
        return radius;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
