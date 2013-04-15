package pt.me.microm.model.collectible;

import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.ICanCollide;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.model.base.WorldModelManager.PointerToFunction;
import pt.me.microm.model.phenomenon.CollisionModel;
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

public class StarModel extends AbstractModel implements ICanCollide {
	private static final String TAG = StarModel.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private Color color = new Color(0.5f,0.5f,0.5f,0.5f);
	
	private Body starBody;	
	private Vector2 starModelOrigin;
	
	private WorldModel wm;
	private BasicShape star;
	
	private StarModel(WorldModel wm, final BasicShape star) {
		this.wm = wm;
		this.star = star;
		
		// 0. Create a loader for the file saved from the editor.
		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("data/bodies/collectibles/collectibles.json"));

		// 1. Create a BodyDef, as usual.
		BodyDef bd = new BodyDef();
		bd.type = BodyType.KinematicBody;

		// 2. Create a FixtureDef, as usual.
		FixtureDef fd = new FixtureDef();
		fd.density = 1;
		fd.friction = 0.5f;
		fd.restitution = 0.3f;
		fd.isSensor = true;

		// 3. Create a Body, as usual.
		starBody = WorldModel.getSingletonInstance().getPhysicsWorld().createBody(bd);

		float modelScale = star.getWidth(); // The width returned by the BodyEditorLoader is normalized to 1. Thats why the scale = <basicshape>.getWidth. 
		
		// 4. Create the body fixture automatically by using the loader.
		loader.attachFixture(starBody, "star", fd, modelScale);
		
		float xOffset = star.getCentroid().x;
		float yOffset = star.getCentroid().y;
		
		// offset
		starModelOrigin = loader.getOrigin("star", modelScale).cpy().add(xOffset-star.getWidth()/2, yOffset-star.getHeight()/2);
		starBody.setTransform(starModelOrigin, starBody.getAngle());

		starBody.setUserData(this); // relacionar com o modelo
		
		
		// Sinaliza os subscritores de que a construção do modelo terminou.
		this.dispatchEvent(new SimpleEvent(EventType.ON_MODEL_INSTANTIATED));		
	}

	public static StarModel getNewInstance(WorldModel wm, BasicShape star){
		return new StarModel(wm, star);
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
		
		starBody.setAngularVelocity(8.0f);
		
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
	public void beginContactWith(ICanCollide oModel) {
		
		if (logger.getLevel() >= Logger.INFO) logger.info("collision detected");

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
	public void endContactWith(ICanCollide oModel) {
		
	}	

	
}
