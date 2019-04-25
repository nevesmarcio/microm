package pt.me.microm.model.base;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CameraControllerDrag extends InputAdapter {
    private static final String TAG = CameraControllerDrag.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(TAG);

    public CameraModel cam;

    enum TransformMode {
        Rotate, Translate, Zoom, None
    }

    Vector3 lookAt = new Vector3();
    TransformMode mode = TransformMode.Translate;
    boolean translated = false;

    public CameraControllerDrag(CameraModel cam) {
        this.cam = cam;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        mode = TransformMode.Rotate;
        last.set(x, y);
        tCurr.set(x, y);
        return true;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        mode = TransformMode.None;
        return true;
    }

    Vector2 tCurr = new Vector2();
    Vector2 last = new Vector2();
    Vector2 delta = new Vector2();
    Vector2 currWindow = new Vector2();
    Vector2 lastWindow = new Vector2();
    Vector3 curr3 = new Vector3();
    Vector3 delta3 = new Vector3();
    Plane lookAtPlane = new Plane(new Vector3(0, 1, 0), 0);
    Matrix4 rotMatrix = new Matrix4();
    Vector3 xAxis = new Vector3(1, 0, 0);
    Vector3 yAxis = new Vector3(0, 1, 0);
    Vector3 point = new Vector3();

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        if (pointer != 0) return false;
        delta.set(x, y).sub(last);

        if (mode == TransformMode.Rotate) {
            point.set(cam.getGameCamera().position).sub(lookAt);

            if (point.cpy().nor().dot(yAxis) < 0.9999f) {
                xAxis.set(cam.getGameCamera().direction).crs(yAxis).nor();
                rotMatrix.setToRotation(xAxis, delta.y / 5);
                point.mul(rotMatrix);
            }

            rotMatrix.setToRotation(yAxis, -delta.x / 5);
            point.mul(rotMatrix);

            cam.getGameCamera().position.set(point.add(lookAt));
            cam.getGameCamera().lookAt(lookAt.x, lookAt.y, lookAt.z);
        }
        if (mode == TransformMode.Zoom) {
            ((PerspectiveCamera)cam.getGameCamera()).fieldOfView -= -delta.y / 10;
        }
        if (mode == TransformMode.Translate) {
            tCurr.set(x, y);
            translated = true;
        }

        cam.getGameCamera().update();
        last.set(x, y);
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        ((PerspectiveCamera)cam.getGameCamera()).fieldOfView -= -amount * Gdx.graphics.getDeltaTime() * 100;
        cam.getGameCamera().update();

        return true;
    }

}
