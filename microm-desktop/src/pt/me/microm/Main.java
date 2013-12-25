package pt.me.microm;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {


	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "seeds-game";
		cfg.useGL20 = false;
		cfg.width = 1280;//1024;//1280;//1600;//800;// 240 480 480 800
		cfg.height = 800;//768;//800;//1200;//480;// 320 800 854 1280
		cfg.vSyncEnabled = false;
		cfg.fullscreen = false;
		cfg.samples = 1;

		
		new LwjglApplication(new GameMicroM(), cfg);
		
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		
	}
}
