package pt.me.microm.infrastructure.event;


/*
 * Thanks to: http://www.therealjoshua.com/2012/07/android-architecture-part-10-the-activity-revisited/
 *
 * Nota: Implementação base do interface Event
 */

public class SimpleEvent implements IEvent {

	protected Object source;
	private Enum<?> type;

	@Override
	public Object getSource() {
		return source;
	}
	@Override
	public void setSource(Object source) {
		this.source = source;
	}
	
	@Override
	public Enum<?> getType() {
		return type;
	}

	public SimpleEvent(Enum<?> type) {
		this.type = type;
	}




}
