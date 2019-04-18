package pt.me.microm.infrastructure.event.dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.me.microm.infrastructure.event.IEvent;
import pt.me.microm.infrastructure.event.listener.IEventListener;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/*
 * Thanks to: http://www.therealjoshua.com/2012/07/android-architecture-part-10-the-activity-revisited/
 *
 * Nota: Esta é a classe que os objectos de modelo extendem.
 * Também há a hipotese de implementarem o interface Dispatcher directamente.
 */
public class EventDispatcher implements IDispatcher {
    private static final String TAG = EventDispatcher.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(TAG);

    private HashMap<Enum<?>, CopyOnWriteArrayList<IEventListener>> listenerMap;
    private IDispatcher target;

    public EventDispatcher() {
        this(null);
    }

    public EventDispatcher(IDispatcher target) {
        listenerMap = new HashMap<Enum<?>, CopyOnWriteArrayList<IEventListener>>();
        this.target = (target != null) ? target : this;
    }

    @Override
    public void addListener(Enum<?> type, IEventListener listener) {
        synchronized (listenerMap) {
            CopyOnWriteArrayList<IEventListener> list = listenerMap.get(type);
            if (list == null) {
                list = new CopyOnWriteArrayList<IEventListener>();
                listenerMap.put(type, list);
            }
            list.add(listener);
        }
    }

    @Override
    public void removeListener(Enum<?> type, IEventListener listener) {
        synchronized (listenerMap) {
            CopyOnWriteArrayList<IEventListener> list = listenerMap.get(type);
            if (list == null)
                return;
            list.remove(listener);
            if (list.size() == 0) {
                listenerMap.remove(type);
            }
        }
    }

    @Override
    public boolean hasListener(Enum<?> type, IEventListener listener) {
        synchronized (listenerMap) {
            CopyOnWriteArrayList<IEventListener> list = listenerMap.get(type);
            if (list == null)
                return false;
            return list.contains(listener);
        }
    }

    @Override
    public void dispatchEvent(IEvent event) {
        if (event == null) {
            logger.error("can not dispatch null event");
            return;
        }
        Enum<?> type = event.getType();
        event.setSource(target);
        CopyOnWriteArrayList<IEventListener> list;
        synchronized (listenerMap) {
            list = listenerMap.get(type);
        }
        if (list == null)
            return;
        for (IEventListener l : list) {
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
