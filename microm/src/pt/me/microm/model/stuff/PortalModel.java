package pt.me.microm.model.stuff;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.events.GameTickEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.IBodyProperties;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.model.base.WorldModelManager.PointerToFunction;
import pt.me.microm.model.events.SimpleEvent;
import pt.me.microm.tools.levelloader.BasicShape;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Logger;

public class PortalModel extends AbstractModel implements IBodyProperties {
	private static final String TAG = PortalModel.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);

	public static enum PortalType {PORTAL_ENTRY, PORTAL_EXIT};
	
	private Vector2[] silhouetteVertex;
	
	private BodyDef portalBodyDef = new BodyDef();
	private ChainShape portalShape; // Fronteira do tabuleiro
	private Body portalBody;
	
	private WorldModel wm;
	private BasicShape portal;
	
	public String portal_name;
	private PortalType portal_type;
	
	private PortalModel(final WorldModel wm, final BasicShape portal, final String portal_name) {
		this.wm = wm;
		this.portal = portal;
		this.portal_name = portal_name;
		
		wm.wmManager.add(new PointerToFunction() {
			
			@Override
			public Object handler(Object ... a) {
				
				silhouetteVertex = portal.getPointsArray();
				
				portalShape = new ChainShape();
				portalShape.createLoop(silhouetteVertex);
								
				portalBodyDef.position.set(portal.getCentroid()); // aqui devia calcular a posicao do centro de massa
				portalBodyDef.type = BodyType.StaticBody;
				
				portalBody = wm.getPhysicsWorld().createBody(portalBodyDef);

				FixtureDef fixDef = new FixtureDef();
				fixDef.shape = portalShape;
				fixDef.isSensor = true;
				fixDef.density = 1.0f;
				fixDef.friction = 0.0f;
				fixDef.restitution = 0.0f;		
				portalBody.createFixture(fixDef);
					
				getBody().setUserData(PortalModel.this); // relacionar com o modelo
				
				wm.addPortal(PortalModel.this);
				
				// Sinaliza os subscritores de que a construção do modelo terminou.
				PortalModel.this.dispatchEvent(new SimpleEvent(EventType.ON_MODEL_INSTANTIATED));		

				return null;
			}
		});
		
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
			wm.waypoint = wm.getLinkedPortal(this).getPosition();
		}
		
	}

	
	// BodyInterface implementation
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
	IBodyProperties box = null;
	@Override
	public void beginContactWith(IBodyProperties oModel) {
		if (boxTouchMyTralala == 0) 
			if (logger.getLevel() >= Logger.INFO) logger.info("daBox touched my trálálá!! says: " + this.portal_name + ". Should be teleported to: " + this.portal_name.replace("entry", "exit"));
		boxTouchMyTralala +=1;
		box = oModel;
	}
	
	@Override
	public void endContactWith(IBodyProperties oModel) {
		boxTouchMyTralala -=1;
		if (boxTouchMyTralala == 0) 
			if (logger.getLevel() >= Logger.INFO) logger.info("daBox left my trálálá!! says: " + this.portal_name);
	}
	
}
