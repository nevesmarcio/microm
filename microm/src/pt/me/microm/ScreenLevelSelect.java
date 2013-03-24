package pt.me.microm;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.view.accessor.SpriteAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
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
import com.badlogic.gdx.utils.Logger;

public class ScreenLevelSelect extends ScreenAbstract {
	
	private static final String TAG = ScreenLevelSelect.class.getSimpleName();
	private static Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private Stage stage;
	
	private SpriteBatch batch;
	private TextureAtlas atlas;
	private Sprite grassSprite;
	
	private TweenManager tweenManager = new TweenManager();	
	
	
	
	public ScreenLevelSelect(Game g) {
		super(g);
		
		stage = new Stage();
//		InputMultiplexer im = (InputMultiplexer) Gdx.input.getInputProcessor();
//		if (im == null) im = new InputMultiplexer();
//		im.addProcessor(stage);
//		Gdx.input.setInputProcessor(im);
		
		Table table = new Table();
		table.debug();
		table.setFillParent(true);
		stage.addActor(table);
		// Add widgets to the table here
		
		TextureAtlas t = new TextureAtlas(Gdx.files.internal("data/scene2d/uiskin.atlas"), Gdx.files.internal("data/scene2d/"));
		Skin skin = new Skin(Gdx.files.internal("data/scene2d/uiskin.json"), t);
		
		
		Actor a;
		table.add(a = new TextButton("levelSelect-btn",skin));
		
		a.addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
//				Gdx.app.log(TAG, event.getClass().getSimpleName() + " >> " + event.toString());
				if (event instanceof ChangeEvent)
					 ScreenLevelSelect.this.g.setScreen(((GameMicroM)ScreenLevelSelect.this.g).theJuice);
				return false;
			}
		});
		
		table.add(a = new TextButton("levelSelect-dummy1",skin));
		table.add(a = new TextButton("levelSelect-dummy2",skin));
		table.add(a = new TextButton("levelSelect-dummy3",skin));
		
		
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
	
	
	
	@Override
	public void render(float delta) {

		
		// Clean do gl context
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0.3f, 0.15f, 0.10f, 0.8f); // brown
		
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
		stage.dispose();
		
	}

}
