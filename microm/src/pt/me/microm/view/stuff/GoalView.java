package pt.me.microm.view.stuff;

import pt.me.microm.GameMicroM;
import pt.me.microm.controller.loop.event.ScreenTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.model.stuff.BoardModel;
import pt.me.microm.model.stuff.GoalModel;
import pt.me.microm.view.AbstractView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Logger;

public class GoalView extends AbstractView {
	private static final String TAG = GoalView.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private GoalModel goalmSrc;

	ShapeRenderer renderer;
	
	
	public GoalView(GoalModel goalmSrc) {
		super(goalmSrc);
		this.goalmSrc = goalmSrc;
	}

	@Override
	public void DelayedInit() {
		renderer = new ShapeRenderer();
		
		float[] vertexes;
		short[] indexes;
		int nr_points = goalmSrc.getBasicShape().getPointsArray().length;
		vertexes = new float[nr_points*4];

		indexes = new short[nr_points];
		
		for (int i = 0; i < nr_points; i++) {
			indexes[i] = (short)i;
			
			vertexes[i*4] = goalmSrc.getBasicShape().getPointsArray()[i].x;
			vertexes[i*4+1] = goalmSrc.getBasicShape().getPointsArray()[i].y;
			vertexes[i*4+2] = 0.0f;
			vertexes[i*4+3] = Color.toFloatBits(goalmSrc.getBasicShape().getFillColor().r,
												goalmSrc.getBasicShape().getFillColor().g,
												goalmSrc.getBasicShape().getFillColor().b,
												goalmSrc.getBasicShape().getFillColor().a);
		}

		goalMesh = new Mesh(true, nr_points, nr_points, 
                new VertexAttribute(Usage.Position, 3, "a_position"),
                new VertexAttribute(Usage.ColorPacked, 4, "a_color"));

		goalMesh.setVertices(vertexes);
		goalMesh.setIndices(indexes);
	}
	
	private Mesh goalMesh;	

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
		Gdx.gl10.glLoadMatrixf(e.getCamera().getGameCamera().view.cpy().translate(goalmSrc.getPosition().x, goalmSrc.getPosition().y, 0.0f).val, 0);
		
	    //mesh.render(GL10.GL_TRIANGLES, 0, 3);
	    if (goalMesh != null)
	    	goalMesh.render(GL10.GL_TRIANGLE_FAN); //GL10.GL_TRIANGLE_FAN //GL10.GL_TRIANGLES //GL10.GL_TRIANGLE_STRIP  
		
	    Gdx.gl10.glPopMatrix();
	    Gdx.gl10.glPopMatrix();			
		
		
		if (GameMicroM.FLAG_DISPLAY_ACTOR_SHAPES) {
			renderer.setProjectionMatrix(e.getCamera().getGameCamera().combined);
			
			Fixture fix = (goalmSrc.getBody().getFixtureList()).get(0);
			ChainShape cs = (ChainShape)fix.getShape();
			
			renderer.begin(ShapeType.Line);
				int vCnt = cs.getVertexCount();
				for (int i = 0; i < vCnt; i++) {
					cs.getVertex(i, pointA); pointA.add(goalmSrc.getBody().getPosition());
					cs.getVertex(i==vCnt-1 ? 0 : i + 1, pointB); pointB.add(goalmSrc.getBody().getPosition());
					renderer.line(pointA.x, pointA.y, pointB.x, pointB.y);
				}
			renderer.end();
		}
		

		
	}

	@Override
	public void draw20(ScreenTickEvent e) {
		
	}	
	
}
