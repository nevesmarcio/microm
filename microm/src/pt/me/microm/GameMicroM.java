package pt.me.microm;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.ICommand;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.utils.Logger;

public class GameMicroM extends Game/*implements ApplicationListener*/ { // it extends the Game so it can handle Screens
	// FLAGS
	public static final boolean FLAG_DEV_ELEMENTS = false; 				// "pre-compiler" equivalent for branching development-only code
	public static final boolean FLAG_DISPLAY_ACTOR_SHAPES = true;		// mostra o desenho das shapes dos actores: walls, dabox, etc.
	public static final boolean FLAG_DISPLAY_ACTOR_TEXTURES = true;		// liga a texturização dos actores
	public static final boolean FLAG_DISPLAY_PARTICLES = true;			// liga o desenho de particulas
	
	private static final String TAG = GameMicroM.class.getSimpleName();
	private static Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);

	/**
	 * Worlflow implementation
	 */
	
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
				level((String)a[0]);
				logger.info("selectAWorld returning!");
				return null;
			}
		}));
		
		
	}
	
	private void level(final String world) {
		setScreen(ScreenLevelSelect.selectALevel(new ICommand() {
			@Override
			public Object handler(Object... a) {
				logger.info("selectALevel ending!");
				theJuice(world, (String)a[0]);
				logger.info("selectALevel returning!");
				return null;
			}
		}, world));
	}
	
	private void theJuice(final String world, final String level) {
		setScreen(ScreenTheJuice.playground(new ICommand() {
			@Override
			public Object handler(Object... a) {
				if (a!=null && ((String)a[0]).equalsIgnoreCase("exit")) {
					logger.info("playground ending!");
					logger.info("playground returning!");
					Gdx.app.exit();					
				}
				if (a!=null && ((String)a[0]).equalsIgnoreCase("pause")) {
					pauseGame();
				}
				return null;
			}
		}, world, level));
	}

	private void pauseGame() {
		setScreen(ScreenPause.pauseGame(new ICommand() {
			@Override
			public Object handler(Object... a) {
				logger.info("pause ending!");
				//theJuice();
				logger.info("pause returning!");				
				return null;
			}
		}));
		
	}
	
	@Override
	public void render() {
		// Clean do gl context
//		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
//		Gdx.gl.glClearColor(0.90f, 0.90f, 0.90f, 1); // almost white
		
		super.render();
	}
	
	
	
}
