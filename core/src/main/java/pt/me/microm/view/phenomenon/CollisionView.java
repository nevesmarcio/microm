package pt.me.microm.view.phenomenon;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.me.microm.GameMicroM;
import pt.me.microm.controller.loop.event.ScreenTickEvent;
import pt.me.microm.model.phenomenon.CollisionModel;
import pt.me.microm.view.AbstractView;


public class CollisionView extends AbstractView {
    private static final String TAG = CollisionView.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(TAG);

    private SpriteBatch batch;

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

        if (GameMicroM.FLAG_DISPLAY_PARTICLES) {
            if (particleEffect != null) {

                // renderização das particles
                float delta = Gdx.graphics.getDeltaTime();
                batch.setProjectionMatrix(e.getCamera().getGameCamera().combined);

                batch.begin();
                particleEffect.setPosition(collisionmSrc.getPosition().x, collisionmSrc.getPosition().y);
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
