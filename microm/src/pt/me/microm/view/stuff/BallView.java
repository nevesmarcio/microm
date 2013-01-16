package pt.me.microm.view.stuff;

import java.util.Iterator;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.events.ScreenTickEvent;
import pt.me.microm.model.stuff.BallModel;
import pt.me.microm.view.AbstractView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.physics.box2d.Fixture;

public class BallView extends AbstractView {
	private static final String TAG = BallView.class.getSimpleName();
	
	private BallModel ballmSrc;
	
	ShapeRenderer renderer;
	
	Texture ballTexture = GAME_CONSTANTS.TEXTURE_BALL;
	Sprite ballSprite;
	
	private SpriteBatch batch;
	
	public BallView(BallModel ballmSrc) {
		super(ballmSrc);
		this.ballmSrc = ballmSrc;
		
		renderer = new ShapeRenderer();
		
		ballTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		ballSprite = new Sprite(ballTexture);
		
		ballSprite.setSize(1.0f, 1.0f);
		ballSprite.setOrigin(0.5f, 0.5f);
		
		batch = new SpriteBatch();
		
	}
	

	@Override
	public void draw(ScreenTickEvent e) {
		
		renderer.setProjectionMatrix(e.getCamera().combined);
		batch.setProjectionMatrix(e.getCamera().combined);
		
		Iterator<Fixture> it = ballmSrc.ballBody.getFixtureList().iterator(); 
		while (it.hasNext()){
			Fixture aux = it.next();
			
			renderer.identity();
			renderer.translate(aux.getBody().getPosition().x, aux.getBody().getPosition().y, 0.0f);
			renderer.rotate(0.0f, 0.0f, 1.0f, (float)Math.toDegrees(aux.getBody().getAngle()));			
				
			renderer.begin(ShapeType.FilledCircle);
				renderer.setColor(ballmSrc.getColor());
				renderer.filledCircle(0.0f, 0.0f, ballmSrc.getRadius(), 50);
			renderer.end();

			renderer.begin(ShapeType.Line);
				renderer.setColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
				renderer.line(0.0f, 0.0f, 0.0f, ballmSrc.getRadius());
			renderer.end();		

			ballSprite.setPosition(aux.getBody().getPosition().x-0.5f, aux.getBody().getPosition().y-0.5f);
			ballSprite.setRotation((float)Math.toDegrees(aux.getBody().getAngle()));			
			
			batch.begin();
				ballSprite.draw(batch);
			batch.end();			
		}


	}


}
