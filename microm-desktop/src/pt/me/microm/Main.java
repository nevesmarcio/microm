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
		cfg.width = 800;
		cfg.height = 800;
		cfg.vSyncEnabled = true;
		
		
		new LwjglApplication(new MicroMGame(), cfg);
		
		Gdx.app.setLogLevel(Application.LOG_INFO);
	}
}
