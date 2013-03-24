package pt.me.microm.model.collectible;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.events.GameTickEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.model.events.SimpleEvent;
import pt.me.microm.model.stuff.BoardModel;
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

public class StarModel extends AbstractModel {
	private static final String TAG = StarModel.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private Color color = new Color(0.5f,0.5f,0.5f,0.5f);
	
	public Body starBody;	
	public Vector2 starModelOrigin;
	
	public BasicShape star;
	
	private StarModel(WorldModel wm, final BasicShape star) {
		this.star = star;
		
		// 0. Create a loader for the file saved from the editor.
		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("data/bodies/collectibles/collectibles.json"));

		// 1. Create a BodyDef, as usual.
		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;

		// 2. Create a FixtureDef, as usual.
		FixtureDef fd = new FixtureDef();
		fd.density = 1;
		fd.friction = 0.5f;
		fd.restitution = 0.3f;

		// 3. Create a Body, as usual.
		starBody = WorldModel.getSingletonInstance().getPhysicsWorld().createBody(bd);

		
		float SCALE = star.getWidth(); // the width returned by the loader is normalized to allways = 1 
		
		// 4. Create the body fixture automatically by using the loader.
		loader.attachFixture(starBody, "star", fd, SCALE);
		
		float xOffset = star.getCentroid().x;
		float yOffset = star.getCentroid().y;
		
		// offset
		starModelOrigin = loader.getOrigin("star", SCALE).cpy().add(xOffset-star.getWidth()/2, yOffset-star.getHeight()/2);
		starBody.setTransform(starModelOrigin, starBody.getAngle());

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
		
	}

	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public Body getBody() {
		return starBody;
	}
	@Override
	public Vector2 getPosition() {
		return null;
	}
	
}
