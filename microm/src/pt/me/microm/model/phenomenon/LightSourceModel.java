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
public class LightSourceModel extends AbstractModel {
	private static final String TAG = LightSourceModel.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	public Vector2 position;
	
	private LightSourceModel(Vector2 position) {
		this.position = position;
		
		// Sinaliza os subscritores de que a construção do modelo terminou.
		LightSourceModel.this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));	    
	}

	public static LightSourceModel getNewInstance(Vector2 position){
		return new LightSourceModel(position);
	}
	
	@Override
	public void handleGameTick(GameTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
	}

	
}
