package pt.me.navigator.model.events;

/*
 * Thanks to: http://www.therealjoshua.com/2012/07/android-architecture-part-10-the-activity-revisited/
 *
 * Nota: Implementação base do interface Event
 */

public class SimpleEvent implements Event {

	private Enum type;
	@Override
	public Enum getType() {
		return type;
	}
	public void setType(Enum type) {
		this.type = type;
	}

	protected Object source;
	@Override
	public Object getSource() {
		return source;
	}
	@Override
	public void setSource(Object source) {
		this.source = source;
	}

	public SimpleEvent(Enum type) {
		this.type = type;
	}

}
