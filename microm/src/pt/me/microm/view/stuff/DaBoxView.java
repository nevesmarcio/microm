package pt.me.microm.view.stuff;

import java.util.Iterator;

import pt.me.microm.GameMicroM;
import pt.me.microm.controller.loop.event.ScreenTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.model.stuff.DaBoxModel;
import pt.me.microm.view.AbstractView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
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
	
		float[] vertexes;
		short[] indexes;
		int nr_points = daBoxmSrc.getBasicShape().getPointsArray().length;
		vertexes = new float[nr_points*4];

		indexes = new short[nr_points];
		
		for (int i = 0; i < nr_points; i++) {
			indexes[i] = (short)i;
			
			vertexes[i*4] = daBoxmSrc.getBasicShape().getPointsArray()[i].x;
			vertexes[i*4+1] = daBoxmSrc.getBasicShape().getPointsArray()[i].y;
			vertexes[i*4+2] = 0.0f;
			vertexes[i*4+3] = Color.toFloatBits(daBoxmSrc.getBasicShape().getFillColor().r,
												daBoxmSrc.getBasicShape().getFillColor().g,
												daBoxmSrc.getBasicShape().getFillColor().b,
												daBoxmSrc.getBasicShape().getFillColor().a);
		}

		daBoxMesh = new Mesh(true, nr_points, nr_points, 
                new VertexAttribute(Usage.Position, 3, "a_position"),
                new VertexAttribute(Usage.ColorPacked, 4, "a_color"));

		daBoxMesh.setVertices(vertexes);
		daBoxMesh.setIndices(indexes);
		
		
		
		
		///////////////////////////////
	
		particleEffect = new ParticleEffect();
	    particleEffect.load(Gdx.files.internal("data/particles/fire.p"), Gdx.files.internal("data/particles"));
	    particleEffect.start();		
				
	}
	
	private Mesh daBoxMesh;
	
	
	Vector2 pointA = new Vector2();
	Vector2 pointB = new Vector2();
	@Override
	public void draw(ScreenTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
		Gdx.gl10.glPushMatrix();
		Gdx.gl10.glMatrixMode(GL10.GL_PROJECTION);
//		Gdx.gl10.glLoadMatrixf(e.getCamera().getGameCamera().projection.cpy().translate(0.0f, 0.0f, 0.0f).val, 0); // poupa umas alocaçoes e memória
		Gdx.gl10.glLoadMatrixf(e.getCamera().getGameCamera().projection.translate(0.0f, 0.0f, 0.0f).val, 0);
		
		Gdx.gl10.glPushMatrix();
		Gdx.gl10.glMatrixMode(GL10.GL_MODELVIEW);
		Gdx.gl10.glLoadMatrixf(e.getCamera().getGameCamera().view.cpy().translate(daBoxmSrc.getPosition().x, daBoxmSrc.getPosition().y, 0.0f).val, 0);
		
	    //mesh.render(GL10.GL_TRIANGLES, 0, 3);
	    if (daBoxMesh != null)
	    	daBoxMesh.render(GL10.GL_TRIANGLE_FAN); //GL10.GL_TRIANGLE_FAN //GL10.GL_TRIANGLES //GL10.GL_TRIANGLE_STRIP  
		
	    Gdx.gl10.glPopMatrix();
	    Gdx.gl10.glPopMatrix();			
		

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
