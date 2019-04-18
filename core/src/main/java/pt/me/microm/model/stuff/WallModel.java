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


public class WallModel extends AbstractModel implements IBodyStatic {
    private static final String TAG = WallModel.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(TAG);

    private Body wallBody;

    private BasicShape wall;

    private EventBus modelEventBus;

    private WallModel(final EventBus modelEventBus, final BasicShape wall, final String wall_name) {
        this.modelEventBus = modelEventBus;
        this.wall = wall;
        setName(wall_name);

        // notify eventBus
        modelEventBus.post(new WallModelEvent(this, WallModelEvent.OnCreate.class));

        // Sinaliza os subscritores de que a construção do modelo terminou.
        WallModel.this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));

    }

    public static WallModel getNewInstance(EventBus modelEventBus, BasicShape wall, String wall_name) {
        return new WallModel(modelEventBus, wall, wall_name);
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
        if ((boxTouchMyTralala > 0) && (box != null)) {
            modelEventBus.post(new WallModelEvent(this,WallModelEvent.OnTouch.class));
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
        if (wallBody != null)
            return wallBody.getPosition();
        else
            return new Vector2();
    }

    @Override
    public float getAngle() {
        if (wallBody != null)
            return wallBody.getAngle();
        else
            return 0f;
    }

    @Override
    public Body getBody() {
        return wallBody;
    }

    @Override
    public void setBody(Body body) {
        this.wallBody = body;
    }

    private int boxTouchMyTralala = 0;
    IBody box = null;

    @Override
    public void beginContactWith(IBody oModel) {
        if (boxTouchMyTralala == 0)
            if (logger.isInfoEnabled()) logger.info("daBox hit da wall!");
        boxTouchMyTralala += 1;
        box = oModel;
    }

    @Override
    public void endContactWith(IBody oModel) {
        boxTouchMyTralala -= 1;
        if (boxTouchMyTralala == 0)
            if (logger.isInfoEnabled()) logger.info("daBox left the wall!");
    }

}
