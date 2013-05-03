package pt.me.microm;

import pt.me.microm.infrastructure.GAME_CONSTANTS;

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
public class LogicTrainingMode {

	private static final String TAG = LogicTrainingMode.class.getSimpleName();
	private static Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);	
	
	public LogicTrainingMode() {
		
	}
	
	
}
