package pt.me.microm.view.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.me.microm.controller.loop.event.ScreenTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.model.ui.TextModel;
import pt.me.microm.view.AbstractView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;


public class TextView extends AbstractView {
    private static final String TAG = TextView.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(TAG);

    private TextModel textmSrc;

    private BitmapFont font;
    private SpriteBatch batch;

    public TextView(TextModel textmSrc) {
        super(textmSrc, 100);
        this.textmSrc = textmSrc;
    }


    @Override
    public void DelayedInit() {
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("data/fonts/monospaced-56-bold.fnt"), false);

        font.setColor(Color.RED);
    }

    private Matrix4 t = new Matrix4();

    @Override
    public void draw(ScreenTickEvent e) {

        batch.setProjectionMatrix(e.getCamera().getGameCamera().combined);

        t.idt();
        t.translate(textmSrc.getBasicShape().getCentroid().x, textmSrc.getBasicShape().getCentroid().y, 0.0f);
        t.scl(1f / GAME_CONSTANTS.DIPIXELS_PER_METER);
        batch.setTransformMatrix(t);

        batch.begin();
        font.draw(batch, textmSrc.getContent(), 0.0f, 0.0f);
        batch.end();

    }

    @Override
    public void draw20(ScreenTickEvent e) {
        draw(e);
    }

}
