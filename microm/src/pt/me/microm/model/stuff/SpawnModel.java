package pt.me.microm.model.stuff;

import java.util.List;
import java.util.Random;

import pt.me.microm.infrastructure.events.GameTickEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.PointerToFunction;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.model.dev.BallModel;
import pt.me.microm.model.events.SimpleEvent;
import pt.me.microm.tools.levelloader.BasicShape;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class SpawnModel extends AbstractModel {
	private static final String TAG = SpawnModel.class.getSimpleName();
	
	private Vector2[] silhouetteVertex;
	
	private BodyDef spawnBodyDef = new BodyDef();
	private ChainShape spawnShape; // Fronteira do tabuleiro
	private Body spawnBody;
	
	
	private SpawnModel(final WorldModel wm, final BasicShape spawn, final List<Vector2> lst) {
		wm.wmManager.add(new PointerToFunction() {

			@Override
			public void handler() {
				
				//deslocamento do centroid
				for (Vector2 v : spawn.getPoints()) {
					v.sub(spawn.getCentroid());
				}				
				
				silhouetteVertex = lst.toArray(new Vector2[]{});
				
				spawnShape = new ChainShape();
				spawnShape.createLoop(silhouetteVertex);
				
				spawnBodyDef.position.set(spawn.getCentroid()); // posição inicial do tabuleiro
				spawnBodyDef.type = BodyType.StaticBody;
				spawnBodyDef.active = false;
				
				spawnBody = wm.getPhysicsWorld().createBody(spawnBodyDef);

				FixtureDef fixDef = new FixtureDef();
				fixDef.shape = spawnShape;
				fixDef.density = 1.0f;
				fixDef.friction = 0.0f;
				fixDef.restitution = 0.0f;		
				spawnBody.createFixture(fixDef);
				getBody().createFixture(fixDef);
					
				getBody().setUserData(SpawnModel.this); // relacionar com o modelo
				
				wm.waypoint = getBody().getPosition();
				
				// Sinaliza os subscritores de que a construção do modelo terminou.
				SpawnModel.this.dispatchEvent(new SimpleEvent(EventType.ON_MODEL_INSTANTIATED));
				
				
				/* Logo após a construção */
				Tween.call(new TweenCallback() {
				@Override public void onEvent(int type, BaseTween<?> source) {
					Gdx.app.postRunnable(new Runnable() {
						
						@Override
						public void run() {
							BallModel.getNewInstance(wm, SpawnModel.this.spawnBody.getPosition().x, SpawnModel.this.spawnBody.getPosition().y);
							
						}
					});
				}
			}).repeat(10, 5.0f).start(wm.tweenManager);				

			}
		});
		
	}
	
	public static SpawnModel getNewInstance(WorldModel wm, BasicShape spawn, List<Vector2> pts){
		return new SpawnModel(wm, spawn, pts);
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
		return spawnBody.getPosition();
	}

	@Override
	public Body getBody() {
		return spawnBody;
	}

}
