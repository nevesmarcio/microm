package pt.me.microm.controller.loop.event;

import java.util.EventObject;

public class GameTickEvent extends EventObject {

	private static final String TAG = GameTickEvent.class.getSimpleName();
	
	private static final long serialVersionUID = 1L;

	public GameTickEvent(Object source) {
		super(source);
	}
	

	private long elapsedNanoTime;
	
	/**/
	public long getElapsedNanoTime() {
		return elapsedNanoTime;
	}
	public void setElapsedNanoTime(long elapsedNanoTime) {
		this.elapsedNanoTime = elapsedNanoTime;
	}
	

}
