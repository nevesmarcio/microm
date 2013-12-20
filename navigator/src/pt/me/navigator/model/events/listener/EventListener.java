package pt.me.navigator.model.events.listener;

import pt.me.navigator.model.events.Event;

/*
 * Thanks to: http://www.therealjoshua.com/2012/07/android-architecture-part-10-the-activity-revisited/
 *
 * Nota: Ok, este interface é o EventListener.
 * É para ser implementado por quem?
 * Este event listener é para ser implementado pela view!!
 */

public interface EventListener {

	void onEvent(Event event);

}
