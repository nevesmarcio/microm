package pt.me.microm.infrastructure.events;

import java.util.EventObject;

import com.badlogic.gdx.graphics.Camera;

public class ScreenTickEvent extends EventObject{
	private static final String TAG = ScreenTickEvent.class.getSimpleName();
	
	private static final long serialVersionUID = 1L;

	private long elapsedNanoTime;
	private Camera camera; 
	
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
	public Camera getCamera() {
		return camera;
	}
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
		
}
