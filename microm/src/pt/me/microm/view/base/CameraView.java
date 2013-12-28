package pt.me.microm.view.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.me.microm.controller.loop.event.ScreenTickEvent;
import pt.me.microm.model.base.CameraModel;
import pt.me.microm.view.AbstractView;


public class CameraView extends AbstractView {
	private static final String TAG = CameraView.class.getSimpleName();
	private static final Logger logger = LoggerFactory.getLogger(TAG);
	
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

	@Override
	public void draw20(ScreenTickEvent e) {
		/* no representation of camera */
	}

}
