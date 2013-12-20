package pt.me.navigator.model.events;

/*
 * Thanks to: http://www.therealjoshua.com/2012/07/android-architecture-part-10-the-activity-revisited/
 *
 * Nota: Este é o objecto transmitido pelo EventDispatcher (Models) para o 
 * EventListener (View).
 */
public interface Event {

	public Enum getType();

	public Object getSource();

	public void setSource(Object source);
}
