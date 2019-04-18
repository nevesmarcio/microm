package pt.me.microm.controller.loop.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.me.microm.model.base.CameraModel;

import java.util.EventObject;


public class ScreenTickEvent extends EventObject {

    private static final String TAG = ScreenTickEvent.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(TAG);

    private static final long serialVersionUID = 1L;

    public ScreenTickEvent(Object source) {
        super(source);
    }

    private long elapsedNanoTime;
    private CameraModel camModel;


    /**/
    public long getElapsedNanoTime() {
        return elapsedNanoTime;
    }

    public void setElapsedNanoTime(long elapsedNanoTime) {
        this.elapsedNanoTime = elapsedNanoTime;
    }

    /**/
    public CameraModel getCamera() {
        return camModel;
    }

    public void setCamera(CameraModel camModel) {
        this.camModel = camModel;
    }

}
