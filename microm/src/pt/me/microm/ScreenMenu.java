package pt.me.microm;

import java.util.UUID;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.ICommand;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.Screen;

public class ScreenMenu implements Screen {
	
	private static final String TAG = ScreenMenu.class.getSimpleName();
	private static Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private Stage stage;
	private Table table;
	
	private ICommand callback;
	
	private UUID devID;
	private ScreenMenu(ICommand callback) {
		logger.info("ALLOC:" + (devID = UUID.randomUUID()).toString());
		
		this.callback = callback;
		
		stage = new Stage();
//		InputMultiplexer im = (InputMultiplexer) Gdx.input.getInputProcessor();
//		if (im == null) im = new InputMultiplexer();
//		im.addProcessor(stage);
//		Gdx.input.setInputProcessor(im);
		
		table = new Table();
		table.debug();
		table.setFillParent(true);
		stage.addActor(table);
		// Add widgets to the table here
		
		TextureAtlas t = new TextureAtlas(Gdx.files.internal("data/scene2d/uiskin.atlas"), Gdx.files.internal("data/scene2d/"));
		Skin skin = new Skin(Gdx.files.internal("data/scene2d/uiskin.json"), t);
		
		stage.addActor(new CheckBox("Ué ?", skin));
		
		Actor a;
		table.add(a = new TextButton("norow-btn",skin));
		a.addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				//Gdx.app.log(TAG, event.getClass().getSimpleName() + " >> " + event.toString());
				if (event instanceof ChangeEvent) {
					ScreenMenu.this.callback.handler(null);
				}
				return false;
			}
		});		
		a.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				//Gdx.app.log(TAG, event.toString());
				//Gdx.app.log(TAG, actor.toString());
			}
		});
		
		table.row();
			table.add(a = new TextButton("row2-btn",skin));
			a.addListener(new EventListener() {
				
				@Override
				public boolean handle(Event event) {
					//logger.info("b pressed!");
					return false;
				}
			});
			table.add(a = new TextButton("row2-btn",skin));
			a.addListener(new EventListener() {
				
				@Override
				public boolean handle(Event event) {
					//logger.info("b pressed!");
					return false;
				}
			});		
		
		table.row();
			table.add(a = new com.badlogic.gdx.scenes.scene2d.ui.Label("row3-lbl", skin));
			a.addListener(new EventListener() {
				
				@Override
				public boolean handle(Event event) {
					//logger.info("b pressed!");
					return false;
				}
			});
		
	}

	public static Screen showMenu(ICommand callback) {
		logger.info("showMenu start!");
		return new ScreenMenu(callback);
	}
	
	
	
	@Override
	public void render(float delta) {

		if (Gdx.input.isKeyPressed(Keys.BACKSPACE)) // use your own criterion here
			callback.handler("back");
		
		// Clean do gl context
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0.3f, 0.15f, 0.10f, 0.8f); // brown
		
        Table.drawDebug(stage); // This is optional, but enables debug lines for tables.
        
        stage.act(delta);
        stage.draw();
        
        
        
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, true);
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
