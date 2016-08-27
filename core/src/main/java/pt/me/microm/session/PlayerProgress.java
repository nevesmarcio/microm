package pt.me.microm.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.me.microm.infrastructure.GAME_CONSTANTS;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

public class PlayerProgress {

	private static final String TAG = PlayerProgress.class.getSimpleName();
	private static final Logger logger = LoggerFactory.getLogger(TAG);	
	
	/* Json serialization */
	private static Gson jsonFacility = new GsonBuilder()
											.serializeNulls()
											.excludeFieldsWithoutExposeAnnotation()
											.create();
	private static PlayerProgress getObjectFromJSON(String json) {
		PlayerProgress aux = null;
		try {
			aux = jsonFacility.fromJson(json, PlayerProgress.class);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) logger.error(e.getMessage());
		}
		return aux;
	}
	private String toJson(){
		return jsonFacility.toJson(this);
	}		

	
	/* Save to, Load from file */
	public void save() {
		String json = this.toJson();
		Gdx.files.local(GAME_CONSTANTS.SAVEGAME_FILE_V1).writeString(json, false);
		
    	logger.info("saved state: dc: " + this.getDeathCount() + "; lpl: " + this.getLastPlayed() + ";");
	}

	public static PlayerProgress Load() throws Exception {
	
		boolean isLocAvailable = Gdx.files.isLocalStorageAvailable();
		if (!isLocAvailable) throw new Exception("No storage available. Load failed!");
		
		if (!Gdx.files.local(GAME_CONSTANTS.SAVEGAME_FILE_V1).exists()) {
			new PlayerProgress().save();
			logger.info("Save file not found. creating one...");
		}
		
		String json = Gdx.files.local(GAME_CONSTANTS.SAVEGAME_FILE_V1).readString();
		PlayerProgress toReturn = getObjectFromJSON(json); 
		
		logger.info("loaded state: dc: " + toReturn.getDeathCount() + "; lpl: " + toReturn.getLastPlayed() + ";");
		
		return toReturn;
	}

	// savegame definition
	@Expose	private MyLevel lastPlayed;
	@Expose	private int deathCount;
	
	public PlayerProgress() {

	}
	
	public MyLevel getLastPlayed() {
		return lastPlayed;
	}
	public void setLastPlayed(MyLevel lastPlayed) {
		this.lastPlayed = lastPlayed;
	}
	public int getDeathCount() {
		return deathCount;
	}
	public void setDeathCount(int deathCount) {
		this.deathCount = deathCount;
	}

	
	
}
