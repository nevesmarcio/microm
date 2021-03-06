package pt.me.microm.model.dev;

import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.IActorBody;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.tools.levelloader.BasicShape;
import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Logger;

public class CoisaModel extends AbstractModel implements IActorBody {
	private static final String TAG = CoisaModel.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private Color color = new Color(0.5f,0.5f,0.5f,0.5f);
	
	private WorldModel wm;
	private Body coisaBody;	
	private Vector2 coisaModelOrigin;
	
	private CoisaModel(WorldModel wm, float xOffset, float yOffset) {
		this.wm = wm;
		
		// 0. Create a loader for the file saved from the editor.
		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("data/bodies/1st_example/1st_example.json"));

		// 1. Create a BodyDef, as usual.
		BodyDef bd = new BodyDef();
		bd.type = BodyType.DynamicBody;

		// 2. Create a FixtureDef, as usual.
		FixtureDef fd = new FixtureDef();
		fd.density = 1;
		fd.friction = 0.5f;
		fd.restitution = 0.3f;

		// 3. Create a Body, as usual.
		coisaBody = wm.getPhysicsWorld().createBody(bd);

		coisaBody.setUserData(CoisaModel.this); // relacionar com o modelo
		
		// 4. Create the body fixture automatically by using the loader.
		loader.attachFixture(coisaBody, "thing", fd, 1.0f);
		
		// offset
		coisaModelOrigin = loader.getOrigin("thing", 1.0f).cpy().add(xOffset, yOffset);
		coisaBody.setTransform(coisaModelOrigin, coisaBody.getAngle());

		// Sinaliza os subscritores de que a construção do modelo terminou.
		this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));		
	}

	public static CoisaModel getNewInstance(WorldModel wm, float xOffset, float yOffset){
		return new CoisaModel(wm, xOffset, yOffset);
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
		
	}

	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}


	//BodyInterface Implementation
	@Override
	public String getName() {
		return super.getName();
	}
	@Override
	public BasicShape getBasicShape() {
		//FIXME: qual é a shape ?
		return null;
	}
	@Override
	public Vector2 getPosition() {
		return coisaBody.getPosition();
	}
	@Override
	public float getAngle() {
		return coisaBody.getAngle();
	}
	@Override
	public Body getBody() {
		return coisaBody;
	}
	
	

	
	@Override /* related to ContactInterface */
	public void beginContactWith(IActorBody oModel) {
		// put non-specific contact logic @ MyContactListener
		// implement specific contact logic by overriding this method on a Model
		if (logger.getLevel() >= Logger.DEBUG) logger.debug("specific beginContactWith: " + this.getClass().getName());
	}
	
	@Override /* related to ContactInterface */
	public void endContactWith(IActorBody oModel) {
		// put non-specific contact logic @ MyContactListener
		// implement specific contact logic by overriding this method on a Model
		if (logger.getLevel() >= Logger.DEBUG) logger.debug("specific endContactWith: " + this.getClass().getName());
	}
	
	
	
}
