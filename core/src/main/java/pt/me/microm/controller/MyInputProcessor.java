package pt.me.microm.controller;

import com.badlogic.gdx.InputProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyInputProcessor implements InputProcessor {
    private static final String TAG = MyInputProcessor.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(TAG);

    public MyInputProcessor() {

    }

    @Override
    public boolean keyDown(int keycode) {
        logger.debug("[KEY]: " + Integer.toString(keycode));

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        return false;
    }

    @Override
    public boolean keyTyped(char character) {

        return false;
    }

    /*
     * this method will not be called on android
     */
    @Override
    public boolean mouseMoved(int x, int y) {

        return false;
    }

    /*
     * this method will not be called on android
     */
    @Override
    public boolean scrolled(int amount) {

        return false;
    }


    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {

        return false;
    }


    @Override
    public boolean touchDragged(int x, int y, int pointer) {

        return false;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {

        return false;
    }


}
