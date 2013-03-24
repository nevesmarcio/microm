package pt.me.microm.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import pt.me.microm.GameMicroM;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.GameTickGenerator;
import pt.me.microm.infrastructure.interfaces.GameTickInterface;
import pt.me.microm.model.events.Event;
import pt.me.microm.model.events.dispatcher.EventDispatcher;
import pt.me.microm.model.events.listener.EventListener;
import pt.me.microm.view.AbstractView;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Logger;

/*
 * Esta classe abstracta garante que por cada modelo é automaticamente instanciada uma view
 * O controller actua directamente sobre o modelo, de tal forma que as views não tem sobre
 * ela quaisquer interacções para além da especificada pelo interface ScreenTickInterface
 */
public abstract class AbstractModel extends EventDispatcher implements Disposable, GameTickInterface, ContactInterface {
	private static final String TAG = AbstractModel.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	public static enum EventType {
		ON_MODEL_INSTANTIATED // Event raised when model finishes its instantiation
	};	
	
	private AbstractView viewRef;
	
	public AbstractModel() {
		//Notifica terceiros da criação deste objecto
		//+++++
		String model = this.getClass().getName();
		if (logger.getLevel() >= Logger.DEBUG) logger.debug("++abstract ctor! - " + model);
		
		model = model.replaceAll("model", "view"); //package part
		model = model.replaceAll("Model", "View"); //ClassName part

		try {
			Class<?> c = Class.forName(model);

			Constructor<?> con = c.getConstructor(this.getClass());
			Object o = con.newInstance(this);
		
			viewRef = (AbstractView) o;
		}
		catch (InvocationTargetException ite) {
			if (logger.getLevel() >= Logger.ERROR) logger.error("Error \"reflecting\" view: " + ite.getTargetException().getMessage());
			if (GameMicroM.FLAG_DEV_ELEMENTS)
				ite.getTargetException().printStackTrace();
		}
		catch (Exception ex) {
			if (logger.getLevel() >= Logger.ERROR) logger.error("Serious error \"reflecting\" view: " + ex.getMessage());
		}
		
		// Tanto a abstractView como o abstractModel escutam o ON_MODEL_INSTANTIATED 
		// para saber quando podem registar os eventos screenTick e gameTick respectivamente 
		this.addListener(AbstractModel.EventType.ON_MODEL_INSTANTIATED, new EventListener() {

			@Override
			public void onEvent(Event event) {
				//Regista este objecto para ser informado dos game ticks 
				GameTickGenerator.getInstance().addEventListener(AbstractModel.this);
			}
		});		
		
		// Este evento tem que ser lançado por casa Model após toda a instanciação efectuada.
		// Tipicamente é a última linha do constructor de cada Modelo. 
		// this.dispatchEvent(new SimpleEvent(EventType.ON_MODEL_INSTANTIATED));
	}

	//public abstract Body getBody();
	//public abstract Vector2 getPosition();
	//public abstract float getAngle();
	
	@Override /* related to Disposable interface */
	public void dispose() {
		if (logger.getLevel() >= Logger.DEBUG) logger.debug("--abstract dispose!");

		//Notifica terceiros da remoção deste objecto
		//-----
		viewRef.dispose();
		
		//Elimina o registo deste objecto para ser informado dos game ticks
		GameTickGenerator.getInstance().removeEventListener(this);
	}
	
	
	@Override /* related to ContactInterface */
	public void beginContactWith(BodyInterface oModel) {
		// put non-specific contact logic @ MyContactListener
		// implement specific contact logic by overriding this method on a Model
	}

	@Override /* related to ContactInterface */
	public void endContactWith(BodyInterface oModel) {
		// put non-specific contact logic @ MyContactListener
		// implement specific contact logic by overriding this method on a Model
	}	
	
}
