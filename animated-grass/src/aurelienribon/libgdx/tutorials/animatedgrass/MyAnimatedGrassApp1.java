package aurelienribon.libgdx.tutorials.animatedgrass;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Sine;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.sun.corba.se.impl.oa.poa.ActiveObjectMap.Key;

/**
 * @author Aurelien Ribon | http://www.aurelienribon.com
 */
public class MyAnimatedGrassApp1 extends ApplicationAdapter {
	private static final String TAG = MyAnimatedGrassApp1.class.getSimpleName();
	
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private TextureAtlas atlas;
	private Sprite grassSprite;
	private BitmapFont font;
	
	float screenW, screenH, w, h;	
	
	private TweenManager tweenManager = new TweenManager();	
	
	Sprite s1; 
	
	@Override
	public void resize(int width, int height) {
		screenW = Gdx.graphics.getWidth();
		screenH = Gdx.graphics.getHeight();
		w = 1;
		h = w * screenH / screenW;
		
		camera = new OrthographicCamera(w*screenW, h*screenW);

//		Gdx.gl.glViewport( (int)width/2-100, (int)height/2-100, 200, 200 );
//		Gdx.gl.glScissor( (int)width/2-100, (int)height/2-100, 200, 200 );
//		Gdx.gl.glEnable( Gdx.gl10.GL_SCISSOR_TEST );	
	}
	
	public void create() {
		//Texture.setEnforcePotImages(false); // ver o melhor sitio para enfiar isto, dado que as texturas estão nas constantes.
		
		screenW = Gdx.graphics.getWidth();
		screenH = Gdx.graphics.getHeight();
		w = 1;
		h = w * screenH / screenW;

		camera = new OrthographicCamera(w*screenW, h*screenW);		
		
		batch = new SpriteBatch();
		atlas = new TextureAtlas(Gdx.files.internal("mydata/microm.pack"));
		
		grassSprite = atlas.createSprite("grass2");
		grassSprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear); // bad fps //linear is best
		//grassSprite.setSize(404, 77);

		font = new BitmapFont(Gdx.files.internal("mydata/arial-15.fnt"), Gdx.files.internal("mydata/arial-15_00.png"), false);
		font.setColor(Color.BLACK);
		
		s1 = new Sprite(new Texture(Gdx.files.internal("ar/pattern.png")));
		//s1.scale(1.5f);

		
//		Gdx.gl.glViewport( (int)screenW/2-100, (int)screenH/2-100, 200, 200 );
//		Gdx.gl.glScissor( (int)screenW/2-100, (int)screenH/2-100, 200, 200 );
//		Gdx.gl.glEnable( Gdx.gl10.GL_SCISSOR_TEST );		
		
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		Tween.call(windCallback).delay(1.0f).start(tweenManager);		
	}
	
	public void render() {		
		tweenManager.update(Gdx.graphics.getDeltaTime());		
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

			grassSprite.setPosition(0-grassSprite.getWidth()/2, 0-grassSprite.getHeight()/2);	
			grassSprite.draw(batch);

			float scaleXY = screenW/s1.getWidth()/2;
			s1.setSize(s1.getWidth()*scaleXY, s1.getHeight()*scaleXY);
			//s1.setPosition(-s1.getWidth()*scaleXY/2, -s1.getHeight()*scaleXY/2);
			s1.setPosition(-screenW/2, -screenH/2);
			
			s1.draw(batch);
			
			font.setColor(Color.BLACK);
			font.drawWrapped(batch, "fps:"+Gdx.graphics.getFramesPerSecond(), -screenW/2, screenH/2, 100, HAlignment.LEFT);
			font.setColor(Color.BLUE);
			font.drawWrapped(batch, "fps:"+Gdx.graphics.getFramesPerSecond(), screenW/2-100, screenH/2, 100, HAlignment.RIGHT);
			font.setColor(Color.RED);
			font.drawWrapped(batch, "fps:"+Gdx.graphics.getFramesPerSecond(), screenW/2-100, -screenH/2+15, 100, HAlignment.RIGHT);
		batch.end();

		if (Gdx.input.isKeyPressed(Keys.SPACE))
			Gdx.app.log(TAG, "w: " + s1.getWidth()*scaleXY + " h: " + s1.getHeight()*scaleXY );		
		
		
	}
	
	private final TweenCallback windCallback = new TweenCallback() {
		@Override
		public void onEvent(int type, BaseTween<?> source) {
			float d = 1.0f;//MathUtils.random() * 0.5f + 0.5f; // duração de 0.5s a 1s
			float t = -0.25f * grassSprite.getHeight();
			
			Tween.to(grassSprite, SpriteAccessor.SKEW_X2X3, d)
				.target(t, t)
				.ease(aurelienribon.tweenengine.equations.Linear.INOUT)
				.repeatYoyo(1, 0)
				.setCallback(windCallback)
				.start(tweenManager);
		}
	};	
	
}
