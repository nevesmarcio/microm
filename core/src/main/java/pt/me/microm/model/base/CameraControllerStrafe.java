package pt.me.microm.model.base;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class CameraControllerStrafe extends InputAdapter {
    private static final String TAG = CameraControllerStrafe.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(TAG);

    public CameraModel cam;

    public CameraControllerStrafe(CameraModel cam) {
        this.cam = cam;
    }


    public void printCameraValues() {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(' ');
        DecimalFormat fmt = new DecimalFormat("0.00", otherSymbols);

        logger.info("cam position: " + fmt.format(cam.getGameCamera().position.x) + ", " + fmt.format(cam.getGameCamera().position.y) + ", " + fmt.format(cam.getGameCamera().position.z));
        logger.info("cam direction: " + fmt.format(cam.getGameCamera().direction.x) + ", " + fmt.format(cam.getGameCamera().direction.y) + ", " + fmt.format(cam.getGameCamera().direction.z));
        logger.info("cam up: " + fmt.format(cam.getGameCamera().up.x) + ", " + fmt.format(cam.getGameCamera().up.y) + ", " + fmt.format(cam.getGameCamera().up.z));
    }

    // InputProcessor interface implementation
    @Override
    public boolean keyDown(int keycode) {

        if (logger.isDebugEnabled()) logger.debug("[KEY]: " + Integer.toString(keycode));

        if (keycode == Keys.P)
            printCameraValues();

        if (keycode == Keys.Q)
            cam.startZoomIn();
        if (keycode == Keys.A)
            cam.startZoomOut();

        if (keycode == Keys.W)
            cam.startLeanFwd();
        if (keycode == Keys.S)
            cam.startLeanBck();

        if (keycode == Keys.Z)
            cam.startRotateLeft();
        if (keycode == Keys.X)
            cam.startRotateRight();

        if (keycode == Keys.E)
            cam.startTiltLeft();
        if (keycode == Keys.R)
            cam.startTiltRight();


        if (keycode == Keys.LEFT) {
            cam.startMoveCameraLeft();
        }

        if (keycode == Keys.RIGHT) {
            cam.startMoveCameraRight();

        }
        if (keycode == Keys.DOWN) {
            cam.startMoveCameraDown();
        }
        if (keycode == Keys.UP) {
            cam.startMoveCameraUp();
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        if (keycode == Keys.Q)
            cam.stopZoomIn();
        if (keycode == Keys.A)
            cam.stopZoomOut();

        if (keycode == Keys.W)
            cam.stopLeanFwd();
        if (keycode == Keys.S)
            cam.stopLeanBck();

        if (keycode == Keys.Z)
            cam.stopRotateLeft();
        if (keycode == Keys.X)
            cam.stopRotateRight();

        if (keycode == Keys.E)
            cam.stopTiltLeft();
        if (keycode == Keys.R)
            cam.stopTiltRight();

        if (keycode == Keys.LEFT) {
            cam.stopMoveCameraLeft();
        }
        if (keycode == Keys.RIGHT) {
            cam.stopMoveCameraRight();
        }
        if (keycode == Keys.UP) {
            cam.stopMoveCameraUp();
        }
        if (keycode == Keys.DOWN) {
            cam.stopMoveCameraDown();
        }

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
