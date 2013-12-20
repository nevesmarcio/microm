package pt.me.navigator.model.events.dispatcher;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.badlogic.gdx.Gdx;

import pt.me.navigator.model.events.Event;
import pt.me.navigator.model.events.listener.EventListener;

/*
 * Thanks to: http://www.therealjoshua.com/2012/07/android-architecture-part-10-the-activity-revisited/
 *
 * Nota: Esta é a classe que os objectos de modelo extendem.
 * Também há a hipotese de implementarem o interface Dispatcher directamente.
 */
public class EventDispatcher implements Dispatcher {
	private static final String TAG = EventDispatcher.class.getSimpleName();

	private HashMap<Enum, CopyOnWriteArrayList<EventListener>> listenerMap;
	private Dispatcher target;

	public EventDispatcher() {
		this(null);
	}

	public EventDispatcher(Dispatcher target) {
		listenerMap = new HashMap<Enum, CopyOnWriteArrayList<EventListener>>();
		this.target = (target != null) ? target : this;
	}

	@Override
	public void addListener(Enum type, EventListener listener) {
		synchronized (listenerMap) {
			CopyOnWriteArrayList<EventListener> list = listenerMap.get(type);
			if (list == null) {
				list = new CopyOnWriteArrayList<EventListener>();
				listenerMap.put(type, list);
			}
			list.add(listener);
		}
	}

	@Override
	public void removeListener(Enum type, EventListener listener) {
		synchronized (listenerMap) {
			CopyOnWriteArrayList<EventListener> list = listenerMap.get(type);
			if (list == null)
				return;
			list.remove(listener);
			if (list.size() == 0) {
				listenerMap.remove(type);
			}
		}
	}

	@Override
	public boolean hasListener(Enum type, EventListener listener) {
		synchronized (listenerMap) {
			CopyOnWriteArrayList<EventListener> list = listenerMap.get(type);
			if (list == null)
				return false;
			return list.contains(listener);
		}
	}

	@Override
	public void dispatchEvent(Event event) {
		if (event == null) {
			Gdx.app.error(TAG, "can not dispatch null event");
			return;
		}
		Enum type = event.getType();
		event.setSource(target);
		CopyOnWriteArrayList<EventListener> list;
		synchronized (listenerMap) {
			list = listenerMap.get(type);
		}
		if (list == null)
			return;
		for (EventListener l : list) {
			l.onEvent(event);
		}
	}

	public void dispose() {
		synchronized (listenerMap) {
			listenerMap.clear();
		}
		target = null;
	}
}
