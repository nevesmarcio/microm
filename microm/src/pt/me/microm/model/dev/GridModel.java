package pt.me.microm.model.dev;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import pt.me.microm.infrastructure.events.GameTickEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.events.SimpleEvent;

public class GridModel extends AbstractModel {
	// Esta classe deverá desenhar uma grid sobre o mundo
	// Serve apenas para ajuda ao desenvolvimento e para compreender a relação de coordenadas/ pixels
	
	private static final String TAG = GridModel.class.getSimpleName();
	
	private float gridSpacing = 1.0f;
	private float boxSize = 10.0f;

	public GridModel() {

		
		// Sinaliza os subscritores de que a construção do modelo terminou.
		this.dispatchEvent(new SimpleEvent(EventType.ON_MODEL_INSTANTIATED)); 
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

	
	@Override
	public Body getBody() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Vector2 getPosition() {
		return null;
	}	
	
}
