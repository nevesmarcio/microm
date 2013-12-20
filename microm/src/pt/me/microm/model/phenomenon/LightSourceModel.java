package pt.me.microm.model.phenomenon;

import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.tools.levelloader.BasicShape;

import box2dLight.ConeLight;
import box2dLight.DirectionalLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
	public RayHandler rayHandler;
	
	private LightSourceModel(WorldModel wm, BasicShape sh, String light_name) {
		this.wm = wm;
		this.sh = sh;
		this.light_name = light_name;

	    if (Gdx.graphics.isGL20Available()) {
			RayHandler.setColorPrecisionMediump();
//			RayHandler.setGammaCorrection(true);
			RayHandler.useDiffuseLight(true);	    	
	    }

		rayHandler = new RayHandler(wm.getPhysicsWorld(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		rayHandler.setCulling(true);
		rayHandler.setBlur(false);
		rayHandler.setShadows(true);
		rayHandler.setAmbientLight(new Color(0.10f, 0.10f, 0.10f, 0.00f));
//	    rayHandler.setAmbientLight(new Color(0.5f, 0.5f, 0.5f, 0.5f));
	    
	    new PointLight(rayHandler, 36*20, new Color(1.00f, 1.00f, 1.00f, 0.25f), 55.0f, sh.getRotationPivot().x, sh.getRotationPivot().y);
//	    new PointLight(rayHandler, 36*20, new Color(1.00f, 1.00f, 1.00f, 0.25f), 55.0f, sh.getRotationPivot().x/100, sh.getRotationPivot().y);
//	    new PointLight(rayHandler, 36*20, new Color(1.00f, 1.00f, 1.00f, 0.25f), 55.0f, sh.getRotationPivot().x/2, sh.getRotationPivot().y);
//	    new ConeLight(rayHandler, 30*5, new Color(1.00f, 1.00f, 1.00f, 0.55f), 30.0f, sh.getRotationPivot().x, sh.getRotationPivot().y, 225, 30);
//	    new ConeLight(rayHandler, 30*5, new Color(1.00f, 1.00f, 1.00f, 0.55f), 30.0f, sh.getRotationPivot().x/100, sh.getRotationPivot().y, 315, 30);
		
		
//		new DirectionalLight(rayHandler, 100, new Color(1.00f, 1.00f, 1.00f, 0.55f), 90);
		
	    		
		
		
		
		// Sinaliza os subscritores de que a construção do modelo terminou.
		LightSourceModel.this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));	    
	}

	public static LightSourceModel getNewInstance(WorldModel wm, BasicShape sh, String light_name){
		return new LightSourceModel(wm, sh, light_name);
	}
	
	@Override
	public void handleGameTick(GameTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
		if (rayHandler!=null)
			rayHandler.update();
	}

	
	@Override
	public void dispose() {
		rayHandler.dispose();
		super.dispose();
	}
	
}
