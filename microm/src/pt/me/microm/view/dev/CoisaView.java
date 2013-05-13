package pt.me.microm.view.dev;

import pt.me.microm.controller.loop.event.ScreenTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.model.dev.CoisaModel;
import pt.me.microm.view.AbstractView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Logger;

public class CoisaView extends AbstractView {
	private static final String TAG = CoisaView.class.getSimpleName();
	private static Logger logger = new Logger(TAG);
	
	private CoisaModel coisamSrc;
	
	ShapeRenderer renderer;
	Sprite coisaSprite;
	private SpriteBatch batch;
	
	public CoisaView(CoisaModel coisamSrc) {
		super(coisamSrc, 1); // FIXME: arranjar umas constantes para definir o zOrder
		this.coisamSrc = coisamSrc;
	}

	@Override
	public void DelayedInit() {
		renderer = new ShapeRenderer();
		
		coisaSprite = GAME_CONSTANTS.devAtlas.createSprite("thing");

		coisaSprite.setSize(1.0f, 1.0f*coisaSprite.getHeight()/coisaSprite.getWidth());
		coisaSprite.setOrigin(0.0f, 0.0f);
		
		batch = new SpriteBatch();
	
	}
	

	@Override
	public void draw(ScreenTickEvent e) {

		/* isto n faz parte deste método. só está aqui devido à ordem dos ctors +*/
		coisaSprite.setPosition(coisamSrc.getBody().getPosition().x, coisamSrc.getBody().getPosition().y);
		coisaSprite.setRotation((float)Math.toDegrees(coisamSrc.getBody().getAngle()));
		
		batch.setProjectionMatrix(e.getCamera().getGameCamera().combined);
		
		batch.begin();
			coisaSprite.draw(batch);
		batch.end();
		
	}

	@Override
	public void draw20(ScreenTickEvent e) {
		
	}
}
