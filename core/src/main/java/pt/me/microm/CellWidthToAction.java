package pt.me.microm;

import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

public class CellWidthToAction extends TemporalAction {
    private Widget widget;
    private Cell<?> cell;
    private float startWidth;
    private float endWidth;


    protected void begin() {
        startWidth = actor.getWidth();
    }

    protected void update(float percent) {
        cell.width(startWidth + (endWidth - startWidth) * percent);
        widget.invalidateHierarchy();
    }

    public float getWidth() {
        return endWidth;
    }

    public void setWidth(float width) {
        endWidth = width;
    }

    public void setCell(Cell<?> cell) {
        this.cell = cell;
    }

    public void setWidget(Widget widget) {
        this.widget = widget;
    }


}
