package pt.me.microm;

import java.util.ArrayList;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.me.microm.api.JsBridgeSingleton;
import pt.me.microm.controller.MyGestureListener;
import pt.me.microm.controller.MyInputProcessor;
import pt.me.microm.controller.loop.GameTickGenerator;
import pt.me.microm.controller.loop.ScreenTickManager;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.ICommand;
import pt.me.microm.infrastructure.event.IEvent;
import pt.me.microm.infrastructure.event.listener.IEventListener;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.base.CameraControllerDrag;
import pt.me.microm.model.base.CameraControllerStrafe;
import pt.me.microm.model.base.CameraModel;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.model.ui.UIMetricsModel;
import pt.me.microm.model.ui.UIModel;
import pt.me.microm.model.ui.utils.FlashMessageManagerModel;
import pt.me.microm.session.PlayerProgress;
import pt.me.microm.tools.levelloader.LevelLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.input.GestureDetector;

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

		// MODELS ////////////////////////////////////////////////////////////////
		cameraModel = new CameraModel();										// camera model
		worldModel = new WorldModel();											// world model
		uiModel = new UIModel(cameraModel, worldModel); 						// constroi o painel informativo?
		uiMetricsModel = new UIMetricsModel();									// metricas?
		flashMessageManagerModel = FlashMessageManagerModel.getInstance(); 		// responsavel para apresentacao de flash messages
		
		if (GameMicroM.FLAG_LOAD_LEVEL) {
			FileHandle h = Gdx.files.internal("data/levels/" + world + "/" + level);
			modelBag = LevelLoader.LoadLevel(h, worldModel, cameraModel);
			if (logger.isInfoEnabled()) logger.info("Nr elements loaded: " + modelBag.size());		
		}
		worldModel.addListener(WorldModel.EventType.ON_WORLD_COMPLETED, new IEventListener() {
			@Override
			public void onEvent(IEvent event) {
				ScreenTheJuice.this.callback.handler("exit", ScreenTheJuice.this);
			}
		});
		
		
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
		multiplexer.addProcessor(uiModel);
//		multiplexer.addProcessor(new CameraControllerDrag(cameraModel));
//		multiplexer.addProcessor(new CameraControllerStrafe(cameraModel));

	}
	
	public static Screen playground(PlayerProgress playerProgress, String world, String level, ICommand callback) {
		if (logger.isInfoEnabled()) logger.info("playground start!");
		return new ScreenTheJuice(callback, world, level);
	}

	String clear_color = "0606060F";//"0606020F";
	@Override
	// the main loop - maximum fps possible (Update rate para a View)
	public void render(float delta) {
		long elapsedNanoTime = (long)(Gdx.graphics.getDeltaTime()*GAME_CONSTANTS.ONE_SECOND_TO_NANO);

        // use your own criterion here
    	if (Gdx.input.isKeyPressed(Keys.BACKSPACE) || Gdx.input.isKeyPressed(Keys.BACK)) {
    		callback.handler("exit", ScreenTheJuice.this);
    	}
    	
    	if (Gdx.input.isKeyPressed(Keys.PLUS)) {
    		callback.handler("pause", ScreenTheJuice.this);
    	}
		
		// Clean do gl context
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(Color.valueOf(clear_color).r, Color.valueOf(clear_color).g, Color.valueOf(clear_color).b, Color.valueOf(clear_color).a);
		

		if ((screenTickManager != null) && screenTickManager.isAvailable())	
			screenTickManager.fireEvent(false, cameraModel, elapsedNanoTime);
		else
			if (logger.isWarnEnabled()) logger.warn("screenTickManager not available");
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
