package pt.me.microm;

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
import pt.me.microm.model.base.CameraControllerDrag;
import pt.me.microm.model.base.CameraControllerStrafe;
import pt.me.microm.model.base.CameraModel;
import pt.me.microm.model.base.WorldModel;
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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;

public class ScreenTheJuice implements Screen {

	private static final String TAG = ScreenTheJuice.class.getSimpleName();
	private static final Logger logger = LoggerFactory.getLogger(TAG);
	
	// CONTROLLER RELATED
	private InputMultiplexer multiplexer;

	// MODEL RELATED
	private WorldModel worldModel;
	private CameraModel cameraModel;
	private UIModel uiModel;	
	
	// VIEW RELATED
	// Todas as views são instanciadas por "reflection"

	private ICommand callback;
	
	private UUID devID;
	private ScreenTheJuice(ICommand callback, String world, String level) {
		logger.info("ALLOC:" + (devID = UUID.randomUUID()).toString());
		
		this.callback = callback;

		Texture.setEnforcePotImages(false); // ver o melhor sitio para enfiar isto, dado que as texturas estão nas constantes.
		
		// MODELS ///////////////////////////////////////////////////////////////
		cameraModel = new CameraModel();
		worldModel = new WorldModel();
		uiModel = new UIModel(cameraModel, worldModel); // constroi o painel informativo?
		
		
		FileHandle h = Gdx.files.internal("data/levels/" + world + "/" + level);
		int nr_elements_loaded = LevelLoader.LoadLevel(h, worldModel, cameraModel);
		if (logger.isInfoEnabled()) logger.info("Nr elements loaded: " + nr_elements_loaded);		
		
		worldModel.addListener(WorldModel.EventType.ON_WORLD_COMPLETED, new IEventListener() {
			@Override
			public void onEvent(IEvent event) {
				ScreenTheJuice.this.callback.handler("completed", ScreenTheJuice.this);

			}
		});
		
		
		// VIEWS  ///////////////////////////////////////////////////////////////
		// Todas as views são instanciadas por "reflection"

		// CONTROLLERS - The GLUE ///////////////////////////////////////////////////////////////
		// Lança o controller dos ticks temporais : x second tick
		GameTickGenerator.getInstance(); //responsável pela actualizacao dos modelos
		ScreenTickManager.getInstance(); //responsável pela actualizacao das views
		
		//responsável pela extensibilidade do controller: delega o controlo a entidades externas (javascript + terminal)
		JsBridgeSingleton cs = JsBridgeSingleton.getInstance(worldModel);
		
		
		
//		//FIXME: for development purposes only
//		RemoteInput receiver = new RemoteInput(7777);
//		Gdx.input = receiver;
		
		
		// Cria o controller dos gestos e regista-o --> este pode actuar quer ao nivel do modelo quer ao nivel da view
//		multiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
//		if (multiplexer == null) multiplexer = new InputMultiplexer();
		multiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(multiplexer);
		
		MyGestureListener myGestureListener = new MyGestureListener();
		MyInputProcessor myInputProcessor = new MyInputProcessor();

		multiplexer.addProcessor(new GestureDetector(1, 1.0f, 1.0f, 1.0f, myGestureListener));
		multiplexer.addProcessor(myInputProcessor);
		multiplexer.addProcessor(worldModel);
		multiplexer.addProcessor(uiModel);
		multiplexer.addProcessor(new CameraControllerDrag(cameraModel));
		multiplexer.addProcessor(new CameraControllerStrafe(cameraModel));
	}
	
	public static Screen playground(PlayerProgress playerProgress, String world, String level, ICommand callback) {
		logger.info("playground start!");
		return new ScreenTheJuice(callback, world, level);
	}


	String clear_color = "0606060F";//"0606020F";
	@Override
	// the main loop - maximum fps possible (Update rate para a View)
	public void render(float delta) {
		long elapsedNanoTime = (long)(Gdx.graphics.getDeltaTime()*GAME_CONSTANTS.ONE_SECOND_TO_NANO);

		
        // use your own criterion here
    	if (Gdx.input.isKeyPressed(Keys.BACKSPACE)) {
    		callback.handler("exit", ScreenTheJuice.this);
    	}
    	
    	if (Gdx.input.isKeyPressed(Keys.PLUS)) {
    		callback.handler("pause", ScreenTheJuice.this);
    	}
		
		// Clean do gl context
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		//Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1); // cinza escuro
		Gdx.gl.glClearColor(Color.valueOf(clear_color).r, Color.valueOf(clear_color).g, Color.valueOf(clear_color).b, Color.valueOf(clear_color).a);
		
		ScreenTickManager.getInstance().fireEvent(false, cameraModel, elapsedNanoTime);    		

	}

	@Override
	public void resize(int width, int height) {
		cameraModel.resize(width, height);
		
	}

	@Override
	public void show() {
		if (logger.isDebugEnabled()) logger.debug("-->show()");
		
		Gdx.input.setInputProcessor(multiplexer);		
			
	}

	@Override
	public void hide() {
		if (logger.isInfoEnabled()) logger.info("-->hide()");
		
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
		worldModel.dispose();
		uiModel.dispose();
		
		GameTickGenerator.getInstance().dispose();
		ScreenTickManager.getInstance().dispose();
		JsBridgeSingleton.getInstance(worldModel).dispose();


		Gdx.input.setInputProcessor(new InputMultiplexer());

		FlashMessageManagerModel.getInstance().dispose();

	}

	@Override
	protected void finalize() throws Throwable {
		logger.info("GC'ed:"+devID);
		super.finalize();
	}
	
	
}
