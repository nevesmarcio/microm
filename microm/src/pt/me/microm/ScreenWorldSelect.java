package pt.me.microm;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.ICommand;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
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
	private ScreenWorldSelect(ICommand callback) {
		logger.info("ALLOC:" + (devID = UUID.randomUUID()).toString());
		
		this.callback = callback;
		
//		boolean isExtAvailable = Gdx.files.isExternalStorageAvailable();
//		boolean isLocAvailable = Gdx.files.isLocalStorageAvailable();
//		
//		String extRoot = Gdx.files.getExternalStoragePath();
//		String locRoot = Gdx.files.getLocalStoragePath();
//		if ((locRoot != null) && locRoot.isEmpty()) locRoot = ".";
//		
//		logger.info("isExtAvailable: " + isExtAvailable + " ::" + extRoot);
//		logger.info("FP: " + Gdx.files.local(extRoot).file().getAbsolutePath());
//		FileHandle[] filesA = Gdx.files.local(extRoot).list();
//		for(FileHandle file: filesA) {
//		   logger.info("\t" + file.name() + "|" + file.length());
//		}		
//		
//		logger.info("isLocAvailable: " + isLocAvailable + " ::" + locRoot);
//		logger.info("FP: " + Gdx.files.local(locRoot).file().getAbsolutePath());
//		FileHandle[] filesB = Gdx.files.local(locRoot).list();
//		for(FileHandle file: filesB) {
//			logger.info("\t" + file.name() + "|" + file.length());
//		}				
		
		logger.debug("FP: " + Gdx.files.internal("data/levels").file().getAbsolutePath());
		FileHandle[] filesC = Gdx.files.internal("data/levels").list();
		
		Pattern pattern = Pattern.compile("world\\.\\d\\.\\w+");
		Matcher matcher;
		List<String> discoveredWorlds = new ArrayList<String>();
		for (FileHandle file : filesC) {
			logger.debug("\t" + file.name() + "|" + (file.isDirectory()?"D":file.length()));
			matcher = pattern.matcher(file.name());
			// Check all occurance
			while (matcher.find()) {
				discoveredWorlds.add(matcher.group());
			}
		}			

		
		
		
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
		
		
		for (final String aWorld : discoveredWorlds) {
			table.add(a = new TextButton(aWorld, skin));
			
			a.addListener(new EventListener() {
				@Override
				public boolean handle(Event event) {
					if (event instanceof ChangeEvent) {
						 logger.info("[**] Selected world: " + aWorld);
						 ScreenWorldSelect.this.callback.handler(aWorld);
					}
					return false;
				}				
			});			
			
		}
		
	}

	public static Screen selectAWorld(ICommand callback) {
		logger.info("selectAWorld start!");
		return new ScreenWorldSelect(callback);
	}	
	
	
	
	@Override
	public void render(float delta) {

        if (Gdx.input.isKeyPressed(Keys.BACKSPACE)) // use your own criterion here
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
