package pt.me.microm.view.ui.utils;

import pt.me.microm.controller.loop.event.ScreenTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.model.ui.utils.FlashMessage;
import pt.me.microm.model.ui.utils.FlashMessageManagerModel;
import pt.me.microm.view.AbstractView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class FlashMessageManagerView extends AbstractView {

	private FlashMessageManagerModel fmmmSrc;
	
	private BitmapFont font;
	private SpriteBatch batch;
	
	public FlashMessageManagerView(FlashMessageManagerModel fmmmSrc) {
		super(fmmmSrc);
		this.fmmmSrc = fmmmSrc;
		}
	
	@Override
	public void DelayedInit() {
		
		batch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("data/fonts/monospaced-56-bold.fnt"), false);
		
		font.setColor(Color.RED);
		
	}
	
	private Matrix4 t = new Matrix4();
	private Vector3 vAux = new Vector3();
	@Override
	public void draw(ScreenTickEvent e) {
		
		Camera c1 = e.getCamera().getGameCamera();
		Camera c2 = e.getCamera().getUiCamera();
		
		for (FlashMessage fm : fmmmSrc.getAfm()) {
			
			if (fm.worldCoord) {
				batch.setProjectionMatrix(e.getCamera().getGameCamera().combined);
				
				t.idt();
				t.translate(fm.position.x, fm.position.y, 0.0f);
				t.scl(fm.scale/GAME_CONSTANTS.DIPIXELS_PER_METER);
				
				batch.setTransformMatrix(t);
				
				batch.begin();
					font.draw(batch, fm.dataSource.get().toString(), 0.0f, 0.0f);
				batch.end();
			
			} else {
				vAux.x = fm.position.x; vAux.y = fm.position.y; vAux.z = 0f;
				c1.project(vAux);
				c2.unproject(vAux);
				vAux.y = -vAux.y;
				
				batch.setProjectionMatrix(e.getCamera().getUiCamera().combined);
			
				t.idt();
				t.translate(vAux);
				t.scl(fm.scale);
				
				batch.setTransformMatrix(t);
				
				batch.begin();
					font.draw(batch, fm.dataSource.get().toString(), 0.0f, 0.0f);
				batch.end();
				
			}
			
			
		}
		
	}

	@Override
	public void dispose() {
		super.dispose();
	}
	

	
}
