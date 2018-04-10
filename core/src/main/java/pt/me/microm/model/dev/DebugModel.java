package pt.me.microm.model.dev;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.base.WorldModel;


public class DebugModel extends AbstractModel {
	private static final String TAG = DebugModel.class.getSimpleName();
	private static final Logger logger = LoggerFactory.getLogger(TAG);
	
	private Vector2 position;
	private float radius = 0.075f;
	
	private Color color = new Color(1.0f,0.0f,0.0f,1.0f);
	

	private DebugModel(final WorldModel wm, final float x, final float y) {
		this.position = new Vector2(x, y);
		
		// Sinaliza os subscritores de que a construção do modelo terminou.
		DebugModel.this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));
	}

	public static DebugModel getNewInstance(WorldModel wm, float x, float y){
		return new DebugModel(wm, x, y);
	}
	
	@Override
	public void handleGameTick(GameTickEvent e) {
		
	}

	public float getRadius() {
		return radius;
	}
	public Vector2 getPosition(){
		return position;
	}

	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	
}
