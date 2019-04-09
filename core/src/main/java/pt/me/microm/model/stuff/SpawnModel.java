package pt.me.microm.model.stuff;

import aurelienribon.tweenengine.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.IBody;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.model.ui.utils.FlashMessageManagerModel;
import pt.me.microm.model.ui.utils.IDataSourceObject;
import pt.me.microm.tools.levelloader.BasicShape;


public class SpawnModel extends AbstractModel implements IBody {
	private static final String TAG = SpawnModel.class.getSimpleName();
	private static final Logger logger = LoggerFactory.getLogger(TAG);
	
	private Body spawnBody;
	
	private WorldModel wm;
	private BasicShape spawn;
	
	private TweenManager tweenManager = new TweenManager();

    public void setDbm(final DaBoxModel dbm) {

        TweenCallback endCB = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {

//				wm.getWorldPhysicsManager().add(new ICommand() {
//
//					@Override
//					public Object handler(Object... a) {

                dbm.create(spawn.getCentroid());

//						return null;
//					}
//				});

            }

        };

        TweenCallback middleCB = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                FlashMessageManagerModel.getInstance().addFlashMessage(new IDataSourceObject<String>() {

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
                .push(Tween.call(middleCB).repeat(countdown-1, 1.0f))
                .push(Tween.call(endCB).delay(countdown))
                .end()
                .start(tweenManager);
    }

    private int countdown = 5;
	private SpawnModel(final WorldModel wm, final BasicShape spawn, final String spawn_name) {
		this.wm = wm;
		this.spawn = spawn;
		setName(spawn_name);
		
		spawnBody = wm.getWorldPhysicsManager().addBody(spawn, this);
		
		wm.setWaypoint(spawnBody.getPosition());

		// Sinaliza os subscritores de que a construção do modelo terminou.
		SpawnModel.this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));

	}
	
	public static SpawnModel getNewInstance(WorldModel wm, BasicShape spawn, String spawn_name){
		return new SpawnModel(wm, spawn, spawn_name);
	}

	
	@Override
	public void handleGameTick(GameTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
		if (spawnBody != null)
			if (logger.isDebugEnabled())
				logger.debug("[Physics-room]: Pos.x:" + String.format("%.2f", spawnBody.getPosition().x)
						+ " Pos.y:" + String.format("%.2f", spawnBody.getPosition().y) 
						+ " Angle:" + String.format("%.2f", spawnBody.getAngle())
						+ " Mass:" + spawnBody.getMass()
						+ " Type:" + spawnBody.getType());			
	
		// Faz o step das "animações"
		tweenManager.update(elapsedNanoTime/(float)GAME_CONSTANTS.ONE_SECOND_TO_NANO);	
	
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
	@Override
	public void setBody(Body body) {

	}

	// ContactInterface implementation
	@Override
	public void beginContactWith(IBody oModel) {

	}
	@Override
	public void endContactWith(IBody oModel) {
		logger.info("Oh nooooooooooooooo! Elvis has left the building!");
	}

	
}
