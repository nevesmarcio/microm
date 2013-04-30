package pt.me.microm.model.stuff;

import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.ICommand;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.IActorBody;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.tools.levelloader.BasicShape;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Logger;

public class BoardModel extends AbstractModel implements IActorBody {
	private static final String TAG = BoardModel.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private Vector2 playzoneVertex[]; // Pontos que definem a fronteira do tabuleiro
	
	private BodyDef playzoneBodyDef = new BodyDef();
	private ChainShape playzoneShape; // Fronteira do tabuleiro
	private Body playzoneBody;
	
	private WorldModel wm;
	private BasicShape board;
	
	private BoardModel(final WorldModel wm, final BasicShape board, final String board_name) {
		this.wm = wm;
		this.board = board;
		setName(board_name);
		
		wm.wmManager.add(new ICommand() {
			
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
				BoardModel.this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));		

				return null;
			}
		});
		
	}
	
	public static BoardModel getNewInstance(WorldModel wm, BasicShape board, String board_name){
		return new BoardModel(wm, board, board_name);
	}

	
	@Override
	public void handleGameTick(GameTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
		if (playzoneBody != null)
			if (logger.getLevel() >= Logger.DEBUG)
				logger.debug("[Physics-room]: Pos.x:" + String.format("%.2f", playzoneBody.getPosition().x)
					+ " Pos.y:" + String.format("%.2f", playzoneBody.getPosition().y) 
					+ " Angle:" + String.format("%.2f", playzoneBody.getAngle())
					+ " Mass:" + playzoneBody.getMass()
					+ " Type:" + playzoneBody.getType());			
	}

	
	// BodyInterface implementation
	@Override
	public String getName() {
		return super.getName();
	}
	@Override
	public BasicShape getBasicShape() {
		return board;
	}
	@Override
	public Vector2 getPosition() {
		return playzoneBody.getPosition();
	}
	@Override
	public float getAngle() {
		return playzoneBody.getAngle(); 
	}
	@Override
	public Body getBody() {
		return playzoneBody;
	}
	

}
