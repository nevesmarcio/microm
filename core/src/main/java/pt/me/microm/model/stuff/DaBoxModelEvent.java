package pt.me.microm.model.stuff;

import pt.me.microm.AbstractModelEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.IEventType;
import pt.me.microm.model.IModelCategory1;
import pt.me.microm.model.IModelCategory2;

public class DaBoxModelEvent extends AbstractModelEvent implements /*IModelCategory1, */IModelCategory2 {

    public interface OnDaBoxModelAHappened extends IEventType {}
    public interface OnDaBoxModelBHappened extends IEventType {}
    public interface OnDaBoxModelCHappened extends IEventType {}

    public DaBoxModelEvent(AbstractModel eventSource, Class<? extends IEventType> eventType) {
        super(eventSource, eventType);
    }
}
