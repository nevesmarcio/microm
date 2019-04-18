package pt.me.microm.model.ui;

import com.google.common.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.tools.levelloader.BasicShape;


public class TextModel extends AbstractModel {
    private static final String TAG = TextModel.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(TAG);

    private BasicShape sh;
    private String content;

    private TextModel(final EventBus modelEventBus, BasicShape sh, String text_name, String content) {
        this.sh = sh;
        this.content = content;

        // Sinaliza os subscritores de que a construção do modelo terminou.
        this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));
    }

    public static TextModel getNewInstance(final EventBus modelEventBus, BasicShape sh, String text_name, String content) {
        return new TextModel(modelEventBus, sh, text_name, content);
    }


    @Override
    public void handleGameTick(GameTickEvent e) {
        long elapsedNanoTime = e.getElapsedNanoTime();

    }


    // BodyInterface implementation
    @Override
    public String getName() {
        return super.getName();
    }

    public BasicShape getBasicShape() {
        return sh;
    }

    public String getContent() {
        return content;
    }

}
