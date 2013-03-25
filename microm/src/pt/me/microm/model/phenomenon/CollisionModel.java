package pt.me.microm.model.phenomenon;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.events.GameTickEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.BodyInterface;
import pt.me.microm.model.PointerToFunction;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.model.events.SimpleEvent;
import pt.me.microm.tools.levelloader.BasicShape;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Logger;

public class CollisionModel extends AbstractModel {
	private static final String TAG = CollisionModel.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	public ParticleEffect particleEffect;
	public Vector2 position;
	
	private CollisionModel(Vector2 position) {
		this.position = position;
		
		particleEffect = new ParticleEffect();
	    particleEffect.load(Gdx.files.internal("data/particles/collision.p"), Gdx.files.internal("data/particles"));
	    particleEffect.start();
	    
		// Sinaliza os subscritores de que a construção do modelo terminou.
		CollisionModel.this.dispatchEvent(new SimpleEvent(EventType.ON_MODEL_INSTANTIATED));	    
	}

	public static CollisionModel getNewInstance(Vector2 position){
		return new CollisionModel(position);
	}
	
	@Override
	public void handleGameTick(GameTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
	}

	
}
