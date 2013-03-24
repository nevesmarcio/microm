package pt.me.microm.view.stuff;

import java.util.Arrays;
import java.util.Iterator;

import pt.me.microm.GameMicroM;
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
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Logger;

public class DaBoxView extends AbstractView {
	private static final String TAG = DaBoxView.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private DaBoxModel daBoxmSrc;
	
	ShapeRenderer renderer;
	
	Sprite daBoxSprite;
	
	SpriteBatch batch = new SpriteBatch();

	//////////////////////////////////////////////////////////////
	
	private static final int FRAME_COLS = 3; // #1
	private static final int FRAME_ROWS = 13; // #2
	private static final int FRAME_WIDTH = 153;
	private static final int FRAME_HEIGHT = 154;

	Animation walkAnimation; // #3
	Texture walkSheet; // #4
	TextureRegion[] walkFrames; // #5
	TextureRegion currentFrame; // #7

	float stateTime; // #8	
	
	Sprite animatedSprite;
	
	float racioW;
	float racioH;
	
	
	public DaBoxView(DaBoxModel daBoxmSrc) {
		super(daBoxmSrc);
		this.daBoxmSrc = daBoxmSrc;
	}

	@Override
	public void DelayedInit() {
		renderer = new ShapeRenderer();
		
		daBoxSprite = GAME_CONSTANTS.devAtlas.createSprite("txr_daBox");		
		daBoxSprite.setSize(daBoxmSrc.getBasicShape().getWidth(), daBoxmSrc.getBasicShape().getHeight());
		daBoxSprite.setOrigin(daBoxmSrc.getBasicShape().getWidth()/2, daBoxmSrc.getBasicShape().getHeight()/2);
		
		/////////////////////////////////////////////////////////
		walkSheet = new Texture(Gdx.files.internal("data/spritesheets/DaBoxRunning.png")); // #9 - textura completa
		
//		racioW = daBoxmSrc.getBasicShape().getWidth()/(FRAME_WIDTH*2);   // estes racios tem a ver com o tamanho da imagem  (512 x 2048). porque?...
//		racioH = daBoxmSrc.getBasicShape().getHeight()/(FRAME_HEIGHT*8);
		racioW = daBoxmSrc.getBasicShape().getWidth()/(FRAME_WIDTH*3); 
		racioH = daBoxmSrc.getBasicShape().getHeight()/(FRAME_HEIGHT*3);
		
		animatedSprite = new Sprite(walkSheet);
		
		TextureRegion[][] tmp = TextureRegion.split(walkSheet, FRAME_WIDTH, FRAME_HEIGHT); // #10 - o array walkFrames contem as posições de cada frame		
		walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		int index = 0;
		for (int i = 0; i < FRAME_ROWS; i++) {
			for (int j = 0; j < FRAME_COLS; j++) {
				walkFrames[index++] = tmp[i][j];
			}
		}
		walkFrames = Arrays.copyOfRange(walkFrames, 0, walkFrames.length-1); // a sheet não tem todas as imagens da matrix COLS*ROWS
		
		
		walkAnimation = new Animation(0.025f, walkFrames); // #11
		stateTime = 0f;		

		// estes racios tem a ver com o tamanho da imagem  (512 x 2048). porque?...
		animatedSprite.setSize(animatedSprite.getWidth()/1, animatedSprite.getHeight()/4);
		animatedSprite.setOrigin(animatedSprite.getOriginX(), animatedSprite.getOriginY()/4);
		
	}
	

	Vector2 pointA = new Vector2();
	Vector2 pointB = new Vector2();
	@Override
	public void draw(ScreenTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
		if (GameMicroM.FLAG_DISPLAY_ACTOR_SHAPES) {
			renderer.setProjectionMatrix(e.getCamera().getGameCamera().combined);

			renderer.identity();
			renderer.translate(daBoxmSrc.getBody().getPosition().x, daBoxmSrc.getBody().getPosition().y, 0.0f);
			renderer.rotate(0.0f, 0.0f, 1.0f, (float)Math.toDegrees(daBoxmSrc.getBody().getAngle()));			
			
			Iterator<Fixture> it = daBoxmSrc.getBody().getFixtureList().iterator();
			Fixture aux = null;
			while (it.hasNext()) {
				aux = it.next();
				
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
			}
		}
			
		if (GameMicroM.FLAG_DISPLAY_ACTOR_TEXTURES) {
			batch.setProjectionMatrix(e.getCamera().getGameCamera().combined);
			batch.begin();
				daBoxSprite.setPosition(daBoxmSrc.getBody().getPosition().x-daBoxmSrc.getBasicShape().getWidth()/2, daBoxmSrc.getBody().getPosition().y-daBoxmSrc.getBasicShape().getHeight()/2);
				daBoxSprite.setRotation((float)Math.toDegrees(daBoxmSrc.getBody().getAngle()));
				daBoxSprite.draw(batch);
			batch.end();
		}
		
        /////////////// ANIMATION /////////////////////
        stateTime += Gdx.graphics.getDeltaTime();                       // #15
        currentFrame = walkAnimation.getKeyFrame(stateTime, true);      // #16
        
        
		
        
        batch.setProjectionMatrix(e.getCamera().getGameCamera().combined.cpy().scale(racioW, racioH, 1)); // .cpy().scale(0.01f, 0.01f, 0.01f)
        batch.begin();
        	animatedSprite.setRegion(currentFrame);
        	animatedSprite.setPosition(	daBoxmSrc.getBody().getPosition().x/racioW - animatedSprite.getWidth()/2,
        								daBoxmSrc.getBody().getPosition().y/racioH - animatedSprite.getHeight()/2);
        	animatedSprite.setRotation((float)Math.toDegrees(daBoxmSrc.getBody().getAngle()));
        	animatedSprite.draw(batch);
        batch.end();
		
		
		
		if (GameMicroM.FLAG_DISPLAY_PARTICLES) {
			// renderização das particles
			float delta = Gdx.graphics.getDeltaTime();
			batch.setProjectionMatrix(e.getCamera().getGameCamera().combined);
			batch.begin();
				daBoxmSrc.particleEffect.setPosition(daBoxmSrc.getPosition().x, daBoxmSrc.getPosition().y);
				daBoxmSrc.particleEffect.draw(batch, delta);
			batch.end();		
		}
		
	}


}
