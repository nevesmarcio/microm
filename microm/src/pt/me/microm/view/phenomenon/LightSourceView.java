package pt.me.microm.view.phenomenon;

import pt.me.microm.GameMicroM;
import pt.me.microm.controller.loop.event.ScreenTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.model.phenomenon.LightSourceModel;
import pt.me.microm.view.AbstractView;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;

public class LightSourceView extends AbstractView {
	private static final String TAG = LightSourceView.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private LightSourceModel lightsourcemSrc;
	
	public LightSourceView(LightSourceModel lightsourcemSrc) {
		super(lightsourcemSrc, 2);
		this.lightsourcemSrc = lightsourcemSrc;
		
	}

	@Override
	public void DelayedInit() {

	    
	}
	

	@Override
	public void draw(ScreenTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
		//////// LIGHTING ///////////
		lightsourcemSrc.rayHandler.setCombinedMatrix(e.getCamera().getGameCamera().combined);
		lightsourcemSrc.rayHandler.render();
	}
	
	@Override
	public void draw20(ScreenTickEvent e) {
		if (GameMicroM.FLAG_CALC_LIGHTING)
			draw(e);
	}
	

	@Override
	public void dispose() {
		super.dispose();
	}
	
}
