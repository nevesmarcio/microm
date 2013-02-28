package pt.me.microm.model.stuff;

import java.util.List;
import java.util.Random;

import pt.me.microm.GameMicroM;
import pt.me.microm.infrastructure.events.GameTickEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.PointerToFunction;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.model.dev.BallModel;
import pt.me.microm.model.dev.CoisaModel;
import pt.me.microm.model.events.SimpleEvent;
import pt.me.microm.model.ui.UIModel.Accessor;
import pt.me.microm.tools.levelloader.BasicShape;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Logger;

public class SpawnModel extends AbstractModel {
	private static final String TAG = SpawnModel.class.getSimpleName();
	private static final Logger logger = new Logger(TAG);
	
	private Vector2[] silhouetteVertex;
	
	private BodyDef spawnBodyDef = new BodyDef();
	private ChainShape spawnShape; // Fronteira do tabuleiro
	private Body spawnBody;
	
	private WorldModel wm;
	private BasicShape spawn;
	
	private int countdown = 4;
	private SpawnModel(final WorldModel wm, final DaBoxModel dbm, final BasicShape spawn) {
		this.wm = wm;
		this.spawn = spawn;
		
		wm.wmManager.add(new PointerToFunction() {

			@Override
			public Object handler(Object ... a) {
				
				silhouetteVertex = spawn.getPointsArray();
				
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
				
//				/* Logo após a construção */
//				Tween.call(new TweenCallback() {
//					@Override
//					public void onEvent(int type, BaseTween<?> source) {
//						BallModel.getNewInstance(wm,
//								SpawnModel.this.spawnBody.getPosition().x,
//								SpawnModel.this.spawnBody.getPosition().y);
//					}
//				}).repeat(10, 1.0f).start(wm.tweenManager);				
				
				return null;
			}
		});
		
		
		TweenCallback endCB = new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {

				wm.wmManager.add(new PointerToFunction() {
					
					@Override
					public Object handler(Object... a) {

						dbm.create(spawn.getCentroid());
						
						// se não colocar isto depois da leitura do board, os objectos caem no espaço
						if (GameMicroM.FLAG_DEV_ELEMENTS) {
							BallModel.getNewInstance(wm, 3.0f, 4.0f); // larga a bola num mundo num tabuleiro
							CoisaModel.getNewInstance(wm, 1.0f, 4.0f);
						}
						
						return null;
					}
				});

			}

		};
		
		TweenCallback middleCB = new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				wm.ui.addFlashMessage(new Accessor<String>() {

					@Override
					public void set(String obj) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public String get() {
						
						return Integer.toString(countdown);
					}
				}, spawn.getCentroid());
				countdown-=1;
			}
		};
		
		Timeline.createSequence()
			.beginParallel()
				.push(Tween.call(middleCB).repeat(2, 1.0f))
				.push(Tween.call(endCB).delay(3.0f))
			.end()
			.start(wm.tweenManager);
		//Tween.call(endCB).start(wm.tweenManager);
//		Tween.call(middleCB).repeat(3, 0.0f).setCallback(endCB).start(wm.tweenManager);


	}
	
	public static SpawnModel getNewInstance(WorldModel wm, DaBoxModel dbm, BasicShape spawn){
		return new SpawnModel(wm, dbm, spawn);
	}

	
	@Override
	public void handleGameTick(GameTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
		if (getBody() != null)
			if (logger.getLevel() == Logger.DEBUG)
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
		return spawnBody.getPosition();
	}

	@Override
	public Body getBody() {
		return spawnBody;
	}

	public BasicShape getBasicShape() {
		return spawn;
	}
	
}
