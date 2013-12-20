package pt.me.navigator.model.base;

import pt.me.navigator.infrastructure.GAME_CONSTANTS;
import pt.me.navigator.infrastructure.events.GameTickEvent;
import pt.me.navigator.model.AbstractModel;
import pt.me.navigator.model.MyContactListener;
import pt.me.navigator.model.dev.GridModel;
import pt.me.navigator.model.events.SimpleEvent;
import pt.me.navigator.model.ui.UIModel;

import com.badlogic.gdx.Gdx;


/* 
 * É no WorldModel que devo construir tudo
 * Eu diria que é o WorldModel que transparece os handles para os objectos controláveis pelo jogador (e pela AI ?)
 * Por exemplo, é o WorldModel que expõe os métodos para movimentar os Pads.
 */
public class WorldModel extends AbstractModel {
	private static final String TAG = WorldModel.class.getSimpleName();
	
	private static WorldModel instance = null;
	
	private GridModel grid;
	private UIModel ui;
	
	private MyContactListener myContactListener;
	
	float ups;
	
	private WorldModel() {
		
	
		// Sinaliza os subscritores de que a construção do modelo terminou.
		this.dispatchEvent(new SimpleEvent(EventType.ON_MODEL_INSTANTIATED));
	}

	private void PopulateWorld() {

		// Modelos complementares ao WorldModel
		grid = new GridModel(); // constroi a grid sobre a qual estão renderizados os objectos - debug purposes		
		ui = new UIModel(); // constroi o painel informativo?
		
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

		setUps((float) (1000.0 / (elapsedNanoTime / GAME_CONSTANTS.ONE_MILISECOND_TO_NANO)));
		
		Gdx.app.debug("[WorldModel timestep]","time elapsed= " + elapsedNanoTime/GAME_CONSTANTS.ONE_SECOND_TO_NANO + "s");
		
	}

	
	/* 
	 * Getters + Setters 
	 */
	
	// Updates Per Second (relativo à cadência dos cálculos físicos)
	public float getUps() {
		return ups;
	}
	public void setUps(float ups) {
		this.ups = ups;
	}


	
	
}
