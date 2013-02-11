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

public class UIView  extends AbstractView {
	private static final String TAG = UIView.class.getSimpleName();
	
	private BitmapFont font1;
	private Vector2 textPosition1 = new Vector2(0, 80);
	
	private UIModel uiSrc;
	
	SpriteBatch batch;
	ShapeRenderer renderer;
	
	public UIView(UIModel uiSrc) {  
		super(uiSrc);
		this.uiSrc = uiSrc;
		
		font1 = new BitmapFont();
		font1.setColor(Color.RED);	
		
		batch = new SpriteBatch();
		renderer = new ShapeRenderer();
	}
	
	
	private Vector3 intersection_point = new Vector3();
	private Plane intersect_plane = new Plane(new Vector3(0.0f,0.0f,1.0f), 0.0f);
	private Ray rr;
	@Override
	public void draw(ScreenTickEvent e) {
		
		for (int i=0; i<GAME_CONSTANTS.MAX_TOUCH_POINTS; i++) {		

			if (uiSrc.getOriginalTestPoint()[i]!=null) {
				rr = e.getCamera().getGameCamera().getPickRay(uiSrc.getOriginalTestPoint()[i].x, uiSrc.getOriginalTestPoint()[i].y);
				Gdx.app.debug(TAG, "PickingTest - ray: " + rr);
				
				Intersector.intersectRayPlane(rr, intersect_plane, intersection_point);
				Gdx.app.debug(TAG, "Intersect: " + intersection_point);
	
	
				/* RED : intersecção calculada pelo RAY */ 
				renderer.setProjectionMatrix(e.getCamera().getGameCamera().combined);
		//        renderer.setProjectionMatrix(e.getCamera().projection.cpy().setToOrtho2D(0.0f, 0.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
				
				renderer.begin(ShapeType.Circle);
					renderer.identity();
					renderer.translate(intersection_point.x, intersection_point.y, intersection_point.z);
					renderer.setColor(Color.RED);
					renderer.circle(0.0f, 0.0f, 0.300f, 20);
					renderer.circle(0.0f, 0.0f, 0.280f, 20);
				renderer.end();
			}		
		}		
		/* BLUE: uso do unproject em perspectiva - obriga a mudar a câmara - dont!! */
//        renderer.begin(ShapeType.Circle);
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

	}

}
