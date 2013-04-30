package pt.me.microm;

import java.util.Arrays;
import java.util.UUID;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.ICommand;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.SnapshotArray;

public class ScreenSplash implements Screen {
	
	private static final String TAG = ScreenSplash.class.getSimpleName();
	private static Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private Stage stage;
	private Table table;

	//////////////////////////////////////////////////////////////
	
	private static final int FRAME_COLS = 3; // #1
	private static final int FRAME_ROWS = 13; // #2
	private static final int FRAME_WIDTH = 153;
	private static final int FRAME_HEIGHT = 154;

	Animation walkAnimation; // #3
	Texture walkSheet; // #4
	TextureRegion[] walkFrames; // #5
	SpriteBatch spriteBatch; // #6
	TextureRegion currentFrame; // #7

	float stateTime; // #8	
	
	private ICommand callback;
	
	private UUID devID;
	private ScreenSplash(ICommand callback) {
		logger.info("ALLOC:" + (devID = UUID.randomUUID()).toString());
		
		this.callback = callback;
		
		stage = new Stage();
		stage.addCaptureListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				if (event instanceof InputEvent) {
					if (logger.getLevel() >= Logger.DEBUG)
						logger.debug(">>> " + ((InputEvent) event).getStageX() + ":" +  ((InputEvent) event).getStageY()); 
				}
				return false;
			}
		});
		
		table = new Table();
		table.debug();
		table.setFillParent(true);
		stage.addActor(table);
		// Add widgets to the table here
		
		TextureAtlas t = new TextureAtlas(Gdx.files.internal("data/scene2d/uiskin.atlas"), Gdx.files.internal("data/scene2d/"));
		Skin skin = new Skin(Gdx.files.internal("data/scene2d/uiskin.json"), t);
		
		Actor a;
		table.add(a = new Label("loading...",skin));
		
		
		
		/////////////////////////////////////////////////////////
		walkSheet = new Texture(Gdx.files.internal("data/spritesheets/DaBoxRunning.png")); // #9
//		TextureRegion[][] tmp = TextureRegion.split(walkSheet,
//				walkSheet.getWidth() / FRAME_COLS, walkSheet.getHeight()
//						/ FRAME_ROWS); // #10
		TextureRegion[][] tmp = TextureRegion.split(walkSheet, FRAME_WIDTH, FRAME_HEIGHT); // #10		
		walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		int index = 0;
		for (int i = 0; i < FRAME_ROWS; i++) {
			for (int j = 0; j < FRAME_COLS; j++) {
				walkFrames[index++] = tmp[i][j];
			}
		}
		walkFrames = Arrays.copyOfRange(walkFrames, 0, walkFrames.length-1);
		
		walkAnimation = new Animation(0.025f, walkFrames); // #11
		spriteBatch = new SpriteBatch(); // #12
		stateTime = 0f;
		
	}
	
	public static Screen doHeavyLoading(ICommand callback) {
		logger.info("doHeavyLoading start!");
		return new ScreenSplash(callback);
	}

	
	@Override
	public void render(float delta) {

        // use your own criterion here
    	if (Gdx.input.isKeyPressed(Keys.ENTER) || Gdx.input.isTouched()) {
    		callback.handler(null);
            //g.setScreen(((GameMicroM)g).getMenu());
    	}
    		
		// Clean do gl context
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0.5f, 0.05f, 0.50f, 0.8f); // brown
		
		if (GameMicroM.FLAG_DEV_ELEMENTS_B)
			Table.drawDebug(stage); // This is optional, but enables debug lines for tables.
        
        stage.act(delta);
        stage.draw();
        
        ////////////////////////////////////////
        stateTime += Gdx.graphics.getDeltaTime();                       // #15
        currentFrame = walkAnimation.getKeyFrame(stateTime, true);      // #16
        spriteBatch.begin();
        spriteBatch.draw(currentFrame, 50, 50);                         // #17
        spriteBatch.end();
        
	}

	@Override
	public void resize(int width, int height) {
		//stage.setViewport(width, height, true);
		stage.setViewport(100, 100, true);// fixando o viewport permite que se fique com um sistema de coordenadas independente da resolução
	}

	@Override
	public void show() {
		if (logger.getLevel() >= Logger.DEBUG) logger.debug("-->show()");

		InputMultiplexer im = new InputMultiplexer();
		im.addProcessor(stage);
		Gdx.input.setInputProcessor(im);
		
	}

	@Override
	public void hide() {
		if (logger.getLevel() >= Logger.DEBUG) logger.debug("-->hide()");
		this.dispose();
		
	}

	@Override
	public void pause() {
		if (logger.getLevel() >= Logger.DEBUG) logger.debug("-->pause()");
		
	}

	@Override
	public void resume() {
		if (logger.getLevel() >= Logger.DEBUG) logger.debug("-->resume()");
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
