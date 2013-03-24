package pt.me.microm;

import java.util.Arrays;

import pt.me.microm.infrastructure.GAME_CONSTANTS;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Logger;

public class ScreenSplash extends ScreenAbstract {
	
	private static final String TAG = ScreenSplash.class.getSimpleName();
	private static Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private Stage stage;

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
	
	
	public ScreenSplash(Game g) {
		super(g);
		
		stage = new Stage();
		
		Table table = new Table();
		table.debug();
		table.setFillParent(true);
		stage.addActor(table);
		// Add widgets to the table here
		
		TextureAtlas t = new TextureAtlas(Gdx.files.internal("data/scene2d/uiskin.atlas"), Gdx.files.internal("data/scene2d/"));
		Skin skin = new Skin(Gdx.files.internal("data/scene2d/uiskin.json"), t);
		
		Actor a;
		table.add(a = new Label("loading...",skin));
		
		
		
		/////////////////////////////////////////////////////////
		walkSheet = new Texture(
				Gdx.files.internal("data/spritesheets/DaBoxRunning.png")); // #9
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

	
	@Override
	public void render(float delta) {

        // use your own criterion here
    	if (Gdx.input.isKeyPressed(Keys.ENTER) || Gdx.input.isTouched())
            g.setScreen(((GameMicroM)g).menu);		
		
		// Clean do gl context
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0.5f, 0.05f, 0.50f, 0.8f); // brown
		
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
		stage.setViewport(width, height, true);
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
		stage.dispose();
		
	}

}
