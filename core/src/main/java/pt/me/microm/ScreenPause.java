package pt.me.microm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
import com.badlogic.gdx.utils.SnapshotArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.me.microm.infrastructure.ICommand;

import java.util.UUID;

public class ScreenPause implements Screen {
	
	private static final String TAG = ScreenPause.class.getSimpleName();
	private static final Logger logger = LoggerFactory.getLogger(TAG);
	
	private Stage stage;
	private Table table;
	
	private ICommand callback;
	
	private UUID devID;
	private ScreenPause(ICommand callback) {
		logger.info("ALLOC:" + (devID = UUID.randomUUID()).toString());
		
		this.callback = callback;

		stage = new Stage();
//		InputMultiplexer im = (InputMultiplexer) Gdx.input.getInputProcessor();
//		if (im == null) im = new InputMultiplexer();
//		im.addProcessor(stage);
//		Gdx.input.setInputProcessor(im);
		
		table = new Table();
		if (GameMicroM.FLAG_DISPLAY_SCENE2D_DEBUG_LINES)
			table.debug();
		table.setFillParent(true);
		stage.addActor(table);
		// Add widgets to the table here
		
		TextureAtlas t = new TextureAtlas(Gdx.files.internal("data/scene2d/skin/uiskin.atlas"), Gdx.files.internal("data/scene2d/skin/"));
		Skin skin = new Skin(Gdx.files.internal("data/scene2d/skin/uiskin.json"), t);
		
		
		Actor a;
		table.add(a = new TextButton("return_to_game",skin));
		
		a.addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				if (event instanceof ChangeEvent) {
					ScreenPause.this.callback.handler("return_to_game");
				}
				return false;
			}
		});
		
		
		table.row();
		
		table.add(a = new TextButton("goto_menu", skin));
		a.addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				if (event instanceof ChangeEvent) {
					ScreenPause.this.callback.handler("goto_menu");
				}
				return false;
			}
		});
		
	}

	public static Screen pauseGame(ICommand callback) {
		logger.info("pause start!");
		return new ScreenPause(callback);
	}


    private static final String clear_color = "0666020F";
    private static final float r = Color.valueOf(clear_color).r;
    private static final float g = Color.valueOf(clear_color).g;
    private static final float b = Color.valueOf(clear_color).b;
    private static final float a = Color.valueOf(clear_color).a;
	@Override
	public void render(float delta) {

//        // Clean do gl context
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(r, g, b, a);
        
        stage.act(delta);
        stage.draw();
        
        
        
	}

	@Override
	public void resize(int width, int height) {
		//stage.setViewport(width, height, true);
//		stage.setViewport(new ExtendViewport(800/2, 480/2));
	}

	@Override
	public void show() {
		logger.debug("-->show()");
		
		InputMultiplexer im = new InputMultiplexer();
		im.addProcessor(stage);
		Gdx.input.setInputProcessor(im);
		
	}

	@Override
	public void hide() {
		logger.debug("-->hide()");
		this.dispose();
		
	}

	@Override
	public void pause() {
		logger.debug("-->pause()");
		
	}

	@Override
	public void resume() {
		logger.debug("-->resume()");
		
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
