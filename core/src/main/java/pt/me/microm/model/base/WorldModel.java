package pt.me.microm.model.base;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.stuff.*;



/* 
 * É no WorldModel que devo construir tudo
 * Eu diria que é o WorldModel que transparece os handles para os objectos controláveis pelo jogador (e pela AI ?)
 * Por exemplo, é o WorldModel que expõe os métodos para movimentar os Pads.
 */
public class WorldModel extends AbstractModel implements GestureListener, InputProcessor {
	private static final String TAG = WorldModel.class.getSimpleName();
	private static final Logger logger = LoggerFactory.getLogger(TAG);
	
//	private static WorldModel instance = null;
	public enum EventType {
		ON_LEVEL_LOADED,		// Event raised when the level is finished loading
		ON_LIFE_LOST,			// Event raised when the player lose a life
		ON_COLLECTIBLE_FOUND,	// Event raised when the player founds a collectible item
		ON_WORLD_COMPLETED	 	// Event raised when the player successfully completes this world
		
	};

	// inner workings 
	private WorldPhysicsManager worldPhysicsManager;
	private PortalModelManager portalManager;
	private TweenManager tweenManager;
	
	// in-game models
	private BoardModel board;
	private SpawnModel spawnModel;
	private DaBoxModel player;
	private Vector2 waypoint;

	
	public WorldModel() {
		worldPhysicsManager = new WorldPhysicsManager();
		portalManager = new PortalModelManager();
		tweenManager = new TweenManager();
		
		// Sinaliza os subscritores de que a construção do modelo terminou.
		this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));
	}

	@Override
	public void handleGameTick(GameTickEvent e) {
		float elapsedNanoTime = e.getElapsedNanoTime();

		// Excites physical engine
		worldPhysicsManager.update(e);
		
		// Excites animation/ tweening engine 
		tweenManager.update(elapsedNanoTime/(float)GAME_CONSTANTS.ONE_SECOND_TO_NANO);

	}

	// inner workings getters	
	public WorldPhysicsManager getWorldPhysicsManager() {
		return worldPhysicsManager;
	}

	public PortalModelManager getPortalManager() {
		return portalManager;
	}

	public TweenManager getTweenManager() {
		return tweenManager;
	}


	/* 
	 * Getters + Setters 
	 */
	public void setBoard(BoardModel b) {
		this.board = b;
	}
	
	public void setSpawnModel(SpawnModel spawnModel) {
		this.spawnModel = spawnModel;
	}
	
	public DaBoxModel getPlayer() {
		return player;
	}

	public void setPlayer(DaBoxModel p) {
		this.player = p;
	}
	

	public Vector2 getWaypoint() {
		return waypoint;
	}

	public void setWaypoint(Vector2 waypoint) {
		this.waypoint = waypoint;
	}


	public void addPortal(PortalModel pm) {
		this.portalManager.portals.add(pm);
	}
	public PortalModel getLinkedPortal(PortalModel a) {
		// procura a referência para o outro portal
		String other_portal_name = a.getName().replace("entry", "exit");

		for (PortalModel p : portalManager.portals) {
			if (p.getName().equals(other_portal_name)) {
				return p;
			}
		}
		return null;
	}
	

//	// Pausa a simulação física
//	private boolean isPauseSim() {
//		return pauseSim;
//	}
//	public void setPauseSim(boolean pauseSim) {
//		this.pauseSim = pauseSim;
//	}
	

//	@Override
//	public Body getBody() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	@Override
//	public Vector2 getPosition() {
//		return null;
//	}
	
	
	@Override
	public void dispose() {
		super.dispose();
	}


	// GestureListener interface implementation
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {

		if (player != null)
			player.jump();
		
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		return false;
	}

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

    @Override
    public void pinchStop() {
        // TODO Auto-generated method stub
    }

    // InputProcessor interface implementation
	@Override
	public boolean keyDown(int keycode) {

		if ((keycode == Keys.SPACE) || (keycode == Keys.MENU))
			if (player.getBody() != null)
				player.jump();
		
		if (keycode == Keys.P) {
//			setPauseSim(true);
		}
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {

		if (keycode == Keys.P) {
//			setPauseSim(false);
		}	
		
		return false;
	}

	@Override
	public boolean keyTyped(char character) {

		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {

		return false;
	}

	@Override
	public boolean scrolled(int amount) {

		return false;
	}

}
