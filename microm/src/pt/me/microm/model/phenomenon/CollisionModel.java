package pt.me.microm.model.phenomenon;

import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Logger;

/**
 * Renderiza uma "explosão de estrelas" numa dada coordenada
 * 
 * Exemplos de utilização:
 * 		-choque com uma estrela
 * 
 * @author mneves
 *
 */
public class CollisionModel extends AbstractModel {
	private static final String TAG = CollisionModel.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	public Vector2 position;
	
	private CollisionModel(Vector2 position) {
		this.position = position;
		
		// Sinaliza os subscritores de que a construção do modelo terminou.
		CollisionModel.this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));	    
	}

	public static CollisionModel getNewInstance(Vector2 position){
		return new CollisionModel(position);
	}
	
	@Override
	public void handleGameTick(GameTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
	}

	
}
