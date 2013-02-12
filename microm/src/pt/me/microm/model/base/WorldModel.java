package pt.me.microm.model.base;

import java.util.Random;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.events.GameTickEvent;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.MyContactListener;
import pt.me.microm.model.dev.BallModel;
import pt.me.microm.model.dev.CoisaModel;
import pt.me.microm.model.dev.GridModel;
import pt.me.microm.model.events.SimpleEvent;
import pt.me.microm.model.stuff.BoardModel;
import pt.me.microm.model.stuff.DaBoxModel;
import pt.me.microm.model.stuff.PortalModel;
import pt.me.microm.model.stuff.PortalModelManager;
import pt.me.microm.model.stuff.SpawnModel;
import pt.me.microm.model.ui.UIModel;
import pt.me.microm.tools.levelloader.LevelLoader;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Logger;


/* 
 * É no WorldModel que devo construir tudo
 * Eu diria que é o WorldModel que transparece os handles para os objectos controláveis pelo jogador (e pela AI ?)
 * Por exemplo, é o WorldModel que expõe os métodos para movimentar os Pads.
 */
public class WorldModel extends AbstractModel {
	private static final String TAG = WorldModel.class.getSimpleName();
	private static final Logger logger = new Logger(TAG);
	
	private static WorldModel instance = null;
	
	private GridModel grid;
	private UIModel ui;
	private BoardModel board;
	private BallModel ball1;
	private BallModel ball2;
	private CoisaModel coisa;
	
	public SpawnModel spawnModel;
	public  DaBoxModel player;
	public PortalModelManager portalManager;
	
	public Vector2 waypoint;
	
//	private Music bgMusic = GAME_CONSTANTS.MUSIC_BACKGROUND;
//	private Sound exampleSound = GAME_CONSTANTS.SOUND_DROP;

	// testes física
	private Vector2 gravity = new Vector2(0.0f, -30.0f);//-9.8f
	private boolean doSleep = true;
	private World physicsWorld = new World(gravity, doSleep);
	
	private boolean pauseSim = false;

	public TweenManager tweenManager = new TweenManager();
	
	public WorldModelManager wmManager = new WorldModelManager();
	
	private MyContactListener myContactListener;
	
	private WorldModel() {
		
//		bgMusic.setLooping(true);
//		//bgMusic.play();
//
//		exampleSound.play();
//		try {
//			Thread.sleep(200);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		exampleSound.play();

		
		// Sinaliza os subscritores de que a construção do modelo terminou.
		this.dispatchEvent(new SimpleEvent(EventType.ON_MODEL_INSTANTIATED));
	}

	private void PopulateWorld() {

		// Modelos complementares ao WorldModel
		//FIXME::ENABLE THIS
		grid = new GridModel(); // constroi a grid sobre a qual estão renderizados os objectos - debug purposes		
		ui = new UIModel(this); // constroi o painel informativo?
		portalManager = new PortalModelManager();
		
///* exemplos de coisas populadas no mundo */		
////		ball1 = BallModel.getNewInstance(this, board, 6.0f, 0.0f); // larga a bola num mundo num tabuleiro
////		ball2 = BallModel.getNewInstance(this, board, 1.0f, 0.0f);
//		coisa = CoisaModel.getNewInstance(this, board, 0.0f);
////		ball1.ballBody.setActive(false);
////		ball2.ballBody.setActive(true);
////		ball1.ballBody.setActive(!ball1.ballBody.isActive());
////		ball2.ballBody.setActive(!ball2.ballBody.isActive());		
//		
//		Tween.call(new TweenCallback() {
//			@Override public void onEvent(int type, BaseTween<?> source) {
//				Gdx.app.postRunnable(new Runnable() {
//					
//					@Override
//					public void run() {
//						BallModel.getNewInstance(WorldModel.this, board, (new Random().nextFloat())*14.0f, (new Random().nextFloat())*14.0f);
//						
//					}
//				});
//			}
//		}).repeat(10, 0.1f).start(tweenManager);
///* fim dos exemplos */
		
		FileHandle h = Gdx.files.internal("data/levels/level0.svg");
		//FIXME::ENABLE THIS
		Gdx.app.log(TAG, "Nr elements loaded: " + LevelLoader.LoadLevel(h, this));
		
		
		// regista o contactListener para que este notifique os objectos quando há choques 
		getPhysicsWorld().setContactListener(myContactListener = new MyContactListener()); //new ContactListenerImpl() 
	
		// treshold de velocidade para considerar colisões inelásticas
		//World.setVelocityThreshold(1.0f);//0.001f
		
		//setPauseSim(false);

	}
	
	//there can only be one world
	public static WorldModel getSingletonInstance(){
		if (instance == null) {
			instance = new WorldModel();
			
			instance.PopulateWorld(); // go have fun!
		}
		return instance;
	}
	 
	
	@Override
	public void handleGameTick(GameTickEvent e) {
		float elapsedNanoTime = e.getElapsedNanoTime();

		if (logger.getLevel() == logger.DEBUG) logger.debug("[WorldModel timestep]: time elapsed=" + elapsedNanoTime/GAME_CONSTANTS.ONE_SECOND_TO_NANO + "s");
		
		// testes de física
		if ((getPhysicsWorld() != null) && !isPauseSim()){
//			getPhysicsWorld().step(elapsedNanoTime/(float)GAME_CONSTANTS.ONE_SECOND_TO_NANO, 8, 3);
			physicsWorld.step((float)GAME_CONSTANTS.GAME_TICK_MILI/(float)GAME_CONSTANTS.ONE_SECOND_TO_MILI, 8, 3);
			
			//TODO: Não entendo pq é que o elapsed nanotime escavaca o esquema todo... :: não é o elapsednanotime. é o cálculo da força a aplicar. com velocidade constante já n se verifica?!?
			//physicsWorld.step(elapsedNanoTime/(float)GAME_CONSTANTS.ONE_SECOND_TO_NANO, 12, 6);
			if (logger.getLevel() == logger.DEBUG) logger.debug("[physics-step]: step=" + elapsedNanoTime/(float)GAME_CONSTANTS.ONE_SECOND_TO_NANO);
		
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
		String other_portal_name = a.portal_name.replace("entry", "exit");

		for (PortalModel p : portalManager.portals) {
			if (p.portal_name.equals(other_portal_name)) {
				return p;
			}
		}
		return null;
	}
	
	// Objecto que trata dos cálculos físicos
	public World getPhysicsWorld() {
		return physicsWorld;
	}
	public void setPhysicsWorld(World physicsWorld) {
		this.physicsWorld = physicsWorld;
	}

	// Pausa a simulação física
	private boolean isPauseSim() {
		return pauseSim;
	}
	public void setPauseSim(boolean pauseSim) {
		this.pauseSim = pauseSim;
	}
	

	
	public void touchDown (CameraModel cam, float positionX, float positionY, int pointer){
		this.ui.touchDown(cam, positionX, positionY, pointer);
	}
	
	public void touchDragged (CameraModel cam, float positionX, float positionY, int pointer) {
		this.ui.touchDragged(cam, positionX, positionY, pointer);
	}
	public void touchUp (CameraModel cam, float positionX, float positionY, int pointer) {
		this.ui.touchUp(cam, positionX, positionY, pointer);
	}

	@Override
	public Body getBody() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Vector2 getPosition() {
		return null;
	}
	
	
}
