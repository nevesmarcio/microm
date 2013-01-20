package pt.me.microm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NameList;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import pt.me.microm.controller.MyGestureListener;
import pt.me.microm.controller.MyInputProcessor;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.GameTickGenerator;
import pt.me.microm.infrastructure.ScreenTickManager;
import pt.me.microm.model.base.CameraModel;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.tools.levelloader.LevelLoader;
import pt.me.microm.tools.levelloader.shape.BasicShape;
import pt.me.microm.view.base.WorldView;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.RemoteInput;
import com.badlogic.gdx.math.Vector3;

public class MicroMGame implements ApplicationListener {
	private static final String TAG = MicroMGame.class.getSimpleName();
	
	// CONTROLLER RELATED
	private MyGestureListener myGestureListener;
	private MyInputProcessor myInputProcessor;

	// MODEL RELATED
	private WorldModel worldModel;
	private CameraModel camModel;
	
	// VIEW RELATED
	// Todas as views são instanciadas por "reflection"


	@Override
	public void create() {		
		Texture.setEnforcePotImages(false); // ver o melhor sitio para enfiar isto, dado que as texturas estão nas constantes.
				

		// MODELS ///////////////////////////////////////////////////////////////
		camModel = new CameraModel();
		worldModel = WorldModel.getSingletonInstance();
		
		// VIEWS  ///////////////////////////////////////////////////////////////
		// Todas as views são instanciadas por "reflection"

		// CONTROLLERS - The GLUE ///////////////////////////////////////////////////////////////
		// Lança o controller dos ticks temporais : x second tick
		GameTickGenerator.getInstance(); //responsavel pela actualizacao dos modelos
		ScreenTickManager.getInstance(); //responsavel pela actualizacao das views

		//FIXME: for development purposes only
//		RemoteInput receiver = new RemoteInput(7777);
//		Gdx.input = receiver;
		
		// Cria o controller dos gestos e regista-o --> este pode actuar quer ao nivel do modelo quer ao nivel da view
		myGestureListener = new MyGestureListener(camModel, worldModel);
		myInputProcessor = new MyInputProcessor(camModel, worldModel);

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
		
		// FIXME:: depois tenho que rever isto -> a saída da aplicação n pode deixar recursos pendurados
		//System.exit(0);
	}
	
	
	@Override
	// the main loop - maximum fps possible (Update rate para a View)
	public void render() {
		long elapsedNanoTime = (long)(Gdx.graphics.getDeltaTime()*GAME_CONSTANTS.ONE_SECOND_TO_NANO);

		// Clean do gl context
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		ScreenTickManager.getInstance().fireEvent(camModel.getCamera(), elapsedNanoTime);		
	}
	

	@Override
	public void resize(int width, int height) {
		camModel.Resize();
		
	}

	@Override
	public void pause() {
		// FIXME: depois tenho que rever isto-> a saída da aplicação não pode deixar recursos pendurados
		//Gdx.app.exit(); 
	}

	@Override
	public void resume() {
	}


}
