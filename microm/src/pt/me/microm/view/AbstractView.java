package pt.me.microm.view;


import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.me.microm.controller.loop.ScreenTickManager;
import pt.me.microm.controller.loop.itf.IScreenTick;
import pt.me.microm.infrastructure.event.IEvent;
import pt.me.microm.infrastructure.event.listener.IEventListener;
import pt.me.microm.model.AbstractModel;

import com.badlogic.gdx.utils.Disposable;


public abstract class AbstractView implements Disposable, IScreenTick {
	private static final String TAG = AbstractView.class.getSimpleName();
	private static final Logger logger = LoggerFactory.getLogger(TAG);
	
	private AbstractModel model;
	
	public AbstractView(AbstractModel model) {
		this(model, 0);
	}
	
	private UUID devID;
	public AbstractView(AbstractModel model, final int zIndex) {
		logger.info("ALLOC:" + (devID = UUID.randomUUID()).toString());
		
		this.model = model;

		this.model.addListener(AbstractModel.EventType.ON_MODEL_INSTANTIATED, new IEventListener() {

			@Override
			public void onEvent(IEvent event) {
				ScreenTickManager.PostRunnable(new Runnable() {
					@Override
					public void run() {
						DelayedInit( ); // Sem o PostRunnable isto iria correr na thread do model
						// Regista este objecto para ser informado dos screen ticks
						// Este registo só pode ser efectuado depois do Modelo instanciado 
						ScreenTickManager.getInstance().addEventListener(AbstractView.this, zIndex);

					}
				});				
			}
		});		
	}
	
	public abstract void DelayedInit();
	
	@Override
	public void dispose() {
		
		//Elimina o registo deste objecto para ser informado dos screen ticks
		ScreenTickManager.getInstance().removeEventListener(this);		
	}

	@Override
	protected void finalize() throws Throwable {
		logger.info("GC'ed:"+devID);
		super.finalize();
	}	
	
}
