package pt.me.microm.tools.levelloader;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import pt.me.microm.GameMicroM;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.model.base.CameraModel;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.model.collectible.StarModel;
import pt.me.microm.model.dev.DebugModel;
import pt.me.microm.model.phenomenon.LightSourceModel;
import pt.me.microm.model.stuff.BoardModel;
import pt.me.microm.model.stuff.DaBoxModel;
import pt.me.microm.model.stuff.GoalModel;
import pt.me.microm.model.stuff.GroundModel;
import pt.me.microm.model.stuff.MagnetModel;
import pt.me.microm.model.stuff.PortalModel;
import pt.me.microm.model.stuff.SpawnModel;
import pt.me.microm.model.stuff.WallModel;
import pt.me.microm.model.trigger.SimpleTriggerModel;
import pt.me.microm.model.ui.TextModel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Logger;

/**
 * This class exposes a static method that allows the reading of a Level from an
 * SVG file
 * 
 * @author Márcio Neves
 * 
 */
public class LevelLoader {
	private static final String TAG = LevelLoader.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private LevelLoader() {}
	
	/**
	 * 
	 * @param board
	 * @param wm
	 */
	private static BoardModel addBoardToWorld(WorldModel wm, BasicShape board, String board_name) {
		if (GameMicroM.FLAG_DEV_ELEMENTS_A) {
			DebugModel m;
			for (Vector2 ap : board.getPointsArray()) {
				m = DebugModel.getNewInstance(wm, ap.x+board.getCentroid().x, ap.y+board.getCentroid().y);
				m.setColor(Color.WHITE);
			}
			m = DebugModel.getNewInstance(wm, board.getCentroid().x, board.getCentroid().y);
			m.setColor(Color.BLACK);
		}		

		BoardModel bm = BoardModel.getNewInstance(wm, board, board_name); 
		wm.setBoard(bm);
		return bm;
	}
	
	/**
	 * 
	 * @param wm
	 * @param dabox
	 * @param dabox_name
	 * @return
	 */
	private static DaBoxModel addDaBoxToWorld(WorldModel wm, BasicShape dabox, String dabox_name) {
		if (GameMicroM.FLAG_DEV_ELEMENTS_A)
			for (Vector2 ap : dabox.getPointsArray()) {
					DebugModel.getNewInstance(wm, ap.x+dabox.getCentroid().x, ap.y+dabox.getCentroid().y);
			}
		
		DaBoxModel dbm = DaBoxModel.getNewInstance(wm, dabox, dabox_name); 
		wm.setPlayer(dbm);
		
		return dbm;
	}	
	
	/**
	 * 
	 * @param spawn
	 * @param wm
	 */
	private static SpawnModel addSpawnToWorld(WorldModel wm, DaBoxModel dbm, BasicShape spawn, String spawn_name) {

		if (GameMicroM.FLAG_DEV_ELEMENTS_A) {
			DebugModel m;
			for (Vector2 ap : spawn.getPointsArray()) {
				m = DebugModel.getNewInstance(wm, ap.x+spawn.getCentroid().x, ap.y+spawn.getCentroid().y);
				m.setColor(Color.BLUE);
			}

			m = DebugModel.getNewInstance(wm, spawn.getCentroid().x, spawn.getCentroid().y);
			m.setColor(Color.CYAN);
		}

		SpawnModel sm = SpawnModel.getNewInstance(wm, dbm, spawn, spawn_name);
		wm.spawnModel = sm;
		return sm;
	}

	/**
	 * 
	 * @param goal
	 * @param wm
	 */
	private static GoalModel addGoalToWorld(WorldModel wm, BasicShape goal, String goal_name) {
		if (GameMicroM.FLAG_DEV_ELEMENTS_A) {
			DebugModel m;
			for (Vector2 ap : goal.getPointsArray()) {
				m = DebugModel.getNewInstance(wm, ap.x+goal.getCentroid().x, ap.y+goal.getCentroid().y);
				m.setColor(Color.GREEN);
			}
			m = DebugModel.getNewInstance(wm, goal.getCentroid().x, goal.getCentroid().y);
			m.setColor(Color.GRAY);

		}
		
		return GoalModel.getNewInstance(wm, goal, goal_name); 
	}
	
	/**
	 * 
	 * @param ground
	 * @param wm
	 */
	private static GroundModel addGroundToWorld(WorldModel wm, BasicShape ground, String ground_name) {
		if (GameMicroM.FLAG_DEV_ELEMENTS_A)
			for (Vector2 ap : ground.getPointsArray()) {
				DebugModel.getNewInstance(wm, ap.x+ground.getCentroid().x, ap.y+ground.getCentroid().y);
			}
		
		return GroundModel.getNewInstance(wm, ground, ground_name);
	}	
	
	
	/**
	 * 
	 * @param portal
	 * @param wm
	 */
	private static PortalModel addPortalToWorld(WorldModel wm, BasicShape portal, String portal_name) {
		if (GameMicroM.FLAG_DEV_ELEMENTS_A) {
			for (Vector2 ap : portal.getPointsArray()) {
				DebugModel.getNewInstance(wm, ap.x+portal.getCentroid().x, ap.y+portal.getCentroid().y);
			}
			DebugModel.getNewInstance(wm, portal.getCentroid().x, portal.getCentroid().y);
		}

		return PortalModel.getNewInstance(wm, portal, portal_name);
	}


	
	/**
	 * 
	 * @param wall
	 * @param wm
	 */
	private static WallModel addWallToWorld(WorldModel wm, BasicShape wall, String wall_name) {
		if (GameMicroM.FLAG_DEV_ELEMENTS_A)
		for (Vector2 ap : wall.getPointsArray()) {
				DebugModel.getNewInstance(wm, ap.x+wall.getCentroid().x, ap.y+wall.getCentroid().y);
		}
		
		
		return WallModel.getNewInstance(wm, wall, wall_name);
		
	}

	/**
	 * 
	 * @param star
	 * @param wm
	 */
	private static StarModel addStarToWorld(WorldModel wm, BasicShape star, String star_name) {
		if (GameMicroM.FLAG_DEV_ELEMENTS_A) {
			DebugModel m = DebugModel.getNewInstance(wm, star.getCentroid().x, star.getCentroid().y);
			m.setColor(Color.WHITE);
			for (Vector2 ap : star.getPointsArray()) {
					DebugModel.getNewInstance(wm, ap.x+star.getCentroid().x, ap.y+star.getCentroid().y);
			}
		}
		
		return StarModel.getNewInstance(wm, star, star_name); 
		
	}	
	
	/**
	 * 
	 * @param trigger
	 * @param wm
	 */
	private static SimpleTriggerModel addTriggerToWorld(WorldModel wm, BasicShape trigger, String trigger_name, String script) {
		if (GameMicroM.FLAG_DEV_ELEMENTS_A)
		for (Vector2 ap : trigger.getPointsArray()) {
			DebugModel m = DebugModel.getNewInstance(wm, ap.x+trigger.getCentroid().x, ap.y+trigger.getCentroid().y);
			m.setColor(Color.PINK);
		}
		
		SimpleTriggerModel tModel = SimpleTriggerModel.getNewInstance(wm, trigger, trigger_name);
		tModel.setScript(script);
		return tModel;
		
	}
	
	/**
	 * 
	 * @param magnet
	 * @param wm
	 */
	private static MagnetModel addMagnetToWorld(WorldModel wm, BasicShape magnet, String magnet_name) {
		if (GameMicroM.FLAG_DEV_ELEMENTS_A)
		for (Vector2 ap : magnet.getPointsArray()) {
			DebugModel m = DebugModel.getNewInstance(wm, ap.x+magnet.getCentroid().x, ap.y+magnet.getCentroid().y);
			m.setColor(Color.MAGENTA);
		}
		
		MagnetModel mModel = MagnetModel.getNewInstance(wm, magnet, magnet_name);
		return mModel;
		
	}
	
	private static TextModel addTextToWorld(WorldModel wm, BasicShape sh, String text_name, final String content) {
		if (GameMicroM.FLAG_DEV_ELEMENTS_A)
			for (Vector2 ap : sh.getPointsArray()) {
				DebugModel m = DebugModel.getNewInstance(wm, ap.x+sh.getCentroid().x, ap.y+sh.getCentroid().y);
				m.setColor(Color.PINK);
			}					
		
		return TextModel.getNewInstance(wm, sh, text_name, content);
		
	}

	private static LightSourceModel addLightSourceToWorld(WorldModel wm, BasicShape sh, String light_name) {
		if (GameMicroM.FLAG_DEV_ELEMENTS_A) {
			DebugModel m;
			for (Vector2 ap : sh.getPointsArray()) {
				m = DebugModel.getNewInstance(wm, ap.x+sh.getCentroid().x, ap.y+sh.getCentroid().y);
				m.setColor(Color.YELLOW);
			}
			m = DebugModel.getNewInstance(wm, sh.getCentroid().x, sh.getCentroid().y);
			m.setColor(Color.YELLOW);
			
			m = DebugModel.getNewInstance(wm,  sh.getRotationPivot().x, sh.getRotationPivot().y);
			m.setColor(Color.YELLOW);
		}
		
		return LightSourceModel.getNewInstance(wm, sh, light_name);
	}
	
	
	
	/**
	 * This function loads a level from a SVG file. It makes a lot of
	 * assumptions about the elements contained on that SVG. No safeguards are
	 * implemented... yet...
	 * 
	 * @param h
	 *            The handle to the SVG file that represents a level
	 * @param camWidth
	 *            The box2d World object
	 * @return number of assets loaded
	 */
	public static int LoadLevel(FileHandle h, WorldModel wm, CameraModel cm) {
		int nrElements = 0;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			//factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			//FileHandle h = Gdx.files.internal("data/levels/sample.xml");
			Document doc = builder.parse(h.read());
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			//XPathExpression expr = xpath.compile("//book[author='Neal Stephenson']/title/text()");
			//expr.evaluate(doc, XPathConstants.STRING);

			XPathExpression expr; 
			// Get Camera
			if (logger.getLevel() >= Logger.INFO) logger.info("Camera...");
			expr = xpath.compile("//svg/g/path[contains(@id,'camera')]");
			NodeList camera = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < camera.getLength(); i++) {
				String d = camera.item(i).getAttributes().getNamedItem("d").getNodeValue();
				String style = camera.item(i).getAttributes().getNamedItem("style").getNodeValue();
				if (logger.getLevel() >= Logger.INFO) logger.info("d= " + d + "; style= " + style + ";");

				BasicShape s = new BasicShape(d, style, ObjectType.CAMERA);

				if (logger.getLevel() >= Logger.INFO) logger.info(s.toString());
				String camera_name = camera.item(i).getAttributes().getNamedItem("id").getNodeValue();
				
				// here we configure the camera
				cm.adjustCamera(s.getWidth(), s.getHeight(), s.getCentroid().x, s.getCentroid().y);
				
				nrElements+=1;
			}
			
			
			// Get Board
			if (logger.getLevel() >= Logger.INFO) logger.info("Board...");
			expr = xpath.compile("//svg/g/path[contains(@id,'board')]");
			NodeList board = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < board.getLength(); i++) {
				String d = board.item(i).getAttributes().getNamedItem("d").getNodeValue();
				String style = board.item(i).getAttributes().getNamedItem("style").getNodeValue();
				if (logger.getLevel() >= Logger.INFO) logger.info("d= " + d + "; style= " + style + ";");

				BasicShape s = new BasicShape(d, style, ObjectType.BOARD);

				if (logger.getLevel() >= Logger.INFO) logger.info(s.toString());
				String board_name = board.item(i).getAttributes().getNamedItem("id").getNodeValue();
				
				addBoardToWorld(wm, s, board_name);
				
				nrElements+=1;
			}
			
			// Get DaBox
			DaBoxModel daBoxRef = null;
			if (logger.getLevel() >= Logger.INFO) logger.info("DaBox...");
			expr = xpath.compile("//svg/g/path[contains(@id,'daBox')]");
			NodeList dabox = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < dabox.getLength(); i++) {
				String d = dabox.item(i).getAttributes().getNamedItem("d").getNodeValue();
				String style = dabox.item(i).getAttributes().getNamedItem("style").getNodeValue();
				if (logger.getLevel() >= Logger.INFO) logger.info("d= " + d + "; style= " + style + ";");
				
				BasicShape s = new BasicShape(d, style, ObjectType.DABOX);
				
				if (logger.getLevel() >= Logger.INFO) logger.info(s.toString());
				String dabox_name = dabox.item(i).getAttributes().getNamedItem("id").getNodeValue();
				
				daBoxRef = addDaBoxToWorld(wm, s, dabox_name);
				
				nrElements+=1;
			}			
			
			// Get Spawn
			if (logger.getLevel() >= Logger.INFO) logger.info("Spawn...");
			expr = xpath.compile("//svg/g/path[contains(@id,'spawn')]");
			NodeList spawn = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < spawn.getLength(); i++) {
				String d = spawn.item(i).getAttributes().getNamedItem("d").getNodeValue();
				String style = spawn.item(i).getAttributes().getNamedItem("style").getNodeValue();
				if (logger.getLevel() >= Logger.INFO) logger.info("d= " + d + "; style= " + style + ";");
				
				BasicShape s = new BasicShape(d, style, ObjectType.SPAWN);

				if (logger.getLevel() >= Logger.INFO) logger.info(s.toString());
				String spawn_name = spawn.item(i).getAttributes().getNamedItem("id").getNodeValue();
				
				addSpawnToWorld(wm, daBoxRef, s, spawn_name);
				
				nrElements+=1;
			}
			
			// Get Goals
			if (logger.getLevel() >= Logger.INFO) logger.info("Goals...");
			expr = xpath.compile("//svg/g/path[contains(@id,'goal')]");
			NodeList goals = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < goals.getLength(); i++) {
				String d = goals.item(i).getAttributes().getNamedItem("d").getNodeValue();
				String style = goals.item(i).getAttributes().getNamedItem("style").getNodeValue();
				if (logger.getLevel() >= Logger.INFO) logger.info("d= " + d + "; style= " + style + ";");
				
				BasicShape s = new BasicShape(d, style, ObjectType.GOAL);

				if (logger.getLevel() >= Logger.INFO) logger.info(s.toString());
				String goal_name = goals.item(i).getAttributes().getNamedItem("id").getNodeValue();
				
				addGoalToWorld(wm, s, goal_name);
				
				nrElements+=1;
			}

			// Get Grounds
			if (logger.getLevel() >= Logger.INFO) logger.info("Grounds...");
			expr = xpath.compile("//svg/g/path[contains(@id,'ground')]");
			NodeList grounds = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < grounds.getLength(); i++) {
				String d = grounds.item(i).getAttributes().getNamedItem("d").getNodeValue();
				String style = grounds.item(i).getAttributes().getNamedItem("style").getNodeValue();
				if (logger.getLevel() >= Logger.INFO) logger.info("d= " + d + "; style= " + style + ";");
				
				BasicShape s = new BasicShape(d, style, ObjectType.GROUND);
				
				if (logger.getLevel() >= Logger.INFO) logger.info(s.toString());
				String ground_name = grounds.item(i).getAttributes().getNamedItem("id").getNodeValue();
				
				addGroundToWorld(wm, s, ground_name);
				
				nrElements+=1;
			}

			// Get portals
			if (logger.getLevel() >= Logger.INFO) logger.info("Portals...");
			//expr = xpath.compile("//svg/g/path[contains(@id,'portal')]/@d");
			expr = xpath.compile("//svg/g/path[contains(@id,'portal')]");
			NodeList portals = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < portals.getLength(); i++) {
				String d = portals.item(i).getAttributes().getNamedItem("d").getNodeValue();
				String style = portals.item(i).getAttributes().getNamedItem("style").getNodeValue();
				if (logger.getLevel() >= Logger.INFO) logger.info("d= " + d + "; style= " + style + ";");
				
				BasicShape s = new BasicShape(d, style, ObjectType.PORTAL);

				if (logger.getLevel() >= Logger.INFO) logger.info(s.toString());
				String portal_name = portals.item(i).getAttributes().getNamedItem("id").getNodeValue();
				
				addPortalToWorld(wm, s, portal_name);
				
				nrElements+=1;
			}

			// Get walls
			if (logger.getLevel() >= Logger.INFO) logger.info("Walls...");
			//expr = xpath.compile("//svg/g/path[contains(@id,'portal')]/@d");
			expr = xpath.compile("//svg/g/path[contains(@id,'wall')]");
			NodeList walls = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < walls.getLength(); i++) {
				String d = walls.item(i).getAttributes().getNamedItem("d").getNodeValue();
				String style = walls.item(i).getAttributes().getNamedItem("style").getNodeValue();
				if (logger.getLevel() >= Logger.INFO) logger.info("d= " + d + "; style= " + style + ";");
				
				BasicShape s = new BasicShape(d, style, ObjectType.WALL);

				if (logger.getLevel() >= Logger.INFO) logger.info(s.toString());
				String wall_name = walls.item(i).getAttributes().getNamedItem("id").getNodeValue();
				
				addWallToWorld(wm, s, wall_name);
				
				nrElements+=1;
			}
			
			// Get stars
			if (logger.getLevel() >= Logger.INFO) logger.info("Stars...");
			//expr = xpath.compile("//svg/g/path[contains(@id,'portal')]/@d");
			expr = xpath.compile("//svg/g/path[contains(@id,'star')]");
			NodeList stars = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < stars.getLength(); i++) {
				String d = stars.item(i).getAttributes().getNamedItem("d").getNodeValue();
				String style = stars.item(i).getAttributes().getNamedItem("style").getNodeValue();
				if (logger.getLevel() >= Logger.INFO) logger.info("d= " + d + "; style= " + style + ";");
				
				BasicShape s = new BasicShape(d, style, ObjectType.STAR);

				if (logger.getLevel() >= Logger.INFO) logger.info(s.toString());
				String star_name = stars.item(i).getAttributes().getNamedItem("id").getNodeValue();
				
				addStarToWorld(wm, s, star_name);
				
				nrElements+=1;
			}
			
			
			// Get text
			if (logger.getLevel() >= Logger.INFO) logger.info("Text...");
			expr = xpath.compile("//svg/g/text[contains(@id,'text')]/tspan");
			NodeList text = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < text.getLength(); i++) {
				String id = text.item(i).getAttributes().getNamedItem("id").getNodeValue();
				if (logger.getLevel() >= Logger.INFO) logger.info("[" + id + "]");
				
				String x = text.item(i).getAttributes().getNamedItem("x").getNodeValue();
				String y = text.item(i).getAttributes().getNamedItem("y").getNodeValue();
				String s = text.item(i).getTextContent();
				
				if (logger.getLevel() >= Logger.INFO) logger.info("[" + id + "] = x: " + x + "; y: " + y + "; ==> '" + s + "'" );
				BasicShape sh = new BasicShape("m " + x + "," + y, "", ObjectType.NONE);
				if (logger.getLevel() >= Logger.INFO) logger.info(".:.:.:. " + sh.getCentroid() + " .:.:.:.");
				
				addTextToWorld(wm, sh, id, s);
				
				nrElements+=1;
			}
			
			
			// Get triggers
			if (logger.getLevel() >= Logger.INFO) logger.info("Triggers...");
			expr = xpath.compile("//svg/g/path[contains(@id,'trigger')]");
			NodeList triggers = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < triggers.getLength(); i++) {
				String d = triggers.item(i).getAttributes().getNamedItem("d").getNodeValue();
				String style = triggers.item(i).getAttributes().getNamedItem("style").getNodeValue();
				if (logger.getLevel() >= Logger.INFO) logger.info("d= " + d + "; style= " + style + ";");
				
				BasicShape s = new BasicShape(d, style, ObjectType.NONE);

				if (logger.getLevel() >= Logger.INFO) logger.info(s.toString());
				String trigger_name = triggers.item(i).getAttributes().getNamedItem("id").getNodeValue();
				String script = triggers.item(i).getAttributes().getNamedItem("custom-script").getNodeValue();
				
				addTriggerToWorld(wm, s, trigger_name, script);
				
				nrElements+=1;
			}			
			

			// Get magnets
			if (logger.getLevel() >= Logger.INFO) logger.info("Magnets...");
			expr = xpath.compile("//svg/g/path[contains(@id,'magnet')]");
			NodeList magnets = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < magnets.getLength(); i++) {
				String d = magnets.item(i).getAttributes().getNamedItem("d").getNodeValue();
				String style = magnets.item(i).getAttributes().getNamedItem("style").getNodeValue();
				if (logger.getLevel() >= Logger.INFO) logger.info("d= " + d + "; style= " + style + ";");
				
				BasicShape s = new BasicShape(d, style, ObjectType.MAGNET);

				if (logger.getLevel() >= Logger.INFO) logger.info(s.toString());
				String magnet_name = magnets.item(i).getAttributes().getNamedItem("id").getNodeValue();
				
				addMagnetToWorld(wm, s, magnet_name);
				
				nrElements+=1;
			}					
			
			// Get lights
			if (logger.getLevel() >= Logger.INFO) logger.info("Lights...");
			expr = xpath.compile("//svg/g/path[contains(@id,'lightsource')]");
			NodeList lightsources = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < lightsources.getLength(); i++) {
				String d = lightsources.item(i).getAttributes().getNamedItem("d").getNodeValue();
				String style = lightsources.item(i).getAttributes().getNamedItem("style").getNodeValue();
				String offsetX = lightsources.item(i).getAttributes().getNamedItem("inkscape:transform-center-x").getNodeValue();
				String offsetY = lightsources.item(i).getAttributes().getNamedItem("inkscape:transform-center-y").getNodeValue();
				if (logger.getLevel() >= Logger.INFO) logger.info("d= " + d + "; style= " + style + "; offset=(" + offsetX + ", " + offsetY + ")");
				
				BasicShape s = new BasicShape(d, offsetX + "," + offsetY, style, ObjectType.LIGHTSOURCE);

				if (logger.getLevel() >= Logger.INFO) logger.info(s.toString());
				String light_name = lightsources.item(i).getAttributes().getNamedItem("id").getNodeValue();
				
				addLightSourceToWorld(wm, s, light_name);
				
				nrElements+=1;
			}								
			
			
			
			if (logger.getLevel() >= Logger.INFO) logger.info("Finished Loading level: " + h.name());
		
		} catch (ParserConfigurationException e) {
			if (logger.getLevel() >= Logger.ERROR) logger.error(e.getMessage());
			e.printStackTrace();
		} catch (SAXException e) {
			if (logger.getLevel() >= Logger.ERROR) logger.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			if (logger.getLevel() >= Logger.ERROR) logger.error(e.getMessage());
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			if (logger.getLevel() >= Logger.ERROR) logger.error(e.getMessage());
			e.printStackTrace();
		}
		return nrElements;
	}
	
}
