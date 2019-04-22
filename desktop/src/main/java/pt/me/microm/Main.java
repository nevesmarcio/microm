package pt.me.microm;


import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.example.projectionviewportcamera.ProjectionViewportCamera;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
    static double getVersion() {
        String version = System.getProperty("java.version");
        int pos = version.indexOf('.');
        pos = version.indexOf('.', pos + 1);
        return Double.parseDouble(version.substring(0, pos));
    }

    public static double JAVA_VERSION = getVersion();

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();

        cfg.setTitle("seeds-game");
        cfg.setWindowedMode(800,480);//800;// 240 480 480 800
        cfg.useVsync(true);
        //cfg.fullscreen = false;
        //cfg.samples = 1;

        Logger logger = LoggerFactory.getLogger(Main.class.getSimpleName());
        logger.debug("Hello world.");
        logger.info("JAVA_VERSION: " + JAVA_VERSION);

//        new Lwjgl3Application(new GameMicroM(), cfg);
        new Lwjgl3Application(new ProjectionViewportCamera(), cfg);

    }
}
