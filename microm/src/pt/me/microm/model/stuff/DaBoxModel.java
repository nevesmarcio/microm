package pt.me.microm.model.stuff;

import pt.me.microm.infrastructure.events.GameTickEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.PointerToFunction;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.model.events.SimpleEvent;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class DaBoxModel extends AbstractModel {
	private static final String TAG = DaBoxModel.class.getSimpleName();
	
	private float side = 0.75f;
	
	private Color color = new Color(0.5f,0.5f,0.5f,0.5f);
	
	private BodyDef daBoxBodyDef = new BodyDef();
	private PolygonShape daBoxShape = new PolygonShape();
	public Body daBoxBody;	
	

	private DaBoxModel(final WorldModel wm, final float scale, final float x, final float y) {
		
		Tween.call(new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				Gdx.app.postRunnable(new Runnable() {

					@Override
					public void run() {

					
						
						
						wm.wmManager.add(new PointerToFunction() {
							
							@Override
							public void handler() {
								
								daBoxBodyDef.type = BodyType.DynamicBody;
								daBoxBodyDef.position.set(x, y);

								daBoxBody = wm.getPhysicsWorld().createBody(daBoxBodyDef);

								daBoxShape.setAsBox(side/2, side/2);
								/*fixture*/
								FixtureDef fixDef = new FixtureDef();
								fixDef.shape = daBoxShape;
								fixDef.density = 1.0f;
								fixDef.friction = 0.0f;
								fixDef.restitution = 0.0f;
								daBoxBody.createFixture(fixDef);
								
								daBoxBody.setUserData(DaBoxModel.this); // relacionar com o modelo
								
								daBoxBody.setSleepingAllowed(false);
											
								// Sinaliza os subscritores de que a construção do modelo terminou.
								DaBoxModel.this.dispatchEvent(new SimpleEvent(EventType.ON_MODEL_INSTANTIATED));				
								
							}
						});						
						
						
						
						

					}
				});

			}

		}).delay(2.0f).start(wm.tweenManager);		
		

		
		

	}

	public static DaBoxModel getNewInstance(WorldModel wm, float scale, float x, float y){
		return new DaBoxModel(wm, scale, x, y);
	}
	
	@Override
	public void handleGameTick(GameTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
		//FIXME temporarily disabled
		daBoxBody.setLinearVelocity(4.0f, daBoxBody.getLinearVelocity().y);
	}

	public float getSide() {
		return side;
	}

	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}

	public void jump() {

		// É necessário escalar as forças mediante o timestep 
		float force_to_apply = 425f; //N
		daBoxBody.applyForceToCenter(0.0f, force_to_apply);
		daBoxBody.applyTorque(-7.0f); //N.m
	}
	
	@Override
	public Body getBody() {
		return daBoxBody;
	}
	
	@Override
	public Vector2 getPosition() {
		return daBoxBody.getPosition();
	}	
	
}
