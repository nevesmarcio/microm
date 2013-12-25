package pt.me.microm.controller.loop.event;

import java.util.EventObject;

import pt.me.microm.infrastructure.GAME_CONSTANTS;

import com.badlogic.gdx.utils.Logger;

public class GameTickEvent extends EventObject {

	private static final String TAG = GameTickEvent.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
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
