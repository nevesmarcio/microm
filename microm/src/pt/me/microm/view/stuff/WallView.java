package pt.me.microm.view.stuff;

import java.util.Iterator;

import pt.me.microm.GameMicroM;
import pt.me.microm.controller.loop.event.ScreenTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.model.dev.BallModel;
import pt.me.microm.model.stuff.DaBoxModel;
import pt.me.microm.model.stuff.WallModel;
import pt.me.microm.view.AbstractView;
import pt.me.microm.view.helper.MeshHelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.loaders.wavefront.ObjLoader;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Logger;

public class WallView extends AbstractView {
	private static final String TAG = WallView.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private WallModel wallmSrc;
	
	ShapeRenderer renderer;
	
	Sprite wallSprite;

	SpriteBatch batch = new SpriteBatch();
	
	MeshHelper meshHelper;
	
	public WallView(WallModel wallmSrc) {
		super(wallmSrc);
		this.wallmSrc = wallmSrc;
	}
	
	@Override
	public void DelayedInit() {
		renderer = new ShapeRenderer();
		
		wallSprite = GAME_CONSTANTS.devAtlas.createSprite("txr_wall");

		wallSprite.setSize(wallmSrc.getBasicShape().getWidth(), wallmSrc.getBasicShape().getHeight());
		wallSprite.setOrigin(wallmSrc.getBasicShape().getWidth()/2, wallmSrc.getBasicShape().getHeight()/2);		
		
		float[] vertexes;
		short[] indexes;
		int nr_points = wallmSrc.getBasicShape().getPointsArray().length;
		vertexes = new float[nr_points*4];

		indexes = new short[nr_points];
		
		for (int i = 0; i < nr_points; i++) {
			indexes[i] = (short)i;
			
			vertexes[i*4] = wallmSrc.getBasicShape().getPointsArray()[i].x;
			vertexes[i*4+1] = wallmSrc.getBasicShape().getPointsArray()[i].y;
			vertexes[i*4+2] = 0.0f;
			vertexes[i*4+3] = Color.toFloatBits(wallmSrc.getBasicShape().getFillColor().r,
												wallmSrc.getBasicShape().getFillColor().g,
												wallmSrc.getBasicShape().getFillColor().b,
												wallmSrc.getBasicShape().getFillColor().a);
		}

		wallMesh = new Mesh(true, nr_points, nr_points, 
                new VertexAttribute(Usage.Position, 3, "a_position"),
                new VertexAttribute(Usage.ColorPacked, 4, "a_color"));

        wallMesh.setVertices(vertexes);
        wallMesh.setIndices(indexes);		
		
        if (Gdx.graphics.isGL20Available()) {
	        meshHelper = new MeshHelper();
			meshHelper.createMesh(wallmSrc.getBasicShape().getMeshValues());
        }
        
	}

	
	private Mesh wallMesh;
	
	
	Vector2 pointA = new Vector2();
	Vector2 pointB = new Vector2();
	@Override
	public void draw(ScreenTickEvent e) {

		
		
		Gdx.gl10.glPushMatrix();
		Gdx.gl10.glMatrixMode(GL10.GL_PROJECTION);
//		Gdx.gl10.glLoadMatrixf(e.getCamera().getGameCamera().projection.cpy().translate(0.0f, 0.0f, 0.0f).val, 0); // poupa umas alocaçoes e memória
		Gdx.gl10.glLoadMatrixf(e.getCamera().getGameCamera().projection.translate(0.0f, 0.0f, 0.0f).val, 0);
		
		Gdx.gl10.glPushMatrix();
		Gdx.gl10.glMatrixMode(GL10.GL_MODELVIEW);
		Gdx.gl10.glLoadMatrixf(e.getCamera().getGameCamera().view.cpy().translate(wallmSrc.getBody().getPosition().x, wallmSrc.getBody().getPosition().y, 0.0f).val, 0);
		
	    //mesh.render(GL10.GL_TRIANGLES, 0, 3);
	    if (wallMesh != null)
	    	wallMesh.render(GL10.GL_TRIANGLE_FAN); //GL10.GL_TRIANGLE_FAN //GL10.GL_TRIANGLES //GL10.GL_TRIANGLE_STRIP  
		
	    Gdx.gl10.glPopMatrix();
	    Gdx.gl10.glPopMatrix();		
		
		
		
		

		if (GameMicroM.FLAG_DISPLAY_ACTOR_SHAPES) {
			renderer.setProjectionMatrix(e.getCamera().getGameCamera().combined);
			
			Iterator<Fixture> it = wallmSrc.getBody().getFixtureList().iterator();
			Fixture aux = null;
			while (it.hasNext()) {
				aux = it.next();
				
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
		
		if (GameMicroM.FLAG_DISPLAY_ACTOR_TEXTURES) {
			batch.setProjectionMatrix(e.getCamera().getGameCamera().combined);
			batch.begin();
				wallSprite.setPosition(wallmSrc.getBody().getPosition().x-wallmSrc.getBasicShape().getWidth()/2,  wallmSrc.getBody().getPosition().y-wallmSrc.getBasicShape().getHeight()/2);
				wallSprite.setRotation((float)Math.toDegrees(wallmSrc.getBody().getAngle()));
				wallSprite.draw(batch);
			batch.end();		
		}
	}

	Matrix4 prj = new Matrix4();
	Matrix4 vw = new Matrix4();
	Matrix4 mdl = new Matrix4();
	@Override
	public void draw20(ScreenTickEvent e) {
		// Enable da transparência
		Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
	    Gdx.graphics.getGL20().glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	    
		prj.set(e.getCamera().getGameCamera().projection);
		vw.set(e.getCamera().getGameCamera().view);
		mdl.idt().translate(wallmSrc.getBody().getPosition().x, wallmSrc.getBody().getPosition().y, 0.0f);
		
		meshHelper.drawMesh(prj, vw, mdl, wallmSrc.getBasicShape().getFillColor());
		
	}
	
	
	@Override
	public void dispose() {
		meshHelper.dispose();
		super.dispose();
	}
	
}
