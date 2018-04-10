package pt.me.microm.controller;

import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyGestureListener implements GestureListener {
    private static final String TAG = MyGestureListener.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(TAG);

    public MyGestureListener() {

    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        if (logger.isDebugEnabled())
            logger.debug("touchDown");

        return false;
    }


    @Override
    public boolean tap(float x, float y, int count, int button) {
        if (logger.isDebugEnabled())
            logger.debug("tap");

        return false;
    }


    @Override
    public boolean longPress(float x, float y) {
        if (logger.isDebugEnabled())
            logger.debug("longPress");

        return false;
    }


    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        if (logger.isDebugEnabled())
            logger.debug("fling");

        return false;
    }


    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if (logger.isDebugEnabled())
            logger.debug("pan");

        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        if (logger.isDebugEnabled())
            logger.debug("panStop");

        return false;
    }

    @Override
    public boolean zoom(float originalDistance, float currentDistance) {
        if (logger.isDebugEnabled())
            logger.debug("zoom");

        return false;
    }

    @Override
    public boolean pinch(Vector2 initialFirstPointer,
                         Vector2 initialSecondPointer, Vector2 firstPointer,
                         Vector2 secondPointer) {
        if (logger.isDebugEnabled())
            logger.debug("pinch");

        return false;
    }

    @Override
    public void pinchStop() {
        if (logger.isDebugEnabled())
            logger.debug("pinchStop");
    }
}
