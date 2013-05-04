package pt.me.microm.session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.me.microm.infrastructure.GAME_CONSTANTS;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

public class PlayerProgress {

	private static final String TAG = PlayerProgress.class.getSimpleName();
	private static Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);	
	
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
			if (logger.getLevel() >= Logger.ERROR) logger.error(e.getMessage());
		}
		return aux;
	}
	private String toJson(){
		return jsonFacility.toJson(this);
	}		

	
	/* Save to, Load from file */
	public void save() {
		// update of savedata from the memory structure
		storyMode.current_world = currentWorld!=null ? currentWorld.getName() : null;
		storyMode.world_state = new HashMap<String, MyWorld>();
		trainingMode.level_state = new HashMap<String, MyLevel>();
		
		Iterator<MyWorld> itAvailableWorlds = availableWorlds.iterator();
		while (itAvailableWorlds.hasNext()) {
			MyWorld auxWorld = itAvailableWorlds.next();
			storyMode.world_state.put(auxWorld.getName(), auxWorld);
			
			Iterator<MyLevel> itAvailableLevels = auxWorld.getLevels().iterator();
			while (itAvailableLevels.hasNext()) {
				MyLevel auxLevel = itAvailableLevels.next();
				trainingMode.level_state.put(auxWorld.getName()+"/"+auxLevel.getName(), auxLevel);
			}
		}
		
		String json = this.toJson();
		Gdx.files.local(GAME_CONSTANTS.SAVEGAME_FILE_V1).writeString(json, false);
	}

	public static PlayerProgress Load() throws Exception {
//		boolean isExtAvailable = Gdx.files.isExternalStorageAvailable();
//		boolean isLocAvailable = Gdx.files.isLocalStorageAvailable();
//		String extRoot = Gdx.files.getExternalStoragePath();
//		String locRoot = Gdx.files.getLocalStoragePath();
//		
//		logger.info("isExtAvailable: " + isExtAvailable + " :: " + extRoot);
//		logger.info("EXTERNAL_FILE_PATH: " + Gdx.files.external("").file().getAbsolutePath());
//		FileHandle[] filesA = Gdx.files.external("").list();
//		for(FileHandle file: filesA) {
//		   logger.debug("\t" + file.name() + "|" + file.length());
//		}
//		logger.info("#files on dir: " + filesA.length);
//		OutputStream extOut = Gdx.files.external(UUID.randomUUID().toString()+".test").write(false);
//		PrintStream extPs = new PrintStream(extOut);
//		
//		extPs.append("hello world!");
//		extPs.flush();
//		extPs.close();
//		
//		logger.info("isLocAvailable: " + isLocAvailable + " :: " + locRoot);
//		logger.info("LOCAL_FILE_PATH: " + Gdx.files.local("").file().getAbsolutePath());
//		FileHandle[] filesB = Gdx.files.local(".").list();
//		for(FileHandle file: filesB) {
//			logger.debug("\t" + file.name() + "|" + file.length());
//		}
//		logger.info("#files on dir: " + filesB.length);
//		OutputStream locOut = Gdx.files.local(UUID.randomUUID().toString()+".test").write(false);
//		PrintStream locPs = new PrintStream(locOut);
//		
//		locPs.append("hello world!");
//		locPs.flush();
//		locPs.close();
		
		boolean isLocAvailable = Gdx.files.isLocalStorageAvailable();
		if (!isLocAvailable) throw new Exception("No storage available. Load failed!");
		
		if (!Gdx.files.local(GAME_CONSTANTS.SAVEGAME_FILE_V1).exists()) {
			new PlayerProgress().save();
			logger.info("Save file not found. creating one...");
		}
		
		String json = Gdx.files.local(GAME_CONSTANTS.SAVEGAME_FILE_V1).readString();
		PlayerProgress toReturn = getObjectFromJSON(json); 
		// until this point, the PlayerProgress has been build with the save data
		// now I need to read the World/Levels available
		toReturn.readAvailableWorlds();
		
		// based on the savegame, inits the player's progress data
		toReturn.initProgressData();
		
		// init the api objects
		toReturn.achievementService = new AchievementService(toReturn.currentWorld, toReturn.availableWorlds);
		toReturn.screenFlowService = new ScreenFlowService(toReturn.currentWorld, toReturn.availableWorlds);
		
		return toReturn;
	}
	
	/* memory structure that stores available worlds and levels */
	private MyWorld currentWorld;
	private List<MyWorld> availableWorlds;
	
	private void readAvailableWorlds() {
		List<MyWorld> availableWorlds = new ArrayList<MyWorld>();
		
		logger.info("searching worlds on FP=" + Gdx.files.internal("data/levels").file().getAbsolutePath());
		FileHandle[] filesC = Gdx.files.internal("data/levels").list();
		
		Pattern pattern = Pattern.compile("world\\.\\d\\.\\w+");
		Matcher matcher;
		for (FileHandle file : filesC) {
			logger.debug("\t" + file.name() + "|" + (file.isDirectory()?"D":file.length()));
			matcher = pattern.matcher(file.name());
			// Check all occurance
			while (matcher.find()) {
				MyWorld toAdd = new MyWorld();
				toAdd.setName(matcher.group());
				logger.info("\tfound>> "+toAdd.getName());
				readAvailableLevels(toAdd);
				
				availableWorlds.add(toAdd);
			}
		}
		Collections.sort(availableWorlds, new Comparator<MyWorld>(){
			@Override
			public int compare(MyWorld o1, MyWorld o2) {
				int wNum1 = Integer.parseInt(o1.getName().split("\\.")[1]);
				int wNum2 = Integer.parseInt(o2.getName().split("\\.")[1]);
				return wNum1-wNum2;
			}});
		this.availableWorlds = availableWorlds;
	}
	private void readAvailableLevels(MyWorld world) {
		List<MyLevel> availableLevels = new ArrayList<MyLevel>();
		
		logger.info("\tsearching levels on FP=" + Gdx.files.internal("data/levels/" + world.getName()).file().getAbsolutePath());
		FileHandle[] filesC = Gdx.files.internal("data/levels/" + world.getName()).list();
		
		Pattern pattern = Pattern.compile("level#[1-9\\.]\\.\\w+\\.svg");
		Matcher matcher;
		world.setLevels(new ArrayList<MyLevel>());
		for (FileHandle file : filesC) {
			logger.debug("\t" + file.name() + "|" + (file.isDirectory()?"D":file.length()));
			matcher = pattern.matcher(file.name());
			// Check all occurance
			
			while (matcher.find()) {
				MyLevel toAdd = new MyLevel();
				toAdd.setName(matcher.group());
				toAdd.setParentWorld(world);
				logger.info("\t\tfound>> "+toAdd.getName());
				
				availableLevels.add(toAdd);
			}
		}
		Collections.sort(availableLevels, new Comparator<MyLevel>() {
			@Override
			public int compare(MyLevel o1, MyLevel o2) {
				int wNum1 = Integer.parseInt(o1.getName().split("\\.")[1]);
				int wNum2 = Integer.parseInt(o2.getName().split("\\.")[1]);
				return wNum1-wNum2;
			}
		});
		world.setLevels(availableLevels);

	}
	

	private void initProgressData() {
		Iterator<MyWorld> itMyWorld = availableWorlds.iterator();
		while (itMyWorld.hasNext()){
			MyWorld destMyWorld = itMyWorld.next();
			MyWorld srcMyWorld = null;

			// init current_world
			if (destMyWorld.getName().equalsIgnoreCase(storyMode.current_world))
				currentWorld = destMyWorld;

			// init world_state
			if (storyMode.world_state.containsKey(destMyWorld.getName())) {
				srcMyWorld = storyMode.world_state.get(destMyWorld.getName());
				
				destMyWorld.completeDataFrom(srcMyWorld);
			
				// init level_state 
				Iterator<MyLevel> itMyLevel = destMyWorld.getLevels().iterator();
				while (itMyLevel.hasNext()) {
					MyLevel destMyLevel = itMyLevel.next();
					MyLevel srcMyLevel = null;
					
					if (trainingMode.level_state.containsKey(destMyWorld.getName()+"/"+destMyLevel.getName())){
						srcMyLevel = trainingMode.level_state.get(destMyWorld.getName()+"/"+destMyLevel.getName());
						
						destMyLevel.completeDataFrom(srcMyLevel);
						
					}
				}
			}
		}
	}
	

	/* and the PlayerProgress API */
	private AchievementService achievementService;
	private ScreenFlowService screenFlowService;
	
	public AchievementService getAchievementService() {
		return achievementService;
	}
	public ScreenFlowService getScreenFlowService() {
		return screenFlowService;
	}
	

	/* savegame definition - VERY IMPORTANT!! - API should not work on this objects, because the are only updated on save operation */
	@Expose	private StoryMode storyMode;
	@Expose	private TrainingMode trainingMode;
	
	private PlayerProgress() {
		// when the object is built from scratch
		availableWorlds = new ArrayList<MyWorld>();
		
		storyMode = new StoryMode();
		trainingMode = new TrainingMode();
	}


	/**
	 * These classes can lookup the appropriate object that represent a world or level
	 * given the world/ level name 
	 */
	private class StoryMode {
		@Expose private String current_world;
		@Expose private HashMap<String, MyWorld> world_state;
		
		private StoryMode() { // default values when object is built from scratch
			current_world = null;
			world_state = new HashMap<String, MyWorld>();
		}
	}
	
	private class TrainingMode {
		@Expose private HashMap<String, MyLevel> level_state;
		
		private TrainingMode() { // default values when object is built from scratch 
			level_state = new HashMap<String, MyLevel>();
		}
	}
	
}
