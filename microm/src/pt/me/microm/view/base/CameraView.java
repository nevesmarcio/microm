package pt.me.microm.view.base;

import pt.me.microm.controller.loop.event.ScreenTickEvent;
import pt.me.microm.model.base.CameraModel;
import pt.me.microm.view.AbstractView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.utils.Logger;

public class CameraView extends AbstractView {
	private static final String TAG = CameraView.class.getSimpleName();
	private static final Logger logger = new Logger(TAG);
	
	private CameraModel camSrc;
	
	public CameraView(CameraModel camSrc) {  
		super(camSrc);
		this.camSrc = camSrc;
	}
	
	@Override
	public void DelayedInit() {
		/* no stuff to initialize */
		
	}
	
	@Override
	public void draw(ScreenTickEvent e) {
		/* no representation of camera */
	}

	
}
