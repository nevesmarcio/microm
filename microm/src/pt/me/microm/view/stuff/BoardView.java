package pt.me.microm.view.stuff;

import pt.me.microm.GameMicroM;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.events.ScreenTickEvent;
import pt.me.microm.model.stuff.BoardModel;
import pt.me.microm.view.AbstractView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;

public class BoardView extends AbstractView {
	private static final String TAG = BoardView.class.getSimpleName();
	
	private BoardModel boardmSrc;
	
	Sprite boardSprite;

	SpriteBatch batch = new SpriteBatch();	
	
	ShapeRenderer renderer;
	
	public BoardView(BoardModel boardmSrc) {
		super(boardmSrc);
		this.boardmSrc = boardmSrc;
	}

	@Override
	public void DelayedInit() {
		renderer = new ShapeRenderer();
		
		boardSprite = GAME_CONSTANTS.devAtlas.createSprite("txr_full_board");
		
		boardSprite.setSize(boardmSrc.getBasicShape().getWidth(), boardmSrc.getBasicShape().getHeight());
		boardSprite.setOrigin(boardmSrc.getBasicShape().getWidth()/2, boardmSrc.getBasicShape().getHeight()/2);
		
	}
	
	
	private Vector2 pointA = new Vector2();
	private Vector2 pointB = new Vector2();
	@Override
	public void draw(ScreenTickEvent e) {
		
		
		if (GameMicroM.FLAG_DISPLAY_ACTOR_SHAPES) {
			renderer.setProjectionMatrix(e.getCamera().getGameCamera().combined);
			
			Fixture fix = (boardmSrc.getBody().getFixtureList()).get(0);
			ChainShape cs = (ChainShape)fix.getShape();
			
			renderer.begin(ShapeType.Line);
			cs.getVertex(0, pointA); pointA.add(boardmSrc.getBody().getPosition());
			cs.getVertex(1, pointB); pointB.add(boardmSrc.getBody().getPosition());
			renderer.line(pointA.x, pointA.y, pointB.x, pointB.y);
			
			cs.getVertex(1, pointA); pointA.add(boardmSrc.getBody().getPosition());	
			cs.getVertex(2, pointB); pointB.add(boardmSrc.getBody().getPosition());
			renderer.line(pointA.x, pointA.y, pointB.x, pointB.y);
			
			cs.getVertex(2, pointA); pointA.add(boardmSrc.getBody().getPosition());	 
			cs.getVertex(3, pointB); pointB.add(boardmSrc.getBody().getPosition());
			renderer.line(pointA.x, pointA.y, pointB.x, pointB.y);
			
			cs.getVertex(3, pointA); pointA.add(boardmSrc.getBody().getPosition());	 
			cs.getVertex(0, pointB); pointB.add(boardmSrc.getBody().getPosition());
			renderer.line(pointA.x, pointA.y, pointB.x, pointB.y);		
			
			renderer.end();
		}
		
		if (GameMicroM.FLAG_DISPLAY_ACTOR_TEXTURES) {
			batch.setProjectionMatrix(e.getCamera().getGameCamera().combined);
			batch.begin();
				boardSprite.setPosition(boardmSrc.getBody().getPosition().x-boardmSrc.getBasicShape().getWidth()/2,  boardmSrc.getBody().getPosition().y-boardmSrc.getBasicShape().getHeight()/2);
				boardSprite.setRotation((float)Math.toDegrees(boardmSrc.getBody().getAngle()));
				boardSprite.draw(batch);
			batch.end();				
		}
		
	}

}
