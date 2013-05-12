package pt.me.microm.view.phenomenon;

import pt.me.microm.controller.loop.event.ScreenTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.model.phenomenon.LightSourceModel;
import pt.me.microm.view.AbstractView;

import box2dLight.ConeLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;

public class LightSourceView extends AbstractView {
	private static final String TAG = LightSourceView.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private RayHandler rayHandler;
	
	private LightSourceModel lightsourcemSrc;
	
	public LightSourceView(LightSourceModel lightsourcemSrc) {
		super(lightsourcemSrc, 0);
		this.lightsourcemSrc = lightsourcemSrc;
		
	}

	@Override
	public void DelayedInit() {
	    rayHandler = new RayHandler(lightsourcemSrc.wm.getPhysicsWorld());
	    rayHandler.setBlur(false);
	    rayHandler.setShadows(false);
	    rayHandler.setAmbientLight(0.75f);
	    RayHandler.useDiffuseLight(false);
	    //new PointLight(rayHandler, 36*10, new Color(1,1,1,0.75f), 30.0f, 10.0f, 7.0f);
	    new ConeLight(rayHandler, 36*10, new Color(0.1f, 0.29f, 0.75f, 0.60f), 30.0f, lightsourcemSrc.sh.getRotationPivot().x, lightsourcemSrc.sh.getRotationPivot().y, 225, 30);

	}
	

	@Override
	public void draw(ScreenTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
		//////// LIGHTING ///////////
		rayHandler.setCombinedMatrix(e.getCamera().getGameCamera().combined);
		rayHandler.updateAndRender();

	}


	@Override
	public void dispose() {
		rayHandler.dispose();
		super.dispose();
	}
	
}
