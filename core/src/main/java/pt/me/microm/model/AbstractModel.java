package pt.me.microm.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.me.microm.controller.loop.GameTickGenerator;
import pt.me.microm.controller.loop.itf.IGameTick;
import pt.me.microm.infrastructure.event.IEvent;
import pt.me.microm.infrastructure.event.dispatcher.EventDispatcher;
import pt.me.microm.infrastructure.event.listener.IEventListener;
import pt.me.microm.view.AbstractView;

import com.badlogic.gdx.utils.Disposable;

/*
 * Esta classe abstracta garante que por cada modelo é automaticamente instanciada uma view
 * O controller actua directamente sobre o modelo, de tal forma que as views não tem sobre
 * ela quaisquer interacções para além da especificada pelo interface ScreenTickInterface
 */
public abstract class AbstractModel extends EventDispatcher implements Disposable, IGameTick, IContact {
	private static final String TAG = AbstractModel.class.getSimpleName();
	private static final Logger logger = LoggerFactory.getLogger(TAG);
	
	public static enum EventType {
		ON_MODEL_INSTANTIATED // Event raised when model finishes its instantiation
	};	
	
	private AbstractView viewRef;
	
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}	
	
	
	private UUID devID;
	public AbstractModel() {
		//Notifica terceiros da criação deste objecto
		String model = this.getClass().getName();
		setName(model); // default name
		if (logger.isDebugEnabled()) logger.debug("++abstract ctor of {} | allocID: {}", this.getClass().getSimpleName(), (devID=UUID.randomUUID()).toString());
		
		model = model.replaceAll("model", "view"); //package part
		model = model.replaceAll("Model", "View"); //ClassName part

		try {
			Class<?> c = Class.forName(model);

			Constructor<?> con = c.getConstructor(this.getClass());
			Object o = con.newInstance(this);
		
			viewRef = (AbstractView) o;
		}
		catch (InvocationTargetException ite) {
			if (logger.isErrorEnabled()) logger.error("Error \"reflecting\" view: ", ite.getTargetException());
			if (logger.isErrorEnabled()) logger.error("exception: ", ite.getTargetException());
		}
		catch (Exception ex) {
			if (logger.isErrorEnabled()) logger.error("Serious error \"reflecting\" view: ", ex);
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

	
	@Override /* related to Disposable interface */
	public void dispose() {
		if (logger.isDebugEnabled()) logger.debug("--abstract dispose of {} | allocID: {}", this.getClass().getSimpleName(), devID);

		//Notifica a view instanciada por reflection para tambem seja removida
		viewRef.dispose();
		viewRef = null;
		
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
		if (logger.isDebugEnabled()) logger.debug("abstract beginContactWith: {}", this.getClass().getName());
		
		int incrementedValue = -1;
		try {
			incrementedValue = currentContactStatus.get(oModel).intValue() + 1;
			currentContactStatus.put(oModel, new Integer(incrementedValue)); // incrementa o nr de contactos com determinado objecto
		} catch (NullPointerException npe) {
			incrementedValue = new Integer(1);
			currentContactStatus.put(oModel, incrementedValue);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) logger.error("exception: ", e);
		}
		
		return incrementedValue;		
	}

	@Override /* related to IContact interface */
	public int subtractPointOfContactWith(IActorBody oModel) {
		// put non-specific contact logic @ MyContactListener
		// implement specific contact logic by overriding this method on a Model
		if (logger.isDebugEnabled()) logger.debug("abstract endContactWith: {}", this.getClass().getName());
		
		int decrementedValue = -1;
		try {
			decrementedValue = currentContactStatus.get(oModel).intValue() - 1;
			currentContactStatus.put(oModel, new Integer(decrementedValue)); // decrementa o nr de contactos com determinado objecto
			if (currentContactStatus.get(oModel).intValue() == 0) currentContactStatus.remove(oModel);
			
		} catch (Exception e) {
			if (logger.isErrorEnabled()) logger.error("exception: ", e);
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
		if (logger.isDebugEnabled()) logger.debug("{} was GC'ed | allocID: {}", this.getClass().getSimpleName(), devID);
		super.finalize();
	}
	
}
