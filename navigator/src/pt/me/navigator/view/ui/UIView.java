package pt.me.navigator.view.ui;

import pt.me.navigator.infrastructure.GAME_CONSTANTS;
import pt.me.navigator.infrastructure.events.ScreenTickEvent;
import pt.me.navigator.model.ui.UIModel;
import pt.me.navigator.view.AbstractView;

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
		
			

	}

}
