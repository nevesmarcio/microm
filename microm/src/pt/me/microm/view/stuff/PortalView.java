package pt.me.microm.view.stuff;

import java.util.Iterator;

import pt.me.microm.GameMicroM;
import pt.me.microm.controller.loop.ScreenTickManager;
import pt.me.microm.controller.loop.event.ScreenTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.model.stuff.BoardModel;
import pt.me.microm.model.stuff.GoalModel;
import pt.me.microm.model.stuff.PortalModel;
import pt.me.microm.model.stuff.SpawnModel;
import pt.me.microm.view.AbstractView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.materials.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.SubMesh;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Logger;

public class PortalView extends AbstractView {
	private static final String TAG = PortalView.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private PortalModel portalmSrc;
	ShapeRenderer renderer;
	
	public PortalView(PortalModel portalmSrc) {
		super(portalmSrc);
		this.portalmSrc = portalmSrc;
	}

	@Override
	public void DelayedInit() {
		renderer = new ShapeRenderer();
		
		float[] vertexes;
		short[] indexes;
		int nr_points = portalmSrc.getBasicShape().getPointsArray().length;
		vertexes = new float[nr_points*4];

		indexes = new short[nr_points];
		
		for (int i = 0; i < nr_points; i++) {
			indexes[i] = (short)i;
			
			vertexes[i*4] = portalmSrc.getBasicShape().getPointsArray()[i].x;
			vertexes[i*4+1] = portalmSrc.getBasicShape().getPointsArray()[i].y;
			vertexes[i*4+2] = 0.0f;
			vertexes[i*4+3] = Color.toFloatBits(portalmSrc.getBasicShape().getFillColor().r,
												portalmSrc.getBasicShape().getFillColor().g,
												portalmSrc.getBasicShape().getFillColor().b,
												portalmSrc.getBasicShape().getFillColor().a);
		}

		portalMesh = new Mesh(true, nr_points, nr_points, 
                new VertexAttribute(Usage.Position, 3, "a_position"),
                new VertexAttribute(Usage.ColorPacked, 4, "a_color"));

		portalMesh.setVertices(vertexes);
		portalMesh.setIndices(indexes);
	}
	
	private Mesh portalMesh;
	
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
		Gdx.gl10.glLoadMatrixf(e.getCamera().getGameCamera().view.cpy().translate(portalmSrc.getPosition().x, portalmSrc.getPosition().y, 0.0f).val, 0);
		
	    //mesh.render(GL10.GL_TRIANGLES, 0, 3);
	    if (portalMesh != null)
	    	portalMesh.render(GL10.GL_TRIANGLE_FAN); //GL10.GL_TRIANGLE_FAN //GL10.GL_TRIANGLES //GL10.GL_TRIANGLE_STRIP  
		
	    Gdx.gl10.glPopMatrix();
	    Gdx.gl10.glPopMatrix();			
		
	    
	    //DEBUG STYLE 			-- desenha a "Shape" do portal
		if (GameMicroM.FLAG_DISPLAY_ACTOR_SHAPES) {
		    renderer.setProjectionMatrix(e.getCamera().getGameCamera().combined);
			
			Iterator<Fixture> it2 = portalmSrc.getBody().getFixtureList().iterator();
			
			while (it2.hasNext()) {
				Fixture aux = it2.next();
				
				renderer.identity();
				renderer.translate(aux.getBody().getPosition().x, aux.getBody().getPosition().y, 0.0f);
				renderer.rotate(0.0f, 0.0f, 1.0f, (float)Math.toDegrees(aux.getBody().getAngle()));
				
				ChainShape cs = (ChainShape)aux.getShape();
				
				renderer.begin(ShapeType.Line);
					int vCnt = cs.getVertexCount();
					for (int i = 0; i < vCnt; i++) {
						cs.getVertex(i, pointA); //pointA.add(portalmSrc.getPortalBody().getPosition());
						cs.getVertex(i==vCnt-1 ? 0 : i + 1, pointB); //pointB.add(portalmSrc.getPortalBody().getPosition());
						renderer.line(pointA.x, pointA.y, pointB.x, pointB.y);
					}
				renderer.end();
				
			}
		}

	}

	@Override
	public void draw20(ScreenTickEvent e) {
		
	}	
	
}
