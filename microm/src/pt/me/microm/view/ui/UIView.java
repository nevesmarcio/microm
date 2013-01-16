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
	
	@Override
	public void draw(ScreenTickEvent e) {
		
		Vector3 v = new Vector3();
		if (uiSrc.getOriginalTestPoint()[0]!=null) {
			Ray rr = e.getCamera().getPickRay(uiSrc.getOriginalTestPoint()[0].x, uiSrc.getOriginalTestPoint()[0].y);
			Gdx.app.log(TAG, "PickingTest - ray: " + rr);
			
			Intersector.intersectRayPlane(rr, new Plane(new Vector3(0f,0f,1f), 0.0f), v);
			Gdx.app.log(TAG, "Intersect: " + v);
		}

		
		renderer.setProjectionMatrix(e.getCamera().combined);
//        renderer.setProjectionMatrix(e.getCamera().projection.cpy().setToOrtho2D(0.0f, 0.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		
		renderer.begin(ShapeType.Circle);
		renderer.identity();
		renderer.translate(v.x, v.y, v.z);
		renderer.setColor(Color.RED);
		renderer.circle(0.0f, 0.0f, 0.300f, 20);
		renderer.circle(0.0f, 0.0f, 0.280f, 20);
		renderer.end();
		
        renderer.begin(ShapeType.Circle);
		for (int i=0; i<GAME_CONSTANTS.MAX_TOUCH_POINTS; i++) {
			if (uiSrc.getTestPoint()[i]!=null){
					renderer.identity();

					
					Vector3 vec = uiSrc.getOriginalTestPoint()[i].cpy();
					//Gdx.app.log(TAG, vec.toString());
					vec.z = 0.0f;
					e.getCamera().near = e.getCamera().position.len();//10f;
					e.getCamera().update();
					e.getCamera().unproject(vec);
					//Gdx.app.log(TAG, vec.toString());
					vec.z = 0.0f; // força a renderização ao plano certo.
					
					e.getCamera().near = 0.10f;
					e.getCamera().update();
					
					renderer.translate(uiSrc.getTestPoint()[i].x, uiSrc.getTestPoint()[i].y, 0.0f);
					renderer.setColor(Color.BLUE);			
					renderer.circle(0.0f, 0.0f, 0.150f, 20);
					renderer.circle(0.0f, 0.0f, 0.140f, 20);
				}
		}
		renderer.end();			
		
		
		
		
		OrthographicCamera c = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		//renderer.setProjectionMatrix(e.getCamera().combined);
		renderer.setProjectionMatrix(c.combined);
//        renderer.setProjectionMatrix(e.getCamera().projection.cpy().setToOrtho2D(0.0f, 0.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		
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
