package pt.me.microm;

import java.util.UUID;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.ICommand;
import pt.me.microm.session.GameContentService;
import pt.me.microm.session.PlayerProgress;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.utils.Logger;

/**
 * This class maintain the context across the life time of the session!
 * @author mneves
 *
 */
public class GameMicroM extends Game/*implements ApplicationListener*/ { // it extends the Game so it can handle Screens
	// FLAGS
	public static final boolean FLAG_DEV_ELEMENTS_A = true; 			// "pre-compiler" equivalent for branching development-only code (lvl A)
	public static final boolean FLAG_DEV_ELEMENTS_B = true; 			// "pre-compiler" equivalent for branching development-only code (lvl B)
	public static final boolean FLAG_DISPLAY_ACTOR_SHAPES = true;		// mostra o desenho das shapes dos actores: walls, dabox, etc.
	public static final boolean FLAG_DISPLAY_PARTICLES = true;			// liga o desenho de particulas
	
	private static final String TAG = GameMicroM.class.getSimpleName();
	private static Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);

	private PlayerProgress playerProgress;
	private GameContentService gameContentService;
	
	/**
	 * Worlflow implementation
	 */
	private UUID devID;
	@Override
	public void create() {		
		devID = UUID.randomUUID();
		
		try {
			playerProgress = PlayerProgress.Load();
		} catch (Exception e) {
			if (logger.getLevel() >= Logger.ERROR) logger.error("Cannot load savegame: " + e.getMessage());
		}
		gameContentService = GameContentService.getInstance();
		
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setCatchMenuKey(true);			

		
		menu(playerProgress);
	}

	private void menu(final PlayerProgress playerProgress) {
		setScreen(ScreenMenu.showMenu(playerProgress, new ICommand() {
			@Override
			public Object handler(final Object... a) {
				logger.info("-->> ScreenMenu callback called!");
				
				if (a!=null && ((String)a[0]).equalsIgnoreCase("back")) {
					logger.info("back");
					
					((Screen)a[1]).hide();
					((Screen)a[1]).dispose();

					setScreen(null);
				} else if (((String)a[0]).equalsIgnoreCase("play")) {
					logger.info("play");
					
					((Screen)a[1]).hide();
					((Screen)a[1]).dispose();

					String[] b = gameContentService.getNextLevel();
					theJuice(playerProgress, b[0], b[1]);
					
				} else if (((String)a[0]).equalsIgnoreCase("supportus")) { 
					logger.info("supportus");
					((Screen)a[1]).hide();
					((Screen)a[1]).dispose();

					dummyScreen(playerProgress);
				} else if (((String)a[0]).equalsIgnoreCase("share")) { 
					logger.info("share");

					((Screen)a[1]).hide();
					((Screen)a[1]).dispose();

					dummyScreen(playerProgress);
				}
					
				logger.info("-->> ScreenMenu callback ending!");
				return null;
			}
		}));

	}
	
	private void dummyScreen(final PlayerProgress playerProgress) {
		setScreen(ScreenDummy.showDummy(playerProgress, new ICommand() {
			
			@Override
			public Object handler(Object... a) {
				logger.info("-->> ScreenDummy callback called!");
				
				if (a!=null && ((String)a[0]).equalsIgnoreCase("back")) {
					logger.info("back");
					
					((Screen)a[1]).hide();
					((Screen)a[1]).dispose();

					menu(playerProgress);
				}
				logger.info("-->> ScreenDummy callback ending!");
				return null;
			}
		}));
	}
	
	private void theJuice(final PlayerProgress playerProgress, String world, String level) {
		setScreen(ScreenTheJuice.playground(playerProgress, world, level, new ICommand() {
			@Override
			public Object handler(final Object... a) {
				logger.info("-->> ScreenTheJuice callback called!");

				((Screen)a[1]).hide();
				((Screen)a[1]).dispose();
				
				setScreen(null);
				
//				if (a!=null && ((String)a[0]).equalsIgnoreCase("exit")) {
//					
//					((Screen)a[1]).hide();
//					((Screen)a[1]).dispose();
//
//					menu(playerProgress);					
//				}
//				if (a!=null && ((String)a[0]).equalsIgnoreCase("pause")) {
//					pauseGame((Screen)a[1]);
//				}
//				if (a!=null && ((String)a[0]).equalsIgnoreCase("completed")) {
//					
//					((Screen)a[1]).hide();
//					((Screen)a[1]).dispose();
//					
//					menu(playerProgress);
//				}
				logger.info("-->> ScreenTheJuice callback ending!");
				return null;
			}
		}));
	}

	private void pauseGame(final Screen theJuice) {
		setScreen(ScreenPause.pauseGame(new ICommand() {
			@Override
			public Object handler(Object... a) {
				logger.info("pause ending!");
				if (a!=null && ((String)a[0]).equalsIgnoreCase("goto_menu")) {
					theJuice.dispose();
					menu(playerProgress);
				}
				else
					setScreen(theJuice);
				
				logger.info("pause returning!");				
				return null;
			}
		}));
		
	}
	
	@Override
	public void render() {


		if (Gdx.input.isKeyPressed(Keys.I) || Gdx.input.isKeyPressed(Keys.BACK))
			playerProgress.setDeathCount(playerProgress.getDeathCount() + 1);
			
		
		if (Gdx.input.isKeyPressed(Keys.SPACE) || Gdx.input.isKeyPressed(Keys.MENU)) { // use your own criterion here
        	if (logger.getLevel() >= Logger.INFO) logger.info("SessionID = " + devID);
			
        	playerProgress.save();
		}
		
		if (Gdx.input.isKeyPressed(Keys.G)) {
			logger.info("..about to force garbage collect");
			System.gc();
		}
		
		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			logger.info("..about to quit app");
//			Gdx.app.exit();
		}
		
		super.render();

		if (this.getScreen() == null) {
			// Clean do gl context
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			Gdx.gl.glClearColor(0.090f, 0.090f, 0.090f, 1); // almost white
			Gdx.app.exit();
		}
	}
	
	
	
}
