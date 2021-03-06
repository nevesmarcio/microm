package pt.me.microm;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.ICommand;
import pt.me.microm.session.MyLevel;
import pt.me.microm.session.PlayerProgress;
import pt.me.microm.view.accessor.SpriteAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

public class ScreenLevelSelect implements Screen {
	
	private static final String TAG = ScreenLevelSelect.class.getSimpleName();
	private static Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private Stage stage;
	private Table table;
	
	private SpriteBatch batch;
	private TextureAtlas atlas;
	private Sprite grassSprite;
	
	private TweenManager tweenManager = new TweenManager();	
	
	private ICommand callback;
	
	private UUID devID;
	private ScreenLevelSelect(ICommand callback, String world, PlayerProgress playerProgress) {
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
		
		int i = 0;
		for (final MyLevel aLevel : playerProgress.getScreenFlowService().getWorldByName(world).getLevels()) { 
			if (i%3 == 0)
				table.row();
			table.add(a = new TextButton(aLevel.getName(), skin));

			a.addListener(new EventListener() {
				@Override
				public boolean handle(Event event) {
					if (event instanceof ChangeEvent) {
						logger.info("[**] Selected level: " + aLevel.getName());
						ScreenLevelSelect.this.callback.handler(aLevel.getName());
					}
					return false;
				}
			});
			i+=1;
		}
		

		
		///////////////////////////////////////
		batch = new SpriteBatch();
		atlas = new TextureAtlas(Gdx.files.internal("data/grass/microm.pack"));
		
		grassSprite = atlas.createSprite("grass2");
		grassSprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear); // bad fps //linear is best
		
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		Tween.call(windCallback).delay(1.0f).start(tweenManager);		
		
	}
	
	private final TweenCallback windCallback = new TweenCallback() {
		@Override
		public void onEvent(int type, BaseTween<?> source) {
			float d = 1.0f;//MathUtils.random() * 0.5f + 0.5f; // duração de 0.5s a 1s
			float t = -0.25f * grassSprite.getHeight();
			
			Tween.to(grassSprite, SpriteAccessor.SKEW_X2X3, d)
				.target(t, t)
				.ease(aurelienribon.tweenengine.equations.Linear.INOUT)
				.repeatYoyo(1, 0)
				.setCallback(windCallback)
				.start(tweenManager);
		}
	};	
	
	public static Screen selectALevel(ICommand callback, String world, PlayerProgress playerProgress) {
		logger.info("selectALevel start!");
		return new ScreenLevelSelect(callback, world, playerProgress);
	}
	
	
	@Override
	public void render(float delta) {

		if (Gdx.input.isKeyPressed(Keys.BACKSPACE) || Gdx.input.isKeyPressed(Keys.BACK)) // use your own criterion here
			callback.handler("back");		
		
		// Clean do gl context
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0.15f, 0.075f, 0.05f, 0.4f); // brown
		
        Table.drawDebug(stage); // This is optional, but enables debug lines for tables.
        
        stage.act(delta);
        stage.draw();
        
        /////////////////////////////////
        tweenManager.update(Gdx.graphics.getDeltaTime());
		
        //batch.setProjectionMatrix(camera.combined);
		batch.begin();

			grassSprite.setPosition(0, 0); //grassSprite.getWidth()/2, grassSprite.getHeight()/2	
			grassSprite.draw(batch);

		batch.end();
        
        
        
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
		Tween.registerAccessor(Sprite.class, null);
		
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
