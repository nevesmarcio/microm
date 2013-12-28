package pt.me.microm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {


	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "seeds-game";
		cfg.useGL20 = false;
		cfg.width = 1280;//1024;//1280;//1600;//800;// 240 480 480 800
		cfg.height = 800;//768;//800;//1200;//480;// 320 800 854 1280
		cfg.vSyncEnabled = true;
		cfg.fullscreen = false;
		cfg.samples = 1;

		Logger logger = LoggerFactory.getLogger(Main.class.getSimpleName());
		logger.debug("Hello world.");
		
		new LwjglApplication(new GameMicroM(), cfg);
	}
}
