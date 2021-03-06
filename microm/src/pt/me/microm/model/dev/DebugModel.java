package pt.me.microm.model.dev;

import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.ICommand;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.base.WorldModel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Logger;

public class DebugModel extends AbstractModel {
	private static final String TAG = DebugModel.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private float radius = 0.075f;
	
	private Color color = new Color(1.0f,0.0f,0.0f,1.0f);
	
	private BodyDef debugBodyDef = new BodyDef();
	private CircleShape debugShape = new CircleShape();// PolygonShape();
	private Body debugBody;	
	
	private DebugModel(final WorldModel wm, final float x, final float y) {
		
		
		wm.wmManager.add(new ICommand() {
			@Override
			public Object handler(Object ... a) {
				debugBodyDef.type = BodyType.StaticBody;
				debugBodyDef.position.set(x, y);

				debugBodyDef.active = false;
				
				debugBody = wm.getPhysicsWorld().createBody(debugBodyDef);

				debugShape.setRadius(radius);
				/*fixture*/
				FixtureDef fixDef = new FixtureDef();
				fixDef.shape = debugShape;
				fixDef.density = 1.0f;
				fixDef.friction = 1.0f;
				fixDef.restitution = 0.75f;
				debugBody.createFixture(fixDef);
				
				debugBody.setUserData(DebugModel.this); // relacionar com o modelo
				
				debugBody.setSleepingAllowed(false);
				
				// Sinaliza os subscritores de que a construção do modelo terminou.
				DebugModel.this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));
				
				return null;
			}
		});

	}

	public static DebugModel getNewInstance(WorldModel wm, float x, float y){
		return new DebugModel(wm, x, y);
	}
	
	@Override
	public void handleGameTick(GameTickEvent e) {
		
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



	public Body getBody() {
		return debugBody;
	}
	
	
	
}
