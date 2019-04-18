package pt.me.microm.controller.loop;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.controller.loop.itf.IGameTick;
import pt.me.microm.controller.loop.itf.IProcessRunnable;
import pt.me.microm.infrastructure.GAME_CONSTANTS;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class GameTickGenerator implements IProcessRunnable, Disposable {
    private static int dbg = 0;
    private static final String TAG = GameTickGenerator.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(TAG);

    private final List<IGameTick> _listeners = new ArrayList<IGameTick>();

    // runnables
    protected final Array<Runnable> runnables = new Array<Runnable>();
    protected final Array<Runnable> executedRunnables = new Array<Runnable>();

    public synchronized void addEventListener(IGameTick listener) {
        if (logger.isInfoEnabled()) logger.info("{} is now receiving gametick events", listener);
        _listeners.add(listener);
        isTempListenersDirty = true;
    }

    public synchronized void removeEventListener(IGameTick listener) {
        boolean result = false;
        result = _listeners.remove(listener);
        if (result) {
            if (logger.isInfoEnabled()) logger.info("{} is no longer receiving gametick events", listener);
            isTempListenersDirty = true;
        } else if (logger.isWarnEnabled())
            logger.warn("listener {} was not registered", listener);

    }

    /**
     * Call this method whenever you want to notify
     * the event listeners of the particular event
     *
     * @param elapsedNanoTime
     */
    private GameTickEvent event = new GameTickEvent(this);            // reutilização do evento
    private Iterator<IGameTick> i;                                            // reutilização da variável para iteração sobre a lista
    private boolean print = false;
    private IGameTick gti;                                                    // reutilização do GameTickInterface
    private List<IGameTick> temp_listeners = new ArrayList<IGameTick>();    // reutilização da lista de listeners
    private boolean isTempListenersDirty = true;                            // variável de controlo para saber se a lista de listeners mudou

    private synchronized void fireEvent(long elapsedNanoTime) {
        event.setElapsedNanoTime(elapsedNanoTime);

        try {
            /* cria uma copia para iterar */
            if (isTempListenersDirty) {
                if (logger.isDebugEnabled())
                    logger.debug("[diff] _listeners|temp_listeners: " + _listeners.size() + "|" + temp_listeners.size());
                temp_listeners.clear();
                temp_listeners.addAll(_listeners);
                isTempListenersDirty = false;
                print = true;
            }

            i = temp_listeners.iterator();
            while (i.hasNext()) {
                gti = i.next();
                gti.handleGameTick(event);

                if (print) {
                    if (logger.isTraceEnabled()) logger.trace("\t[TickGen]" + gti.getClass().getName());
                }
            }
            print = false;
        } catch (ConcurrentModificationException ex) {
            if (logger.isWarnEnabled()) logger.warn("[EXCEPTION]: ", ex);
            throw ex;
        }

        // release to allow GC
        gti = null;
    }

    private static GameTickGenerator instance = null;

    private class MyThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);

            SecureRandom random = new SecureRandom();
            t.setName("_gticker_" + new BigInteger(48, random).toString(32));

            //t.setName("_game_tick_generator_" + UUID.randomUUID().toString());

            return t;
            //return Executors.defaultThreadFactory().newThread(r);
        }
    }

    private GameTickGenerator() {
        if (logger.isInfoEnabled()) logger.info("warming up tick machine...");
        gameTick = new ScheduledThreadPoolExecutor(1);
        gameTick.setThreadFactory(new MyThreadFactory());
        gameTick.scheduleAtFixedRate(new GameCycleTask(), 0, GAME_CONSTANTS.GAME_TICK_MILI, TimeUnit.MILLISECONDS);
    }

    public static GameTickGenerator getInstance() {
        if (instance == null) {
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            if (logger.isInfoEnabled())
                logger.info("called by: {}.{}", stackTraceElements[2].getClassName(), stackTraceElements[2].getMethodName());

            if (dbg > 0) throw new RuntimeException("Nope! Not again!");
            dbg += 1;
            instance = new GameTickGenerator();
        }
        return instance;
    }

    public synchronized boolean isAvailable() {
        return instance == null ? false : true;
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
                if (logger.isErrorEnabled()) logger.error("Something fishy is going on here... Ex:", e);
            }

            if (logger.isDebugEnabled())
                logger.debug("Time's up (miliseconds)! {}", elapsedNanoTime / GAME_CONSTANTS.ONE_MILISECOND_TO_NANO);

            lastTick = thisTick;
        }
    }


    @Override
    public synchronized void dispose() {
        if (logger.isInfoEnabled()) logger.info("shutting down tick machine...");

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

    @Override
    protected void finalize() throws Throwable {
        if (logger.isInfoEnabled()) logger.info("GC'ed!");
        super.finalize();
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
     * Static shortcut to the postRunnable method
     *
     * @param runnable
     */
    public static void PostRunnable(Runnable runnable) {
        GameTickGenerator.getInstance().postRunnable(runnable);
    }

}
