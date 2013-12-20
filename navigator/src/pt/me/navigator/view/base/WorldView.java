package pt.me.navigator.view.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.me.navigator.infrastructure.GAME_CONSTANTS;
import pt.me.navigator.infrastructure.events.ScreenTickEvent;
import pt.me.navigator.model.base.WorldModel;
import pt.me.navigator.view.AbstractView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.utils.Logger;

public class WorldView extends AbstractView {
	private static final String TAG = WorldView.class.getSimpleName();
	
	private float fps;
	
	private BitmapFont font1;
	private Vector2 textPosition1 = new Vector2(0, 60);
	
	private BitmapFont font2;
	private Vector2 textPosition2 = new Vector2(0, 40);	
	
	private BitmapFont font3;
	private Vector2 textPosition3 = new Vector2(0, 20);
	
	private boolean direction = true;
	
	private WorldModel wmSrc;
	
	SpriteBatch batch = new SpriteBatch();
	
	public WorldView(WorldModel wmSrc) {  
		super(wmSrc);
		this.wmSrc = wmSrc;
		
		font1 = new BitmapFont();
		font1.setColor(Color.RED);
		
		font2 = new BitmapFont();
		font2.setColor(Color.BLUE);
		
		font3 = new BitmapFont();
		font3.setColor(Color.WHITE);
		

	}
	
	@Override
	public void draw(ScreenTickEvent e) {
		
		/* renderização dos status fps + ups */
		long elapsedNanoTime = e.getElapsedNanoTime();


		batch.setProjectionMatrix(e.getCamera().getGameCamera().combined);
//		batch.setProjectionMatrix(e.getCamera().combined.cpy().scl(1f/Gdx.graphics.getWidth()*GAME_CONSTANTS.MODEL_SCREEN_WIDTH_CAPACITY)); //.cpy().scl(1f/Gdx.graphics.getWidth()*GAME_CONSTANTS.MODEL_SCREEN_WIDTH_CAPACITY)
		batch.getProjectionMatrix().setToOrtho2D(0.0f, 0.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				
		fps = (float) (1000.0f / (elapsedNanoTime / (float)GAME_CONSTANTS.ONE_MILISECOND_TO_NANO));
		
		batch.begin();
			font1.draw(batch, "fps: " + (int) fps 
					+ " accel:" 
					+ Gdx.input.getAccelerometerX() + ", "  
					+ Gdx.input.getAccelerometerY() + ", "
					+ Gdx.input.getAccelerometerZ() + ", "
					+ Gdx.input.getAzimuth() + ", "
					, (int)textPosition1.x, (int)textPosition1.y);
			
			font2.draw(batch, "timerupdate (ups): " + wmSrc.getUps(), (int)textPosition2.x, (int)textPosition2.y);
			
			// só para demonstrar que a renderização está a ocorrer ao ritmo dos fps's.
			if (font3.getScaleX()>1.5f || font3.getScaleX()<0.5f)
				direction =!direction;
			font3.setScale(font3.getScaleX()+(direction?0.1f:-0.1f));
			font3.draw(batch, "X--X", (int)textPosition3.x, (int)textPosition3.y);

		batch.end();

		
		
		
		
	}



	
}
