package pt.me.navigator.infrastructure;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

import pt.me.navigator.infrastructure.events.ScreenTickEvent;
import pt.me.navigator.infrastructure.interfaces.ScreenTickInterface;
import pt.me.navigator.model.base.CameraModel;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Logger;

public class ScreenTickManager implements Disposable {
	private static final String TAG = ScreenTickManager.class.getSimpleName();
	private static final Logger logger = new Logger(TAG);
	
	private List<ScreenTickInterface> _listeners = new ArrayList<ScreenTickInterface>();

	public synchronized void addEventListener(ScreenTickInterface listener) {
		_listeners.add(listener);
	}

	public synchronized void removeEventListener(ScreenTickInterface listener) {
		_listeners.remove(listener);
	}


	/**
	 * Call this method whenever you want to notify
	 * the event listeners of the particular event
	 * 
	 * @param camera
	 * @param elapsedNanoTime
	 */
	private ScreenTickEvent event = new ScreenTickEvent(this); 									// reutilização do evento
	private List<ScreenTickInterface> temp_listeners = new ArrayList<ScreenTickInterface>(); 	// reutilização da lista de listeners
	private Iterator<ScreenTickInterface> i; 													// reutilização da variável do iterator
	private boolean print = false;
	private ScreenTickInterface x; 
	public synchronized void fireEvent(CameraModel camModel, long elapsedNanoTime) {
		event.setCamera(camModel);
		event.setElapsedNanoTime(elapsedNanoTime);
		
		try {
			//FIXME: implementar heuristica para saber se vale ou não a pena - tradeoff entre memória / cpu :: guardar flag na estrutura
			/* cria uma copia para iterar */
			
			if (_listeners.size() != temp_listeners.size()) {
				if (logger.getLevel() == logger.DEBUG) logger.debug("[diff] _listeners|temp_listeners: " + _listeners.size() + "|" + temp_listeners.size());
				temp_listeners.clear();
				temp_listeners.addAll(_listeners);
				print = true;
			}
			
			i = temp_listeners.iterator();
			while (i.hasNext()) {
				x = (ScreenTickInterface) i.next();
				x.draw(event);
				if (print) {
					if (logger.getLevel() == logger.DEBUG) logger.debug(x.getClass().getName());
				}
			}
			print = false;
		} catch (ConcurrentModificationException ex) {
			if (logger.getLevel() == logger.DEBUG) logger.debug("[ScreenTickGen-EXCEPTION] CurrentThreadID: " + Long.toString(Thread.currentThread().getId()));
			throw ex;
		}		

	}
	
	private static ScreenTickManager instance = null;

	private ScreenTickManager() {
	}
	
	public static ScreenTickManager getInstance() {
		if (instance == null) {
			instance = new ScreenTickManager();
		}
		return instance;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
