package pt.me.microm;

import java.util.UUID;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.ICommand;
import pt.me.microm.session.PlayerProgress;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.utils.Logger;

public class ScreenDummy implements Screen {
	
	private static final String TAG = ScreenDummy.class.getSimpleName();
	private static Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private PlayerProgress playerProgress;
	private ICommand callback;
	
	
	private UUID devID;
	private ScreenDummy(PlayerProgress playerProgress, ICommand callback) {
		logger.info("showDummy start!");
		logger.info("ALLOC:" + (devID = UUID.randomUUID()).toString());
		
		this.playerProgress = playerProgress;
		this.callback = callback;
	}

	public static Screen showDummy(PlayerProgress playerProgress, ICommand callback) {
		return new ScreenDummy(playerProgress, callback);
	}
	
	
	private String clear_color = "1606120F";
	@Override
	public void render(float delta) {
		long elapsedNanoTime = (long)(Gdx.graphics.getDeltaTime()*GAME_CONSTANTS.ONE_SECOND_TO_NANO);

		// Clean do gl context
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(Color.valueOf(clear_color).r, Color.valueOf(clear_color).g, Color.valueOf(clear_color).b, Color.valueOf(clear_color).a);
		
		
		if (Gdx.input.isKeyPressed(Keys.BACKSPACE) || Gdx.input.isKeyPressed(Keys.BACK)) // use your own criterion here
			callback.handler("back", ScreenDummy.this);
		
		

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		if (logger.getLevel() == Logger.DEBUG) logger.debug("-->show()");
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
		logger.info("showDummy disposed!");
	}
	
	@Override
	protected void finalize() throws Throwable {
		logger.info("GC'ed:"+devID);
		super.finalize();
	}	

}
