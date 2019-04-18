package pt.me.microm.model.phenomenon;

import com.badlogic.gdx.math.Vector2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;


/**
 * Renderiza uma "explosão de estrelas" numa dada coordenada
 * <p>
 * Exemplos de utilização:
 * -choque com uma estrela
 *
 * @author mneves
 */
public class CollisionModel extends AbstractModel {
    private static final String TAG = CollisionModel.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(TAG);

    private Vector2 position;

    private CollisionModel(Vector2 position) {
        this.position = position;

        // Sinaliza os subscritores de que a construção do modelo terminou.
        CollisionModel.this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));
    }

    public static CollisionModel getNewInstance(Vector2 position) {
        return new CollisionModel(position);
    }

    @Override
    public void handleGameTick(GameTickEvent e) {
        long elapsedNanoTime = e.getElapsedNanoTime();

    }


    public Vector2 getPosition() {
        return position;
    }

}
