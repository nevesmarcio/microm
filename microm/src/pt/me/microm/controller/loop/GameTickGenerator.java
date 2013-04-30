package pt.me.microm.controller.loop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.controller.loop.itf.IGameTick;
import pt.me.microm.controller.loop.itf.IProcessRunnable;
import pt.me.microm.infrastructure.GAME_CONSTANTS;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.TimeUtils;

public class GameTickGenerator implements IProcessRunnable, Disposable {
	private static final String TAG = GameTickGenerator.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);

	private final List<IGameTick> _listeners = new ArrayList<IGameTick>();

	// runnables
	protected final Array<Runnable> runnables = new Array<Runnable>();
	protected final Array<Runnable> executedRunnables = new Array<Runnable>();	
	
	public synchronized void addEventListener(IGameTick listener) {

		if (logger.getLevel() == Logger.DEBUG) logger.debug("[TickGen-addEventListener-begin]: CurrentThreadID: " + Long.toString(Thread.currentThread().getId()));
		_listeners.add(listener);
		isTempListenersDirty = true;
		if (logger.getLevel() == Logger.DEBUG) logger.debug("[TickGen-addEventListener-end]: CurrentThreadID: " + Long.toString(Thread.currentThread().getId()));
	}

	public synchronized void removeEventListener(IGameTick listener) {

		if (logger.getLevel() == Logger.DEBUG) logger.debug("[TickGen-removeEventListener-begin]: CurrentThreadID: " + Long.toString(Thread.currentThread().getId()));
		_listeners.remove(listener);
		isTempListenersDirty = true;
		if (logger.getLevel() == Logger.DEBUG) logger.debug("[TickGen-removeEventListener-end]: CurrentThreadID: " + Long.toString(Thread.currentThread().getId()));
	}

	/**
	 * Call this method whenever you want to notify
	 * the event listeners of the particular event
	 * @param elapsedNanoTime
	 */
	private GameTickEvent event = new GameTickEvent(this); 		// reutilização do evento
	private Iterator<IGameTick> i; 								// reutilização da variável para iteração sobre a lista
	private IGameTick gti; 										// reutilização do GameTickInterface
	private List<IGameTick> temp_listeners =
			new ArrayList<IGameTick>();							// reutilização da lista de listeners
	private boolean isTempListenersDirty = true;				// variável de controlo para saber se a lista de listeners mudou
	private synchronized void fireEvent(long elapsedNanoTime) {
		if (logger.getLevel() == Logger.DEBUG) logger.debug("[TickGen-fireEvent]: CurrentThreadID: " + Long.toString(Thread.currentThread().getId()));
		
		event.setElapsedNanoTime(elapsedNanoTime);

		try {
			/* cria uma copia para iterar */
			if (isTempListenersDirty) {
				temp_listeners.clear();
				temp_listeners.addAll(_listeners);
				isTempListenersDirty = false;
			}
			
			i = temp_listeners.iterator();
			while (i.hasNext()) {
				gti = i.next();
				if (logger.getLevel() == Logger.DEBUG) logger.debug("\t[TickGen]" + gti.getClass().getName());
				
				gti.handleGameTick(event);
			}
		} catch (ConcurrentModificationException ex) {
			if (logger.getLevel() == Logger.DEBUG) logger.debug("[TickGen-EXCEPTION]: CurrentThreadID: " + Long.toString(Thread.currentThread().getId()));
			ex.printStackTrace();
			throw ex;
		}
	}

	private static GameTickGenerator instance = null;

	
	private class MyThreadFactory implements ThreadFactory  {

		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r);
			t.setName("_game_tick_generator_" + UUID.randomUUID().toString());
			return t;
			//return Executors.defaultThreadFactory().newThread(r);
		}
	}
	
	private GameTickGenerator() {
		gameTick = new ScheduledThreadPoolExecutor(1);
		gameTick.setThreadFactory(new MyThreadFactory());
		gameTick.scheduleAtFixedRate(new GameCycleTask(), 0, GAME_CONSTANTS.GAME_TICK_MILI, TimeUnit.MILLISECONDS);

	}

	public static GameTickGenerator getInstance() {
		if (instance == null) {
			instance = new GameTickGenerator();
			
			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			logger.info("getInstance called by: " +  stackTraceElements[2].getClassName() + "." + stackTraceElements[2].getMethodName());			
		}
		return instance;
	}

	private ScheduledThreadPoolExecutor gameTick;

	private class GameCycleTask implements Runnable {
		long lastTick = TimeUtils.nanoTime();

		@Override
		public void run() {
			long thisTick = TimeUtils.nanoTime();
			long elapsedNanoTime = thisTick - lastTick;

			try {
				// fire timed event
				fireEvent(elapsedNanoTime);
				
				// execute registered runnables
				synchronized (runnables) {
					executedRunnables.clear();
					executedRunnables.addAll(runnables);
					runnables.clear();
				}

				for (int i = 0; i < executedRunnables.size; i++) {
					executedRunnables.get(i).run(); // calls out to random app code that could do anything ...
				}				
				
				
				
			} catch (Exception e) {
				if (logger.getLevel() >= Logger.ERROR) logger.error("Something fishy is going on here... Ex:" + e.getMessage());
			}

			if (logger.getLevel() >= Logger.DEBUG) logger.debug("Time's up (miliseconds)!" + elapsedNanoTime / GAME_CONSTANTS.ONE_MILISECOND_TO_NANO);

			lastTick = thisTick;
		}
	}


	@Override
	public synchronized void dispose() {
		gameTick.shutdownNow();
		gameTick.purge();
//		try {
//			gameTick.awaitTermination(1000, TimeUnit.MILLISECONDS);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		_listeners.clear();
		temp_listeners.clear();
		runnables.clear();
		
		event = null;
		instance = null;

	}

	/**
	 * This method allows that external code adds a runnable to be executed on
	 * the GameTickManager's thread context
	 */
	@Override
	public void postRunnable(Runnable runnable) {
		synchronized (runnables) {
			runnables.add(runnable);
		}
		
	}
	
	/**
	 *  Static shortcut to the postRunnable method
	 * @param runnable
	 */
	public static void PostRunnable(Runnable runnable)  {
		GameTickGenerator.getInstance().postRunnable(runnable);
	}
	
}
