package pt.me.microm.controller.loop.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EventObject;


public class GameTickEvent extends EventObject {

    private static final String TAG = GameTickEvent.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(TAG);

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
