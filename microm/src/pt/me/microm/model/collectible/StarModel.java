package pt.me.microm.model.collectible;

import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.IActorBody;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.model.phenomenon.CollisionModel;
import pt.me.microm.tools.levelloader.BasicShape;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Logger;

public class StarModel extends AbstractModel implements IActorBody {
	private static final String TAG = StarModel.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private Color color = new Color(0.5f,0.5f,0.5f,0.5f);
	
	private Body starBody;	
	
	private WorldModel wm;
	private BasicShape star;
	
	private StarModel(WorldModel wm, final BasicShape star, final String star_name) {
		this.wm = wm;
		this.star = star;
		setName(star_name);
		
		starBody = wm.getWorldPhysicsManager().addBody(star, this);
		
		// Sinaliza os subscritores de que a construção do modelo terminou.
		this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));		
		
	}

	public static StarModel getNewInstance(WorldModel wm, BasicShape star, String star_name){
		return new StarModel(wm, star, star_name);
	}
	
	@Override
	public void handleGameTick(GameTickEvent e) {
//		long elapsedNanoTime = e.getElapsedNanoTime();
//
//		Gdx.app.debug("[Physics-ball]"," :"+ballBody.getLinearVelocity().len() + " ::" + ballBody.getPosition().x + "--" +ballBody.getPosition().y);
//		
//		Gdx.app.debug("[Physics-ball]", 		  "Pos.x:" + String.format("%.2f", ballBody.getPosition().x)
//				+ " Pos.y:" + String.format("%.2f", ballBody.getPosition().y) 
//				+ " Angle:" + String.format("%.2f", ballBody.getAngle())
//				+ " Mass:" + ballBody.getMass()
//				+ " Type:" + ballBody.getType());
		
		starBody.setAngularVelocity(2.0f);
		
	}

	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}


	
	// BodyInterface Implementation
	@Override
	public String getName() {
		return super.getName();
	}
	@Override
	public BasicShape getBasicShape() {
		return star;
	}
	@Override
	public Vector2 getPosition() {
		return starBody.getPosition();
	}
	@Override
	public float getAngle() {
		return starBody.getAngle();
	}
	@Override
	public Body getBody() {
		return starBody;
	}

	
	
	@Override
	public void beginContactWith(IActorBody oModel) {
		
		if (logger.getLevel() >= Logger.DEBUG) logger.debug("collision detected");

		CollisionModel.getNewInstance(getPosition());	//oModel.getPosition()

//		wm.wmManager.add(new PointerToFunction() {
//			
//			@Override
//			public Object handler(Object... a) {
//				wm.getPhysicsWorld().destroyBody(starBody);
//				
//				return null;
//			}
//		});
//		
//		
//		this.dispose();

	}

	
	@Override
	public void endContactWith(IActorBody oModel) {
		
	}	

	
}
