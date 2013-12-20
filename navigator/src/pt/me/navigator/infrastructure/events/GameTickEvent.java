package pt.me.navigator.infrastructure.events;

import java.util.EventObject;


public class GameTickEvent extends EventObject{
	private static final String TAG = GameTickEvent.class.getSimpleName();
	
	private static final long serialVersionUID = 1L;

	private long elapsedNanoTime;
	
	public GameTickEvent(Object source) {
		super(source);
	}

	public long getElapsedNanoTime() {
		return elapsedNanoTime;
	}

	public void setElapsedNanoTime(long elapsedNanoTime) {
		this.elapsedNanoTime = elapsedNanoTime;
	}
	
}
