package pt.me.microm;

import java.util.UUID;

import pt.me.microm.api.JsBridgeSingleton;
import pt.me.microm.controller.MyGestureListener;
import pt.me.microm.controller.MyInputProcessor;
import pt.me.microm.controller.loop.GameTickGenerator;
import pt.me.microm.controller.loop.ScreenTickManager;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.ICommand;
import pt.me.microm.infrastructure.event.IEvent;
import pt.me.microm.infrastructure.event.listener.IEventListener;
import pt.me.microm.model.base.CameraModel;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.model.dev.GridModel;
import pt.me.microm.model.ui.UIModel;
import pt.me.microm.model.ui.utils.FlashMessageManagerModel;
import pt.me.microm.tools.levelloader.LevelLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.utils.Logger;

public class ScreenTheJuice implements Screen {

	private static final String TAG = ScreenTheJuice.class.getSimpleName();
	private static Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	// CONTROLLER RELATED
	private InputMultiplexer multiplexer;

	// MODEL RELATED
	private WorldModel worldModel;
	private CameraModel cameraModel;
	private GridModel grid;
	private UIModel ui;	
	
	// VIEW RELATED
	// Todas as views são instanciadas por "reflection"

	private ICommand callback;
	
	private UUID devID;
	private ScreenTheJuice(ICommand callback, String world, String level) {
		logger.info("ALLOC:" + (devID = UUID.randomUUID()).toString());
		
		this.callback = callback;

		Texture.setEnforcePotImages(true); // ver o melhor sitio para enfiar isto, dado que as texturas estão nas constantes.
		
		// MODELS ///////////////////////////////////////////////////////////////
		cameraModel = new CameraModel();
		worldModel = new WorldModel();
		ui = new UIModel(cameraModel, worldModel); // constroi o painel informativo?
		
		
		FileHandle h = Gdx.files.internal("data/levels/" + world + "/" + level);
		int nr_elements_loaded = LevelLoader.LoadLevel(h, worldModel, cameraModel);
		if (logger.getLevel() == Logger.INFO) logger.info("Nr elements loaded: " + nr_elements_loaded);		
		
		worldModel.addListener(WorldModel.EventType.ON_WORLD_COMPLETED, new IEventListener() {
			@Override
			public void onEvent(IEvent event) {
				ScreenTheJuice.this.callback.handler("completed", ScreenTheJuice.this);

			}
		});
		
		
		// Modelos complementares ao WorldModel
		if (GameMicroM.FLAG_DEV_ELEMENTS_B)
			grid = new GridModel(); // constroi a grid sobre a qual estão renderizados os objectos - debug purposes		

			
		
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
//		multiplexer.addProcessor(cameraModel);
		multiplexer.addProcessor(ui);

	}
	
	public static Screen playground(ICommand callback, String world, String level) {
		logger.info("playground start!");
		return new ScreenTheJuice(callback, world, level);
	}


	String clear_color = "0606060F";//"0606020F";
	@Override
	// the main loop - maximum fps possible (Update rate para a View)
	public void render(float delta) {
		long elapsedNanoTime = (long)(Gdx.graphics.getDeltaTime()*GAME_CONSTANTS.ONE_SECOND_TO_NANO);
		
        // use your own criterion here
    	if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
    		callback.handler("exit");
    	}
    	
    	if (Gdx.input.isKeyPressed(Keys.PLUS) || Gdx.input.isKeyPressed(Keys.BACK)) {
    		callback.handler("pause", this);
    	}
		
    	if (Gdx.graphics.isGL20Available()) {
    		
    		// Clean do gl context
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//			Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1); // cinza escuro
			Gdx.gl.glClearColor(Color.valueOf(clear_color).r, Color.valueOf(clear_color).g, Color.valueOf(clear_color).b, Color.valueOf(clear_color).a);
    		
			ScreenTickManager.getInstance().fireEvent(true, cameraModel, elapsedNanoTime);
			
    	} else {
			// Clean do gl context
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			//Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1); // cinza escuro
			Gdx.gl.glClearColor(Color.valueOf(clear_color).r, Color.valueOf(clear_color).g, Color.valueOf(clear_color).b, Color.valueOf(clear_color).a);
			
    		ScreenTickManager.getInstance().fireEvent(false, cameraModel, elapsedNanoTime);    		
    	}

	}

	@Override
	public void resize(int width, int height) {
		cameraModel.resize(width, height);
		
	}

	@Override
	public void show() {
		if (logger.getLevel() >= Logger.DEBUG) logger.debug("-->show()");
		
		Gdx.input.setInputProcessor(multiplexer);		
			
	}

	@Override
	public void hide() {
		if (logger.getLevel() >= Logger.INFO) logger.info("-->hide()");
		
	}

	@Override
	public void pause() {
		if (logger.getLevel() >= Logger.DEBUG) logger.debug("-->pause()");
		
	}

	@Override
	public void resume() {
		if (logger.getLevel() >= Logger.DEBUG) logger.debug("-->resume()");
		
	}

	@Override
	public void dispose() {
		worldModel.dispose();
		ui.dispose();
		grid.dispose();
		
		GameTickGenerator.getInstance().dispose();
		ScreenTickManager.getInstance().dispose();
		JsBridgeSingleton.getInstance(worldModel).dispose();


		Gdx.input.setInputProcessor(new InputMultiplexer());

		FlashMessageManagerModel.getInstance().dispose();

//		cameraModel.dispose();
//		cameraModel = null;
//		
//		Gdx.input.setInputProcessor(null);
//		myGestureListener = null;
//		myInputProcessor = null;
//		worldModel = null;
	}

	@Override
	protected void finalize() throws Throwable {
		logger.info("GC'ed:"+devID);
		super.finalize();
	}
	
	
}
