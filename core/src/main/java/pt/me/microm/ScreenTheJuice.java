package pt.me.microm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.me.microm.api.JsBridgeSingleton;
import pt.me.microm.controller.MyGestureListener;
import pt.me.microm.controller.MyInputProcessor;
import pt.me.microm.controller.loop.GameTickGenerator;
import pt.me.microm.controller.loop.ScreenTickManager;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.ICommand;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.IModelCategory1;
import pt.me.microm.model.IModelCategory2;
import pt.me.microm.model.base.CameraControllerStrafe;
import pt.me.microm.model.base.CameraModel;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.model.stuff.DaBoxModel;
import pt.me.microm.model.stuff.GoalModelEvent;
import pt.me.microm.model.stuff.SpawnModel;
import pt.me.microm.model.ui.UIMetricsModel;
import pt.me.microm.model.ui.UIModel;
import pt.me.microm.model.ui.utils.FlashMessageManagerModel;
import pt.me.microm.session.PlayerProgress;
import pt.me.microm.tools.levelloader.LevelLoader;

import java.util.ArrayList;
import java.util.UUID;

public class ScreenTheJuice implements Screen {

    private static final String TAG = ScreenTheJuice.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(TAG);

    // CONTROLLER RELATED
    private GameTickGenerator gameTickGenerator;
    private ScreenTickManager screenTickManager;
    private InputMultiplexer multiplexer;
    private JsBridgeSingleton cs;

    // MODEL RELATED
    private WorldModel worldModel;
    private CameraModel cameraModel;
    private UIModel uiModel;
    private UIMetricsModel uiMetricsModel;
    private FlashMessageManagerModel flashMessageManagerModel;
    private ArrayList<AbstractModel> modelBag;
    private EventBus modelEventBus;

    // VIEW RELATED
    // Todas as views são instanciadas por "reflection"

    private ICommand callback;

    private UUID devID;

    private ScreenTheJuice(ICommand callback, String world, String level) {
        this.callback = callback;
        if (logger.isDebugEnabled()) logger.debug("ALLOC: {}", (devID = UUID.randomUUID()).toString());

        // CONTROLLERS - The GLUE ///////////////////////////////////////////////////////////////
        // Lança o controller dos ticks temporais : x second tick
        gameTickGenerator = GameTickGenerator.getInstance(); //responsável pela actualizacao dos modelos
        screenTickManager = ScreenTickManager.getInstance(); //responsável pela actualizacao das views

        // MODELS EVENTBUS
        modelEventBus = new EventBus();
        class Lst {
//			@Subscribe
//			void listenA(DebugModelEvent debugModelEvent) {
//				logger.info("%%%%%-{}", debugModelEvent);
//			}

            @Subscribe
            void listenB(IModelCategory1 debugModelEvent) {
                logger.info("%%%%%-{}", debugModelEvent);
            }

            @Subscribe
            void listenC(IModelCategory2 debugModelEvent) {
                logger.info("%%%%%-{}", debugModelEvent);
            }

            @Subscribe
            void listenGoal(GoalModelEvent goalModelEvent) {
                if (goalModelEvent.getEventType() == GoalModelEvent.OnTouch.class)
                    ScreenTheJuice.this.callback.handler("exit", ScreenTheJuice.this);
            }
        }
        modelEventBus.register(new Lst());

        // MODELS ////////////////////////////////////////////////////////////////
        cameraModel = CameraModel.getInstance();                                        // camera model
        worldModel = new WorldModel(modelEventBus);                                            // world model
        uiModel = new UIModel(cameraModel, worldModel);                        // constroi o painel informativo?
        uiMetricsModel = new UIMetricsModel();                                    // metricas?
        flashMessageManagerModel = FlashMessageManagerModel.getInstance();        // responsavel para apresentacao de flash messages

        if (GameMicroM.FLAG_LOAD_LEVEL) {
            FileHandle h = Gdx.files.internal("data/levels/" + world + "/" + level);
            modelBag = LevelLoader.LoadLevel(h, modelEventBus);
            // todo: dependency injection here
            DaBoxModel dbm = null;//dabox injection in spawn
            SpawnModel spawnModel = null;
            for (AbstractModel am : modelBag) {
                if (am instanceof SpawnModel)
                    spawnModel = (SpawnModel) am;
                if (am instanceof DaBoxModel)
                    dbm = (DaBoxModel) am;
            }
            spawnModel.setDbm(dbm);
            worldModel.setPlayer(dbm);
            worldModel.setSpawnModel(spawnModel);


            if (logger.isInfoEnabled()) logger.info("Nr elements loaded: " + modelBag.size());
        }

        // VIEWS  ///////////////////////////////////////////////////////////////
        // Todas as views são instanciadas por "reflection"


        // CONTROLLER EXTENSIBILITY ////////
        // responsável pela extensibilidade do controller: delega o controlo a entidades externas (javascript + terminal)
        cs = JsBridgeSingleton.getInstance(worldModel);


//		//FIXME: for development purposes only
//		RemoteInput receiver = new RemoteInput(7777);
//		Gdx.input = receiver;


        // Cria o controller dos gestos e regista-o --> este pode actuar quer ao nivel do modelo quer ao nivel da view
        multiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(multiplexer);

        MyGestureListener myGestureListener = new MyGestureListener();
        MyInputProcessor myInputProcessor = new MyInputProcessor();

        multiplexer.addProcessor(new GestureDetector(1, 1.0f, 1.0f, 1.0f, myGestureListener));
        multiplexer.addProcessor(myInputProcessor);
        multiplexer.addProcessor(worldModel);
        multiplexer.addProcessor(new GestureDetector(1, 1.0f, 1.0f, 1.0f, worldModel));
        multiplexer.addProcessor(uiModel);
//		multiplexer.addProcessor(new CameraControllerDrag(cameraModel));
        multiplexer.addProcessor(new CameraControllerStrafe(cameraModel));

    }

    public static Screen playground(PlayerProgress playerProgress, String world, String level, ICommand callback) {
        if (logger.isInfoEnabled()) logger.info("playground start!");
        return new ScreenTheJuice(callback, world, level);
    }

    private static final String clear_color = "0606060F";//"0606020F";
    private static final float r = Color.valueOf(clear_color).r;
    private static final float g = Color.valueOf(clear_color).g;
    private static final float b = Color.valueOf(clear_color).b;
    private static final float a = Color.valueOf(clear_color).a;

    @Override
    // the main loop - maximum fps possible (Update rate para a View)
    public void render(float delta) {
        long elapsedNanoTime = (long) (Gdx.graphics.getDeltaTime() * GAME_CONSTANTS.ONE_SECOND_TO_NANO);

        // use your own criterion here
        if (Gdx.input.isKeyPressed(Keys.BACKSPACE) || Gdx.input.isKeyPressed(Keys.BACK)) {
            callback.handler("exit", ScreenTheJuice.this);
        }

        if (Gdx.input.isKeyPressed(Keys.P)) {
            callback.handler("pause", ScreenTheJuice.this);
        }

        // Clean do gl context
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(r, g, b, a);

        if ((screenTickManager != null) && screenTickManager.isAvailable())
            screenTickManager.fireEvent(true, cameraModel, elapsedNanoTime);
        else if (logger.isWarnEnabled()) logger.warn("screenTickManager not available");
    }

    @Override
    public void resize(int width, int height) {
        cameraModel.resize(width, height);
    }

    @Override
    public void show() {
        if (logger.isDebugEnabled()) logger.debug("-->show()");
    }

    @Override
    public void hide() {
        if (logger.isDebugEnabled()) logger.debug("-->hide()");
    }

    @Override
    public void pause() {
        if (logger.isDebugEnabled()) logger.debug("-->pause()");
    }

    @Override
    public void resume() {
        if (logger.isDebugEnabled()) logger.debug("-->resume()");
    }

    @Override
    public void dispose() {
        if (logger.isInfoEnabled()) logger.info("disposing...");

        // Do not dispose this ones! They are to be reused as long as the app is running
//		gameTickGenerator.dispose();
//		screenTickManager.dispose();

        cameraModel.dispose();
        worldModel.dispose();
        uiModel.dispose();
        uiMetricsModel.dispose();
        flashMessageManagerModel.dispose();

        // dispose of modelBag
        if (modelBag != null)
            for (AbstractModel m : modelBag)
                m.dispose();

        cs.dispose();
    }

    @Override
    protected void finalize() throws Throwable {
        if (logger.isDebugEnabled()) logger.debug("GC'ed: {}", devID);
        super.finalize();
    }


}
