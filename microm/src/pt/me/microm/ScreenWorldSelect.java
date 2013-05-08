package pt.me.microm;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.ICommand;
import pt.me.microm.session.MyWorld;
import pt.me.microm.session.PlayerProgress;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.SnapshotArray;

public class ScreenWorldSelect implements Screen {
	
	private static final String TAG = ScreenWorldSelect.class.getSimpleName();
	private static Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private Stage stage;
	private Table table;
	
	private ICommand callback;
	
	private UUID devID;
	private ScreenWorldSelect(ICommand callback, PlayerProgress playerProgress) {
		logger.info("ALLOC:" + (devID = UUID.randomUUID()).toString());
		
		this.callback = callback;
		
		stage = new Stage();
//		InputMultiplexer im = (InputMultiplexer) Gdx.input.getInputProcessor();
//		if (im == null) im = new InputMultiplexer();
//		im.addProcessor(stage);
//		Gdx.input.setInputProcessor(im);
		
		table = new Table();
		if (GameMicroM.FLAG_DEV_ELEMENTS_B)
			table.debug();
		table.setFillParent(true);
		stage.addActor(table);
		// Add widgets to the table here
		
		TextureAtlas t = new TextureAtlas(Gdx.files.internal("data/scene2d/uiskin.atlas"), Gdx.files.internal("data/scene2d/"));
		Skin skin = new Skin(Gdx.files.internal("data/scene2d/uiskin.json"), t);
		
		Actor a;
//		table.add(a = new TextButton("SPECIAL-btn",skin));
//		
//		a.addListener(new EventListener() {
//			@Override
//			public boolean handle(Event event) {
////				Gdx.app.log(TAG, event.getClass().getSimpleName() + " >> " + event.toString());
//				if (event instanceof ChangeEvent)
//					 ScreenWorldSelect.this.g.setScreen(((GameMicroM)ScreenWorldSelect.this.g).getLevelSelect());
//				return false;
//			}
//		});
		
		
		for (final MyWorld aWorld : playerProgress.getScreenFlowService().getAllWorlds()) {
			
			table.row();
			table.add(a = new TextButton(aWorld.getName(), skin));
			
			a.addListener(new EventListener() {
				@Override
				public boolean handle(Event event) {
					if (event instanceof ChangeEvent) {
						 logger.info("[**] Selected world: " + aWorld.getName());
						 ScreenWorldSelect.this.callback.handler(aWorld.getName());
					}
					return false;
				}				
			});			
			
		}
		
	}

	public static Screen selectAWorld(ICommand callback, PlayerProgress playerProgress) {
		logger.info("selectAWorld start!");
		return new ScreenWorldSelect(callback, playerProgress);
	}	
	
	
	
	@Override
	public void render(float delta) {

        if (Gdx.input.isKeyPressed(Keys.BACKSPACE) || Gdx.input.isKeyPressed(Keys.BACK)) // use your own criterion here
        	callback.handler("back");
        	
		// Clean do gl context
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0.27f, 0.135f, 0.09f, 0.72f); // brown
		
        Table.drawDebug(stage); // This is optional, but enables debug lines for tables.
        
        stage.act(delta);
        stage.draw();
        
        
        
	}

	@Override
	public void resize(int width, int height) {
		//stage.setViewport(width, height, true);
		stage.setViewport(800/2, 480/2, true);
	}

	@Override
	public void show() {
		if (logger.getLevel() == Logger.DEBUG) logger.debug("-->show()");
	
		InputMultiplexer im = new InputMultiplexer();
		im.addProcessor(stage);
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
		// clean actor listeners
		SnapshotArray<Actor> items = table.getChildren();
		for (int i = 0; i<items.size; i++) {
			Array<EventListener> el = items.get(i).getListeners();
			for (int j = 0; j<el.size; j++){
				items.get(i).removeListener(el.get(j));
			}
			Array<EventListener> ecl = items.get(i).getCaptureListeners();
			for (int j = 0; j<ecl.size; j++){
				items.get(i).removeCaptureListener(ecl.get(j));
			}
		}
		table.clear();
		stage.clear();
		stage.dispose();
		
	}

	@Override
	protected void finalize() throws Throwable {
		logger.info("GC'ed:"+devID);
		super.finalize();
	}
	
}
