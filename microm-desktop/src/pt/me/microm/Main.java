package pt.me.microm;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {


	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "microm-game";
		cfg.useGL20 = false;
		cfg.width = 800;// 240 480 480 800
		cfg.height = 480;// 320 800 854 1280
		cfg.vSyncEnabled = true;
		//cfg.fullscreen = true;
		
		new LwjglApplication(new GameMicroM(), cfg);
		
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
	}
}
