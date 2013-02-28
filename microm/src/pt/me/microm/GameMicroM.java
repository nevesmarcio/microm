package pt.me.microm;

import pt.me.microm.controller.MyGestureListener;
import pt.me.microm.controller.MyInputProcessor;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.GameTickGenerator;
import pt.me.microm.infrastructure.ScreenTickManager;
import pt.me.microm.model.base.CameraModel;
import pt.me.microm.model.base.WorldModel;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.RemoteInput;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Logger;

public class GameMicroM extends Game/*implements ApplicationListener*/ { // it extends the Game so it can handle Screens
	// FLAGS
	public static final boolean FLAG_DEV_ELEMENTS = false; 				// "pre-compiler" equivalent for branching development-only code
	public static final boolean FLAG_DISPLAY_ACTOR_SHAPES = false;		// mostra o desenho das shapes dos actores: walls, dabox, etc.
	public static final boolean FLAG_DISPLAY_ACTOR_TEXTURES = true;		// liga a texturização dos actores
	public static final boolean FLAG_DISPLAY_PARTICLES = true;			// liga o desenho de particulas
	
	private static final String TAG = GameMicroM.class.getSimpleName();
	private static Logger logger = new Logger(TAG);
	

	public ScreenAbstract splash;
	public ScreenTheJuice theJuice;
	
	@Override
	public void create() {		
		splash = new ScreenSplash(this);
		theJuice = new ScreenTheJuice(this);
		
		setScreen(splash);
		
	}
	
}
