package pt.me.microm.model.dev;

import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.ICanCollide;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.model.base.WorldModelManager.PointerToFunction;
import pt.me.microm.tools.levelloader.BasicShape;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Logger;

public class BallModel extends AbstractModel implements ICanCollide {
	private static final String TAG = BallModel.class.getSimpleName();
	private static final Logger logger = new Logger(TAG);
	
	private float radius = 0.5f;
	
	private Color color = new Color(0.5f,0.5f,0.5f,0.5f);
	
	private BodyDef ballBodyDef = new BodyDef();
	private CircleShape ballShape = new CircleShape();// PolygonShape();
	private Body ballBody;	
	

	private BallModel(final WorldModel wm, final float x, final float y) {
		wm.wmManager.add(new PointerToFunction() {
			
			@Override
			public Object handler(Object ... a) {
				ballBodyDef.type = BodyType.DynamicBody;
				ballBodyDef.position.set(x, y); // coordenada de largada da bola

				//ballBodyDef.bullet =  true;
				
				ballBody = wm.getPhysicsWorld().createBody(ballBodyDef);

				ballShape.setRadius(radius);
				/*fixture*/
				FixtureDef fixDef = new FixtureDef();
				fixDef.shape = ballShape;
				fixDef.density = 1.0f;
				fixDef.friction = 0.9f;
				fixDef.restitution = 0.5f;
				ballBody.createFixture(fixDef);
				
				ballBody.setUserData(BallModel.this); // relacionar com o modelo
				
				// É necessário escalar as forças mediante o timestep 
				//float force_to_apply = -10.0f / (float)GAME_CONSTANTS.GAME_TICK_MILI * (float)GAME_CONSTANTS.ONE_SECOND_TO_MILI;
				//wDynamicBox.applyForceToCenter(0.0f, force_to_apply);
				
//				ballBody.setLinearVelocity(200.0f, 0.0f);//13 devido ao diametro (14-1)

				ballBody.setSleepingAllowed(true);
				ballBody.setBullet(false);
				
				//wDynamicBox.applyTorque(50.0f);
						
				// Sinaliza os subscritores de que a construção do modelo terminou.
				BallModel.this.dispatchEvent(new SimpleEvent(EventType.ON_MODEL_INSTANTIATED));
				
				return null;
				
			}
		});
		
		

	}

	public static BallModel getNewInstance(WorldModel wm, float x, float y){
		return new BallModel(wm, x, y);
	}
	
	@Override
	public void handleGameTick(GameTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();

		if (logger.getLevel() >= Logger.DEBUG) {
			logger.debug("[Physics-ball] :"+ballBody.getLinearVelocity().len() + " ::" + ballBody.getPosition().x + "--" +ballBody.getPosition().y);
			logger.debug("[Physics-ball] : Pos.x:" + String.format("%.2f", ballBody.getPosition().x)
					+ " Pos.y:" + String.format("%.2f", ballBody.getPosition().y) 
					+ " Angle:" + String.format("%.2f", ballBody.getAngle())
					+ " Mass:" + ballBody.getMass()
					+ " Type:" + ballBody.getType());			

		}
		
	}

	public float getRadius() {
		return radius;
	}

	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}


	// BodyInterface Implementation
	@Override
	public BasicShape getBasicShape() {
		//FIXME: qual é a shape ?
		return null;
	}
	@Override
	public Vector2 getPosition() {
		return ballBody.getPosition();
	}	
	@Override
	public float getAngle() {
		return ballBody.getAngle();
	}
	@Override
	public Body getBody() {
		return ballBody;
	}
	
}
