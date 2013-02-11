package pt.me.microm.view.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.events.ScreenTickEvent;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.view.AbstractView;

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
	
	ShapeRenderer renderer;
	
	Texture worldTexture = GAME_CONSTANTS.TEXTURE_BG;
	Sprite worldSprite;
	
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
		
		renderer = new ShapeRenderer();
		
		worldTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		worldSprite = new Sprite(worldTexture);
		
		worldSprite.setSize(15.0f, 15.0f);
		worldSprite.setOrigin(0.0f, 0.0f);
	}
	
	private List<Contact> temp = new ArrayList<Contact>();
	@Override
	public void draw(ScreenTickEvent e) {
		
		batch.setProjectionMatrix(e.getCamera().getGameCamera().combined);
		
		batch.begin();
			worldSprite.draw(batch);
		batch.end();		
		
		renderer.setProjectionMatrix(e.getCamera().getGameCamera().combined);

		/* renderização dos joints */
		Iterator<Joint> it = wmSrc.getPhysicsWorld().getJoints(); 
		while (it.hasNext()){
			Joint aux = it.next();
			
			renderer.identity();
			renderer.translate(aux.getAnchorA().x, aux.getAnchorA().y, 0.0f);
			
			renderer.begin(ShapeType.Line);
				renderer.setColor(1.0f, 0.5f, 0.5f, 1.0f);
				renderer.line(0.0f, 0.0f, aux.getAnchorB().x-aux.getAnchorA().x, aux.getAnchorB().y-aux.getAnchorA().y);
			renderer.end();
		}
		
		/* renderização dos contactos */ 
		temp.clear();
		temp.addAll(wmSrc.getPhysicsWorld().getContactList());
		for (int i=0; i < temp.size(); i++) {
		
			Contact aux = temp.get(i);
			
			WorldManifold wmfold = aux.getWorldManifold();

			for (int j = 0; j<wmfold.getNumberOfContactPoints(); j++) {
				Vector2 cpt = wmfold.getPoints()[j];
				
				renderer.identity();
				renderer.translate(cpt.x, cpt.y, 0.0f);

				renderer.begin(ShapeType.FilledCircle);
				renderer.setColor(1.0f, 1.0f, 1.0f, 1.0f);
				renderer.filledCircle(0.0f, 0.0f, 0.1f, 10);
				renderer.end();
			}
		}

		/* renderização dos status fps + ups */
		long elapsedNanoTime = e.getElapsedNanoTime();
		

//		batch.setProjectionMatrix(e.getCamera().combined.cpy().scl(1f/Gdx.graphics.getWidth()*GAME_CONSTANTS.MODEL_SCREEN_WIDTH_CAPACITY)); //.cpy().scl(1f/Gdx.graphics.getWidth()*GAME_CONSTANTS.MODEL_SCREEN_WIDTH_CAPACITY)
		batch.getProjectionMatrix().setToOrtho2D(0.0f, 0.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				
		fps = (float) (1000.0f / (elapsedNanoTime / (float)GAME_CONSTANTS.ONE_MILISECOND_TO_NANO));
		
		batch.begin();
			font1.draw(batch, "fps: " + fps, (int)textPosition1.x, (int)textPosition1.y);
			
			font2.draw(batch, "gameupdate (ups): " + wmSrc.getUps(), (int)textPosition2.x, (int)textPosition2.y);
			
			// só para demonstrar que a renderização está a ocorrer ao ritmo dos fps's.
			if (font3.getScaleX()>1.5f || font3.getScaleX()<0.5f)
				direction =!direction;
			font3.setScale(font3.getScaleX()+(direction?0.1f:-0.1f));
			font3.draw(batch, "X--X", (int)textPosition3.x, (int)textPosition3.y);

		batch.end();

	}



	
}
