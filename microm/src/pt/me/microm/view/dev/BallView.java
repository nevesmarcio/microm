package pt.me.microm.view.dev;

import java.util.Iterator;

import pt.me.microm.controller.loop.event.ScreenTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.model.dev.BallModel;
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
import com.badlogic.gdx.utils.Logger;

public class BallView extends AbstractView {
	private static final String TAG = BallView.class.getSimpleName();
	private static final Logger logger = new Logger(TAG);
	
	private BallModel ballmSrc;
	
	ShapeRenderer renderer;
	Sprite ballSprite;
	private SpriteBatch batch;
	
	public BallView(BallModel ballmSrc) {
		super(ballmSrc, 1);
		this.ballmSrc = ballmSrc;
	}
	
	@Override
	public void DelayedInit() {
		renderer = new ShapeRenderer();
		
		ballSprite = GAME_CONSTANTS.devAtlas.createSprite("ball");
		
		ballSprite.setSize(1.0f, 1.0f);
		ballSprite.setOrigin(0.5f, 0.5f);
		
		batch = new SpriteBatch();
	}

	
	@Override
	public void draw(ScreenTickEvent e) {
		
		renderer.setProjectionMatrix(e.getCamera().getGameCamera().combined);
		batch.setProjectionMatrix(e.getCamera().getGameCamera().combined);
		
		Iterator<Fixture> it = ballmSrc.getBody().getFixtureList().iterator(); 
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
	
	@Override
	public void draw20(ScreenTickEvent e) {
		
	}

}
