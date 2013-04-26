package pt.me.microm.controller.loop;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pt.me.microm.controller.loop.event.ScreenTickEvent;
import pt.me.microm.controller.loop.itf.IProcessRunnable;
import pt.me.microm.controller.loop.itf.IScreenTick;
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
public class ScreenTickManager implements IProcessRunnable, Disposable {
	private static final String TAG = ScreenTickManager.class.getSimpleName();
	private static final Logger logger = new Logger(TAG);
	
	private final TreeMap<Integer, List<IScreenTick>> _listeners = new TreeMap<Integer, List<IScreenTick>>();
	
	/**
	 * the default zIndex is 0
	 * @param listener
	 */
	public synchronized void addEventListener(IScreenTick listener) {
		addEventListener(listener, 0);
	}
	
	/**
	 * specifies a zIndex
	 * @param listener
	 * @param zIndex
	 */
	public synchronized void addEventListener(IScreenTick listener, int zIndex) {
		try {
			_listeners.get(zIndex).add(listener);
		} catch (Exception e) { //FIXME: especificar a excepção
			_listeners.put(zIndex, new ArrayList<IScreenTick>());
			_listeners.get(zIndex).add(listener);
		}
		
		isTempListenersDirty = true;
	}

	public synchronized void removeEventListener(IScreenTick listener) {
		for (List<IScreenTick> subList : _listeners.values()) {
			try {
				subList.remove(listener);
				isTempListenersDirty = true;
			} catch (Exception e) { //FIXME: especificar a excepção
				if (logger.getLevel() >= Logger.DEBUG) logger.debug(e.getMessage());
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
	private ScreenTickEvent event = new ScreenTickEvent(this); 			// reutilização do evento
	private TreeMap<Integer, List<IScreenTick>> temp_listeners = 
			new TreeMap<Integer, List<IScreenTick>>(); 					// reutilização da lista de listeners
	private boolean isTempListenersDirty = true;
	private Iterator<Map.Entry<Integer, List<IScreenTick>>> i; 			// reutilização da variável do iterator
	private boolean print = false;
	private ArrayList<IScreenTick> x; 
	public synchronized void fireEvent(CameraModel camModel, long elapsedNanoTime) {
		event.setCamera(camModel);
		event.setElapsedNanoTime(elapsedNanoTime);
		
		try {
			/* cria uma copia para iterar */
			if (isTempListenersDirty) {
				if (logger.getLevel() >= Logger.DEBUG) logger.debug("[diff] _listeners|temp_listeners: " + _listeners.size() + "|" + temp_listeners.size());
				temp_listeners.clear();
				for (Map.Entry<Integer, List<IScreenTick>> zGroup : _listeners.entrySet())
					temp_listeners.put(zGroup.getKey(), zGroup.getValue());
				
				isTempListenersDirty = false;
				print = true;
			}
			
			i = temp_listeners.entrySet().iterator();
			while (i.hasNext()) {
				x = (ArrayList<IScreenTick>) i.next().getValue();
				
				for (IScreenTick it : x) {
					it.draw(event);
					if (print) {
						if (logger.getLevel() >= Logger.DEBUG) logger.debug(x.getClass().getName());
					}					
				}

			}
			print = false;
		} catch (ConcurrentModificationException ex) {
			if (logger.getLevel() >= Logger.DEBUG) logger.debug("[ScreenTickGen-EXCEPTION] CurrentThreadID: " + Long.toString(Thread.currentThread().getId()));
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
	public synchronized void dispose() {
		event = null;
		_listeners.clear();
		instance = null;
		
	}

	/**
	 * This method allows that external code adds a runnable to be executed on
	 * the ScreenTickManager's thread context
	 */
	@Override
	public void postRunnable(Runnable runnable) {
		Gdx.app.postRunnable(runnable);
	}
	
	/**
	 *  Static shortcut to the postRunnable method
	 * @param runnable
	 */
	public static void PostRunnable(Runnable runnable)  {
		ScreenTickManager.getInstance().postRunnable(runnable);
	}
}
