package pt.me.microm.view.stuff;

import java.util.Iterator;

import pt.me.microm.GameMicroM;
import pt.me.microm.controller.loop.event.ScreenTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.model.dev.BallModel;
import pt.me.microm.model.stuff.DaBoxModel;
import pt.me.microm.model.stuff.MagnetModel;
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

public class MagnetView extends AbstractView {
	private static final String TAG = MagnetView.class.getSimpleName();
	
	private MagnetModel magnetmSrc;
	
	ShapeRenderer renderer;
	
	Sprite magnetSprite;

	SpriteBatch batch = new SpriteBatch();
	
	public MagnetView(MagnetModel magnetmSrc) {
		super(magnetmSrc);
		this.magnetmSrc = magnetmSrc;
	}
	
	@Override
	public void DelayedInit() {
		renderer = new ShapeRenderer();
		
		magnetSprite = GAME_CONSTANTS.devAtlas.createSprite("txr_wall");

		magnetSprite.setSize(magnetmSrc.getBasicShape().getWidth(), magnetmSrc.getBasicShape().getHeight());
		magnetSprite.setOrigin(magnetmSrc.getBasicShape().getWidth()/2, magnetmSrc.getBasicShape().getHeight()/2);		
		
	}

	
	Vector2 pointA = new Vector2();
	Vector2 pointB = new Vector2();
	@Override
	public void draw(ScreenTickEvent e) {
		
		if (GameMicroM.FLAG_DISPLAY_ACTOR_SHAPES) {
			renderer.setProjectionMatrix(e.getCamera().getGameCamera().combined);
			
			Iterator<Fixture> it = magnetmSrc.getBody().getFixtureList().iterator();
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
				magnetSprite.setPosition(magnetmSrc.getBody().getPosition().x-magnetmSrc.getBasicShape().getWidth()/2,  magnetmSrc.getBody().getPosition().y-magnetmSrc.getBasicShape().getHeight()/2);
				magnetSprite.setRotation((float)Math.toDegrees(magnetmSrc.getBody().getAngle()));
				magnetSprite.draw(batch);
			batch.end();		
		}
	}


}
