package pt.me.microm.infrastructure.event.listener;

import pt.me.microm.infrastructure.event.IEvent;

/*
 * Thanks to: http://www.therealjoshua.com/2012/07/android-architecture-part-10-the-activity-revisited/
 *
 * Nota: Ok, este interface é o EventListener.
 * É para ser implementado por quem?
 * Este event listener é para ser implementado pela view!!
 */

public interface IEventListener {

	void onEvent(IEvent event);

}
