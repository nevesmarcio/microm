package pt.me.microm.infrastructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import pt.me.microm.infrastructure.events.GameTickEvent;
import pt.me.microm.infrastructure.interfaces.GameTickInterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;

public class GameTickGenerator implements Disposable{
	private static final String TAG = GameTickGenerator.class.getSimpleName();

	private List<GameTickInterface> _listeners = new ArrayList<GameTickInterface>();

	public synchronized void addEventListener(GameTickInterface listener) {

		Gdx.app.debug(TAG, "[TickGen-addEventListener-begin]: CurrentThreadID: " + Long.toString(Thread.currentThread().getId()));
		_listeners.add(listener);
		Gdx.app.debug(TAG, "[TickGen-addEventListener-end]: CurrentThreadID: " + Long.toString(Thread.currentThread().getId()));
	}

	public synchronized void removeEventListener(GameTickInterface listener) {

		Gdx.app.debug(TAG, "[TickGen-removeEventListener-begin]: CurrentThreadID: " + Long.toString(Thread.currentThread().getId()));
		_listeners.remove(listener);
		Gdx.app.debug(TAG, "[TickGen-removeEventListener-end]: CurrentThreadID: " + Long.toString(Thread.currentThread().getId()));
	}

	// call this method whenever you want to notify
	// the event listeners of the particular event
	private synchronized void fireEvent(long elapsedNanoTime) {
		Gdx.app.debug(TAG, "[TickGen-fireEvent]: CurrentThreadID: " + Long.toString(Thread.currentThread().getId()));
		
		GameTickEvent event = new GameTickEvent(this);
		event.setElapsedNanoTime(elapsedNanoTime);

		try {
			/* cria uma copia para iterar */
			List<GameTickInterface> temp_listeners = new ArrayList<GameTickInterface>();
			temp_listeners.addAll(_listeners);
			
			Iterator<GameTickInterface> i = temp_listeners.iterator();
			while (i.hasNext()) {
				GameTickInterface gti = i.next();
				Gdx.app.debug(TAG, "\t[TickGen]" + gti.getClass().getName());
				
				gti.handleGameTick(event);
			}
		} catch (ConcurrentModificationException ex) {
			Gdx.app.log(TAG, "[TickGen-EXCEPTION]: CurrentThreadID: " + Long.toString(Thread.currentThread().getId()));
			throw ex;
		}
	}

	private static GameTickGenerator instance = null;

	private GameTickGenerator() {
		gameTick = new Timer();
		gameTick.scheduleAtFixedRate(new GameCycleTask(), 0, GAME_CONSTANTS.GAME_TICK_MILI);
	}

	public static GameTickGenerator getInstance() {
		if (instance == null) {
			instance = new GameTickGenerator();
		}
		return instance;
	}

	private Timer gameTick;

	private class GameCycleTask extends TimerTask {
		long lastTick = TimeUtils.nanoTime();

		@Override
		public void run() {
			long thisTick = TimeUtils.nanoTime();
			long elapsedNanoTime = thisTick - lastTick;

			try {
				fireEvent(elapsedNanoTime);
			} catch (Exception e) {
				Gdx.app.error(TAG, "Something fishy is going on here... Ex:" + e.getMessage());
			}

			Gdx.app.debug(TAG, "Time's up (miliseconds)!"
					+ elapsedNanoTime / GAME_CONSTANTS.ONE_MILISECOND_TO_NANO);

			lastTick = thisTick;
		}
	}

	@Override
	public void dispose() {
		gameTick.cancel();
		gameTick = null;
	}

}
