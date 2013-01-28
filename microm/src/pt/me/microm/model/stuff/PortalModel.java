package pt.me.microm.model.stuff;

import java.util.List;

import pt.me.microm.infrastructure.events.GameTickEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.PointerToFunction;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.model.events.SimpleEvent;
import pt.me.microm.tools.levelloader.BasicShape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class PortalModel extends AbstractModel {
	private static final String TAG = PortalModel.class.getSimpleName();

	public static enum PortalType {PORTAL_ENTRY, PORTAL_EXIT};
	
	private Vector2 portalPosition; // posição do tabuleiro no espaço
	
	private Vector2[] silhouetteVertex;
	
	private BodyDef portalBodyDef = new BodyDef();
	private ChainShape portalShape; // Fronteira do tabuleiro
	private Body portalBody;
	
	private WorldModel wm;
	
	public String portal_name;
	private PortalType portal_type;
	
	private PortalModel(final WorldModel wm, final BasicShape portal, final String portal_name) {

		wm.wmManager.add(new PointerToFunction() {
			
			@Override
			public void handler() {
				
				PortalModel.this.wm = wm;
				PortalModel.this.portal_name = portal_name;
				
				//deslocamento do centroid
				for (Vector2 v : portal.getPoints()) {
					v.sub(portal.getCentroid());
				}
				
				silhouetteVertex = portal.getPoints().toArray(new Vector2[]{});
				
				portalShape = new ChainShape();
				portalShape.createLoop(silhouetteVertex);
								
				portalBodyDef.position.set(portal.getCentroid()); // aqui devia calcular a posicao do centro de massa
				portalBodyDef.type = BodyType.StaticBody;
				
				setPortalBody(wm.getPhysicsWorld().createBody(portalBodyDef));

				FixtureDef fixDef = new FixtureDef();
				fixDef.shape = portalShape;
				fixDef.isSensor = true;
				fixDef.density = 1.0f;
				fixDef.friction = 0.0f;
				fixDef.restitution = 0.0f;		
				portalBody.createFixture(fixDef);
				getPortalBody().createFixture(fixDef);
					
				getPortalBody().setUserData(PortalModel.this); // relacionar com o modelo
				
				wm.addPortal(PortalModel.this);
				
				// Sinaliza os subscritores de que a construção do modelo terminou.
				PortalModel.this.dispatchEvent(new SimpleEvent(EventType.ON_MODEL_INSTANTIATED));		

			}
		});
		
	}
	
	public static PortalModel getNewInstance(WorldModel wm, BasicShape portal, String portal_name){
		return new PortalModel(wm, portal, portal_name);
	}

	
	@Override
	public void handleGameTick(GameTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
		if (getPortalBody() != null)
		Gdx.app.debug("[Physics-room]", 		  "Pos.x:" + String.format("%.2f", getPortalBody().getPosition().x)
				+ " Pos.y:" + String.format("%.2f", getPortalBody().getPosition().y) 
				+ " Angle:" + String.format("%.2f", getPortalBody().getAngle())
				+ " Mass:" + getPortalBody().getMass()
				+ " Type:" + getPortalBody().getType());
		
		// Corre a lógica de teleportação
		if ((boxTouchMyTralala > 0) && (box!=null)) {
			box.daBoxBody.setTransform(wm.getLinkedPortal(this).getPortalPosition(), box.daBoxBody.getAngle());
			box = null;
		}
		
	}

	
	/* Getters - Setters do tabuleiro */
	// Posição do tabuleiro
	public Vector2 getPortalPosition() {
		return portalBody.getPosition();
	}


	public Body getPortalBody() {
		return portalBody;
	}
	public void setPortalBody(Body portalBody) {
		this.portalBody = portalBody;
	}

	private int boxTouchMyTralala = 0;
	DaBoxModel box = null;
	@Override
	public void beginContactWith(AbstractModel oModel) {
		if (boxTouchMyTralala == 0) Gdx.app.log(TAG, "daBox touched my trálálá!! says: " + this.portal_name + ". Should be teleported to: " + this.portal_name.replace("entry", "exit"));
		boxTouchMyTralala +=1;
		box = (DaBoxModel)oModel;
	}
	
	@Override
	public void endContactWith(AbstractModel oModel) {
		boxTouchMyTralala -=1;
		if (boxTouchMyTralala == 0) Gdx.app.log(TAG, "daBox left my trálálá!! says: " + this.portal_name);
	}
	
}
