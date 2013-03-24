package pt.me.microm.infrastructure;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pt.me.microm.infrastructure.events.ScreenTickEvent;
import pt.me.microm.infrastructure.interfaces.ScreenTickInterface;
import pt.me.microm.model.base.CameraModel;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Logger;
/**
 * documentar esta merda, senão daqui a uns tempos (horas), não percebo bolha do que escrevi
 * 
 * 
 * @author mneves
 *
 */
public class ScreenTickManager implements Disposable {
	private static final String TAG = ScreenTickManager.class.getSimpleName();
	private static final Logger logger = new Logger(TAG);
	
	private TreeMap<Integer, List<ScreenTickInterface>> _listeners = new TreeMap<Integer, List<ScreenTickInterface>>();
	
	/**
	 * the default zIndex is 0
	 * @param listener
	 */
	public synchronized void addEventListener(ScreenTickInterface listener) {
		addEventListener(listener, 0);
	}
	
	/**
	 * specifies a zIndex
	 * @param listener
	 * @param zIndex
	 */
	public synchronized void addEventListener(ScreenTickInterface listener, int zIndex) {
		try {
			_listeners.get(zIndex).add(listener);
		} catch (Exception e) { //FIXME: especificar a excepção
			_listeners.put(zIndex, new ArrayList<ScreenTickInterface>());
			_listeners.get(zIndex).add(listener);
		}
		
		isTempListenersDirty = true;
	}

	public synchronized void removeEventListener(ScreenTickInterface listener) {
		for (List<ScreenTickInterface> subList : _listeners.values()) {
			try {
				subList.remove(listener);
				isTempListenersDirty = true;
			} catch (Exception e) { //FIXME: especificar a excepção
				if (logger.getLevel() == Logger.DEBUG) logger.debug(e.getMessage());
			}
		}

	}


	/**
	 * Call this method whenever you want to notify
	 * the event listeners of the particular event
	 * 
	 * @param camera
	 * @param elapsedNanoTime
	 */
	private ScreenTickEvent event = new ScreenTickEvent(this); 					// reutilização do evento
	private TreeMap<Integer, List<ScreenTickInterface>> temp_listeners = 
			new TreeMap<Integer, List<ScreenTickInterface>>(); 					// reutilização da lista de listeners
	private boolean isTempListenersDirty = true;
	private Iterator<Map.Entry<Integer, List<ScreenTickInterface>>> i; 			// reutilização da variável do iterator
	private boolean print = false;
	private ArrayList<ScreenTickInterface> x; 
	public synchronized void fireEvent(CameraModel camModel, long elapsedNanoTime) {
		event.setCamera(camModel);
		event.setElapsedNanoTime(elapsedNanoTime);
		
		try {
			/* cria uma copia para iterar */
			if (isTempListenersDirty) {
				if (logger.getLevel() == Logger.DEBUG) logger.debug("[diff] _listeners|temp_listeners: " + _listeners.size() + "|" + temp_listeners.size());
				temp_listeners.clear();
				for (Map.Entry<Integer, List<ScreenTickInterface>> zGroup : _listeners.entrySet())
					temp_listeners.put(zGroup.getKey(), zGroup.getValue());
				
				isTempListenersDirty = false;
				print = true;
			}
			
			i = temp_listeners.entrySet().iterator();
			while (i.hasNext()) {
				x = (ArrayList<ScreenTickInterface>) i.next().getValue();
				
				for (ScreenTickInterface it : x) {
					it.draw(event);
					if (print) {
						if (logger.getLevel() == Logger.DEBUG) logger.debug(x.getClass().getName());
					}					
				}

			}
			print = false;
		} catch (ConcurrentModificationException ex) {
			if (logger.getLevel() == Logger.DEBUG) logger.debug("[ScreenTickGen-EXCEPTION] CurrentThreadID: " + Long.toString(Thread.currentThread().getId()));
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
