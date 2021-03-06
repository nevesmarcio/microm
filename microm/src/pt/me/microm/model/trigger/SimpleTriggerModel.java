package pt.me.microm.model.trigger;

import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.ICommand;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.IActorBody;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.tools.levelloader.BasicShape;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Logger;

public class SimpleTriggerModel extends AbstractModel implements IActorBody {
	private static final String TAG = SimpleTriggerModel.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);

	private Vector2[] silhouetteVertex;
	
	private BodyDef triggerBodyDef = new BodyDef();
	private ChainShape triggerShape; // Fronteira do tabuleiro
	private Body triggerBody;
	
	private WorldModel wm;
	private BasicShape trigger;
	private String script;
	
	private SimpleTriggerModel(final WorldModel wm, final BasicShape trigger, final String name) {
		this.wm = wm;
		this.trigger = trigger;
		setName(name);
		
		wm.wmManager.add(new ICommand() {
			
			@Override
			public Object handler(Object ... a) {
				
				silhouetteVertex = trigger.getPointsArray();
				
				triggerShape = new ChainShape();
				triggerShape.createLoop(silhouetteVertex);
								

				// não esquecer que é este start-position que fode com a porca toda!
				triggerBodyDef.position.set(trigger.getCentroid()); // aqui devia calcular a posicao do centro de massa
				triggerBodyDef.type = BodyType.StaticBody;
				
				triggerBody = wm.getPhysicsWorld().createBody(triggerBodyDef);

				FixtureDef fixDef = new FixtureDef();
				fixDef.shape = triggerShape;
				fixDef.density = 1.0f;
				fixDef.friction = 0.0f;
				fixDef.restitution = 0.0f;
				fixDef.isSensor = true;
				triggerBody.createFixture(fixDef);
					
				triggerBody.setUserData(SimpleTriggerModel.this); // relacionar com o modelo
				
				// Sinaliza os subscritores de que a construção do modelo terminou.
				SimpleTriggerModel.this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));		
				
				return null;
			}
		});
		
	}
	
	public static SimpleTriggerModel getNewInstance(WorldModel wm, BasicShape wall, String name){
		return new SimpleTriggerModel(wm, wall, name);
	}

	
	@Override
	public void handleGameTick(GameTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
		if (triggerBody != null)
			if (logger.getLevel() >= Logger.DEBUG)
				logger.debug("[Physics-room]: Pos.x:" + String.format("%.2f", getBody().getPosition().x)
					+ " Pos.y:" + String.format("%.2f", getBody().getPosition().y) 
					+ " Angle:" + String.format("%.2f", getBody().getAngle())
					+ " Mass:" + getBody().getMass()
					+ " Type:" + getBody().getType());

	
	}

	
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
	
	
	// BodyInterface implementation
	@Override 
	public String getName() {
		return super.getName();
	}
	@Override
	public BasicShape getBasicShape() {
		return trigger;
	}
	@Override
	public Vector2 getPosition() {
		return triggerBody.getPosition();
	}
	@Override
	public float getAngle() {
		return triggerBody.getAngle();
	}
	@Override
	public Body getBody() {
		return triggerBody;
	}


	@Override
	public void beginContactWith(IActorBody oModel) {
		if (logger.getLevel() >= Logger.DEBUG) logger.debug("da trigger has been hitted!");
	}
	
	@Override
	public void endContactWith(IActorBody oModel) {
		if (logger.getLevel() >= Logger.DEBUG) logger.debug("da trigger has been cleared!");
	}

}
