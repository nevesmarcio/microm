package pt.me.microm.view;

import pt.me.microm.infrastructure.ScreenTickManager;
import pt.me.microm.infrastructure.interfaces.IScreenTick;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.events.IEvent;
import pt.me.microm.model.events.listener.IEventListener;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Logger;

public abstract class AbstractView implements Disposable, IScreenTick {
	private static final String TAG = AbstractView.class.getSimpleName();
	private static final Logger logger = new Logger(TAG);
	
	private AbstractModel model;
	
	public AbstractView(AbstractModel model) {
		this(model, 0);
	}
	
	public AbstractView(AbstractModel model, final int zIndex) {
		this.model = model;

		model.addListener(AbstractModel.EventType.ON_MODEL_INSTANTIATED, new IEventListener() {

			@Override
			public void onEvent(IEvent event) {
				DelayedInit( ); // Isto vai correr na thread do model
				// Regista este objecto para ser informado dos screen ticks
				// Este registo só pode ser efectuado depois do Modelo instanciado 
				ScreenTickManager.getInstance().addEventListener(AbstractView.this, zIndex);

			}
		});		
	}
	
	public abstract void DelayedInit();
	
	@Override
	public void dispose() {
		
		//Elimina o registo deste objecto para ser informado dos screen ticks
		ScreenTickManager.getInstance().removeEventListener(this);		
	}

}
