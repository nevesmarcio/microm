package pt.me.microm.infrastructure.events;

import java.util.EventObject;

import pt.me.microm.model.base.CameraModel;

import com.badlogic.gdx.graphics.Camera;

public class ScreenTickEvent extends EventObject {
	private static final String TAG = ScreenTickEvent.class.getSimpleName();
	
	private static final long serialVersionUID = 1L;

	private long elapsedNanoTime;
	private CameraModel camModel; 
	
	public ScreenTickEvent(Object source) {
		super(source);
	}

	/**/
	public long getElapsedNanoTime() {
		return elapsedNanoTime;
	}
	public void setElapsedNanoTime(long elapsedNanoTime) {
		this.elapsedNanoTime = elapsedNanoTime;
	}
	
	/**/
	public CameraModel getCamera() {
		return camModel;
	}
	public void setCamera(CameraModel camModel) {
		this.camModel = camModel;
	}
		
}
