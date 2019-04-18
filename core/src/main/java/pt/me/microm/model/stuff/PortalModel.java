package pt.me.microm.model.stuff;

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


public class PortalModel extends AbstractModel implements IBodyStatic {
    private static final String TAG = PortalModel.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(TAG);


    private Body portalBody;

    private BasicShape portal;

    private EventBus modelEventBus;

    private PortalModel(final EventBus modelEventBus, final BasicShape portal, final String portal_name) {
        this.modelEventBus = modelEventBus;
        this.portal = portal;
        setName(portal_name);

        modelEventBus.post(new PortalModelEvent(this, PortalModelEvent.OnCreate.class));

        // Sinaliza os subscritores de que a construção do modelo terminou.
        PortalModel.this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));

    }

    public static PortalModel getNewInstance(final EventBus modelEventBus, BasicShape portal, String portal_name) {
        return new PortalModel(modelEventBus, portal, portal_name);
    }


    @Override
    public void handleGameTick(GameTickEvent e) {
        long elapsedNanoTime = e.getElapsedNanoTime();

//		if (getBody() != null)
//			if (logger.getLevel()==Logger.DEBUG)
//				logger.debug("[Physics-room]: Pos.x:" + String.format("%.2f", getBody().getPosition().x)
//					+ " Pos.y:" + String.format("%.2f", getBody().getPosition().y) 
//					+ " Angle:" + String.format("%.2f", getBody().getAngle())
//					+ " Mass:" + getBody().getMass()
//					+ " Type:" + getBody().getType());

        // Corre a lógica de teleportação e waypoint
        if ((boxTouchMyTralala > 0) && (box != null)) {
            modelEventBus.post(new PortalModelEvent(this, PortalModelEvent.OnTouch.class));
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
        return portal;
    }

    @Override
    public Vector2 getPosition() {
        if (portalBody != null)
            return portalBody.getPosition();
        else
            return new Vector2();
    }

    @Override
    public float getAngle() {
        if (portalBody != null)
            return portalBody.getAngle();
        else
            return 0f;
    }

    @Override
    public Body getBody() {
        return portalBody;
    }

    @Override
    public void setBody(Body body) {
        this.portalBody = body;
    }

    // ContactInterface implementation
    private int boxTouchMyTralala = 0;
    IBody box = null;

    @Override
    public void beginContactWith(IBody oModel) {
        if (boxTouchMyTralala == 0)
            if (logger.isDebugEnabled())
                logger.info("daBox touched my trálálá!! says: " + this.getName() + ". Should be teleported to: " + this.getName().replace("entry", "exit"));
        boxTouchMyTralala += 1;
        box = oModel;
    }

    @Override
    public void endContactWith(IBody oModel) {
        boxTouchMyTralala -= 1;
        if (boxTouchMyTralala == 0)
            if (logger.isDebugEnabled()) logger.info("daBox left my trálálá!! says: " + this.getName());
    }

}
