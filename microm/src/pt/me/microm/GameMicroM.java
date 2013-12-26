package pt.me.microm;

import java.util.UUID;

import pt.me.microm.controller.loop.ScreenTickManager;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.ICommand;
import pt.me.microm.session.PlayerProgress;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
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
	
	
	/**
	 * Worlflow implementation
	 */
	private UUID devID;
	@Override
	public void create() {		
		devID = UUID.randomUUID();
		
		try {
			playerProgress = PlayerProgress.Load();
			
			logger.info("loaded state: dc: " + playerProgress.getDeathCount() + "; lpl: " + playerProgress.getLastPlayed() + ";");
			
		} catch (Exception e) {
			if (logger.getLevel() >= Logger.ERROR) logger.error("Cannot load savegame: " + e.getMessage());
		}
		
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setCatchMenuKey(true);			

		menu(playerProgress);
	}

	private void menu(PlayerProgress playerProgress) {
		setScreen(ScreenMenu.showMenu(playerProgress, new ICommand() {
			@Override
			public Object handler(Object... a) {
				logger.info("showMenu ending!");
				if (a!=null && ((String)a[0]).equalsIgnoreCase("back"))
					logger.info("back!");
				else				
					theJuice("world.1.justforkicks", "level#1.0.svg");
				logger.info("showMenu returning!");
				return null;
			}
		}));
	}
	
	private void theJuice(final String world, final String level) {
		setScreen(ScreenTheJuice.playground(new ICommand() {
			@Override
			public Object handler(final Object... a) {
				if (a!=null && ((String)a[0]).equalsIgnoreCase("exit")) {
					logger.info("playground ending!");
					logger.info("playground returning!");
					Gdx.app.exit();					
				}
				if (a!=null && ((String)a[0]).equalsIgnoreCase("pause")) {
					pauseGame((Screen)a[1]);
				}
				if (a!=null && ((String)a[0]).equalsIgnoreCase("completed")) {
					ScreenTickManager.PostRunnable(new Runnable() {
						
						@Override
						public void run() {
							((ScreenTheJuice)a[1]).dispose();
							menu(playerProgress);

						}
					});
				}				
				return null;
			}
		}, world, level));
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
		// Clean do gl context
//		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
//		Gdx.gl.glClearColor(0.90f, 0.90f, 0.90f, 1); // almost white
        
		if (Gdx.input.isKeyPressed(Keys.I))
			playerProgress.setDeathCount(playerProgress.getDeathCount() + 1);
			
		
		if (Gdx.input.isKeyPressed(Keys.SPACE) || Gdx.input.isKeyPressed(Keys.MENU)) { // use your own criterion here
        	if (logger.getLevel() >= Logger.INFO) logger.info("SessionID = " + devID);
			
        	playerProgress.save();
        	logger.info("saved state: dc: " + playerProgress.getDeathCount() + "; lpl: " + playerProgress.getLastPlayed() + ";");
		}
		
		super.render();
	}
	
	
	
}
