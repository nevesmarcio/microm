package pt.me.microm.view.ui;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.events.ScreenTickEvent;
import pt.me.microm.model.ui.UIModel;
import pt.me.microm.view.AbstractView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Logger;

public class UIView  extends AbstractView {
	private static final String TAG = UIView.class.getSimpleName();
	private static Logger logger = new Logger(TAG);
	
//	private BitmapFont font1;
//	private Vector2 textPosition1 = new Vector2(0, 80);
	
	private BitmapFont font1;
	private Vector2 textPosition1 = new Vector2(0, 60);
	
	private BitmapFont font2;
	private Vector2 textPosition2 = new Vector2(0, 40);	
	
	private BitmapFont font3;
	private Vector2 textPosition3 = new Vector2(0, 20);
	
	private boolean direction = true;
	
	private float fps;
	
	private UIModel uiSrc;
	
	SpriteBatch batch;
	ShapeRenderer renderer;
	
	public UIView(UIModel uiSrc) {  
		super(uiSrc);
		this.uiSrc = uiSrc;
		
//		font1 = new BitmapFont();
//		font1.setColor(Color.RED);	
		
		font1 = new BitmapFont();
		font1.setColor(Color.RED);
		
		font2 = new BitmapFont();
		font2.setColor(Color.BLUE);
		
		font3 = new BitmapFont();
		font3.setColor(Color.WHITE);
				
		batch = new SpriteBatch();
		renderer = new ShapeRenderer();
	}
	
	
	private Vector3 intersection_point = new Vector3();
	private Plane intersect_plane = new Plane(new Vector3(0.0f,0.0f,1.0f), 0.0f);
	private Ray rr;
	@Override
	public void draw(ScreenTickEvent e) {
		
		/* RED : intersecção calculada pelo RAY */
		for (int i=0; i<GAME_CONSTANTS.MAX_TOUCH_POINTS; i++) {		

			if (uiSrc.getOriginalTestPoint()[i]!=null) {
				rr = e.getCamera().getGameCamera().getPickRay(uiSrc.getOriginalTestPoint()[i].x, uiSrc.getOriginalTestPoint()[i].y);
				if (logger.getLevel() == logger.DEBUG) logger.debug("PickingTest - ray: " + rr);
				
				Intersector.intersectRayPlane(rr, intersect_plane, intersection_point);
				if (logger.getLevel() == logger.DEBUG) logger.debug("Intersect: " + intersection_point);
	
				renderer.setProjectionMatrix(e.getCamera().getGameCamera().combined);
				
				renderer.begin(ShapeType.Circle);
					renderer.identity();
					renderer.translate(intersection_point.x, intersection_point.y, intersection_point.z);
					renderer.setColor(Color.RED);
					renderer.circle(0.0f, 0.0f, 0.300f, 20);
					renderer.circle(0.0f, 0.0f, 0.280f, 20);
				renderer.end();
			}		
		}		
	
		
		/* GREEN : renderização do local onde o rato está a apontar - 2D */
		OrthographicCamera c;
		renderer.setProjectionMatrix((c = e.getCamera().getUiCamera()).combined);
		
        renderer.begin(ShapeType.Circle);
		for (int i=0; i<GAME_CONSTANTS.MAX_TOUCH_POINTS; i++) {
			if (uiSrc.getTestPoint()[i]!=null){
					renderer.identity();

					Vector3 vec = uiSrc.getOriginalTestPoint()[i].cpy();
					c.unproject(vec);
					
//					Vector3 vec = uiSrc.getTestPoint()[i].cpy();
//					Gdx.app.log(TAG, vec.toString());
//					vec.z = 0.0f;
//					e.getCamera().near = 10f;
//					e.getCamera().update();
//					e.getCamera().unproject(vec);
//					Gdx.app.log(TAG, vec.toString());
//					vec.z = 0.0f; // força a renderização ao plano certo.
//					
//					e.getCamera().near = 0.10f;
//					e.getCamera().update();
					
//					renderer.translate(uiSrc.getTestPoint()[i].x, uiSrc.getTestPoint()[i].y, 0.0f);
					renderer.translate(vec.x, vec.y, vec.z);
					renderer.setColor(Color.GREEN);
					renderer.circle(0.0f, 0.0f, 30.0f, 20);
					renderer.circle(0.0f, 0.0f, 28.0f, 20);
				}
		}
		renderer.end();				

		
//		/* BLUE: uso do unproject em perspectiva - obriga a mudar a câmara - dont!! */
//      renderer.begin(ShapeType.Circle);
//		for (int i=0; i<GAME_CONSTANTS.MAX_TOUCH_POINTS; i++) {
//			if (uiSrc.getTestPoint()[i]!=null){
//					renderer.identity();
//
//					
//					Vector3 vec = uiSrc.getOriginalTestPoint()[i].cpy();
//					//Gdx.app.log(TAG, vec.toString());
//					vec.z = 0.0f;
//					e.getCamera().near = e.getCamera().position.len();//10f;
//					e.getCamera().update();
//					e.getCamera().unproject(vec);
//					//Gdx.app.log(TAG, vec.toString());
//					vec.z = 0.0f; // força a renderização ao plano certo.
//					
//					e.getCamera().near = 0.10f;
//					e.getCamera().update();
//					
//					renderer.translate(uiSrc.getTestPoint()[i].x, uiSrc.getTestPoint()[i].y, 0.0f);
//					renderer.setColor(Color.BLUE);			
//					renderer.circle(0.0f, 0.0f, 0.150f, 20);
//					renderer.circle(0.0f, 0.0f, 0.140f, 20);
//				}
//		}
//		renderer.end();			
//			
		
		/* renderização dos status fps + ups */
		long elapsedNanoTime = e.getElapsedNanoTime();
		
//		batch.setProjectionMatrix(e.getCamera().combined.cpy().scl(1f/Gdx.graphics.getWidth()*GAME_CONSTANTS.MODEL_SCREEN_WIDTH_CAPACITY)); //.cpy().scl(1f/Gdx.graphics.getWidth()*GAME_CONSTANTS.MODEL_SCREEN_WIDTH_CAPACITY)
//		batch.getProjectionMatrix().setToOrtho2D(0.0f, 0.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.setProjectionMatrix(e.getCamera().getUiCamera().combined);
				
		fps = (float) (1000.0f / (elapsedNanoTime / (float)GAME_CONSTANTS.ONE_MILISECOND_TO_NANO));
		
		batch.begin();
			font1.draw(batch, "fps: " + fps, (int)textPosition1.x, (int)textPosition1.y);
			
			font2.draw(batch, "gameupdate (ups): " + uiSrc.getUps(), (int)textPosition2.x, (int)textPosition2.y);
			
			// só para demonstrar que a renderização está a ocorrer ao ritmo dos fps's.
			if (font3.getScaleX()>1.5f || font3.getScaleX()<0.5f)
				direction =!direction;
			font3.setScale(font3.getScaleX()+(direction?0.1f:-0.1f));
			font3.draw(batch, "X--X", (int)textPosition3.x, (int)textPosition3.y);

		batch.end();
		
		
		
		
	}

}
