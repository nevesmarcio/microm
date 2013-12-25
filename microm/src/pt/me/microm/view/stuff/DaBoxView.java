package pt.me.microm.view.stuff;

import java.util.Iterator;

import pt.me.microm.GameMicroM;
import pt.me.microm.controller.loop.event.ScreenTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.model.stuff.DaBoxModel;
import pt.me.microm.view.AbstractView;
import pt.me.microm.view.helper.SimpleRendererHelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Logger;

public class DaBoxView extends AbstractView {
	private static final String TAG = DaBoxView.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private DaBoxModel daBoxmSrc;
	
	private ShapeRenderer renderer;
	
	private Mesh daBoxMesh;
	
	private SpriteBatch batch;
	
	private ParticleEffect particleEffect;
	
	public DaBoxView(DaBoxModel daBoxmSrc) {
		super(daBoxmSrc, 10);
		this.daBoxmSrc = daBoxmSrc;
	}

	@Override
	public void DelayedInit() {

		renderer = new ShapeRenderer();
		batch = new SpriteBatch();
		
		///////////////////////////////
	
		daBoxMesh = SimpleRendererHelper.buildMesh(daBoxmSrc.getBasicShape());
		
		///////////////////////////////
	
		particleEffect = new ParticleEffect();
	    particleEffect.load(Gdx.files.internal("data/particles/fire.p"), Gdx.files.internal("data/particles"));
	    particleEffect.start();		
				
	}
	
	
	
	
	Vector2 pointA = new Vector2();
	Vector2 pointB = new Vector2();
	@Override
	public void draw(ScreenTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
		SimpleRendererHelper.drawMesh(e.getCamera(), daBoxmSrc, daBoxMesh);
		
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
						if (daBoxmSrc.getBody().isAwake())
							renderer.setColor(1.0f, 0.0f, 0.0f, 1.0f);
						else
							renderer.setColor(0.0f, 1.0f, 1.0f, 1.0f);
						renderer.line(pointA.x, pointA.y, pointB.x, pointB.y);
					}
				renderer.end();
				renderer.begin(ShapeType.Line);
					renderer.setColor(1.0f, 1.0f, 1.0f, 1.0f);
					renderer.line(0.0f, 0.0f, 0.1f, 0.1f);
				renderer.end();				
			}
		}
			
        
		if (GameMicroM.FLAG_DISPLAY_PARTICLES) {
			// renderização das particles
			float delta = Gdx.graphics.getDeltaTime();
			batch.setProjectionMatrix(e.getCamera().getGameCamera().combined);
			batch.begin();
				particleEffect.setPosition(daBoxmSrc.getPosition().x, daBoxmSrc.getPosition().y);
				particleEffect.draw(batch, delta);
			batch.end();		
		}
		
	}

	@Override
	public void draw20(ScreenTickEvent e) {
		
	}	
	
	@Override
	public void dispose() {
		super.dispose();
	}
}
