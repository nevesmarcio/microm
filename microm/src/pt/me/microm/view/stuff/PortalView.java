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
	private static final Logger logger = new Logger(TAG);
	
	private PortalModel portalmSrc;
	ShapeRenderer renderer;
	
	Texture texture;
	Sprite sprite;

	SpriteBatch batch;
	Mesh mesh;
	SubMesh sm;
	
	float[] vertexes;
	short[] indexes;
	
	public PortalView(PortalModel portalmSrc) {
		super(portalmSrc);
		this.portalmSrc = portalmSrc;
	}

	@Override
	public void DelayedInit() {
		renderer = new ShapeRenderer();
		batch = new SpriteBatch();
		
		final AtlasRegion r = GAME_CONSTANTS.devAtlas.findRegion("square2");
		// Fixme: avaliar 
		texture = r.getTexture(); // isto devolve sempre a textura inteira e não o quadradinho que pretendo... isto para ficar resolvido tem que se usar o UV MAP!!
		
//		ScreenTickManager.PostRunnable(new Runnable() { //Garante que as instrucções OPENGL correm na thread do GUI, onde existe um contexto OPENGL
//
//			@Override
//			public void run() {
				// texture stuff
				texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
				texture.setWrap(TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
				
				Gdx.graphics.getGL10().glEnable(GL10.GL_TEXTURE_2D);
				Gdx.graphics.getGL10().glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE); // GL10.GL_REPLACE ou GL10.GL_BLEND n funca bem... (ainda n percebo nada disto de opengl)
			    Gdx.graphics.getGL10().glEnable(GL10.GL_BLEND);
			    Gdx.graphics.getGL10().glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

				
			    // mesh & uvmap stuff
				vertexes = new float[] { 
//						-0.25f, 0.5f, 0, /*Color.toFloatBits(0, 0, 255, 255),*/ 0.5f, 0, 
//						-0.5f, -0.5f, 0, /*Color.toFloatBits(255, 0, 0, 255),*/ 0, 2,
//		                0.5f, -0.5f, 0, /*Color.toFloatBits(0, 255, 0, 255),*/ 2, 2,
//		                0.25f, 0.5f, 0, /*Color.toFloatBits(0, 0, 255, 255),*/ 1.5f, 0,

//						-0.25f, 0.5f, 0, /*Color.toFloatBits(0, 0, 255, 255),*/ 0.5f, 0, 
//						0.25f, 0.5f, 0, /*Color.toFloatBits(0, 0, 255, 255),*/ 1.5f, 0,
//						0.5f, -0.5f, 0, /*Color.toFloatBits(0, 255, 0, 255),*/ 2, 2,
//						-0.5f, -0.5f, 0, /*Color.toFloatBits(255, 0, 0, 255),*/ 0, 2,

//						-0.25f, 0.5f, 0, /*Color.toFloatBits(0, 0, 255, 255),*/ -0.5f, -1.0f, 
//						0.25f, 0.5f, 0, /*Color.toFloatBits(0, 0, 255, 255),*/ 0.5f, -1.0f,
//						0.5f, -0.5f, 0, /*Color.toFloatBits(0, 255, 0, 255),*/ 1.0f, 1.0f,
//						-0.5f, -0.5f, 0, /*Color.toFloatBits(255, 0, 0, 255),*/ -1.0f, 1.0f,

//						-0.25f, 0.5f, 0, /*Color.toFloatBits(0, 0, 255, 255),*/ 0.0f, -0.5f, 
//						0.25f, 0.5f, 0, /*Color.toFloatBits(0, 0, 255, 255),*/ 1.0f, -0.5f,
//						0.5f, -0.5f, 0, /*Color.toFloatBits(0, 255, 0, 255),*/ 1.5f, 1.5f,
//						-0.5f, -0.5f, 0, /*Color.toFloatBits(255, 0, 0, 255),*/ -0.5f, 1.5f,

						-0.25f, 0.5f, 0, /*Color.toFloatBits(0, 0, 255, 255),*/ 0.0f, 0.0f, 
						0.25f, 0.5f, 0, /*Color.toFloatBits(0, 0, 255, 255),*/ 0.02f, 0.0f,
						0.5f, -0.5f, 0, /*Color.toFloatBits(0, 255, 0, 255),*/ 0.03f, 0.04f,
						-0.5f, -0.5f, 0, /*Color.toFloatBits(255, 0, 0, 255),*/ -0.01f, 0.04f,				

//						-0.25f, 0.5f, 0, /*Color.toFloatBits(0, 0, 255, 255),*/ 0.0f, 0.0f, 
//						0.25f, 0.5f, 0, /*Color.toFloatBits(0, 0, 255, 255),*/ 0.2f, 0.0f,
//						0.5f, -0.5f, 0, /*Color.toFloatBits(0, 255, 0, 255),*/ 0.3f, 0.4f,
//						-0.5f, -0.5f, 0, /*Color.toFloatBits(255, 0, 0, 255),*/ -0.1f, 0.4f,				
						
		                };
				indexes = new short[] { 0, 1, 2, 3 };			    
			    
				Iterator<Fixture> it1 = portalmSrc.getBody().getFixtureList().iterator();
				while (it1.hasNext()) {
					Fixture aux = it1.next();

					ChainShape cs = (ChainShape)aux.getShape();
					int vCnt = cs.getVertexCount();
					
					float[] uvmap = new float[vCnt*2];
					Vector2 temp = new Vector2();
					for (int i = 0; i<vCnt; i++) {
						cs.getVertex(i, temp);	
						logger.info(">>" + temp.toString());
						uvmap[2*i] = temp.x;
						uvmap[2*i+1] = temp.y;
					}

					vertexes = new float[vCnt*5];
					
					indexes = new short[vCnt];
					for (int i = 0; i < vCnt; i++) {
						cs.getVertex(i, temp); //pointA.add(portalmSrc.getPortalBody().getPosition());
							vertexes[i*5+0] = temp.x;
							vertexes[i*5+1] = temp.y;
							vertexes[i*5+2] = 0;
							switch (i) {
							case 0:
								vertexes[i*5+3] = r.getU(); //0,0 ; 1,0; 1,1; 0,1
								vertexes[i*5+4] = r.getV();								
								break;
							case 1:
								vertexes[i*5+3] = r.getU2(); //0,0 ; 1,0; 1,1; 0,1
								vertexes[i*5+4] = r.getV();
								break;
							case 2:
								vertexes[i*5+3] = r.getU2(); //0,0 ; 1,0; 1,1; 0,1
								vertexes[i*5+4] = r.getV2();
								break;
							case 3:
								vertexes[i*5+3] = r.getU(); //0,0 ; 1,0; 1,1; 0,1
								vertexes[i*5+4] = r.getV2();
								break;
							default:
								break;
							}

							indexes[i] = (short)i;
						}
				}	        
			        
			        
			        
		        mesh = new Mesh(true, 4, 4, 
		                new VertexAttribute(Usage.Position, 3, "a_position"),
		                /*new VertexAttribute(Usage.ColorPacked, 4, "a_color"),*/
		                new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoords"));
	
		        mesh.setVertices(vertexes);
		        mesh.setIndices(indexes);

			    
//			}
//		});	
		

		
	}
	
	
	private Vector2 pointA = new Vector2();
	private Vector2 pointB = new Vector2();
	@Override
	public void draw(ScreenTickEvent e) {
		//MESH STYLE

		//FIXME:: o que é que está a desligar esta opção? (e que me obriga a repetir isto a cada ciclo?)
		Gdx.graphics.getGL10().glEnable(GL10.GL_TEXTURE_2D);

		
		Gdx.gl10.glPushMatrix();
		Gdx.gl10.glMatrixMode(GL10.GL_PROJECTION);
//		Gdx.gl10.glLoadMatrixf(e.getCamera().getGameCamera().projection.cpy().translate(0.0f, 0.0f, 0.0f).val, 0); // poupa umas alocaçoes e memória
		Gdx.gl10.glLoadMatrixf(e.getCamera().getGameCamera().projection.translate(0.0f, 0.0f, 0.0f).val, 0);
		
		Gdx.gl10.glPushMatrix();
		Gdx.gl10.glMatrixMode(GL10.GL_MODELVIEW);
		Gdx.gl10.glLoadMatrixf(e.getCamera().getGameCamera().view.cpy().translate(portalmSrc.getBody().getPosition().x, portalmSrc.getBody().getPosition().y, 0.0f).val, 0);
		
	    texture.bind();
	    //mesh.render(GL10.GL_TRIANGLES, 0, 3);
	    if (mesh != null)
	    	mesh.render(GL10.GL_TRIANGLE_FAN); //GL10.GL_TRIANGLE_FAN //GL10.GL_TRIANGLES //GL10.GL_TRIANGLE_STRIP  
		
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

		// BATCH STYLE 			-- desenha a texturazinha prequenina centrada no portal
//		batch.setProjectionMatrix(e.getCamera().getGameCamera().combined);
//		
//		batch.begin();
//			batch.draw(texture, portalmSrc.getPosition().x, portalmSrc.getPosition().y, (float)1/(float)5, (float)1/(float)5);
//		batch.end();

		
	}

}
