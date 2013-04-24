package pt.me.microm.model.dev;

import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;

import com.badlogic.gdx.utils.Logger;

public class GridModel extends AbstractModel {
	// Esta classe deverá desenhar uma grid sobre o mundo
	// Serve apenas para ajuda ao desenvolvimento e para compreender a relação de coordenadas/ pixels
	
	private static final String TAG = GridModel.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private float gridSpacing = 1.0f;
	private float boxSize = 10.0f;

	public GridModel() {

		// Sinaliza os subscritores de que a construção do modelo terminou.
		this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED)); 
	}
	
	@Override
	public void handleGameTick(GameTickEvent e) {
		// TODO Auto-generated method stub
	}
	
	public float getGridSpacing() {
		return gridSpacing;
	}
	public void setGridSpacing(float gridSpacing) {
		this.gridSpacing = gridSpacing;
	}


	public float getBoxSize() {
		return boxSize;
	}
	public void setBoxSize(float boxSize) {
		this.boxSize = boxSize;
	}

	
}
