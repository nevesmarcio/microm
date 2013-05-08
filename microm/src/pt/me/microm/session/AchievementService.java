package pt.me.microm.session;

import java.util.List;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.session.PlayerProgress.SessionState;

import com.badlogic.gdx.utils.Logger;

public class AchievementService {

	private static final String TAG = AchievementService.class.getSimpleName();
	private static Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);	
	
	private SessionState sessionState;
	private MyWorld currentWorld;
	private List<MyWorld> data;
	
	protected AchievementService(SessionState sessionPrefs, MyWorld currentWorld, List<MyWorld> data) {
		this.sessionState = sessionPrefs;
		this.currentWorld = currentWorld;
		this.data = data;
		
	}
	
	/**
	 * Some foo function
	 */
	public void foo() {
		
	}	

	
	
	
	
	
}
