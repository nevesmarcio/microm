package pt.me.microm.view.phenomenon;

import pt.me.microm.GameMicroM;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.events.ScreenTickEvent;
import pt.me.microm.model.phenomenon.CollisionModel;
import pt.me.microm.view.AbstractView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;

public class CollisionView extends AbstractView {
	private static final String TAG = CollisionView.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	SpriteBatch batch;

	private CollisionModel collisionmSrc;
	
	public CollisionView(CollisionModel collisionmSrc) {
		super(collisionmSrc);
		this.collisionmSrc = collisionmSrc;
		
	}

	@Override
	public void DelayedInit() {
		 batch = new SpriteBatch();
	}
	

	@Override
	public void draw(ScreenTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();

		if (GameMicroM.FLAG_DISPLAY_PARTICLES) {
			// renderização das particles
			float delta = Gdx.graphics.getDeltaTime();
			batch.setProjectionMatrix(e.getCamera().getGameCamera().combined);
			batch.begin();
				collisionmSrc.particleEffect.setPosition(collisionmSrc.position.x, collisionmSrc.position.y);
				collisionmSrc.particleEffect.draw(batch, delta);
			batch.end();		
		}
		
	}


}
