package pt.me.microm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.ICommand;
import pt.me.microm.session.PlayerProgress;

import java.util.UUID;


public class ScreenDummy implements Screen {

    private static final String TAG = ScreenDummy.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(TAG);

    private PlayerProgress playerProgress;
    private ICommand callback;


    private UUID devID;

    private ScreenDummy(PlayerProgress playerProgress, ICommand callback) {
        if (logger.isInfoEnabled())
            logger.info("showDummy start!");
        if (logger.isInfoEnabled())
            logger.info("ALLOC:" + (devID = UUID.randomUUID()).toString());

        this.playerProgress = playerProgress;
        this.callback = callback;
    }

    public static Screen showDummy(PlayerProgress playerProgress, ICommand callback) {
        return new ScreenDummy(playerProgress, callback);
    }


    private static final String clear_color = "F606120F";
    private static final float r = Color.valueOf(clear_color).r;
    private static final float g = Color.valueOf(clear_color).g;
    private static final float b = Color.valueOf(clear_color).b;
    private static final float a = Color.valueOf(clear_color).a;

    @Override
    public void render(float delta) {
        long elapsedNanoTime = (long) (Gdx.graphics.getDeltaTime() * GAME_CONSTANTS.ONE_SECOND_TO_NANO);

        // Clean do gl context
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(r, g, b, a);

        if (Gdx.input.isKeyPressed(Keys.BACKSPACE) || Gdx.input.isKeyPressed(Keys.BACK)) // use your own criterion here
            callback.handler("back", ScreenDummy.this);

    }

    @Override
    public void resize(int width, int height) {
        if (logger.isDebugEnabled())
            logger.debug("-->resize({},{})", new Object[]{width, height});
    }

    @Override
    public void show() {
        if (logger.isDebugEnabled())
            logger.debug("-->show()");
    }

    @Override
    public void hide() {
        if (logger.isDebugEnabled())
            logger.debug("-->hide()");
    }

    @Override
    public void pause() {
        if (logger.isDebugEnabled())
            logger.debug("-->pause()");
    }

    @Override
    public void resume() {
        if (logger.isDebugEnabled())
            logger.debug("-->resume()");
    }


    @Override
    public void dispose() {
        if (logger.isInfoEnabled())
            logger.info("showDummy disposed!");
    }

    @Override
    protected void finalize() throws Throwable {
        if (logger.isInfoEnabled())
            logger.info("GC'ed:" + devID);
        super.finalize();
    }

}
