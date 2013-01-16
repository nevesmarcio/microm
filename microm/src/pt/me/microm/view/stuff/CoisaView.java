package pt.me.microm.view.stuff;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.events.ScreenTickEvent;
import pt.me.microm.model.stuff.CoisaModel;
import pt.me.microm.view.AbstractView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class CoisaView extends AbstractView {
	private static final String TAG = CoisaView.class.getSimpleName();
	
	private CoisaModel coisamSrc;
	
	ShapeRenderer renderer;
	
	Texture coisaTexture = GAME_CONSTANTS.TEXTURE_THING;
	Sprite coisaSprite;
	
	private SpriteBatch batch;
	
	public CoisaView(CoisaModel coisamSrc) {
		super(coisamSrc);
		this.coisamSrc = coisamSrc;
		
		renderer = new ShapeRenderer();
		
		coisaTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		coisaSprite = new Sprite(coisaTexture);

		coisaSprite.setSize(5.0f, 5.0f*coisaSprite.getHeight()/coisaSprite.getWidth());
		coisaSprite.setOrigin(0.0f, 0.0f);
		
		batch = new SpriteBatch();
		
	}
	

	@Override
	public void draw(ScreenTickEvent e) {

		/* isto n faz parte deste método. só está aqui devido à ordem dos ctors +*/
		coisaSprite.setPosition(coisamSrc.coisaBody.getPosition().x, coisamSrc.coisaBody.getPosition().y);
		coisaSprite.setRotation((float)Math.toDegrees(coisamSrc.coisaBody.getAngle()));
		
		batch.setProjectionMatrix(e.getCamera().combined);
		
		batch.begin();
		coisaSprite.draw(batch);
		batch.end();
		
	}


}
