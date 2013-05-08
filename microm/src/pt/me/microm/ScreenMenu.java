package pt.me.microm;

import java.util.UUID;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.ICommand;
import pt.me.microm.session.PlayerProgress;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.SnapshotArray;

public class ScreenMenu implements Screen {
	
	private static final String TAG = ScreenMenu.class.getSimpleName();
	private static Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private Stage stage;
	private Table table;
	
	private PlayerProgress playerProgress;
	private ICommand callback;
	
	private UUID devID;
	private ScreenMenu(PlayerProgress playerProgress, ICommand callback) {
		logger.info("ALLOC:" + (devID = UUID.randomUUID()).toString());
		
		this.playerProgress = playerProgress;
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
		table.bottom();
		stage.addActor(table);
		// Add widgets to the table here
		
		TextureAtlas t = new TextureAtlas(Gdx.files.internal("data/scene2d/uiskin.atlas"), Gdx.files.internal("data/scene2d/"));
		Skin skin = new Skin(Gdx.files.internal("data/scene2d/uiskin.json"), t);
		
		Actor a;
		stage.addActor(a = new CheckBox("Ué ?", skin));
		a.setPosition(100.0f, 0.0f);
		final Actor b;
		stage.addActor(b = new Label("#: "+playerProgress.getScreenFlowService().getWorldByName("world.1.justforkicks").getCurrentDeathCount(), skin));
		b.addAction(new Action() {
			@Override
			public boolean act(float delta) {
				((Label)b).setText("#: "+ScreenMenu.this.playerProgress.getScreenFlowService().getWorldByName("world.1.justforkicks").getCurrentDeathCount());
				return false;
			}
		});
		
		table.row().height(50.0f);
			a = new TextButton("Story Mode",skin);
			table.add(a).align(Align.left | Align.top).fill();
			a.addListener(new EventListener() {
				@Override
				public boolean handle(Event event) {
					//Gdx.app.log(TAG, event.getClass().getSimpleName() + " >> " + event.toString());
					if (event instanceof ChangeEvent) {
						ScreenMenu.this.playerProgress.getScreenFlowService().getSessionState().gameMode = 0;
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
			
			a = new TextButton("Training Mode",skin);
			table.add(a).align(Align.right | Align.top).fill();
			a.addListener(new EventListener() {
				@Override
				public boolean handle(Event event) {
					//Gdx.app.log(TAG, event.getClass().getSimpleName() + " >> " + event.toString());
					if (event instanceof ChangeEvent) {
						ScreenMenu.this.playerProgress.getScreenFlowService().getSessionState().gameMode = 1;
						ScreenMenu.this.callback.handler(null);
					}
					return false;
				}
			});
			
			
		table.row().uniform();
			table.add(a = new com.badlogic.gdx.scenes.scene2d.ui.Label("_more:", skin)).align(Align.left | Align.bottom).spaceTop(10.0f);
			a.addListener(new EventListener() {
				@Override
				public boolean handle(Event event) {
					//logger.info("b pressed!");
					return false;
				}
			});
			
		table.row().uniform();
			table.add(a = new TextButton("Go full!",skin)).align(Align.left | Align.top).width(100).height(20.0f).fill();
			a.addListener(new EventListener() {
				@Override
				public boolean handle(Event event) {
					//logger.info("b pressed!");
					return false;
				}
			});
			table.add(a = new TextButton("+ games!",skin)).align(Align.right | Align.top).width(100).height(20.0f).fill();
			a.addListener(new EventListener() {
				@Override
				public boolean handle(Event event) {
					//logger.info("b pressed!");
					return false;
				}
			});		
		
		table.row().uniform();
			a = new TextButton("Rate & Share", skin);
			table.add(a).colspan(2).align(Align.center | Align.top).fill(false).height(20f).pad(5.0f);
			a.addListener(new EventListener() {
				@Override
				public boolean handle(Event event) {
					//logger.info("b pressed!");
					return false;
				}
			});
		
	}

	public static Screen showMenu(PlayerProgress playerProgress, ICommand callback) {
		logger.info("showMenu start!");
		return new ScreenMenu(playerProgress, callback);
	}
	
	
	
	@Override
	public void render(float delta) {

		if (Gdx.input.isKeyPressed(Keys.BACKSPACE) || Gdx.input.isKeyPressed(Keys.BACK)) // use your own criterion here
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
//		stage.setViewport(width, height, true);
		stage.setViewport(800/2, 480/2, true);// fixando o viewport permite que se fique com um sistema de coordenadas independente da resolução
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
