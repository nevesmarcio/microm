package pt.me.microm.infrastructure;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

import pt.me.microm.infrastructure.events.ScreenTickEvent;
import pt.me.microm.infrastructure.interfaces.ScreenTickInterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Disposable;

public class ScreenTickManager implements Disposable{
	private static final String TAG = ScreenTickManager.class.getSimpleName();
	
	private List<ScreenTickInterface> _listeners = new ArrayList<ScreenTickInterface>();

	public synchronized void addEventListener(ScreenTickInterface listener) {
		_listeners.add(listener);
	}

	public synchronized void removeEventListener(ScreenTickInterface listener) {
		_listeners.remove(listener);
	}

	// call this method whenever you want to notify
	// the event listeners of the particular event
	public synchronized void fireEvent(Camera camera, long elapsedNanoTime) {
		ScreenTickEvent event = new ScreenTickEvent(this);
		event.setCamera(camera);
		event.setElapsedNanoTime(elapsedNanoTime);
		
		try {
			/* cria uma copia para iterar */
			List<ScreenTickInterface> temp_listeners = new ArrayList<ScreenTickInterface>();
			temp_listeners.addAll(_listeners);
			
			
			Iterator<ScreenTickInterface> i = temp_listeners.iterator();
			while (i.hasNext()) {
				((ScreenTickInterface) i.next()).draw(event);
			}
		} catch (ConcurrentModificationException ex) {
			Gdx.app.debug("[ScreenTickGen-EXCEPTION]", "CurrentThreadID: " + Long.toString(Thread.currentThread().getId()));
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
