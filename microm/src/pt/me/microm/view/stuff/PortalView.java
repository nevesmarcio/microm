package pt.me.microm.view.stuff;

import java.util.Iterator;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.events.ScreenTickEvent;
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
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Logger;

public class PortalView extends AbstractView {
	private static final String TAG = PortalView.class.getSimpleName();
	private static final Logger logger = new Logger(TAG);
	
	private PortalModel portalmSrc;
	ShapeRenderer renderer;
	
	Texture texture = GAME_CONSTANTS.TEXTURE_SQUARE1;
//	Sprite sprite; 
	SpriteBatch batch;
	Mesh mesh;
	
	float[] vertexes;
	short[] indexes;
	
	public PortalView(PortalModel portalmSrc) {
		super(portalmSrc);
		this.portalmSrc = portalmSrc;
		
		renderer = new ShapeRenderer();
		
//		sprite = new Sprite(texture);
		
		batch = new SpriteBatch();
		
		vertexes = new float[] { -0.5f, -0.5f, 0, /*Color.toFloatBits(255, 0, 0, 255),*/ 0, 2,
				                0.5f, -0.5f, 0, /*Color.toFloatBits(0, 255, 0, 255),*/ 2, 2,
				                1.0f, 0.5f, 0, /*Color.toFloatBits(0, 255, 0, 255),*/ 3, 0,
				                0.0f, 0.5f, 0, /*Color.toFloatBits(0, 0, 255, 255),*/ 1, 0 };
		indexes = new short[] { 0, 1, 2, 3 };		
		
	}
	
	private Vector2 pointA = new Vector2();
	private Vector2 pointB = new Vector2();
	@Override
	public void draw(ScreenTickEvent e) {
		//MESH STYLE

		Iterator<Fixture> it1 = portalmSrc.getBody().getFixtureList().iterator();
		
	
		if (mesh == null) {
	        mesh = new Mesh(true, 4, 4, 
	                new VertexAttribute(Usage.Position, 3, "a_position"),
	                /*new VertexAttribute(Usage.ColorPacked, 4, "a_color"),*/
	                new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoords"));

	        
			while (it1.hasNext()) {
				Fixture aux = it1.next();
				
//				ChainShape cs = (ChainShape)aux.getShape();
//				Vector2 pointA = new Vector2();
//				
//				int vCnt = cs.getVertexCount();
//				vertexes = new float[vCnt*5];
//				indexes = new short[vCnt];
//				for (int i = 0; i < vCnt; i++) {
//					cs.getVertex(i, pointA); //pointA.add(portalmSrc.getPortalBody().getPosition());
//					vertexes[i*5+0] = pointA.x;
//					vertexes[i*5+1] = pointA.y;
//					vertexes[i*5+2] = 0;
//					vertexes[i*5+3] = 0;
//					vertexes[i*5+4] = 0;
//					indexes[i] = (short)i;
//				}
			}	        
	        
	        
	        
	        
	        mesh.setVertices(vertexes);
	        mesh.setIndices(indexes);
		}
		
		Gdx.graphics.getGL10().glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
		Gdx.graphics.getGL10().glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
		Gdx.graphics.getGL10().glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		Gdx.graphics.getGL10().glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		Gdx.graphics.getGL10().glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE); // GL10.GL_REPLACE ou GL10.GL_BLEND n funca bem... (ainda n percebo nada disto de opengl)

		
		Gdx.graphics.getGL10().glEnable(GL10.GL_TEXTURE_2D);
	    Gdx.graphics.getGL10().glEnable(GL10.GL_BLEND);
	    Gdx.graphics.getGL10().glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	    texture.bind();
	    mesh.render(GL10.GL_TRIANGLE_FAN, 0, 4); //GL_TRIAGLES; GL_TRIANGLE_STRIP; GL_TRIANGLE_FAN
	    Gdx.graphics.getGL10().glDisable(GL10.GL_BLEND);
	    Gdx.graphics.getGL10().glDisable(GL10.GL_TEXTURE_2D);
	    
		
		
		//DEBUG STYLE
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

		// BATCH STYLE			
		batch.setProjectionMatrix(e.getCamera().getGameCamera().combined);
		
		batch.begin();
			batch.draw(texture, portalmSrc.getPosition().x, portalmSrc.getPosition().y, (float)1/(float)5, (float)1/(float)5);
		batch.end();

		
	}

}
