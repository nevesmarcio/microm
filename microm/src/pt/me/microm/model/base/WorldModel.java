package pt.me.microm.model.base;

import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.MyContactListener;
import pt.me.microm.model.stuff.BoardModel;
import pt.me.microm.model.stuff.DaBoxModel;
import pt.me.microm.model.stuff.PortalModel;
import pt.me.microm.model.stuff.PortalModelManager;
import pt.me.microm.model.stuff.SpawnModel;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Logger;


/* 
 * É no WorldModel que devo construir tudo
 * Eu diria que é o WorldModel que transparece os handles para os objectos controláveis pelo jogador (e pela AI ?)
 * Por exemplo, é o WorldModel que expõe os métodos para movimentar os Pads.
 */
public class WorldModel extends AbstractModel implements GestureListener, InputProcessor {
	private static final String TAG = WorldModel.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
//	private static WorldModel instance = null;
	public static enum EventType {
		ON_LEVEL_LOADED,		// Event raised when the level is finished loading
		ON_LIFE_LOST,			// Event raised when the player lose a life
		ON_COLLECTIBLE_FOUND,	// Event raised when the player founds a collectible item
		ON_WORLD_COMPLETED	 	// Event raised when the player successfully completes this world
		
	};
	
	private BoardModel board;
	
	public SpawnModel spawnModel;
	public  DaBoxModel player;
	private PortalModelManager portalManager;
	
	public Vector2 waypoint;
	
	// testes física
	private Vector2 gravity = new Vector2(0.0f, -30.0f);//-9.8f
	private boolean doSleep = true;
	private World physicsWorld = new World(gravity, doSleep);
	
	private boolean pauseSim = false;

	private TweenManager tweenManager = new TweenManager();
	
	public WorldModelManager wmManager = new WorldModelManager();
	
	public MyContactListener myContactListener;
	
	public WorldModel() {
	
		PopulateWorld();
		
		// Sinaliza os subscritores de que a construção do modelo terminou.
		this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));
	}

	private void PopulateWorld() {
		
//		setPauseSim(true);
		
		portalManager = new PortalModelManager();

		// regista o contactListener para que este notifique os objectos quando há choques 
		getPhysicsWorld().setContactListener(myContactListener = new MyContactListener()); //new ContactListenerImpl() 
	
		// treshold de velocidade para considerar colisões inelásticas
		//World.setVelocityThreshold(1.0f);//0.001f
		
	}
	

	 
	
	@Override
	public void handleGameTick(GameTickEvent e) {
		float elapsedNanoTime = e.getElapsedNanoTime();

		if (logger.getLevel() == Logger.DEBUG) logger.debug("[WorldModel timestep]: time elapsed=" + elapsedNanoTime/GAME_CONSTANTS.ONE_SECOND_TO_NANO + "s");
		
		// testes de física
		if ((getPhysicsWorld() != null) && !isPauseSim()){
//			getPhysicsWorld().step(elapsedNanoTime/(float)GAME_CONSTANTS.ONE_SECOND_TO_NANO, 8, 3);
			physicsWorld.step((float)GAME_CONSTANTS.GAME_TICK_MILI/(float)GAME_CONSTANTS.ONE_SECOND_TO_MILI, 8, 3);
			
			//TODO: Não entendo pq é que o elapsed nanotime escavaca o esquema todo... :: não é o elapsednanotime. é o cálculo da força a aplicar. com velocidade constante já n se verifica?!?
			//physicsWorld.step(elapsedNanoTime/(float)GAME_CONSTANTS.ONE_SECOND_TO_NANO, 12, 6);
			if (logger.getLevel() == Logger.DEBUG) logger.debug("[physics-step]: step=" + elapsedNanoTime/(float)GAME_CONSTANTS.ONE_SECOND_TO_NANO);
		
			//É após o step que se pode processar o adicionar/ remover objectos no physicsWorld
			wmManager.process();
			
			// Faz o step das "animações"
			tweenManager.update(elapsedNanoTime/(float)GAME_CONSTANTS.ONE_SECOND_TO_NANO);
		}

	}

	
	/* 
	 * Getters + Setters 
	 */
	public void setBoard(BoardModel b) {
		this.board = b;
	}
	
	public void setPlayer(DaBoxModel p) {
		this.player = p;
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
	
	// Objecto que trata dos cálculos físicos
	public World getPhysicsWorld() {
		return physicsWorld;
	}
	private void setPhysicsWorld(World physicsWorld) {
		this.physicsWorld = physicsWorld;
	}

	// Pausa a simulação física
	private boolean isPauseSim() {
		return pauseSim;
	}
	public void setPauseSim(boolean pauseSim) {
		this.pauseSim = pauseSim;
	}
	

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

	// InputProcessor interface implementation
	@Override
	public boolean keyDown(int keycode) {

		if ((keycode == Keys.SPACE) || (keycode == Keys.MENU))
			if (player.getBody() != null)
				player.jump();
		
		if (keycode == Keys.P) {
			setPauseSim(true);
		}
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {

		if (keycode == Keys.P) {
			setPauseSim(false);
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
