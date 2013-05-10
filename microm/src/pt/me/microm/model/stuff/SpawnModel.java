package pt.me.microm.model.stuff;

import pt.me.microm.GameMicroM;
import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.ICommand;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.IActorBody;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.model.dev.BallModel;
import pt.me.microm.model.ui.utils.FlashMessageManagerModel;
import pt.me.microm.model.ui.utils.IDataSourceObject;
import pt.me.microm.tools.levelloader.BasicShape;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Logger;

public class SpawnModel extends AbstractModel implements IActorBody {
	private static final String TAG = SpawnModel.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private Vector2[] silhouetteVertex;
	
	private BodyDef spawnBodyDef = new BodyDef();
	private ChainShape spawnShape; // Fronteira do tabuleiro
	private Body spawnBody;
	
	private WorldModel wm;
	private BasicShape spawn;
	
	private int countdown = 4;
	private SpawnModel(final WorldModel wm, final DaBoxModel dbm, final BasicShape spawn, final String spawn_name) {
		this.wm = wm;
		this.spawn = spawn;
		setName(spawn_name);
		
		wm.wmManager.add(new ICommand() {

			@Override
			public Object handler(Object ... a) {
				
				silhouetteVertex = spawn.getPointsArray();
				
				spawnShape = new ChainShape();
				spawnShape.createLoop(silhouetteVertex);
				
				spawnBodyDef.position.set(spawn.getCentroid()); // posição inicial do tabuleiro
				spawnBodyDef.type = BodyType.StaticBody;
				
				spawnBody = wm.getPhysicsWorld().createBody(spawnBodyDef);

				FixtureDef fixDef = new FixtureDef();
				fixDef.shape = spawnShape;
				fixDef.isSensor = true;
				fixDef.density = 1.0f;
				fixDef.friction = 0.0f;
				fixDef.restitution = 0.0f;		
				spawnBody.createFixture(fixDef);
					
				getBody().setUserData(SpawnModel.this); // relacionar com o modelo
				
				wm.waypoint = getBody().getPosition();
				
				// Sinaliza os subscritores de que a construção do modelo terminou.
				SpawnModel.this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));
				
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

				wm.wmManager.add(new ICommand() {
					
					@Override
					public Object handler(Object... a) {

						dbm.create(spawn.getCentroid());
						
						// se não colocar isto depois da leitura do board, os objectos caem no espaço
						float Yoffset = 4.0f;
						if (GameMicroM.FLAG_DEV_ELEMENTS_B) {
//							for (float i=0.0f;i<5.0;i+=0.1f)
								BallModel.getNewInstance(wm, 2.0f/*+i*/, 4.0f+Yoffset); // larga a bola num mundo num tabuleiro
//							for (float i=0.0f;i<1.0;i+=0.1f)
//								CoisaModel.getNewInstance(wm, 1.0f/*+i*/, 2.0f+Yoffset);
								
						}
						
						return null;
					}
				});

			}

		};
		
		TweenCallback middleCB = new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				FlashMessageManagerModel.getInstance(wm.tweenManager).addFlashMessage(new IDataSourceObject<String>() {

					@Override
					public void set(String obj) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public String get() {
						
						return Integer.toString(countdown);
					}
				}, spawn.getCentroid().cpy(), 1.0f, true);
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
	
	public static SpawnModel getNewInstance(WorldModel wm, DaBoxModel dbm, BasicShape spawn, String spawn_name){
		return new SpawnModel(wm, dbm, spawn, spawn_name);
	}

	
	@Override
	public void handleGameTick(GameTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
		if (spawnBody != null)
			if (logger.getLevel() >= Logger.DEBUG)
				logger.debug("[Physics-room]: Pos.x:" + String.format("%.2f", spawnBody.getPosition().x)
						+ " Pos.y:" + String.format("%.2f", spawnBody.getPosition().y) 
						+ " Angle:" + String.format("%.2f", spawnBody.getAngle())
						+ " Mass:" + spawnBody.getMass()
						+ " Type:" + spawnBody.getType());			
	}

	
	// BodyInterface implementation
	@Override
	public String getName() {
		return super.getName();
	}
	@Override
	public BasicShape getBasicShape() {
		return spawn;
	}
	@Override
	public Vector2 getPosition() {
		return spawnBody.getPosition();
	}
	@Override
	public float getAngle() {
		return spawnBody.getAngle();
	}
	@Override
	public Body getBody() {
		return spawnBody;
	}
	

	// ContactInterface implementation
	@Override
	public void beginContactWith(IActorBody oModel) {

	}
	@Override
	public void endContactWith(IActorBody oModel) {
		logger.info("Oh nooooooooooooooo! Elvis has left the building!");
	}

	
}
