package pt.me.microm;

import java.util.UUID;

import pt.me.microm.api.JsBridgeSingleton;
import pt.me.microm.controller.MyGestureListener;
import pt.me.microm.controller.MyInputProcessor;
import pt.me.microm.controller.loop.GameTickGenerator;
import pt.me.microm.controller.loop.ScreenTickManager;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.ICommand;
import pt.me.microm.model.MyContactListener;
import pt.me.microm.model.base.CameraModel;
import pt.me.microm.model.base.WorldModel;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.utils.Logger;

public class ScreenTheJuice implements Screen {

	private static final String TAG = ScreenTheJuice.class.getSimpleName();
	private static Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	// CONTROLLER RELATED
	private MyGestureListener myGestureListener;
	private MyInputProcessor myInputProcessor;

	// MODEL RELATED
	private WorldModel worldModel;
	private CameraModel cameraModel;
	
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
		worldModel = new WorldModel(world, level);
		
		// VIEWS  ///////////////////////////////////////////////////////////////
		// Todas as views são instanciadas por "reflection"

		// CONTROLLERS - The GLUE ///////////////////////////////////////////////////////////////
		// Lança o controller dos ticks temporais : x second tick
		GameTickGenerator.getInstance(); //responsável pela actualizacao dos modelos
		ScreenTickManager.getInstance(); //responsável pela actualizacao das views
		
		//responsável pela extensibilidade do controller: delega o controlo a entidades externas (javascript + terminal)
		JsBridgeSingleton cs = JsBridgeSingleton.getInstance();
		worldModel.myContactListener.addListener(MyContactListener.EventType.ON_COLLISION_BEGIN, cs);
		worldModel.myContactListener.addListener(MyContactListener.EventType.ON_COLLISION_END, cs);

		
//		//FIXME: for development purposes only
//		RemoteInput receiver = new RemoteInput(7777);
//		Gdx.input = receiver;
		
		
		// Cria o controller dos gestos e regista-o --> este pode actuar quer ao nivel do modelo quer ao nivel da view
		myGestureListener = new MyGestureListener(cameraModel, worldModel);
		myInputProcessor = new MyInputProcessor(cameraModel, worldModel);

	}
	
	public static Screen playground(ICommand callback, String world, String level) {
		logger.info("playground start!");
		return new ScreenTheJuice(callback, world, level);
	}


	@Override
	// the main loop - maximum fps possible (Update rate para a View)
	public void render(float delta) {
		long elapsedNanoTime = (long)(Gdx.graphics.getDeltaTime()*GAME_CONSTANTS.ONE_SECOND_TO_NANO);
		
        // use your own criterion here
    	if (Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK)) {
    		callback.handler("exit");
    	}
    	
    	if (Gdx.input.isKeyPressed(Keys.PLUS)) {
    		callback.handler("pause", this);
    	}
		
		// Clean do gl context
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1); // cinza escuro
		
		ScreenTickManager.getInstance().fireEvent(cameraModel, elapsedNanoTime);
	}

	@Override
	public void resize(int width, int height) {
		cameraModel.Resize();
		
	}

	@Override
	public void show() {
		if (logger.getLevel() == Logger.DEBUG) logger.debug("-->show()");
		
		InputMultiplexer multiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
		if (multiplexer == null) multiplexer = new InputMultiplexer();
		//InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(new GestureDetector(1, 1.0f, 1.0f, 1.0f, myGestureListener));
		multiplexer.addProcessor(myInputProcessor);
		Gdx.input.setInputProcessor(multiplexer);		
		
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setCatchMenuKey(true);		
	}

	@Override
	public void hide() {
		if (logger.getLevel() == Logger.INFO) logger.info("-->hide()");
		
	}

	@Override
	public void pause() {
		if (logger.getLevel() == Logger.DEBUG) logger.debug("-->pause()");
		
	}

	@Override
	public void resume() {
		if (logger.getLevel() == Logger.DEBUG) logger.debug("-->resume()");
		
	}

	@Override
	public void dispose() {
		cameraModel.dispose();
		cameraModel = null;
		GameTickGenerator.getInstance().dispose();
		ScreenTickManager.getInstance().dispose();

//		JsBridgeSingleton.getInstance().dispose();		
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
