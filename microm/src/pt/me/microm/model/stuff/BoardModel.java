package pt.me.microm.model.stuff;

import java.util.List;

import pt.me.microm.infrastructure.events.GameTickEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.PointerToFunction;
import pt.me.microm.model.AbstractModel.EventType;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.model.events.SimpleEvent;
import pt.me.microm.tools.levelloader.BasicShape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class BoardModel extends AbstractModel {
	private static final String TAG = BoardModel.class.getSimpleName();
	
	private Vector2 boardPosition; // posição do tabuleiro no espaço
	
	private Vector2 playzoneVertex[]; // Pontos que definem a fronteira do tabuleiro
	
	private BodyDef playzoneBodyDef = new BodyDef();
	private ChainShape playzoneShape; // Fronteira do tabuleiro
	private Body playzoneBody;
	
	
	private BoardModel(final WorldModel wm, final BasicShape board, final List<Vector2> lst) {
		wm.wmManager.add(new PointerToFunction() {
			
			@Override
			public void handler() {
				
				//deslocamento do centroid
				for (Vector2 v : board.getPoints()) {
					v.sub(board.getCentroid());
				}
				
				playzoneVertex = lst.toArray(new Vector2[]{});
				
				playzoneShape = new ChainShape();
				playzoneShape.createLoop(playzoneVertex);
				
				playzoneBodyDef.position.set(board.getCentroid()); // posição inicial do tabuleiro
				playzoneBodyDef.type = BodyType.StaticBody;
				
				setPlayzoneBody(wm.getPhysicsWorld().createBody(playzoneBodyDef));

				FixtureDef fixDef = new FixtureDef();
				fixDef.shape = playzoneShape;
				fixDef.density = 1.0f;
				fixDef.friction = 0.0f;
				fixDef.restitution = 0.0f;		
				playzoneBody.createFixture(fixDef);
				getPlayzoneBody().createFixture(fixDef);
					
				getPlayzoneBody().setUserData(BoardModel.this); // relacionar com o modelo
				
				
				// Sinaliza os subscritores de que a construção do modelo terminou.
				BoardModel.this.dispatchEvent(new SimpleEvent(EventType.ON_MODEL_INSTANTIATED));		

				
			}
		});
		
	}
	
	public static BoardModel getNewInstance(WorldModel wm, BasicShape board, List<Vector2> pts){
		return new BoardModel(wm, board, pts);
	}

	
	@Override
	public void handleGameTick(GameTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
		if (getPlayzoneBody() != null)
		Gdx.app.debug("[Physics-room]", 		  "Pos.x:" + String.format("%.2f", getPlayzoneBody().getPosition().x)
				+ " Pos.y:" + String.format("%.2f", getPlayzoneBody().getPosition().y) 
				+ " Angle:" + String.format("%.2f", getPlayzoneBody().getAngle())
				+ " Mass:" + getPlayzoneBody().getMass()
				+ " Type:" + getPlayzoneBody().getType());			
	}

	
	/* Getters - Setters do tabuleiro */
	// Posição do tabuleiro
	public Vector2 getBoardPosition() {
		return boardPosition;
	}
	public void setBoardPosition(Vector2 boardPosition) {
		this.boardPosition = boardPosition;
	}

	public Body getPlayzoneBody() {
		return playzoneBody;
	}
	public void setPlayzoneBody(Body playzoneBody) {
		this.playzoneBody = playzoneBody;
	}

}
