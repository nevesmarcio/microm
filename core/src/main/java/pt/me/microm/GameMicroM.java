package pt.me.microm;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.me.microm.infrastructure.ICommand;
import pt.me.microm.session.GameContentService;
import pt.me.microm.session.PlayerProgress;

import java.util.UUID;

/**
 * This class maintain the context across the life time of the session!
 *
 * @author mneves
 */
public class GameMicroM extends Game/*implements ApplicationListener*/ { // it extends the Game so it can handle Screens
    // FLAGS
    public static final boolean FLAG_DEBUG_POINTS = true;                    // show circles around every point loaded from level designer
    public static final boolean FLAG_DEV_ELEMENTS_A = true;                // "pre-compiler" equivalent for branching development-only code (lvl A)
    public static final boolean FLAG_LOAD_LEVEL = true;                    // do not load the level just display controls and UI
    public static final boolean FLAG_DISPLAY_SCENE2D_DEBUG_LINES = true;    // do not show scene2d debug lines
    public static final boolean FLAG_DISPLAY_ACTOR_SHAPES = true;            // mostra o desenho das shapes dos actores: walls, dabox, etc.
    public static final boolean FLAG_DISPLAY_PARTICLES = true;                // liga o desenho de particulas

    private static final String TAG = GameMicroM.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(TAG);

    private PlayerProgress playerProgress;
    private GameContentService gameContentService;

    /**
     * Worlflow implementation - all screen transitions implemented in this class.
     */
    private UUID devID;

    @Override
    public void create() {
        devID = UUID.randomUUID();

        try {
            playerProgress = PlayerProgress.Load();
        } catch (Exception e) {
            if (logger.isErrorEnabled()) logger.error("Cannot load savegame: ", e);
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
                logger.info("-->> ScreenMenu callback called with '{}' command!", (String) a[0]);

                if (a != null && ((String) a[0]).equalsIgnoreCase("back")) {

                    ((Screen) a[1]).hide();
                    ((Screen) a[1]).dispose();

                    setScreen(null);

                } else if (((String) a[0]).equalsIgnoreCase("play")) {

                    ((Screen) a[1]).hide();
                    ((Screen) a[1]).dispose();

                    String[] b = gameContentService.getNextLevel();
                    theJuice(playerProgress, b[0], b[1]);

                } else if (((String) a[0]).equalsIgnoreCase("supportus")) {
                    ((Screen) a[1]).hide();
                    ((Screen) a[1]).dispose();

                    dummyScreen(playerProgress);
                } else if (((String) a[0]).equalsIgnoreCase("share")) {

                    ((Screen) a[1]).hide();
                    ((Screen) a[1]).dispose();

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
                logger.info("-->> ScreenDummy callback called with '{}' command!", (String) a[0]);

                if (a != null && ((String) a[0]).equalsIgnoreCase("back")) {

                    ((Screen) a[1]).hide();
                    ((Screen) a[1]).dispose();

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
                logger.info("-->> ScreenTheJuice callback called with '{}' command!", (String) a[0]);

                if (a != null && ((String) a[0]).equalsIgnoreCase("pause")) {
                    pauseGame((Screen) a[1]);
                } else {

                    ((Screen) a[1]).hide();
                    ((Screen) a[1]).dispose();

                    menu(playerProgress);
                }
//				setScreen(null);


//				if (a!=null && ((String)a[0]).equalsIgnoreCase("exit")) {
//					
//					((Screen)a[1]).hide();
//					((Screen)a[1]).dispose();
//
//					menu(playerProgress);					
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
                logger.info("-->> ScreenPause callback called with '{}' command!", (String) a[0]);

                if (a != null && ((String) a[0]).equalsIgnoreCase("goto_menu")) {
                    theJuice.dispose();
                    menu(playerProgress);
                } else
                    setScreen(theJuice);

                logger.info("-->> ScreenPause callback ending!");
                return null;
            }
        }));

    }

    @Override
    public void render() {

        //FIXME: need to find a strategy to bind the keys to the screens - like this they are shared across all screens
        if (Gdx.input.isKeyPressed(Keys.I) || Gdx.input.isKeyPressed(Keys.BACK))
            playerProgress.setDeathCount(playerProgress.getDeathCount() + 1);


        if (Gdx.input.isKeyPressed(Keys.SPACE) || Gdx.input.isKeyPressed(Keys.MENU)) { // use your own criterion here
            logger.info("SessionID = " + devID);

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
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            Gdx.gl.glClearColor(0.090f, 0.090f, 0.990f, 1); // almost white
//			Gdx.app.exit();
        }
    }


}
