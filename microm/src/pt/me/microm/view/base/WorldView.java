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
	private static final Logger logger = new Logger(TAG);

	private WorldModel wmSrc;
	
	ShapeRenderer renderer;
	
	Texture worldTexture = GAME_CONSTANTS.TEXTURE_BG;
	Sprite worldSprite;
	
	SpriteBatch batch = new SpriteBatch();
	
	public WorldView(WorldModel wmSrc) {  
		super(wmSrc);
		this.wmSrc = wmSrc;
		
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


	}



	
}
