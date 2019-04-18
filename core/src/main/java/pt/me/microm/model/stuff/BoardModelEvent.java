package pt.me.microm.model.stuff;

import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.AbstractModelEvent;
import pt.me.microm.model.IEventType;
import pt.me.microm.model.IModelCategory1;

public class BoardModelEvent extends AbstractModelEvent implements IModelCategory1/*, IModelCategory2*/ {

    public interface OnBoardModelXHappened extends IEventType {
    }

    public interface OnBoardModelYHappened extends IEventType {
    }

    public interface OnBoardModelZHappened extends IEventType {
    }

    public BoardModelEvent(AbstractModel eventSource, Class<? extends IEventType> eventType) {
        super(eventSource, eventType);
    }
}
