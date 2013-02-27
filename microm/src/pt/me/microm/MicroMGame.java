package pt.me.microm;

import pt.me.microm.controller.MyGestureListener;
import pt.me.microm.controller.MyInputProcessor;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.GameTickGenerator;
import pt.me.microm.infrastructure.ScreenTickManager;
import pt.me.microm.model.base.CameraModel;
import pt.me.microm.model.base.WorldModel;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.RemoteInput;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Logger;

public class MicroMGame implements ApplicationListener {
	// FLAGS
	public static final boolean FLAG_DEV_ELEMENTS = false; 				// "pre-compiler" equivalent for branching development-only code
	public static final boolean FLAG_DISPLAY_ACTOR_SHAPES = false;		// mostra o desenho das shapes dos actores: walls, dabox, etc.
	public static final boolean FLAG_DISPLAY_ACTOR_TEXTURES = true;		// liga a texturização dos actores
	public static final boolean FLAG_DISPLAY_PARTICLES = false;			// liga o desenho de particulas
	
	private static final String TAG = MicroMGame.class.getSimpleName();
	private static Logger logger = new Logger(TAG);
	
	// CONTROLLER RELATED
	private MyGestureListener myGestureListener;
	private MyInputProcessor myInputProcessor;

	// MODEL RELATED
	private WorldModel worldModel;
	private CameraModel cameraModel;
	
	// VIEW RELATED
	// Todas as views são instanciadas por "reflection"


	@Override
	public void create() {		
		Texture.setEnforcePotImages(true); // ver o melhor sitio para enfiar isto, dado que as texturas estão nas constantes.

		// MODELS ///////////////////////////////////////////////////////////////
		cameraModel = new CameraModel();
		worldModel = WorldModel.getSingletonInstance();
		
		// VIEWS  ///////////////////////////////////////////////////////////////
		// Todas as views são instanciadas por "reflection"

		// CONTROLLERS - The GLUE ///////////////////////////////////////////////////////////////
		// Lança o controller dos ticks temporais : x second tick
		GameTickGenerator.getInstance(); //responsável pela actualizacao dos modelos
		ScreenTickManager.getInstance(); //responsável pela actualizacao das views

//		//FIXME: for development purposes only
//		RemoteInput receiver = new RemoteInput(7777);
//		Gdx.input = receiver;
		
		
		// Cria o controller dos gestos e regista-o --> este pode actuar quer ao nivel do modelo quer ao nivel da view
		myGestureListener = new MyGestureListener(cameraModel, worldModel);
		myInputProcessor = new MyInputProcessor(cameraModel, worldModel);

		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(new GestureDetector(1, 1.0f, 1.0f, 1.0f, myGestureListener));
		multiplexer.addProcessor(myInputProcessor);
		Gdx.input.setInputProcessor(multiplexer);		
		//Gdx.input.setCatchBackKey(true);
		//Gdx.input.setCatchMenuKey(true);
		
		

		
	}
	
	@Override
	public void dispose() {
		//## ASSETS UNLOAD
		
		GAME_CONSTANTS.DisposeAllObjects();
		
		GameTickGenerator.getInstance().dispose();
		ScreenTickManager.getInstance().dispose();
		
	}
	
	
	@Override
	// the main loop - maximum fps possible (Update rate para a View)
	public void render() {
		long elapsedNanoTime = (long)(Gdx.graphics.getDeltaTime()*GAME_CONSTANTS.ONE_SECOND_TO_NANO);
	
		// Clean do gl context
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1); // cinza
		
		ScreenTickManager.getInstance().fireEvent(cameraModel, elapsedNanoTime);
		
	}
	

	@Override
	public void resize(int width, int height) {
		cameraModel.Resize();
		
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}


}
