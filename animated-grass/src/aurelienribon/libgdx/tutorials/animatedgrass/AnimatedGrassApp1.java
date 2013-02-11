package aurelienribon.libgdx.tutorials.animatedgrass;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * @author Aurelien Ribon | http://www.aurelienribon.com
 */
public class AnimatedGrassApp1 extends ApplicationAdapter {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private TextureAtlas atlas;
	private Sprite grassSprite;
	

	
	public void create() {
		float screenW = Gdx.graphics.getWidth();
		float screenH = Gdx.graphics.getHeight();
		float w = 1;
		float h = w * screenH / screenW;
		
		camera = new OrthographicCamera(w, h);
		batch = new SpriteBatch();
		
		atlas = new TextureAtlas("data/grass.pack");
		
		grassSprite = atlas.createSprite("grass");
		grassSprite.setSize(w, w * grassSprite.getHeight() / grassSprite.getWidth());
		grassSprite.setPosition(-w/2, -h/2);
		
		
	}
	
	public void render() {		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		grassSprite.draw(batch);
		batch.end();
	}
}
