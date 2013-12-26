package pt.me.microm.session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.me.microm.infrastructure.GAME_CONSTANTS;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Logger;

/**
 * 
 * @author mneves
 *
 */
public class GameContentService {
	
	private static final String TAG = GameContentService .class.getSimpleName();
	private static Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);	

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
	
	public GameContentService() {
		readAvailableWorlds();
		
		Iterator<MyWorld> itMyWorld = availableWorlds.iterator();
		while (itMyWorld.hasNext()) {
			readAvailableLevels(itMyWorld.next());
		}
		
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
