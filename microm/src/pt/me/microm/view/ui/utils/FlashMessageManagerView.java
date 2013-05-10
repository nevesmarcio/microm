package pt.me.microm.view.ui.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

import pt.me.microm.controller.loop.event.ScreenTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.model.ui.TextModel;
import pt.me.microm.model.ui.utils.FlashMessage;
import pt.me.microm.model.ui.utils.FlashMessageAccessor;
import pt.me.microm.model.ui.utils.FlashMessageManagerModel;
import pt.me.microm.view.AbstractView;
import aurelienribon.tweenengine.Tween;

public class FlashMessageManagerView extends AbstractView {

	private FlashMessageManagerModel fmmmSrc;
	
	private BitmapFont font;
	private SpriteBatch batch;
	
	public FlashMessageManagerView(FlashMessageManagerModel fmmmSrc) {
		super(fmmmSrc);
		this.fmmmSrc = fmmmSrc;
		
		// regista no tween manager o accessor para as FlashMessages
		Tween.registerAccessor(FlashMessage.class, new FlashMessageAccessor());
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
		
		for (FlashMessage fm : fmmmSrc.afm) {
			t.idt();
			t.translate(fm.position.x, fm.position.y, 0.0f);
			t.scl(fm.scale/GAME_CONSTANTS.DIPIXELS_PER_METER);
			
			batch.setTransformMatrix(t);
			
			batch.begin();
				font.draw(batch, fm.dataSource.get().toString(), 0.0f, 0.0f);
			batch.end();			
			
		}
		
	}

	@Override
	public void dispose() {
		Tween.registerAccessor(FlashMessage.class, null);
		super.dispose();
	}
	

	
}
