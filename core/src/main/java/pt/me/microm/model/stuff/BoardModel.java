package pt.me.microm.model.stuff;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.IActorBody;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.tools.levelloader.BasicShape;


public class BoardModel extends AbstractModel implements IActorBody {
	private static final String TAG = BoardModel.class.getSimpleName();
	private static final Logger logger = LoggerFactory.getLogger(TAG);
	
	private Body playzoneBody;
	
	private WorldModel wm;
	private BasicShape board;
	
	private BoardModel(final WorldModel wm, final BasicShape board, final String board_name) {
		this.wm = wm;
		this.board = board;
		setName(board_name);

		//todo: this cannot be added here - it is on a different thread where the game simulation occurs ,therefore prone to create problems
		playzoneBody = wm.getWorldPhysicsManager().addBody(board, this);
		
		// Sinaliza os subscritores de que a construção do modelo terminou.
		BoardModel.this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));

	}
	
	public static BoardModel getNewInstance(WorldModel wm, BasicShape board, String board_name){
		return new BoardModel(wm, board, board_name);
	}

	
	@Override
	public void handleGameTick(GameTickEvent e) {
		long elapsedNanoTime = e.getElapsedNanoTime();
		
		if (playzoneBody != null)
			if (logger.isDebugEnabled())
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
