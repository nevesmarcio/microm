package pt.me.microm.view.stuff;

import pt.me.microm.GameMicroM;
import pt.me.microm.controller.loop.event.ScreenTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.model.stuff.SpawnModel;
import pt.me.microm.view.AbstractView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Logger;

public class SpawnView extends AbstractView {
	private static final String TAG = SpawnView.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private SpawnModel spawnmSrc;

	ShapeRenderer renderer;
	
	public SpawnView(SpawnModel spawnmSrc) {
		super(spawnmSrc);
		this.spawnmSrc = spawnmSrc;
	}

	@Override
	public void DelayedInit() {
		renderer = new ShapeRenderer();

		float[] vertexes;
		short[] indexes;
		int nr_points = spawnmSrc.getBasicShape().getPointsArray().length;
		vertexes = new float[nr_points*4];

		indexes = new short[nr_points];
		
		for (int i = 0; i < nr_points; i++) {
			indexes[i] = (short)i;
			
			vertexes[i*4] = spawnmSrc.getBasicShape().getPointsArray()[i].x;
			vertexes[i*4+1] = spawnmSrc.getBasicShape().getPointsArray()[i].y;
			vertexes[i*4+2] = 0.0f;
			vertexes[i*4+3] = Color.toFloatBits(spawnmSrc.getBasicShape().getFillColor().r,
												spawnmSrc.getBasicShape().getFillColor().g,
												spawnmSrc.getBasicShape().getFillColor().b,
												spawnmSrc.getBasicShape().getFillColor().a);
		}

		spawnMesh = new Mesh(true, nr_points, nr_points, 
                new VertexAttribute(Usage.Position, 3, "a_position"),
                new VertexAttribute(Usage.ColorPacked, 4, "a_color"));

		spawnMesh.setVertices(vertexes);
		spawnMesh.setIndices(indexes);
	}
	
	private Mesh spawnMesh;
	
	
	private Vector2 pointA = new Vector2();
	private Vector2 pointB = new Vector2();
	@Override
	public void draw(ScreenTickEvent e) {
		
		Gdx.gl10.glPushMatrix();
		Gdx.gl10.glMatrixMode(GL10.GL_PROJECTION);
//		Gdx.gl10.glLoadMatrixf(e.getCamera().getGameCamera().projection.cpy().translate(0.0f, 0.0f, 0.0f).val, 0); // poupa umas alocaçoes e memória
		Gdx.gl10.glLoadMatrixf(e.getCamera().getGameCamera().projection.translate(0.0f, 0.0f, 0.0f).val, 0);
		
		Gdx.gl10.glPushMatrix();
		Gdx.gl10.glMatrixMode(GL10.GL_MODELVIEW);
		Gdx.gl10.glLoadMatrixf(e.getCamera().getGameCamera().view.cpy().translate(spawnmSrc.getPosition().x, spawnmSrc.getPosition().y, 0.0f).val, 0);
		
	    //mesh.render(GL10.GL_TRIANGLES, 0, 3);
	    if (spawnMesh != null)
	    	spawnMesh.render(GL10.GL_TRIANGLE_FAN); //GL10.GL_TRIANGLE_FAN //GL10.GL_TRIANGLES //GL10.GL_TRIANGLE_STRIP  
		
	    Gdx.gl10.glPopMatrix();
	    Gdx.gl10.glPopMatrix();	
		
		
		if (GameMicroM.FLAG_DISPLAY_ACTOR_SHAPES) {
			renderer.setProjectionMatrix(e.getCamera().getGameCamera().combined);
			
			Fixture fix = (spawnmSrc.getBody().getFixtureList()).get(0);
			ChainShape cs = (ChainShape)fix.getShape();
			
			renderer.begin(ShapeType.Line);
				int vCnt = cs.getVertexCount();
				for (int i = 0; i < vCnt; i++) {
					cs.getVertex(i, pointA); pointA.add(spawnmSrc.getBody().getPosition());
					cs.getVertex(i==vCnt-1 ? 0 : i + 1, pointB); pointB.add(spawnmSrc.getBody().getPosition());
					renderer.line(pointA.x, pointA.y, pointB.x, pointB.y);
				}
			renderer.end();
		}
		
	}
	
	
	
	@Override
	public void draw20(ScreenTickEvent e) {
		
	}	
}
