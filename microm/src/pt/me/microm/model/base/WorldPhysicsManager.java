package pt.me.microm.model.base;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.ICommand;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.MyContactListener;
import pt.me.microm.tools.levelloader.BasicShape;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


/**
 * 
 * @author Márcio Neves
 *
 * Esta classe deverá fazer a gestão das adições/ remoções/ transformações sobre o mundo
 * Serve para garantir que estas operações são feitas fora do step
 * Uso o Command Interface para garantir que os objectos são criados fora do
 * "step" do box2d
 * 
 */
public class WorldPhysicsManager {
	private static final String TAG = WorldPhysicsManager.class.getSimpleName();
	private static final Logger logger = LoggerFactory.getLogger(TAG);
	
	// testes física
	private Vector2 gravity = new Vector2(0.0f, -30.0f);//-9.8f
	private boolean doSleep = true;
	private World physicsWorld = new World(gravity, doSleep);
	
	private boolean pauseSim = false;
	
	public MyContactListener myContactListener;
	
	private Queue<ICommand> toAddQueue;
	
	
	public WorldPhysicsManager() {
		toAddQueue = new ConcurrentLinkedQueue<ICommand>();
		
//		setPauseSim(true);
		
		// regista o contactListener para que este notifique os objectos quando há choques 
		physicsWorld.setContactListener(myContactListener = new MyContactListener()); //new ContactListenerImpl() 
	
		// treshold de velocidade para considerar colisões inelásticas
		//physicsWorld.setVelocityThreshold(1.0f);//0.001f
	}
	
	/**
	 * Função que permite o agendamento da manipulação dos objectos fora do step do motor fisico
	 * 
	 * @param e
	 */
	private void add(ICommand e) {
		toAddQueue.add(e);
	}

	
	/**
	 * Adds a ChainShape body (kinematic)
	 * @param shape
	 * @param model
	 * @return
	 */
	public Body addBody(BasicShape shape, AbstractModel model) {
		Body aBody;
		
		Vector2[] silhouetteVertex;
		BodyDef aBodyDef = new BodyDef();
		ChainShape aShape; // Fronteira do tabuleiro

		silhouetteVertex = shape.getPointsArray();
		
		aShape = new ChainShape();
		aShape.createLoop(silhouetteVertex);
						
		// não esquecer que é este start-position que fode com a porca toda!
		aBodyDef.position.set(shape.getCentroid()); // aqui devia calcular a posicao do centro de massa
		aBodyDef.type = shape.getType().getBodyType();
		
		aBody = physicsWorld.createBody(aBodyDef);

		FixtureDef fixDef = new FixtureDef();
		fixDef.shape = aShape;
		fixDef.isSensor = shape.getType().isSensor();
		fixDef.density = shape.getType().getDensity();
		fixDef.friction = shape.getType().getFriction();
		fixDef.restitution = shape.getType().getRestitution();		
		
		aBody.createFixture(fixDef);
		aBody.setUserData(model); // relacionar com o modelo
		
		return aBody; // for delegation purposes
		
	}
	
	public Body addBodyDynamic(BasicShape shape, AbstractModel model) {
		Body aBody;
		
		Vector2[] silhouetteVertex;
		BodyDef daBoxBodyDef = new BodyDef();
		PolygonShape daBoxShape;
		
		// FIXME:: fazer isto sem ser às cegas!
		// CCW vertices
		silhouetteVertex = shape.getPointsArray();
		Vector2[] t = new Vector2[silhouetteVertex.length];
		for (int i = 0; i < silhouetteVertex.length; i++)
			t[silhouetteVertex.length - 1 - i] = silhouetteVertex[i];

		daBoxShape = new PolygonShape();
		daBoxShape.set(t);
		
//		float newRadius = 0.001f;
//		logger.debug(">>>>>>> setting radius of DaBox from ...... " + daBoxShape.getRadius() + " ....... to " + newRadius);
//		daBoxShape.setRadius(newRadius);
		
		daBoxBodyDef.position.set(shape.getCentroid());
		daBoxBodyDef.type = shape.getType().getBodyType();

		aBody = physicsWorld.createBody(daBoxBodyDef);

		/* fixture */
		FixtureDef fixDef = new FixtureDef();
		fixDef.shape = daBoxShape;
		fixDef.density = shape.getType().getDensity();
		fixDef.friction = shape.getType().getFriction();
		fixDef.restitution = shape.getType().getRestitution();

		aBody.createFixture(fixDef);
		aBody.setUserData(model); // relacionar com o modelo
		aBody.setSleepingAllowed(true);		
		aBody.setBullet(false);
		aBody.setActive(false);
		
		return aBody;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public World getPhysicsWorld() {
		return physicsWorld;
	}

	/**
	 * Função a invocar fora do step do motor fisico
	 */
	public void update(GameTickEvent e) {
		float elapsedNanoTime = e.getElapsedNanoTime();
		
		if (logger.isDebugEnabled()) logger.debug("[WorldModel timestep]: time elapsed=" + elapsedNanoTime/GAME_CONSTANTS.ONE_SECOND_TO_NANO + "s");
		
		// testes de física
//		if ((getPhysicsWorld() != null) && !isPauseSim()){
//			getPhysicsWorld().step(elapsedNanoTime/(float)GAME_CONSTANTS.ONE_SECOND_TO_NANO, 8, 3);
			physicsWorld.step((float)GAME_CONSTANTS.GAME_TICK_MILI/(float)GAME_CONSTANTS.ONE_SECOND_TO_MILI, 8, 3);
			
			//TODO: Não entendo pq é que o elapsed nanotime escavaca o esquema todo... :: não é o elapsednanotime. é o cálculo da força a aplicar. com velocidade constante já n se verifica?!?
			//physicsWorld.step(elapsedNanoTime/(float)GAME_CONSTANTS.ONE_SECOND_TO_NANO, 12, 6);
			if (logger.isDebugEnabled()) logger.debug("[physics-step]: step=" + elapsedNanoTime/(float)GAME_CONSTANTS.ONE_SECOND_TO_NANO);
//		}		
		
		// É após o step que se pode processar o adicionar/ remover objectos no physicsWorld		
		Iterator<ICommand> it = toAddQueue.iterator();
		while (it.hasNext()) {
			ICommand pm = it.next();
			pm.handler();
			it.remove();
		}
	}

}
