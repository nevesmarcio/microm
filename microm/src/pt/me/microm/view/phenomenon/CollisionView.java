package pt.me.microm.view.phenomenon;

import pt.me.microm.GameMicroM;
import pt.me.microm.controller.loop.ScreenTickManager;
import pt.me.microm.controller.loop.event.ScreenTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.model.phenomenon.CollisionModel;
import pt.me.microm.view.AbstractView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;

public class CollisionView extends AbstractView {
	private static final String TAG = CollisionView.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	SpriteBatch batch;

	private CollisionModel collisionmSrc;
	
	public ParticleEffect particleEffect;
	
	public CollisionView(CollisionModel collisionmSrc) {
		super(collisionmSrc, 100);
		this.collisionmSrc = collisionmSrc;
		
	}

	@Override
	public void DelayedInit() {
		 batch = new SpriteBatch();

		 particleEffect = new ParticleEffect();
		 particleEffect.load(Gdx.files.internal("data/particles/collision.p"), Gdx.files.internal("data/particles"));
		 particleEffect.start();
	}
	

	@Override
	public void draw(ScreenTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();

		// Enable da transparência
		Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
	    Gdx.graphics.getGL20().glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);		
		
		if (GameMicroM.FLAG_DISPLAY_PARTICLES) {
			if (particleEffect != null) {
			
				// renderização das particles
				float delta = Gdx.graphics.getDeltaTime();
				batch.setProjectionMatrix(e.getCamera().getGameCamera().combined);

				batch.begin();
					particleEffect.setPosition(collisionmSrc.position.x, collisionmSrc.position.y);
					particleEffect.draw(batch, delta);
				batch.end();		
			}
		}
		
	}
	
	@Override
	public void draw20(ScreenTickEvent e) {
		draw(e);
	}

}
