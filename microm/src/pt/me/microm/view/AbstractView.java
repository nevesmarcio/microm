package pt.me.microm.view;

import pt.me.microm.infrastructure.ScreenTickManager;
import pt.me.microm.infrastructure.interfaces.ScreenTickInterface;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.events.Event;
import pt.me.microm.model.events.listener.EventListener;

import com.badlogic.gdx.utils.Disposable;

public abstract class AbstractView implements Disposable, ScreenTickInterface {
	private static final String TAG = AbstractView.class.getSimpleName();
	
	private AbstractModel model;
	public AbstractView(AbstractModel model) {
		this(model, 0);
	}

	public AbstractView(AbstractModel model, final int zIndex) {
		this.model = model;
		model.addListener(AbstractModel.EventType.ON_MODEL_INSTANTIATED, new EventListener() {

			@Override
			public void onEvent(Event event) {
				// Regista este objecto para ser informado dos screen ticks
				// Este registo só pode ser efectuado depois do Modelo instanciado 
				ScreenTickManager.getInstance().addEventListener(AbstractView.this, zIndex);
			}
		});		
	}
	
	
	@Override
	public void dispose() {
		
		//Elimina o registo deste objecto para ser informado dos screen ticks
		ScreenTickManager.getInstance().removeEventListener(this);		
	}

}
