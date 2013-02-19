package pt.me.microm.view.stuff;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.events.ScreenTickEvent;
import pt.me.microm.model.stuff.BoardModel;
import pt.me.microm.view.AbstractView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;

public class BoardView extends AbstractView {
	private static final String TAG = BoardView.class.getSimpleName();
	
	private BoardModel boardmSrc;
	Texture texture = GAME_CONSTANTS.TEXTURE_DROID;
	ShapeRenderer renderer;
	
	public BoardView(BoardModel boardmSrc) {
		super(boardmSrc);
		this.boardmSrc = boardmSrc;
		
		renderer = new ShapeRenderer();
	}
	
	
	private Vector2 pointA = new Vector2();
	private Vector2 pointB = new Vector2();
	@Override
	public void draw(ScreenTickEvent e) {
		
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

}
