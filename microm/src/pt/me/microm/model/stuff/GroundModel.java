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

public class GroundModel extends AbstractModel {
	private static final String TAG = GroundModel.class.getSimpleName();
	
	private Vector2 groundPosition; // posição do tabuleiro no espaço
	
	private Vector2[] silhouetteVertex;
	
	private BodyDef groundBodyDef = new BodyDef();
	private ChainShape groundShape; // Fronteira do tabuleiro
	private Body groundBody;
	
	
	private GroundModel(final WorldModel wm, final List<Vector2> lst) {
		
		wm.wmManager.add(new PointerToFunction() {
			
			@Override
			public void handler() {

				silhouetteVertex = lst.toArray(new Vector2[]{});
				
				groundShape = new ChainShape();
				groundShape.createLoop(silhouetteVertex);
				
				groundBodyDef.position.set(0.0f, 0.0f); // posição inicial do tabuleiro
				groundBodyDef.type = BodyType.StaticBody;
				
				groundBody = wm.getPhysicsWorld().createBody(groundBodyDef);

				FixtureDef fixDef = new FixtureDef();
				fixDef.shape = groundShape;
				fixDef.density = 1.0f;
				fixDef.friction = 0.9f;
				fixDef.restitution = 0.0f;		
				groundBody.createFixture(fixDef);
				getBody().createFixture(fixDef);
					
				getBody().setUserData(GroundModel.this); // relacionar com o modelo
				
				
				// Sinaliza os subscritores de que a construção do modelo terminou.
				GroundModel.this.dispatchEvent(new SimpleEvent(EventType.ON_MODEL_INSTANTIATED));	
				
			}
		});
	
	}
	
	public static GroundModel getNewInstance(WorldModel wm, List<Vector2> pts){
		return new GroundModel(wm, pts);
	}

	
	@Override
	public void handleGameTick(GameTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
		if (getBody() != null)
		Gdx.app.debug("[Physics-room]", 		  "Pos.x:" + String.format("%.2f", getBody().getPosition().x)
				+ " Pos.y:" + String.format("%.2f", getBody().getPosition().y) 
				+ " Angle:" + String.format("%.2f", getBody().getAngle())
				+ " Mass:" + getBody().getMass()
				+ " Type:" + getBody().getType());			
	}

	
	/* Getters - Setters do tabuleiro */
	// Posição do tabuleiro
	@Override
	public Vector2 getPosition() {
		return groundPosition;
	}
	@Override
	public Body getBody() {
		return groundBody;
	}
	

}
