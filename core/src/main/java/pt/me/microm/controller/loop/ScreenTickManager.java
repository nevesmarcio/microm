package pt.me.microm.controller.loop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.me.microm.controller.loop.event.ScreenTickEvent;
import pt.me.microm.controller.loop.itf.IProcessRunnable;
import pt.me.microm.controller.loop.itf.IScreenTick;
import pt.me.microm.model.base.CameraModel;

import java.util.*;
/**
 * documentar esta merda, senão daqui a uns tempos (horas), não percebo bolha do que escrevi
 * 
 * 
 * @author mneves
 *
 */
public class ScreenTickManager implements IProcessRunnable, Disposable {
	private static int dbg = 0;
	private static final String TAG = ScreenTickManager.class.getSimpleName();
	private static final Logger logger = LoggerFactory.getLogger(TAG);
	
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
		if (logger.isInfoEnabled()) logger.info("{} is now receiving draw events", listener);
		try {
			_listeners.get(zIndex).add(listener);
		} catch (NullPointerException npe) { // if no listeners are registered for a given z-index create a new array to support them
			_listeners.put(zIndex, new ArrayList<IScreenTick>());
			_listeners.get(zIndex).add(listener);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) logger.error("exception: ", e);
		}
		
		isTempListenersDirty = true;
	}

	public synchronized void removeEventListener(IScreenTick listener) {
		boolean result = false;
		
		for (List<IScreenTick> subList : _listeners.values()) {
			result = subList.remove(listener);
			if (result) {
				if (logger.isInfoEnabled()) logger.info("{} is no longer receiving draw events", listener);
				isTempListenersDirty = true;	
				break;
			}
		}
		if (!result && logger.isWarnEnabled())
			logger.warn("listener {} was not registered", listener);
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
	private ArrayList<IScreenTick> sti; 
	public synchronized void fireEvent(boolean drawGL20, CameraModel camModel, long elapsedNanoTime) {
		event.setCamera(camModel);
		event.setElapsedNanoTime(elapsedNanoTime);
		
		try {
			/* cria uma copia para iterar */
			if (isTempListenersDirty) {
				if (logger.isDebugEnabled()) logger.debug("[diff] _listeners|temp_listeners: " + _listeners.size() + "|" + temp_listeners.size());
				temp_listeners.clear();
				for (Map.Entry<Integer, List<IScreenTick>> zGroup : _listeners.entrySet())
					temp_listeners.put(zGroup.getKey(), zGroup.getValue());
				
				isTempListenersDirty = false;
				print = true;
			}
			
			i = temp_listeners.entrySet().iterator();
			while (i.hasNext()) {
				sti = (ArrayList<IScreenTick>) i.next().getValue();

				for (IScreenTick it : sti) {
					if (drawGL20) it.draw20(event); else it.draw(event);
					if (print) {
						if (logger.isTraceEnabled()) logger.trace(sti.getClass().getName());
					}					
				}

			}
			print = false;
		} catch (ConcurrentModificationException ex) {
			if (logger.isErrorEnabled()) logger.error("[EXCEPTION]: ", ex);
			throw ex;
		}
		
		// release to allow GC
		sti = null;
		event.setCamera(null);
	}
	
	private static ScreenTickManager instance = null;

	private ScreenTickManager() {
		if (logger.isInfoEnabled()) logger.info("warming up draw machine...");
	}
	
	public static ScreenTickManager getInstance() {
		if (instance == null) {
			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			if (logger.isInfoEnabled()) logger.info("called by: {}.{}", stackTraceElements[2].getClassName(), stackTraceElements[2].getMethodName());
			
			if (dbg>0) throw new RuntimeException("Nope! Not again!");
			dbg+=1;			
			
			instance = new ScreenTickManager();
		}
		return instance;
	}
	
	public synchronized boolean isAvailable() {
		return instance == null ? false : true;
	}
	
	@Override
	public synchronized void dispose() {
		if (logger.isInfoEnabled()) logger.info("shutting down draw machine...");
		
		event = null;
		_listeners.clear();
		instance = null;
	}

	@Override
	protected void finalize() throws Throwable {
		if (logger.isInfoEnabled()) logger.info("GC'ed!");
		super.finalize();
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
