package pt.me.navigator.model.events.dispatcher;

import pt.me.navigator.model.events.Event;
import pt.me.navigator.model.events.listener.EventListener;

/*
 * Thanks to: http://www.therealjoshua.com/2012/07/android-architecture-part-10-the-activity-revisited/
 *
 * Nota: Este é o interface que os eventDispatchers implementam.
 * Para conveniência já existe uma implementação deste interface para 
 * que se possa extender directamente dessa classe base --> EventDispatcher.
 */
public interface Dispatcher {
	void addListener(Enum type, EventListener listener);

	void removeListener(Enum type, EventListener listener);

	boolean hasListener(Enum type, EventListener listener);

	void dispatchEvent(Event event);
}