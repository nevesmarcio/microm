package pt.me.microm.model.stuff;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.google.common.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.IBodyDynamic;
import pt.me.microm.tools.levelloader.BasicShape;


public class DaBoxModel extends AbstractModel implements IBodyDynamic {
	private static final String TAG = DaBoxModel.class.getSimpleName();
	private static final Logger logger = LoggerFactory.getLogger(TAG);
	
	private Color color = new Color(0.5f,0.5f,0.5f,0.5f);

	private Body daBoxBody;

	private BasicShape dabox;
	
	public void create(Vector2 pos) {
		daBoxBody.setTransform(pos, daBoxBody.getAngle());
		// FIXME:: Activar o daBox!
		daBoxBody.setActive(true);		
	}
	
	private DaBoxModel(final EventBus modelEventBus, final BasicShape dabox, final String dabox_name) {
		this.dabox = dabox; 
		setName(dabox_name);

		modelEventBus.post(new DaBoxModelEvent(this, DaBoxModelEvent.OnCreate.class));


		// Sinaliza os subscritores de que a construção do modelo terminou.
		DaBoxModel.this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));	    				
		
	}

	public static DaBoxModel getNewInstance(EventBus modelEventBus, BasicShape dabox, String dabox_name){
		return new DaBoxModel(modelEventBus, dabox, dabox_name);
	}
	
	@Override
	public void handleGameTick(GameTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
		// internal engine
		//FIXME: julgo que este "set" à bruta do movimento provoca aqueles saltinhos estúpidos.
		// O setBullet = true não resolve o comportamento...
		// Com método do impulso o problema também se manifesta
		// Ao diminuir a espessura da skin, o problema parece manifestar-se menos frequentemente... Este comportamento dos saltinhos deve ter a ver com as aproximações do box2d
		// Ao fazer resize do screen aumenta a probabilidade de acontecer
		
		// Análise do link http://www.iforce2d.net/b2dtut/constant-speed
		
		// Método da velocidade linear
		//daBoxBody.setLinearVelocity(4.6f, daBoxBody.getLinearVelocity().y);

		if (daBoxBody!=null) {

			// Método do impulso
			Vector2 vel = daBoxBody.getLinearVelocity();
			float desiredXvel = 4.6f;
			float velChange = desiredXvel - vel.x;
			float impulse = daBoxBody.getMass() * velChange; //disregard time factor

			daBoxBody.applyLinearImpulse(new Vector2(impulse, 0.0f), daBoxBody.getWorldCenter(), true);
		}
	}

	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}

	public void jump() {
		if (daBoxBody!=null) {
			//FIXME: É necessário escalar as forças mediante o timestep
			float force_to_apply = 235f; //N
			daBoxBody.applyForceToCenter(0.0f, force_to_apply, true);
			daBoxBody.applyTorque(10.0f, true); //N.m
		}
	}

	// BodyInterface implementation
	@Override
	public String getName() {
		return super.getName();
	}
	@Override
	public BasicShape getBasicShape() {
		return dabox;
	}
	@Override
	public Vector2 getPosition() {
		return daBoxBody.getPosition();
	}
	@Override
	public float getAngle() {
		return daBoxBody.getAngle();
	}
	@Override
	public Body getBody() {
		return daBoxBody;
	}
	@Override
	public void setBody(Body body) {
		this.daBoxBody = body;
	}
	
	
}
