package pt.me.microm._package_by_feature_.screen;

import java.util.UUID;

import org.mozilla.javascript.ast.NewExpression;

import pt.me.microm.GameMicroM;
import pt.me.microm.controller.loop.GameTickGenerator;
import pt.me.microm.controller.loop.ScreenTickManager;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.ICommand;
import pt.me.microm.model.base.CameraControllerDrag;
import pt.me.microm.model.base.CameraModel;
import pt.me.microm.model.dev.GridModel;
import pt.me.microm.model.ui.UIMetricsModel;
import pt.me.microm.session.MyWorld;
import pt.me.microm.session.PlayerProgress;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.SnapshotArray;
import com.esotericsoftware.tablelayout.Cell;

public class WorldNavigatorController implements Screen {
	
	private static final String TAG = WorldNavigatorController.class.getSimpleName();
	private static Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private CameraModel cameraModel;
	private GridModel gridModel;
	private UIMetricsModel uiMetricsModel;
	
	private CameraControllerDrag camcontrollerDrag;
	
	private ICommand callback;

	
	private WorldNavigatorView.ViewListener viewListener = new WorldNavigatorView.ViewListener() {
		
		@Override
		public void previousWorld() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void nextWorld() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void levelClicked(String levelId) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void go(String aWorld) {
			 WorldNavigatorController.this.callback.handler(aWorld);
			
		}
	};
	private WorldNavigatorView view;
	private WorldNavigatorModel model;
	
	
	private UUID devID;
	private WorldNavigatorController(ICommand callback, PlayerProgress playerProgress) {
		logger.info("ALLOC:" + (devID = UUID.randomUUID()).toString());
		
		this.callback = callback;
		
		cameraModel = new CameraModel();
		gridModel = new GridModel();
		uiMetricsModel = new UIMetricsModel();
		
		GameTickGenerator.getInstance(); //responsável pela actualizacao dos modelos
		ScreenTickManager.getInstance(); //responsável pela actualizacao das views
		
		camcontrollerDrag = new CameraControllerDrag(cameraModel);
	
		model = new WorldNavigatorModel(playerProgress);
		view = new WorldNavigatorView(viewListener, model);
		
	}

	public static Screen selectAWorld(ICommand callback, PlayerProgress playerProgress) {
		logger.info("selectAWorld start!");
		return new WorldNavigatorController(callback, playerProgress);
	}	
	
	
	private String clear_color = "7f7f7fff";//"0606020F";
	@Override
	public void render(float delta) {
		long elapsedNanoTime = (long)(Gdx.graphics.getDeltaTime()*GAME_CONSTANTS.ONE_SECOND_TO_NANO);
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
				
		Gdx.gl.glClearColor(Color.valueOf(clear_color).r, Color.valueOf(clear_color).g, Color.valueOf(clear_color).b, Color.valueOf(clear_color).a);
		
		ScreenTickManager.getInstance().fireEvent(true, cameraModel, elapsedNanoTime);
		
        if (Gdx.input.isKeyPressed(Keys.BACKSPACE) || Gdx.input.isKeyPressed(Keys.BACK)) // use your own criterion here
        	callback.handler("back");
        
        view.render(delta);
        
	}

	@Override
	public void resize(int width, int height) {
		cameraModel.resize(width, height);
		
		view.resize(width, height);
	}

	@Override
	public void show() {
		if (logger.getLevel() == Logger.DEBUG) logger.debug("-->show()");
	
		InputMultiplexer im = new InputMultiplexer();
		
		im.addProcessor(view.stage);
		im.addProcessor(camcontrollerDrag);
		
		Gdx.input.setInputProcessor(im);
	}

	@Override
	public void hide() {
		if (logger.getLevel() == Logger.DEBUG) logger.debug("-->hide()");
    	this.dispose();
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
		GameTickGenerator.getInstance().dispose();
		ScreenTickManager.getInstance().dispose();
		view.dispose();
	}

	@Override
	protected void finalize() throws Throwable {
		logger.info("GC'ed:"+devID);
		super.finalize();
	}
	
}
