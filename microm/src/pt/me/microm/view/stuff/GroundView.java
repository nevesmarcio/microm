package pt.me.microm.view.stuff;

import pt.me.microm.GameMicroM;
import pt.me.microm.controller.loop.event.ScreenTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.model.stuff.GroundModel;
import pt.me.microm.view.AbstractView;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;

public class GroundView extends AbstractView {
	private static final String TAG = GroundView.class.getSimpleName();
	
	private GroundModel groundmSrc;
	
	Sprite groundSprite;
	SpriteBatch batch = new SpriteBatch();
	
	ShapeRenderer renderer;
	
	public GroundView(GroundModel groundmSrc) {
		super(groundmSrc);
		this.groundmSrc = groundmSrc;
	}

	@Override
	public void DelayedInit() {
		renderer = new ShapeRenderer();
		
		groundSprite = GAME_CONSTANTS.devAtlas.createSprite("txr_ground");		

		groundSprite.setSize(groundmSrc.getBasicShape().getWidth(), groundmSrc.getBasicShape().getHeight());
		groundSprite.setOrigin(groundmSrc.getBasicShape().getWidth()/2, groundmSrc.getBasicShape().getHeight()/2);		
		
	}
	
	private Vector2 pointA = new Vector2();
	private Vector2 pointB = new Vector2();
	@Override
	public void draw(ScreenTickEvent e) {
		
		if (GameMicroM.FLAG_DISPLAY_ACTOR_SHAPES) {
			renderer.setProjectionMatrix(e.getCamera().getGameCamera().combined);
			
			Fixture fix = (groundmSrc.getBody().getFixtureList()).get(0);
			ChainShape cs = (ChainShape)fix.getShape();
			
			renderer.begin(ShapeType.Line);
				int vCnt = cs.getVertexCount();
				for (int i = 0; i < vCnt; i++) {
					cs.getVertex(i, pointA); pointA.add(groundmSrc.getBody().getPosition());
					cs.getVertex(i==vCnt-1 ? 0 : i + 1, pointB); pointB.add(groundmSrc.getBody().getPosition());
					renderer.line(pointA.x, pointA.y, pointB.x, pointB.y);
				}
			renderer.end();
		}
		
		if (GameMicroM.FLAG_DISPLAY_ACTOR_TEXTURES) {		
			batch.setProjectionMatrix(e.getCamera().getGameCamera().combined);
			batch.begin();
				groundSprite.setPosition(groundmSrc.getBody().getPosition().x-groundmSrc.getBasicShape().getWidth()/2,  groundmSrc.getBody().getPosition().y-groundmSrc.getBasicShape().getHeight()/2);
				groundSprite.setRotation((float)Math.toDegrees(groundmSrc.getBody().getAngle()));
				groundSprite.draw(batch);
			batch.end();		
		}
		
	}

	@Override
	public void draw20(ScreenTickEvent e) {
		
	}	
	
}
