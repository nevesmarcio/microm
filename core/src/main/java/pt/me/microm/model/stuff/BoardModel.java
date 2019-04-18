package pt.me.microm.model.stuff;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.google.common.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.IBodyStatic;
import pt.me.microm.tools.levelloader.BasicShape;


public class BoardModel extends AbstractModel implements IBodyStatic {
    private static final String TAG = BoardModel.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(TAG);

    private Body playzoneBody;

    private BasicShape board;

    private BoardModel(final EventBus modelEventBus, final BasicShape board, final String board_name) {
        this.board = board;
        setName(board_name);

        // notify eventBus
        modelEventBus.post(new BoardModelEvent(this, BoardModelEvent.OnCreate.class));

        // Sinaliza os subscritores de que a construção do modelo terminou.
        BoardModel.this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));
    }

    public static BoardModel getNewInstance(EventBus modelEventBus, BasicShape board, String board_name) {
        return new BoardModel(modelEventBus, board, board_name);
    }


    @Override
    public void handleGameTick(GameTickEvent e) {
        long elapsedNanoTime = e.getElapsedNanoTime();

        if (playzoneBody != null)
            if (logger.isDebugEnabled())
                logger.debug("[Physics-room]: Pos.x:" + String.format("%.2f", playzoneBody.getPosition().x)
                        + " Pos.y:" + String.format("%.2f", playzoneBody.getPosition().y)
                        + " Angle:" + String.format("%.2f", playzoneBody.getAngle())
                        + " Mass:" + playzoneBody.getMass()
                        + " Type:" + playzoneBody.getType());
    }


    // BodyInterface implementation
    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public BasicShape getBasicShape() {
        return board;
    }

    @Override
    public Vector2 getPosition() {
        if (playzoneBody != null)
            return playzoneBody.getPosition();
        else
            return new Vector2();
    }

    @Override
    public float getAngle() {

        if (playzoneBody != null)
            return playzoneBody.getAngle();
        else
            return 0f;
    }

    @Override
    public Body getBody() {
        return playzoneBody;
    }

    @Override
    public void setBody(Body playzoneBody) {
        this.playzoneBody = playzoneBody;
    }
}
