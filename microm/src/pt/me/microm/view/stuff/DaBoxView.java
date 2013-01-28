package pt.me.microm.view.stuff;

import java.util.Iterator;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.events.ScreenTickEvent;
import pt.me.microm.model.dev.BallModel;
import pt.me.microm.model.stuff.DaBoxModel;
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

public class DaBoxView extends AbstractView {
	private static final String TAG = DaBoxView.class.getSimpleName();
	
	private DaBoxModel daBoxmSrc;
	
	ShapeRenderer renderer;
	
	Texture daBoxTexture = GAME_CONSTANTS.TEXTURE_BALL;
	
	public DaBoxView(DaBoxModel daBoxmSrc) {
		super(daBoxmSrc);
		this.daBoxmSrc = daBoxmSrc;
		
		renderer = new ShapeRenderer();
		
		daBoxTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
	}
	

	@Override
	public void draw(ScreenTickEvent e) {
		
		renderer.setProjectionMatrix(e.getCamera().combined);
		
		Iterator<Fixture> it = daBoxmSrc.daBoxBody.getFixtureList().iterator(); 
		while (it.hasNext()){
			Fixture aux = it.next();
			
			renderer.identity();
			renderer.translate(aux.getBody().getPosition().x, aux.getBody().getPosition().y, 0.0f);
			renderer.rotate(0.0f, 0.0f, 1.0f, (float)Math.toDegrees(aux.getBody().getAngle()));
			
			renderer.begin(ShapeType.FilledRectangle);
				renderer.setColor(daBoxmSrc.getColor());
				renderer.filledRect(-daBoxmSrc.getSide()/2, -daBoxmSrc.getSide()/2, daBoxmSrc.getSide(), daBoxmSrc.getSide());
			renderer.end();

			renderer.begin(ShapeType.Line);
				renderer.setColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
				renderer.line(0.0f, -daBoxmSrc.getSide()/2, 0.0f, daBoxmSrc.getSide()/2);
			renderer.end();				
			
		}


	}


}
