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

public class GoalModel extends AbstractModel {
	private static final String TAG = GoalModel.class.getSimpleName();
	private static final Logger logger = new Logger(TAG);
	
	private Vector2[] silhouetteVertex;
	
	private BodyDef goalBodyDef = new BodyDef();
	private ChainShape goalShape; // Fronteira do tabuleiro
	private Body goalBody;
	
	private GoalModel(final WorldModel wm, final BasicShape goal, final List<Vector2> lst) {
		wm.wmManager.add(new PointerToFunction() {
			
			@Override
			public void handler() {
				
				//deslocamento do centroid
				for (Vector2 v : goal.getPoints()) {
					v.sub(goal.getCentroid());
				}				
				
				silhouetteVertex = lst.toArray(new Vector2[]{});
				
				goalShape = new ChainShape();
				goalShape.createLoop(silhouetteVertex);
				
				goalBodyDef.position.set(goal.getCentroid()); // posição inicial do tabuleiro
				goalBodyDef.type = BodyType.StaticBody;
				
				goalBody = wm.getPhysicsWorld().createBody(goalBodyDef);

				FixtureDef fixDef = new FixtureDef();
				fixDef.isSensor = true;
				fixDef.shape = goalShape;
				fixDef.density = 1.0f;
				fixDef.friction = 0.0f;
				fixDef.restitution = 0.0f;		
				goalBody.createFixture(fixDef);
				getBody().createFixture(fixDef);
					
				getBody().setUserData(GoalModel.this); // relacionar com o modelo
				
				
				// Sinaliza os subscritores de que a construção do modelo terminou.
				GoalModel.this.dispatchEvent(new SimpleEvent(EventType.ON_MODEL_INSTANTIATED));		
				
			}
		});
		
	}
	
	public static GoalModel getNewInstance(WorldModel wm, BasicShape goal, List<Vector2> pts){
		return new GoalModel(wm, goal, pts);
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
	}

	
	/* Getters - Setters do tabuleiro */
	// Posição do tabuleiro
	@Override
	public Vector2 getPosition() {
		return goalBody.getPosition();
	}
	@Override
	public Body getBody() {
		return goalBody;
	}

	
	@Override
	public void beginContactWith(AbstractModel oModel) {
		Gdx.app.log(TAG, "Oh yeah!!");
	}
	
	@Override
	public void endContactWith(AbstractModel oModel) {
		Gdx.app.log(TAG, "Oh nooooooooooooooo!!");
	}
	
	

}
