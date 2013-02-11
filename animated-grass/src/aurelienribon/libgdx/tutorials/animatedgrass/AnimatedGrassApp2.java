package aurelienribon.libgdx.tutorials.animatedgrass;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Sine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;

/**
 * @author Aurelien Ribon | http://www.aurelienribon.com
 */
public class AnimatedGrassApp2 extends ApplicationAdapter {
	private TweenManager tweenManager = new TweenManager();
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
		
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		Tween.call(windCallback).start(tweenManager);
	}
	
	public void render() {
		tweenManager.update(Gdx.graphics.getDeltaTime());
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		grassSprite.draw(batch);
		batch.end();
	}
	
	private final TweenCallback windCallback = new TweenCallback() {
		@Override
		public void onEvent(int type, BaseTween<?> source) {
			float d = MathUtils.random() * 0.5f + 0.5f;
			float t = -0.5f * grassSprite.getHeight();
			
			Tween.to(grassSprite, SpriteAccessor.SKEW_X2X3, d).target(t, t)
				.ease(Sine.INOUT)
				.repeatYoyo(1, 0)
				.setCallback(windCallback)
				.start(tweenManager);
		}
	};
}
