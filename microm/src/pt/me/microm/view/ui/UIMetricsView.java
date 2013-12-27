package pt.me.microm.view.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.me.microm.GameMicroM;
import pt.me.microm.controller.loop.event.ScreenTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.model.ui.UIMetricsModel;
import pt.me.microm.view.AbstractView;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;


public class UIMetricsView  extends AbstractView {
	private static final String TAG = UIMetricsView.class.getSimpleName();
	private static final Logger logger = LoggerFactory.getLogger(TAG);
	
	private BitmapFont font;
	private BitmapFont pulsingFont;

	private Vector2 textPosition1 = new Vector2(0, 60);
	private Vector2 textPosition2 = new Vector2(0, 40);	
	private Vector2 textPosition3 = new Vector2(0, 20);
	
	private boolean direction = true;
	
	private float fps;
	
	private UIMetricsModel uiSrc;
	
	private SpriteBatch batch;
	private ShapeRenderer renderer;
	
	public UIMetricsView(UIMetricsModel uiSrc) {  
		super(uiSrc, 100); // FIXME: arranjar umas constantes para definir o zOrder
		this.uiSrc = uiSrc;
	}
	
	@Override
	public void DelayedInit() {
		font = new BitmapFont();
		pulsingFont = new BitmapFont();

		
		batch = new SpriteBatch();
		renderer = new ShapeRenderer();
	}
	
	private String xx = "X--X";
	@Override
	public void draw(ScreenTickEvent e) {
		
		if (GameMicroM.FLAG_DEV_ELEMENTS_A || true) {
			/* renderização dos status fps + ups */
			long elapsedNanoTime = e.getElapsedNanoTime();
			
			batch.setProjectionMatrix(e.getCamera().getUiCamera().combined);
					
			fps = (float) (1000.0f / (elapsedNanoTime / (float)GAME_CONSTANTS.ONE_MILISECOND_TO_NANO));
			
			batch.begin();
				font.setColor(Color.RED);	
				font.draw(batch, "fps: " + fps, (int)textPosition1.x - e.getCamera().getUiCamera().viewportWidth/2, (int)textPosition1.y - e.getCamera().getUiCamera().viewportHeight/2);
				
				font.setColor(Color.BLUE);
				font.draw(batch, "gameupdate (ups): " + uiSrc.getUps(), (int)textPosition2.x - e.getCamera().getUiCamera().viewportWidth/2, (int)textPosition2.y - e.getCamera().getUiCamera().viewportHeight/2);
				
				// só para demonstrar que a renderização está a ocorrer ao ritmo dos fps's.
				pulsingFont.setColor(Color.WHITE);
				if (pulsingFont.getScaleX()>1.5f || pulsingFont.getScaleX()<0.5f)
					direction =!direction;
				pulsingFont.setScale(pulsingFont.getScaleX()+(direction?0.1f:-0.1f));
				pulsingFont.draw(batch, xx, (int)textPosition3.x - e.getCamera().getUiCamera().viewportWidth/2, (int)textPosition3.y - e.getCamera().getUiCamera().viewportHeight/2);
				
			batch.end();
		}

		
	}

	@Override
	public void draw20(ScreenTickEvent e) {
		draw(e);
		
	}	
	
}
