package pt.me.microm.model.dev;

import pt.me.microm.model.IModelCategory2;

public class DebugModelEvent implements IModelCategory2 {
    public enum EventType {
        ON_DEBUG_MODEL_SPAWN
    }

    DebugModel eventSource;
    EventType eventType;

    public DebugModelEvent(DebugModel eventSource, EventType eventType) {
        this.eventSource = eventSource;
        this.eventType = eventType;
    }

    public DebugModel getEventSource() {
        return eventSource;
    }

    public EventType getEventType() {
        return eventType;
    }
}
