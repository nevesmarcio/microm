package pt.me.microm.model.phenomenon;

import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.tools.levelloader.BasicShape;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Logger;

/**
 * 
 * @author mneves
 *
 */
public class LightSourceModel extends AbstractModel {
	private static final String TAG = LightSourceModel.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	public WorldModel wm;
	public BasicShape sh;
	private String light_name;
	
	private LightSourceModel(WorldModel wm, BasicShape sh, String light_name) {
		this.wm = wm;
		this.sh = sh;
		this.light_name = light_name;
		

		
		// Sinaliza os subscritores de que a construção do modelo terminou.
		LightSourceModel.this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));	    
	}

	public static LightSourceModel getNewInstance(WorldModel wm, BasicShape sh, String light_name){
		return new LightSourceModel(wm, sh, light_name);
	}
	
	@Override
	public void handleGameTick(GameTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
		
	}

	
}
