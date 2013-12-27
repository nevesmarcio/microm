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


public class PortalModel extends AbstractModel implements IActorBody {
	private static final String TAG = PortalModel.class.getSimpleName();
	private static final Logger logger = LoggerFactory.getLogger(TAG);

	public static enum PortalType {PORTAL_ENTRY, PORTAL_EXIT};
	
	private Body portalBody;
	
	private WorldModel wm;
	private BasicShape portal;
	
	private PortalType portal_type;
	
	private PortalModel(final WorldModel wm, final BasicShape portal, final String portal_name) {
		this.wm = wm;
		this.portal = portal;
		setName(portal_name);
		
		portalBody = wm.getWorldPhysicsManager().addBody(portal, this);
		
		wm.addPortal(PortalModel.this);
		
		// Sinaliza os subscritores de que a construção do modelo terminou.
		PortalModel.this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));		
		
	}
	
	public static PortalModel getNewInstance(WorldModel wm, BasicShape portal, String portal_name){
		return new PortalModel(wm, portal, portal_name);
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
		if ((boxTouchMyTralala > 0) && (box!=null)) {
			box.getBody().setTransform(wm.getLinkedPortal(this).getPosition(), box.getBody().getAngle());
			box = null;
			wm.setWaypoint(wm.getLinkedPortal(this).getPosition());
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
		return portalBody.getPosition();
	}	
	@Override
	public float getAngle() {
		return portalBody.getAngle();
	}
	@Override
	public Body getBody() {
		return portalBody;
	}
	
	
	// ContactInterface implementation
	private int boxTouchMyTralala = 0;
	IActorBody box = null;
	@Override
	public void beginContactWith(IActorBody oModel) {
		if (boxTouchMyTralala == 0) 
			if (logger.isDebugEnabled()) logger.info("daBox touched my trálálá!! says: " + this.getName() + ". Should be teleported to: " + this.getName().replace("entry", "exit"));
		boxTouchMyTralala +=1;
		box = oModel;
	}
	
	@Override
	public void endContactWith(IActorBody oModel) {
		boxTouchMyTralala -=1;
		if (boxTouchMyTralala == 0) 
			if (logger.isDebugEnabled()) logger.info("daBox left my trálálá!! says: " + this.getName());
	}
	
}
