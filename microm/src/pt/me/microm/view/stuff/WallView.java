package pt.me.microm.view.stuff;

import java.util.Iterator;

import pt.me.microm.GameMicroM;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.events.ScreenTickEvent;
import pt.me.microm.model.dev.BallModel;
import pt.me.microm.model.stuff.DaBoxModel;
import pt.me.microm.model.stuff.WallModel;
import pt.me.microm.view.AbstractView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;

public class WallView extends AbstractView {
	private static final String TAG = WallView.class.getSimpleName();
	
	private WallModel wallmSrc;
	
	ShapeRenderer renderer;
	
	Sprite wallSprite;

	SpriteBatch batch = new SpriteBatch();
	
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
		
	}

	
	Vector2 pointA = new Vector2();
	Vector2 pointB = new Vector2();
	@Override
	public void draw(ScreenTickEvent e) {
		
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


}
