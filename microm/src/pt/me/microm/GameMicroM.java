package pt.me.microm;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.ICommand;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Logger;

public class GameMicroM extends Game/*implements ApplicationListener*/ { // it extends the Game so it can handle Screens
	// FLAGS
	public static final boolean FLAG_DEV_ELEMENTS = false; 				// "pre-compiler" equivalent for branching development-only code
	public static final boolean FLAG_DISPLAY_ACTOR_SHAPES = true;		// mostra o desenho das shapes dos actores: walls, dabox, etc.
	public static final boolean FLAG_DISPLAY_ACTOR_TEXTURES = true;		// liga a texturização dos actores
	public static final boolean FLAG_DISPLAY_PARTICLES = true;			// liga o desenho de particulas
	
	private static final String TAG = GameMicroM.class.getSimpleName();
	private static Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);

	@Override
	public void create() {		
		setScreen(ScreenSplash.doHeavyLoading(new ICommand() {
			@Override
			public Object handler(Object... a) {
				logger.info("doHeavyLoading ending!");
				menu();
				logger.info("doHeavyLoading returning!");
				return null;
			}
		}));
		
	}

	private void menu() {
		setScreen(ScreenMenu.showMenu(new ICommand() {
			@Override
			public Object handler(Object... a) {
				logger.info("showMenu ending!");
				world();
				logger.info("showMenu returning!");
				return null;
			}
		}));
	}
	
	private void world() {
		setScreen(ScreenWorldSelect.selectAWorld(new ICommand() {
			@Override
			public Object handler(Object... a) {
				logger.info("selectAWorld ending!");
				level();
				logger.info("selectAWorld returning!");
				return null;
			}
		}));
		
		
	}
	
	private void level() {
		setScreen(ScreenLevelSelect.selectALevel(new ICommand() {
			@Override
			public Object handler(Object... a) {
				logger.info("selectALevel ending!");
				theJuice();
				logger.info("selectALevel returning!");
				return null;
			}
		}));
	}
	
	private void theJuice() {
		setScreen(ScreenTheJuice.playground(new ICommand() {
			@Override
			public Object handler(Object... a) {
				logger.info("playground ending!");
				logger.info("playground returning!");
				Gdx.app.exit();
				return null;
			}
		}));
	}

	
	//setScreen(new ScreenMenu(GameMicroM.this));
//	levelSelect = new ScreenLevelSelect(GameMicroM.this);
//	theJuice = new ScreenTheJuice(GameMicroM.this);
//	pausePopUp = new ScreenPause(GameMicroM.this);
//	setScreen(theJuice);		
	
	
	
}
