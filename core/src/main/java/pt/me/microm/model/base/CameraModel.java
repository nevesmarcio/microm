package pt.me.microm.model.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;


public class CameraModel extends AbstractModel {
    private static final String TAG = CameraModel.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(TAG);

    private Camera uiCamera;      // camera dedicada ao UI
    private Viewport uiViewport;
    private Camera gameCamera;    // game camera
    private Viewport gameViewport;

    private float fovy;
    private Vector3 camCenter;
    private float camDistance;

    private static CameraModel SINGLE_INSTANCE = null;

    public static CameraModel getInstance() {
        if (SINGLE_INSTANCE == null) {
            synchronized (CameraModel.class) {
                SINGLE_INSTANCE = new CameraModel();
            }
        }
        return SINGLE_INSTANCE;
    }

    private CameraModel() {
        initCamera();

        // Sinaliza os subscritores de que a construção do modelo terminou.
        this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));
    }


    private void initCamera() {
        float screenWidth = Gdx.graphics.getWidth(); // logical pixel, not physical pixel
        float screenHeight = Gdx.graphics.getHeight(); // logical pixel, not physical pixel

        //## UI CAMERA STUFF
        uiCamera = new OrthographicCamera(screenWidth/ screenHeight, 1);
        uiViewport = new FitViewport(screenWidth, screenHeight, uiCamera);

        //## GAME CAMERA STUFF
        fovy = 67; // mudando o fov, muda imenso a distância da camera ao viewport - calculo em camDistance
        gameCamera = new PerspectiveCamera(fovy, screenWidth / screenHeight, 1);
        gameViewport = new FitViewport(screenWidth, screenHeight, gameCamera);
    }


    /**
     * these are world units!!
     * @param windowWidth
     * @param windowHeight
     * @param positionX
     * @param positionY
     */
    float windowWidth;
    float windowHeight;
    float positionX;
    float positionY;
    public void adjustCamera(float windowWidth, float windowHeight, float positionX, float positionY) {
        this.windowWidth =windowWidth;
        this.windowHeight=windowHeight;
        this.positionX=positionX;
        this.positionY=positionY;

        // calculo da posição da camera atendendo ao tamanho do board e ao fovy
        camCenter = new Vector3(positionX, positionY, 0.0f);

        camDistance = windowHeight / (2.0f * (float) Math.tan(Math.toRadians(fovy / 2.0f))); // calculate camera distance so that Y axis shows 100% of the "camera" object read from SVG

        gameCamera.position.set(camCenter);
        //gameCamera.translate(camCenter);
        gameCamera.translate(0f,0f, camDistance);

        gameCamera.lookAt(camCenter.x, camCenter.y, camCenter.z);

        gameCamera.near = camDistance -1.0f; // given that it is a 2D game, clip the camera just before the plane of the game
        gameCamera.far = camDistance +1.0f; // given that it is a 2D game, clip the camera just after the plane of the game

        gameCamera.update();

    }

    /**
     * @param width  the canvas width
     * @param height the canvas height
     */

    public void resize(int width, int height) {

        initCamera();

        uiViewport.update(width, height);

        gameViewport.update(width, height);

        adjustCamera(windowWidth, windowHeight, positionX, positionY);
    }

    public Camera getUiCamera() {
        return uiCamera;
    }

    public Camera getGameCamera() {
        return gameCamera;
    }


    private static int CAM_MOVE_LEFT = 0x00000001;
    private static int CAM_MOVE_RIGHT = 0x00000002;
    private static int CAM_MOVE_UP = 0x00000004;
    private static int CAM_MOVE_DOWN = 0x00000008;

    private static int CAM_ZOOM_OUT = 0x00000010;
    private static int CAM_ZOOM_IN = 0x00000020;

    private static int CAM_LEAN_FWD = 0x00000040;
    private static int CAM_LEAN_BCK = 0x00000080;

    private static int CAM_TILT_LEFT = 0x00000100;
    private static int CAM_TILT_RIGHT = 0x00000200;

    private static int CAM_ROTATE_LEFT = 0x00000400;
    private static int CAM_ROTATE_RIGHT = 0x00000800;

    private int flags = 0x00000000;

    private Vector3 temp;
    private static float MVSPD = 0.1f * 10f;
    private static float ROTSPD = 1.0f;

    @Override
    public void handleGameTick(GameTickEvent e) {

        if ((flags & CAM_LEAN_FWD) != 0) {
            temp = gameCamera.up.cpy();
            temp.crs(gameCamera.direction);
            gameCamera.rotate(temp, 1.0f * ROTSPD);
        }
        if ((flags & CAM_LEAN_BCK) != 0) {
            temp = gameCamera.up.cpy();
            temp.crs(gameCamera.direction);
            gameCamera.rotate(temp, -1.0f * ROTSPD);
        }

        if ((flags & CAM_ROTATE_LEFT) != 0)
            gameCamera.rotate(gameCamera.up, -1.0f * ROTSPD);

        if ((flags & CAM_ROTATE_RIGHT) != 0)
            gameCamera.rotate(gameCamera.up, 1.0f * ROTSPD);


        if ((flags & CAM_TILT_LEFT) != 0) {
            gameCamera.rotate(gameCamera.direction, -1.0f * ROTSPD);
        }

        if ((flags & CAM_TILT_RIGHT) != 0) {
            gameCamera.rotate(gameCamera.direction, 1.0f * ROTSPD);
        }

        if ((flags & CAM_MOVE_LEFT) != 0) {
            temp = gameCamera.up.cpy();
            temp.crs(gameCamera.direction);
            temp.scl(1.0f * MVSPD);

            gameCamera.translate(temp);
        }
        if ((flags & CAM_MOVE_RIGHT) != 0) {
            temp = gameCamera.up.cpy();
            temp.crs(gameCamera.direction);
            temp.scl(-1.0f * MVSPD);

            gameCamera.translate(temp);
        }

        if ((flags & CAM_MOVE_UP) != 0) {
            temp = gameCamera.up.cpy();
            temp.scl(1.0f * MVSPD);

            gameCamera.translate(temp);
        }
        if ((flags & CAM_MOVE_DOWN) != 0) {
            temp = gameCamera.up.cpy();
            temp.scl(-1.0f * MVSPD);

            gameCamera.translate(temp);
        }


        if ((flags & CAM_ZOOM_OUT) != 0) {
            temp = gameCamera.direction.cpy();
            temp.scl(-1.0f * MVSPD);

            gameCamera.translate(temp);
        }
        if ((flags & CAM_ZOOM_IN) != 0) {
            temp = gameCamera.direction.cpy();
            temp.scl(1.0f * MVSPD);

            gameCamera.translate(temp);
        }


        if (flags != 0) {
            gameCamera.update(); // faz update às matrizes da camera após os movimentos
        }

    }

    public void startLeanFwd() {
        flags |= CAM_LEAN_FWD;
    }

    public void stopLeanFwd() {
        flags &= ~CAM_LEAN_FWD;
    }

    public void startLeanBck() {
        flags |= CAM_LEAN_BCK;
    }

    public void stopLeanBck() {
        flags &= ~CAM_LEAN_BCK;
    }

    public void startTiltLeft() {
        flags |= CAM_TILT_LEFT;
    }

    public void stopTiltLeft() {
        flags &= ~CAM_TILT_LEFT;
    }

    public void startTiltRight() {
        flags |= CAM_TILT_RIGHT;
    }

    public void stopTiltRight() {
        flags &= ~CAM_TILT_RIGHT;
    }

    public void startRotateLeft() {
        flags |= CAM_ROTATE_LEFT;
    }

    public void stopRotateLeft() {
        flags &= ~CAM_ROTATE_LEFT;
    }

    public void startRotateRight() {
        flags |= CAM_ROTATE_RIGHT;
    }

    public void stopRotateRight() {
        flags &= ~CAM_ROTATE_RIGHT;
    }

    public void startMoveCameraLeft() {
        flags |= CAM_MOVE_LEFT;
    }

    public void stopMoveCameraLeft() {
        flags &= ~CAM_MOVE_LEFT;
    }

    public void startMoveCameraRight() {
        flags |= CAM_MOVE_RIGHT;
    }

    public void stopMoveCameraRight() {
        flags &= ~CAM_MOVE_RIGHT;
    }

    public void startMoveCameraUp() {
        flags |= CAM_MOVE_UP;
    }

    public void stopMoveCameraUp() {
        flags &= ~CAM_MOVE_UP;
    }

    public void startMoveCameraDown() {
        flags |= CAM_MOVE_DOWN;
    }

    public void stopMoveCameraDown() {
        flags &= ~CAM_MOVE_DOWN;
    }

    public void startZoomOut() {
        flags |= CAM_ZOOM_OUT;
    }

    public void stopZoomOut() {
        flags &= ~CAM_ZOOM_OUT;
    }

    public void startZoomIn() {
        flags |= CAM_ZOOM_IN;
    }

    public void stopZoomIn() {
        flags &= ~CAM_ZOOM_IN;
    }

}
