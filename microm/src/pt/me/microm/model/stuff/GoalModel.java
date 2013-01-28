package pt.me.microm.model.stuff;

import java.util.List;

import pt.me.microm.infrastructure.events.GameTickEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.PointerToFunction;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.model.events.SimpleEvent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class GoalModel extends AbstractModel {
	private static final String TAG = GoalModel.class.getSimpleName();
	
	private Vector2 goalPosition; // posição do goal
	
	private Vector2[] silhouetteVertex;
	
	private BodyDef goalBodyDef = new BodyDef();
	private ChainShape goalShape; // Fronteira do tabuleiro
	private Body goalBody;
	
	private GoalModel(final WorldModel wm, final List<Vector2> lst) {
		wm.wmManager.add(new PointerToFunction() {
			
			@Override
			public void handler() {
				silhouetteVertex = lst.toArray(new Vector2[]{});
				
				goalShape = new ChainShape();
				goalShape.createLoop(silhouetteVertex);
				
				goalBodyDef.position.set(0.0f, 0.0f); // posição inicial do tabuleiro
				goalBodyDef.type = BodyType.StaticBody;
				goalBodyDef.active = false;
				
				setGoalBody(wm.getPhysicsWorld().createBody(goalBodyDef));

				FixtureDef fixDef = new FixtureDef();
				fixDef.shape = goalShape;
				fixDef.density = 1.0f;
				fixDef.friction = 0.0f;
				fixDef.restitution = 0.0f;		
				goalBody.createFixture(fixDef);
				getGoalBody().createFixture(fixDef);
					
				getGoalBody().setUserData(GoalModel.this); // relacionar com o modelo
				
				
				// Sinaliza os subscritores de que a construção do modelo terminou.
				GoalModel.this.dispatchEvent(new SimpleEvent(EventType.ON_MODEL_INSTANTIATED));		
				
			}
		});
		
	}
	
	public static GoalModel getNewInstance(WorldModel wm, List<Vector2> pts){
		return new GoalModel(wm, pts);
	}

	
	@Override
	public void handleGameTick(GameTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
		if (getGoalBody() != null)
		Gdx.app.debug("[Physics-room]", 		  "Pos.x:" + String.format("%.2f", getGoalBody().getPosition().x)
				+ " Pos.y:" + String.format("%.2f", getGoalBody().getPosition().y) 
				+ " Angle:" + String.format("%.2f", getGoalBody().getAngle())
				+ " Mass:" + getGoalBody().getMass()
				+ " Type:" + getGoalBody().getType());			
	}

	
	/* Getters - Setters do tabuleiro */
	// Posição do tabuleiro
	public Vector2 getGoalPosition() {
		return goalPosition;
	}
	public void setGoalPosition(Vector2 goalPosition) {
		this.goalPosition = goalPosition;
	}

	public Body getGoalBody() {
		return goalBody;
	}
	public void setGoalBody(Body goalBody) {
		this.goalBody = goalBody;
	}

}
