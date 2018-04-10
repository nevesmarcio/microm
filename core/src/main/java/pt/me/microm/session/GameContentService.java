package pt.me.microm.session;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 
 * @author mneves
 *
 */
public class GameContentService {
	
	private static final String TAG = GameContentService .class.getSimpleName();
	private static final Logger logger = LoggerFactory.getLogger(TAG);	

	private List<MyWorld> availableWorlds;
	
	/**
	 * Returns a list of all available worlds
	 * @return a list of all available worlds
	 */
	public List<MyWorld> getAllWorlds() {
		return Collections.unmodifiableList(availableWorlds);
	}
	
	/**
	 * Searches for a world with a given name
	 * @param worldName
	 * @return a world object
	 */
	public MyWorld getWorldByName(String worldName) {
		MyWorld auxWorld = null;
		
		Iterator<MyWorld> itAvailableWorlds = availableWorlds.iterator();
		while (itAvailableWorlds.hasNext()) {
			auxWorld = itAvailableWorlds.next();
			
			if (auxWorld.getName().equals(worldName))
				break;
		}
		return auxWorld;
	}
	
	public String[] getNextLevel() {
		//todo: implement this
		return new String[] {"world.1.justforkicks", "level#1.0.svg"};
	}
	
	
	private static GameContentService instance = null;
	
	public static GameContentService getInstance() {
		if (instance == null) {
			instance = new GameContentService();
		}
		return instance;
	}
	
	private GameContentService() {
		availableWorlds = new ArrayList<MyWorld>();
		readAvailableWorlds();
		
		Iterator<MyWorld> itMyWorld = availableWorlds.iterator();
		while (itMyWorld.hasNext()) {
			readAvailableLevels(itMyWorld.next());
		}
		
		logger.info("finished searching for levels...");
	}	
	
	
	private void readAvailableWorlds() {
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
	
	
	
}
