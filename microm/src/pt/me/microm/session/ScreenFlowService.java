package pt.me.microm.session;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.session.PlayerProgress.SessionState;

import com.badlogic.gdx.utils.Logger;

/**
 * This class should take care of the screens workflow that implements the training "room".
 * The player should be able to choose each level that he already completed successfully on story mode.
 * 	-check world declaration files
 *  -check savegame and cross info to build database
 *  
 *  Esta classe deverá ter um interface em comum com a classe LogicStoryMode para que possam ser orquestradas a partir do GameMicrom. ***Será possível?***
 *  
 * 
 * @author mneves
 *
 */
public class ScreenFlowService {
	
	private static final String TAG = ScreenFlowService .class.getSimpleName();
	private static Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);	

	private SessionState sessionState;
	private MyWorld currentWorld;
	private List<MyWorld> data;
	
	protected ScreenFlowService(SessionState sessionState, MyWorld currentWorld, List<MyWorld> data) {
		this.sessionState = sessionState;
		this.currentWorld = currentWorld;
		this.data = data;
	}
	
	/**
	 * Returns a list of all available worlds
	 * @return a list of all available worlds
	 */
	public List<MyWorld> getAllWorlds() {
		return Collections.unmodifiableList(data);
	}
	
	/**
	 * Searches for a world with a given name
	 * @param worldName
	 * @return a world object
	 */
	public MyWorld getWorldByName(String worldName) {
		MyWorld auxWorld = null;
		
		Iterator<MyWorld> itAvailableWorlds = data.iterator();
		while (itAvailableWorlds.hasNext()) {
			auxWorld = itAvailableWorlds.next();
			
			if (auxWorld.getName().equals(worldName))
				break;
		}
		return auxWorld;
	}
	

	/**
	 * This method allows to manipulate the SessionState variables
	 *   gameMode: what game mode the player wants to play (Story or Training) 
	 *   isMuted: should I play sound effects & music ?
	 * 
	 * @return the SessionState object
	 */
	public SessionState getSessionState() {
		return sessionState;
	}
	
	
}
