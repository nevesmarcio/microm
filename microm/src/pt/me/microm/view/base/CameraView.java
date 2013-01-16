package pt.me.microm.view.base;

import pt.me.microm.infrastructure.events.ScreenTickEvent;
import pt.me.microm.model.base.CameraModel;
import pt.me.microm.view.AbstractView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;

public class CameraView extends AbstractView {
	private static final String TAG = CameraView.class.getSimpleName();
	
	private CameraModel camSrc;
	
	public CameraView(CameraModel camSrc) {  
		super(camSrc);
		this.camSrc = camSrc;
	}
	
	@Override
	public void draw(ScreenTickEvent e) {
		/* no representation of camera */
	}

	
}
