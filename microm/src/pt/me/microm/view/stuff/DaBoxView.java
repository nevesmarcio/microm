package pt.me.microm.view.stuff;

import java.util.Iterator;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.events.ScreenTickEvent;
import pt.me.microm.model.dev.BallModel;
import pt.me.microm.model.stuff.DaBoxModel;
import pt.me.microm.view.AbstractView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class DaBoxView extends AbstractView {
	private static final String TAG = DaBoxView.class.getSimpleName();
	
	private DaBoxModel daBoxmSrc;
	
	ShapeRenderer renderer;
	
	Texture daBoxTexture = GAME_CONSTANTS.TEXTURE_BALL;
	
	SpriteBatch batch = new SpriteBatch();
	
	public DaBoxView(DaBoxModel daBoxmSrc) {
		super(daBoxmSrc);
		this.daBoxmSrc = daBoxmSrc;
		
		renderer = new ShapeRenderer();
		
		daBoxTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
	}
	

	Vector2 pointA = new Vector2();
	Vector2 pointB = new Vector2();
	@Override
	public void draw(ScreenTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
		renderer.setProjectionMatrix(e.getCamera().getGameCamera().combined);
		
		Iterator<Fixture> it = daBoxmSrc.getBody().getFixtureList().iterator();
		
		while (it.hasNext()) {
			Fixture aux = it.next();
			
			renderer.identity();
			renderer.translate(aux.getBody().getPosition().x, aux.getBody().getPosition().y, 0.0f);
			renderer.rotate(0.0f, 0.0f, 1.0f, (float)Math.toDegrees(aux.getBody().getAngle()));
			
			PolygonShape cs = (PolygonShape)aux.getShape();

			renderer.begin(ShapeType.Line);
				int vCnt = cs.getVertexCount();
				for (int i = 0; i < vCnt; i++) {
					cs.getVertex(i, pointA); //pointA.add(portalmSrc.getPortalBody().getPosition());
					cs.getVertex(i==vCnt-1 ? 0 : i + 1, pointB); //pointB.add(portalmSrc.getPortalBody().getPosition());
					renderer.line(pointA.x, pointA.y, pointB.x, pointB.y);
				}
			renderer.end();
			renderer.begin(ShapeType.Line);
				renderer.setColor(1.0f, 1.0f, 1.0f, 1.0f);
				renderer.line(0.0f, 0.0f, 0.1f, 0.1f);
			renderer.end();				
			
			float delta = Gdx.graphics.getDeltaTime();
//		    GL10 gl = Gdx.app.getGraphics().getGL10();
//		    gl.glClear(GL10.GL_COLOR_BUFFER_BIT);			
			
			batch.setProjectionMatrix(e.getCamera().getUiCamera().combined);
			batch.begin();
				daBoxmSrc.particleEffect.setPosition(0.0f, 0.0f);
				daBoxmSrc.particleEffect.draw(batch, delta);
			batch.end();
		}


	}


}
