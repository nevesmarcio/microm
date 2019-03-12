package pt.me.microm;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class Main {
	public static double JAVA_VERSION = getVersion ();

	static double getVersion () {
		String version = System.getProperty("java.version");
		int pos = version.indexOf('.');
		pos = version.indexOf('.', pos+1);
		return Double.parseDouble (version.substring (0, pos));
	}

	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "seeds-game";
		cfg.width = 800;//320;//1024;//1280;//1600;//800;// 240 480 480 800
		cfg.height = 480;//240;//768;//800;//1200;//480;// 320 800 854 1280
		cfg.vSyncEnabled = true;
		cfg.fullscreen = false;
		cfg.samples = 1;

		Logger logger = LoggerFactory.getLogger(Main.class.getSimpleName());
		logger.debug("Hello world.");
		logger.info("JAVA_VERSION: " + JAVA_VERSION);

		new LwjglApplication(new GameMicroM(), cfg);
	}
}
