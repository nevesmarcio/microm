package pt.me.microm.controller.loop.event;

import java.util.EventObject;

import pt.me.microm.model.base.CameraModel;

public class ScreenTickEvent extends EventObject {
	
	private static final String TAG = ScreenTickEvent.class.getSimpleName();
	
	private static final long serialVersionUID = 1L;
	
	public ScreenTickEvent(Object source) {
		super(source);
	}
	
	private long elapsedNanoTime;
	private CameraModel camModel; 
	

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
