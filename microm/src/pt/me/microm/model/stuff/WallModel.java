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

public class WallModel extends AbstractModel {
	private static final String TAG = WallModel.class.getSimpleName();

	private Vector2 wallPosition; // posição da barreira
	
	private Vector2[] silhouetteVertex;
	
	private BodyDef wallBodyDef = new BodyDef();
	private ChainShape wallShape; // Fronteira do tabuleiro
	private Body wallBody;
	
	private WorldModel wm;
	
	private WallModel(final WorldModel wm, final BasicShape wall) {

		wm.wmManager.add(new PointerToFunction() {
			
			@Override
			public void handler() {
				
				WallModel.this.wm = wm;
				
				//deslocamento do centroid
				for (Vector2 v : wall.getPoints()) {
					v.sub(wall.getCentroid());
				}
				
				silhouetteVertex = wall.getPoints().toArray(new Vector2[]{});
				
				wallShape = new ChainShape();
				wallShape.createLoop(silhouetteVertex);
								

				// não esquecer que é este start-position que fode com a porca toda!
				wallBodyDef.position.set(wall.getCentroid()); // aqui devia calcular a posicao do centro de massa
				wallBodyDef.type = BodyType.StaticBody;
				
				setWallBody(wm.getPhysicsWorld().createBody(wallBodyDef));

				FixtureDef fixDef = new FixtureDef();
				fixDef.shape = wallShape;
				fixDef.isSensor = true;
				fixDef.density = 1.0f;
				fixDef.friction = 0.0f;
				fixDef.restitution = 0.0f;		
				wallBody.createFixture(fixDef);
				getWallBody().createFixture(fixDef);
					
				getWallBody().setUserData(WallModel.this); // relacionar com o modelo
				
				// Sinaliza os subscritores de que a construção do modelo terminou.
				WallModel.this.dispatchEvent(new SimpleEvent(EventType.ON_MODEL_INSTANTIATED));		

			}
		});
		
	}
	
	public static WallModel getNewInstance(WorldModel wm, BasicShape wall){
		return new WallModel(wm, wall);
	}

	
	@Override
	public void handleGameTick(GameTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
		if (getWallBody() != null)
		Gdx.app.debug("[Physics-room]", 		  "Pos.x:" + String.format("%.2f", getWallBody().getPosition().x)
				+ " Pos.y:" + String.format("%.2f", getWallBody().getPosition().y) 
				+ " Angle:" + String.format("%.2f", getWallBody().getAngle())
				+ " Mass:" + getWallBody().getMass()
				+ " Type:" + getWallBody().getType());
		

		
	}

	
	/* Getters - Setters do tabuleiro */
	// Posição do tabuleiro
	public Vector2 getWallPosition() {
		return wallBody.getPosition();
	}


	public Body getWallBody() {
		return wallBody;
	}
	public void setWallBody(Body wallBody) {
		this.wallBody = wallBody;
	}

	private int boxTouchMyTralala = 0;
	DaBoxModel box = null;
	@Override
	public void beginContactWith(AbstractModel oModel) {
//		if (boxTouchMyTralala == 0) Gdx.app.log(TAG, "daBox touched my trálálá!! says: " + this.wall_name);
//		boxTouchMyTralala +=1;
//		box = (DaBoxModel)oModel;
	}
	
	@Override
	public void endContactWith(AbstractModel oModel) {
//		boxTouchMyTralala -=1;
//		if (boxTouchMyTralala == 0) Gdx.app.log(TAG, "daBox left my trálálá!! says: " + this.wall_name);
	}
	
}
