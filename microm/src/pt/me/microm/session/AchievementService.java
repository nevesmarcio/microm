package pt.me.microm.session;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import pt.me.microm.infrastructure.GAME_CONSTANTS;

import com.badlogic.gdx.utils.Logger;

public class AchievementService {

	private static final String TAG = AchievementService.class.getSimpleName();
	private static Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);	
	
	private MyWorld currentWorld;
	private List<MyWorld> data;
	
	protected AchievementService(MyWorld currentWorld, List<MyWorld> data) {
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
	 * Some foo function
	 */
	public void foo() {
		
	}	

	
	
}
