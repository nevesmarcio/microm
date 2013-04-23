package pt.me.microm.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.UUID;

import pt.me.microm.GameMicroM;
import pt.me.microm.controller.loop.GameTickGenerator;
import pt.me.microm.controller.loop.itf.IGameTick;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.event.IEvent;
import pt.me.microm.infrastructure.event.dispatcher.EventDispatcher;
import pt.me.microm.infrastructure.event.listener.IEventListener;
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
public abstract class AbstractModel extends EventDispatcher implements Disposable, IGameTick, IContact {
	private static final String TAG = AbstractModel.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	public static enum EventType {
		ON_MODEL_INSTANTIATED // Event raised when model finishes its instantiation
	};	
	
	private AbstractView viewRef;
	
	private UUID devID;
	public AbstractModel() {
		logger.info("ALLOC:" + (devID = UUID.randomUUID()).toString());
		
		//Notifica terceiros da criação deste objecto
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
		this.addListener(AbstractModel.EventType.ON_MODEL_INSTANTIATED, new IEventListener() {

			@Override
			public void onEvent(IEvent event) {
				//Regista este objecto para ser informado dos game ticks 
				GameTickGenerator.getInstance().addEventListener(AbstractModel.this);
			}
		});		
		
		// Este evento tem que ser lançado por cada Model após toda a instanciação efectuada.
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
		
		// para todos os objectos referenciados como colisões, tem que haver a respectiva remoção
		for (IActorBody element : currentContactStatus.keySet()) {
			((AbstractModel)element).disposeNotif(this);
		}
		
		
		//Elimina o registo deste objecto para ser informado dos game ticks
		GameTickGenerator.getInstance().removeEventListener(this);
	}
	
	
	private HashMap<IActorBody, Integer> currentContactStatus = new HashMap<IActorBody, Integer>();
	
	@Override /* related to IContact interface */
	public int addPointOfContactWith(IActorBody oModel) {
		// put non-specific contact logic @ MyContactListener
		// implement specific contact logic by overriding this method on a Model
		if (logger.getLevel() >= Logger.DEBUG) logger.debug("abstract beginContactWith: " + this.getClass().getName());
		
		int incrementedValue = -1;
		try {
			incrementedValue = currentContactStatus.get(oModel).intValue() + 1;
			currentContactStatus.put(oModel, new Integer(incrementedValue)); // incrementa o nr de contactos com determinado objecto
		} catch (NullPointerException npe) {
			incrementedValue = new Integer(1);
			currentContactStatus.put(oModel, incrementedValue);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		return incrementedValue;		
	}

	@Override /* related to IContact interface */
	public int subtractPointOfContactWith(IActorBody oModel) {
		// put non-specific contact logic @ MyContactListener
		// implement specific contact logic by overriding this method on a Model
		if (logger.getLevel() >= Logger.DEBUG) logger.debug("abstract endContactWith: " + this.getClass().getName());
		
		int decrementedValue = -1;
		try {
			decrementedValue = currentContactStatus.get(oModel).intValue() - 1;
			currentContactStatus.put(oModel, new Integer(decrementedValue)); // decrementa o nr de contactos com determinado objecto
			if (currentContactStatus.get(oModel).intValue() == 0) currentContactStatus.remove(oModel);
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		return decrementedValue;
	}
	
	
	@Override /* related to IContact interface */
	//public abstract void beginContactWith(ICanCollide oModel);
	public void beginContactWith(IActorBody oModel) {}
	@Override /* related to IContact interface */
	//public abstract void endContactWith(ICanCollide oModel);
	public void endContactWith(IActorBody oModel) {}
	
	
	@Override /* related to IContact interface */
	public void disposeNotif(AbstractModel oModel) {
		if (currentContactStatus.containsKey(oModel))
			currentContactStatus.remove(oModel);
	}
	
	@Override
	protected void finalize() throws Throwable {
		logger.info("GC'ed:"+devID);
		super.finalize();
	}	
	
}
