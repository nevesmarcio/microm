package pt.me.microm.view.collectible;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.events.ScreenTickEvent;
import pt.me.microm.model.collectible.StarModel;
import pt.me.microm.model.dev.CoisaModel;
import pt.me.microm.view.AbstractView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Logger;

public class StarView extends AbstractView {
	private static final String TAG = StarView.class.getSimpleName();
	private static Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private StarModel starmSrc;
	
	ShapeRenderer renderer;
	Sprite starSprite;
	private SpriteBatch batch;
	
	public StarView(StarModel starmSrc) {
		super(starmSrc, 1); // FIXME: arranjar umas constantes para definir o zOrder
		this.starmSrc = starmSrc;
	}

	@Override
	public void DelayedInit() {
		renderer = new ShapeRenderer();
		
		starSprite = GAME_CONSTANTS.devAtlas.createSprite("star");

		starSprite.setSize(1.0f, 1.0f*starSprite.getHeight()/starSprite.getWidth());
		starSprite.setOrigin(0.0f, 0.0f);
		
		batch = new SpriteBatch();
	
	}
	

	@Override
	public void draw(ScreenTickEvent e) {

		/* isto n faz parte deste método. só está aqui devido à ordem dos ctors +*/
		starSprite.setPosition(starmSrc.starBody.getPosition().x, starmSrc.starBody.getPosition().y);
		starSprite.setRotation((float)Math.toDegrees(starmSrc.starBody.getAngle()));
		
		batch.setProjectionMatrix(e.getCamera().getGameCamera().combined);
		
		batch.begin();
			starSprite.draw(batch);
		batch.end();
		
	}


}
