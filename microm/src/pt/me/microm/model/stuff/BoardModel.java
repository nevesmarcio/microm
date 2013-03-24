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
import com.badlogic.gdx.utils.Logger;

public class BoardModel extends AbstractModel {
	private static final String TAG = BoardModel.class.getSimpleName();
	private static final Logger logger = new Logger(TAG);
	
	private Vector2 boardPosition; // posição do tabuleiro no espaço
	
	private Vector2 playzoneVertex[]; // Pontos que definem a fronteira do tabuleiro
	
	private BodyDef playzoneBodyDef = new BodyDef();
	private ChainShape playzoneShape; // Fronteira do tabuleiro
	private Body playzoneBody;
	
	private WorldModel wm;
	private BasicShape board;
	
	private BoardModel(final WorldModel wm, final BasicShape board) {
		this.wm = wm;
		this.board = board;
		
		wm.wmManager.add(new PointerToFunction() {
			
			@Override
			public Object handler(Object ... a) {
				
				playzoneVertex = board.getPointsArray();
				
				playzoneShape = new ChainShape();
				playzoneShape.createLoop(playzoneVertex);
				
				playzoneBodyDef.position.set(board.getCentroid()); // posição inicial do tabuleiro
				playzoneBodyDef.type = BodyType.StaticBody;
				
				playzoneBody = wm.getPhysicsWorld().createBody(playzoneBodyDef);

				FixtureDef fixDef = new FixtureDef();
				fixDef.shape = playzoneShape;
				fixDef.density = 1.0f;
				fixDef.friction = 0.0f;
				fixDef.restitution = 0.0f;		
				playzoneBody.createFixture(fixDef);
				getBody().createFixture(fixDef);
					
				getBody().setUserData(BoardModel.this); // relacionar com o modelo
				
				
				// Sinaliza os subscritores de que a construção do modelo terminou.
				BoardModel.this.dispatchEvent(new SimpleEvent(EventType.ON_MODEL_INSTANTIATED));		

				return null;
			}
		});
		
	}
	
	public static BoardModel getNewInstance(WorldModel wm, BasicShape board){
		return new BoardModel(wm, board);
	}

	
	@Override
	public void handleGameTick(GameTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
		if (getBody() != null)
			if (logger.getLevel() == logger.DEBUG)
				logger.debug("[Physics-room]: Pos.x:" + String.format("%.2f", getBody().getPosition().x)
					+ " Pos.y:" + String.format("%.2f", getBody().getPosition().y) 
					+ " Angle:" + String.format("%.2f", getBody().getAngle())
					+ " Mass:" + getBody().getMass()
					+ " Type:" + getBody().getType());			
	}

	
	/* Getters - Setters do tabuleiro */
	// Posição do tabuleiro
//	@Override
	public Vector2 getPosition() {
		return boardPosition;
	}
//	@Override
	public Body getBody() {
		return playzoneBody;
	}
	
	public BasicShape getBasicShape() {
		return board;
	}

}
