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
import com.badlogic.gdx.utils.Logger;

public class WallModel extends AbstractModel {
	private static final String TAG = WallModel.class.getSimpleName();
	private static final Logger logger = new Logger(TAG);

	private Vector2 wallPosition; // posição da barreira
	
	private Vector2[] silhouetteVertex;
	
	private BodyDef wallBodyDef = new BodyDef();
	private ChainShape wallShape; // Fronteira do tabuleiro
	private Body wallBody;
	
	private WorldModel wm;
	
	private WallModel(final WorldModel wm, final BasicShape wall) {

		wm.wmManager.add(new PointerToFunction() {
			
			@Override
			public Object handler(Object ... a) {
				
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
				
				wallBody = wm.getPhysicsWorld().createBody(wallBodyDef);

				FixtureDef fixDef = new FixtureDef();
				fixDef.shape = wallShape;
				fixDef.density = 1.0f;
				fixDef.friction = 0.0f;
				fixDef.restitution = 0.0f;		
				wallBody.createFixture(fixDef);
					
				getBody().setUserData(WallModel.this); // relacionar com o modelo
				
				// Sinaliza os subscritores de que a construção do modelo terminou.
				WallModel.this.dispatchEvent(new SimpleEvent(EventType.ON_MODEL_INSTANTIATED));		
				
				return null;
			}
		});
		
	}
	
	public static WallModel getNewInstance(WorldModel wm, BasicShape wall){
		return new WallModel(wm, wall);
	}

	
	@Override
	public void handleGameTick(GameTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
		if (getBody() != null)
			if (logger.getLevel() == logger.DEBUG)
				logger.debug("[Physics-room]: Pos.x:" + String.format("%.2f", getBody().getPosition().x)
					+ " Pos.y:" + String.format("%.2f", getBody().getPosition().y) 
					+ " Angle:" + String.format("%.2f", getBody().getAngle())
					+ " Mass:" + getBody().getMass()
					+ " Type:" + getBody().getType());
		

		// Corre a lógica de teleportação
		if ((boxTouchMyTralala > 0) && (box!=null)) {
			box.getBody().setTransform(wm.waypoint, box.getBody().getAngle());
			box = null;
		}
		
		
		
	}

	
	/* Getters - Setters do tabuleiro */
	@Override
	public Vector2 getPosition() {
		return wallBody.getPosition();
	}

	@Override
	public Body getBody() {
		return wallBody;
	}


	
	private int boxTouchMyTralala = 0;
	AbstractModel box = null;
	@Override
	public void beginContactWith(AbstractModel oModel) {
		if (boxTouchMyTralala == 0) 
			if (logger.getLevel() == logger.INFO) logger.info("daBox hit da wall!");
		boxTouchMyTralala +=1;
		box = (DaBoxModel)oModel;
	}
	
	@Override
	public void endContactWith(AbstractModel oModel) {
		boxTouchMyTralala -=1;
		if (boxTouchMyTralala == 0) 
			if (logger.getLevel() == logger.INFO) logger.info("daBox left the wall!");
	}
	
}
