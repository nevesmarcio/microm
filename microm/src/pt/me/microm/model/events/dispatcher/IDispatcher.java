package pt.me.microm.model.events.dispatcher;

import pt.me.microm.model.events.IEvent;
import pt.me.microm.model.events.listener.IEventListener;

/*
 * Thanks to: http://www.therealjoshua.com/2012/07/android-architecture-part-10-the-activity-revisited/
 *
 * Nota: Este é o interface que os eventDispatchers implementam.
 * Para conveniência já existe uma implementação deste interface para 
 * que se possa extender directamente dessa classe base --> EventDispatcher.
 */
public interface IDispatcher {
	void addListener(Enum type, IEventListener listener);

	void removeListener(Enum type, IEventListener listener);

	boolean hasListener(Enum type, IEventListener listener);

	void dispatchEvent(IEvent event);
}