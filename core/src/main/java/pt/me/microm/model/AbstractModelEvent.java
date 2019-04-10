package pt.me.microm.model;

/**
 * All modelEvents derive from this abstract class - this allows models to subscribe to eventbus by this type
 */
public abstract class AbstractModelEvent {

    public interface OnCreate extends IEventType {
    }

    AbstractModel eventSource;
    Class<? extends IEventType> eventType;

    public AbstractModelEvent(AbstractModel eventSource, Class<? extends IEventType> eventType) {
        this.eventSource = eventSource;
        this.eventType = eventType;
    }

    public AbstractModel getEventSource() {
        return eventSource;
    }

    public Class<? extends IEventType> getEventType() {
        return eventType;
    }
}
