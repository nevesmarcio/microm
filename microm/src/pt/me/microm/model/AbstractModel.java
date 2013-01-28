package pt.me.microm.model;

import java.lang.reflect.Constructor;

import pt.me.microm.infrastructure.GameTickGenerator;
import pt.me.microm.infrastructure.interfaces.GameTickInterface;
import pt.me.microm.model.events.Event;
import pt.me.microm.model.events.dispatcher.EventDispatcher;
import pt.me.microm.model.events.listener.EventListener;
import pt.me.microm.view.AbstractView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;

/*
 * Esta classe abstracta garante que por cada modelo é automaticamente instanciada uma view
 * O controller actua directamente sobre o modelo, de tal forma que as views não tem sobre
 * ela quaisquer interacções para além da especificada pelo interface ScreenTickInterface
 */
public abstract class AbstractModel extends EventDispatcher implements Disposable, GameTickInterface, ContactInterface {
	private static final String TAG = AbstractModel.class.getSimpleName();
	
	public static enum EventType {
		ON_MODEL_INSTANTIATED // Event raised when model finishes its instantiation
	};	
	
	private AbstractView viewRef;
	
	public AbstractModel() {
		Gdx.app.debug(TAG, "++abstract ctor!");

		//Notifica terceiros da criação deste objecto
		//+++++
		String model = this.getClass().getName();
		model = model.replaceAll("model", "view"); //package part
		model = model.replaceAll("Model", "View"); //ClassName part

		try {
			Class<?> c = Class.forName(model);

			Constructor<?> con = c.getConstructor(this.getClass());
			Object o = con.newInstance(this);
		
			viewRef = (AbstractView) o;
		}
		catch (Exception e) {
			Gdx.app.error(TAG, "Error \"reflecting\" view: " + e.getMessage());
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

	
	@Override /* related to Disposable interface */
	public void dispose() {
		Gdx.app.debug(TAG, "--abstract dispose!");

		//Notifica terceiros da remoção deste objecto
		//-----
		viewRef.dispose();
		
		//Elimina o registo deste objecto para ser informado dos game ticks
		GameTickGenerator.getInstance().removeEventListener(this);
	}
	
	
	@Override /* related to ContactInterface */
	public void beginContactWith(AbstractModel oModel) {
		// put non-specific contact logic @ MyContactListener
		// implement specific contact logic by overriding this method on a Model
	}

	@Override /* related to ContactInterface */
	public void endContactWith(AbstractModel oModel) {
		// put non-specific contact logic @ MyContactListener
		// implement specific contact logic by overriding this method on a Model
	}	
	
}
