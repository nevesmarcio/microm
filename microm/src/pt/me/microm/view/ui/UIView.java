package pt.me.microm.view.ui;

import pt.me.microm.GameMicroM;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.events.ScreenTickEvent;
import pt.me.microm.model.ui.UIModel;
import pt.me.microm.view.AbstractView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
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
	
	private BitmapFont font;
	private BitmapFont pulsingFont;
	private BitmapFont tweenFont;
	
	private Vector2 textPosition1 = new Vector2(0, 60);
	private Vector2 textPosition2 = new Vector2(0, 40);	
	private Vector2 textPosition3 = new Vector2(0, 20);
	
	private boolean direction = true;
	
	private float fps;
	
	private UIModel uiSrc;
	
	SpriteBatch batch;
	ShapeRenderer renderer;
	
	public UIView(UIModel uiSrc) {  
		super(uiSrc, 2); // FIXME: arranjar umas constantes para definir o zOrder
		this.uiSrc = uiSrc;
	}
	
	@Override
	public void DelayedInit() {
		font = new BitmapFont();
		pulsingFont = new BitmapFont();
		tweenFont = new BitmapFont();
		
		batch = new SpriteBatch();
		renderer = new ShapeRenderer();
	}
	
	private Vector3 intersection_point = new Vector3();
	private Plane intersect_plane = new Plane(new Vector3(0.0f,0.0f,1.0f), 0.0f);
	private Ray rr;
	private String xx = "X--X";
	@Override
	public void draw(ScreenTickEvent e) {
		
		/* RED : intersecção calculada pelo RAY */
		for (int i=0; i<GAME_CONSTANTS.MAX_TOUCH_POINTS; i++) {		

			if (uiSrc.getOriginalTestPoint()[i]!=null) {
				rr = e.getCamera().getGameCamera().getPickRay(uiSrc.getOriginalTestPoint()[i].x, uiSrc.getOriginalTestPoint()[i].y);
				if (logger.getLevel() == Logger.DEBUG) logger.debug("PickingTest - ray: " + rr);
				
				Intersector.intersectRayPlane(rr, intersect_plane, intersection_point);
				if (logger.getLevel() == Logger.DEBUG) logger.debug("Intersect: " + intersection_point);
	
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
		
		if (GameMicroM.FLAG_DEV_ELEMENTS) {
			/* renderização dos status fps + ups */
			long elapsedNanoTime = e.getElapsedNanoTime();
			
//			batch.setProjectionMatrix(e.getCamera().combined.cpy().scl(1f/Gdx.graphics.getWidth()*GAME_CONSTANTS.MODEL_SCREEN_WIDTH_CAPACITY)); //.cpy().scl(1f/Gdx.graphics.getWidth()*GAME_CONSTANTS.MODEL_SCREEN_WIDTH_CAPACITY)
//			batch.getProjectionMatrix().setToOrtho2D(0.0f, 0.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			batch.setProjectionMatrix(e.getCamera().getUiCamera().combined);
					
			fps = (float) (1000.0f / (elapsedNanoTime / (float)GAME_CONSTANTS.ONE_MILISECOND_TO_NANO));
			
			batch.begin();
				font.setColor(Color.RED);	
				font.draw(batch, "fps: " + fps, (int)textPosition1.x - e.getCamera().getUiCamera().viewportWidth/2, (int)textPosition1.y - e.getCamera().getUiCamera().viewportHeight/2);
				
				font.setColor(Color.BLUE);
				font.draw(batch, "gameupdate (ups): " + uiSrc.getUps(), (int)textPosition2.x - e.getCamera().getUiCamera().viewportWidth/2, (int)textPosition2.y - e.getCamera().getUiCamera().viewportHeight/2);
				
				// só para demonstrar que a renderização está a ocorrer ao ritmo dos fps's.
				pulsingFont.setColor(Color.WHITE);
				if (pulsingFont.getScaleX()>1.5f || pulsingFont.getScaleX()<0.5f)
					direction =!direction;
				pulsingFont.setScale(pulsingFont.getScaleX()+(direction?0.1f:-0.1f));
				pulsingFont.draw(batch, xx, (int)textPosition3.x - e.getCamera().getUiCamera().viewportWidth/2, (int)textPosition3.y - e.getCamera().getUiCamera().viewportHeight/2);
				
			batch.end();
		}
		
		/* renderização das flashmessages */
		batch.begin();

			Camera ca;
			for (UIModel.FlashMessage fm : uiSrc.afm) {
				
				batch.setProjectionMatrix((ca = e.getCamera().getUiCamera()).combined);
				Vector3 blah = new Vector3(fm.position.x, fm.position.y, 0.0f);
				ca.unproject(blah);

				tweenFont.setScale(fm.scale);
				tweenFont.draw(batch, fm.dataSource.get().toString(), blah.x, blah.y);
			}
		
		batch.end();
		
	}

}
