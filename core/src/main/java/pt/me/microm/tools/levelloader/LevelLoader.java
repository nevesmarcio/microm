package pt.me.microm.tools.levelloader;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import pt.me.microm.GameMicroM;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.base.CameraModel;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.model.collectible.StarModel;
import pt.me.microm.model.dev.DebugModel;
import pt.me.microm.model.stuff.BoardModel;
import pt.me.microm.model.stuff.DaBoxModel;
import pt.me.microm.model.stuff.GoalModel;
import pt.me.microm.model.stuff.GroundModel;
import pt.me.microm.model.stuff.PortalModel;
import pt.me.microm.model.stuff.SpawnModel;
import pt.me.microm.model.stuff.WallModel;
import pt.me.microm.model.trigger.SimpleTriggerModel;
import pt.me.microm.model.ui.TextModel;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * This class exposes a static method that allows the reading of a Level from an
 * SVG file
 * 
 * @author Márcio Neves
 * 
 */
public class LevelLoader {
	private static final String TAG = LevelLoader.class.getSimpleName();
	private static final Logger logger = LoggerFactory.getLogger(TAG);
	
	private LevelLoader() {}
	
	/**
	 * 
	 * @param board
	 * @param wm
	 */
	private static BoardModel addBoardToWorld(ArrayList<AbstractModel> modelBag, WorldModel wm, BasicShape board, String board_name) {
		if (GameMicroM.FLAG_DEBUG_POINTS) {
			DebugModel m;
			for (Vector2 ap : board.getPointsArray()) {
				m = DebugModel.getNewInstance(wm, ap.x+board.getCentroid().x, ap.y+board.getCentroid().y);
				m.setColor(Color.WHITE);
				modelBag.add(m);
			}
			m = DebugModel.getNewInstance(wm, board.getCentroid().x, board.getCentroid().y);
			m.setColor(Color.BLACK);
			modelBag.add(m);
		}		

		BoardModel bm = BoardModel.getNewInstance(wm, board, board_name);
		wm.setBoard(bm);
		
		modelBag.add(bm);
		
		return bm;
	}
	
	/**
	 * 
	 * @param wm
	 * @param dabox
	 * @param dabox_name
	 * @return
	 */
	private static DaBoxModel addDaBoxToWorld(ArrayList<AbstractModel> modelBag, WorldModel wm, BasicShape dabox, String dabox_name) {
		if (GameMicroM.FLAG_DEBUG_POINTS) {
			DebugModel m;
			for (Vector2 ap : dabox.getPointsArray()) {
					m = DebugModel.getNewInstance(wm, ap.x+dabox.getCentroid().x, ap.y+dabox.getCentroid().y);
					modelBag.add(m);
			}
		}
		
		DaBoxModel dbm = DaBoxModel.getNewInstance(wm, dabox, dabox_name); 
		wm.setPlayer(dbm);
		
		modelBag.add(dbm);
		
		return dbm;
	}	
	
	/**
	 * 
	 * @param spawn
	 * @param wm
	 */
	private static SpawnModel addSpawnToWorld(ArrayList<AbstractModel> modelBag, WorldModel wm, DaBoxModel dbm, BasicShape spawn, String spawn_name) {

		if (GameMicroM.FLAG_DEBUG_POINTS) {
			DebugModel m;
			for (Vector2 ap : spawn.getPointsArray()) {
				m = DebugModel.getNewInstance(wm, ap.x+spawn.getCentroid().x, ap.y+spawn.getCentroid().y);
				m.setColor(Color.BLUE);
				modelBag.add(m);
			}

			m = DebugModel.getNewInstance(wm, spawn.getCentroid().x, spawn.getCentroid().y);
			m.setColor(Color.CYAN);
			modelBag.add(m);
		}

		SpawnModel sm = SpawnModel.getNewInstance(wm, dbm, spawn, spawn_name);
		wm.setSpawnModel(sm);
		
		modelBag.add(sm);
		
		return sm;
	}

	/**
	 * 
	 * @param goal
	 * @param wm
	 */
	private static GoalModel addGoalToWorld(ArrayList<AbstractModel> modelBag, WorldModel wm, BasicShape goal, String goal_name) {
		if (GameMicroM.FLAG_DEBUG_POINTS) {
			DebugModel m;
			for (Vector2 ap : goal.getPointsArray()) {
				m = DebugModel.getNewInstance(wm, ap.x+goal.getCentroid().x, ap.y+goal.getCentroid().y);
				m.setColor(Color.GREEN);
				modelBag.add(m);
			}
			m = DebugModel.getNewInstance(wm, goal.getCentroid().x, goal.getCentroid().y);
			m.setColor(Color.GRAY);
			modelBag.add(m);
		}
		
		GoalModel gm = GoalModel.getNewInstance(wm, goal, goal_name);
		modelBag.add(gm);
		
		return gm;
	}
	
	/**
	 * 
	 * @param ground
	 * @param wm
	 */
	private static GroundModel addGroundToWorld(ArrayList<AbstractModel> modelBag, WorldModel wm, BasicShape ground, String ground_name) {
		if (GameMicroM.FLAG_DEBUG_POINTS) {
			DebugModel m;
			for (Vector2 ap : ground.getPointsArray()) {
				m = DebugModel.getNewInstance(wm, ap.x+ground.getCentroid().x, ap.y+ground.getCentroid().y);
				modelBag.add(m);
			}
		}
		
		GroundModel gm = GroundModel.getNewInstance(wm, ground, ground_name);
		modelBag.add(gm);
		
		return gm;
	}	
	
	
	/**
	 * 
	 * @param portal
	 * @param wm
	 */
	private static PortalModel addPortalToWorld(ArrayList<AbstractModel> modelBag, WorldModel wm, BasicShape portal, String portal_name) {
		if (GameMicroM.FLAG_DEBUG_POINTS) {
			DebugModel m;
			for (Vector2 ap : portal.getPointsArray()) {
				m = DebugModel.getNewInstance(wm, ap.x+portal.getCentroid().x, ap.y+portal.getCentroid().y);
				modelBag.add(m);
			}
			m = DebugModel.getNewInstance(wm, portal.getCentroid().x, portal.getCentroid().y);
			modelBag.add(m);
		}

		PortalModel pm = PortalModel.getNewInstance(wm, portal, portal_name);
		modelBag.add(pm);
		
		return pm;
	}


	
	/**
	 * 
	 * @param wall
	 * @param wm
	 */
	private static WallModel addWallToWorld(ArrayList<AbstractModel> modelBag, WorldModel wm, BasicShape wall, String wall_name) {
		if (GameMicroM.FLAG_DEBUG_POINTS) {
			DebugModel m;
			for (Vector2 ap : wall.getPointsArray()) {
				m = DebugModel.getNewInstance(wm, ap.x+wall.getCentroid().x, ap.y+wall.getCentroid().y);
				modelBag.add(m);
			}
		}
		
		WallModel wam = WallModel.getNewInstance(wm, wall, wall_name);
		modelBag.add(wam);
		
		return wam;
	}

	/**
	 * 
	 * @param star
	 * @param wm
	 */
	private static StarModel addStarToWorld(ArrayList<AbstractModel> modelBag, WorldModel wm, BasicShape star, String star_name) {
		if (GameMicroM.FLAG_DEBUG_POINTS) {
			DebugModel m = DebugModel.getNewInstance(wm, star.getCentroid().x, star.getCentroid().y);
			m.setColor(Color.WHITE);
			modelBag.add(m);
			for (Vector2 ap : star.getPointsArray()) {
				m = DebugModel.getNewInstance(wm, ap.x+star.getCentroid().x, ap.y+star.getCentroid().y);
				modelBag.add(m);
			}
		}
		StarModel sm = StarModel.getNewInstance(wm, star, star_name);
		modelBag.add(sm);
		
		return sm;
	}	
	
	/**
	 * 
	 * @param trigger
	 * @param wm
	 */
	private static SimpleTriggerModel addTriggerToWorld(ArrayList<AbstractModel> modelBag, WorldModel wm, BasicShape trigger, String trigger_name, String script) {
		if (GameMicroM.FLAG_DEBUG_POINTS) {
			DebugModel m;
			for (Vector2 ap : trigger.getPointsArray()) {
				m = DebugModel.getNewInstance(wm, ap.x+trigger.getCentroid().x, ap.y+trigger.getCentroid().y);
				m.setColor(Color.PINK);
				modelBag.add(m);
			}
		}
		SimpleTriggerModel tModel = SimpleTriggerModel.getNewInstance(wm, trigger, trigger_name);
		tModel.setScript(script);
		modelBag.add(tModel);
		return tModel;
		
	}
	

	
	private static TextModel addTextToWorld(ArrayList<AbstractModel> modelBag, WorldModel wm, BasicShape sh, String text_name, final String content) {
		if (GameMicroM.FLAG_DEBUG_POINTS) {
			DebugModel m;
			for (Vector2 ap : sh.getPointsArray()) {
				m = DebugModel.getNewInstance(wm, ap.x+sh.getCentroid().x, ap.y+sh.getCentroid().y);
				m.setColor(Color.PINK);
				modelBag.add(m);
			}					
		}
		TextModel tm = TextModel.getNewInstance(wm, sh, text_name, content);
		modelBag.add(tm);
		return tm;
		
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
	public static ArrayList<AbstractModel> LoadLevel(FileHandle h, WorldModel wm, CameraModel cm) {
		int nrElements = 0;
		ArrayList<AbstractModel> modelBag = new ArrayList<AbstractModel>();
		
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
			if (logger.isInfoEnabled()) logger.info("Camera...");
			expr = xpath.compile("//svg/g/path[contains(@id,'camera')]");
			NodeList camera = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < camera.getLength(); i++) {
				String d = camera.item(i).getAttributes().getNamedItem("d").getNodeValue();
				String style = camera.item(i).getAttributes().getNamedItem("style").getNodeValue();
				if (logger.isInfoEnabled()) logger.info("d= " + d + "; style= " + style + ";");

				BasicShape s = new BasicShape(d, style, ObjectType.CAMERA);

				String camera_name = camera.item(i).getAttributes().getNamedItem("id").getNodeValue();
				
				// here we configure the camera
				cm.adjustCamera(s.getWidth(), s.getHeight(), s.getCentroid().x, s.getCentroid().y);
				
				nrElements+=1;
			}
			
			
			// Get Board
			if (logger.isInfoEnabled()) logger.info("Board...");
			expr = xpath.compile("//svg/g/path[contains(@id,'board')]");
			NodeList board = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < board.getLength(); i++) {
				String d = board.item(i).getAttributes().getNamedItem("d").getNodeValue();
				String style = board.item(i).getAttributes().getNamedItem("style").getNodeValue();
				if (logger.isInfoEnabled()) logger.info("d= " + d + "; style= " + style + ";");

				BasicShape s = new BasicShape(d, style, ObjectType.BOARD);
				String board_name = board.item(i).getAttributes().getNamedItem("id").getNodeValue();
				addBoardToWorld(modelBag, wm, s, board_name);
				
				nrElements+=1;
			}

			// Get DaBox
			DaBoxModel daBoxRef = null;
			if (logger.isInfoEnabled()) logger.info("DaBox...");
			expr = xpath.compile("//svg/g/path[contains(@id,'daBox')]");
			NodeList dabox = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < dabox.getLength(); i++) {
				String d = dabox.item(i).getAttributes().getNamedItem("d").getNodeValue();
				String style = dabox.item(i).getAttributes().getNamedItem("style").getNodeValue();
				if (logger.isInfoEnabled()) logger.info("d= " + d + "; style= " + style + ";");

				BasicShape s = new BasicShape(d, style, ObjectType.DABOX);
				String dabox_name = dabox.item(i).getAttributes().getNamedItem("id").getNodeValue();
				daBoxRef = addDaBoxToWorld(modelBag, wm, s, dabox_name);

				nrElements+=1;
			}

			// Get Spawn
			if (logger.isInfoEnabled()) logger.info("Spawn...");
			expr = xpath.compile("//svg/g/path[contains(@id,'spawn')]");
			NodeList spawn = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < spawn.getLength(); i++) {
				String d = spawn.item(i).getAttributes().getNamedItem("d").getNodeValue();
				String style = spawn.item(i).getAttributes().getNamedItem("style").getNodeValue();
				if (logger.isInfoEnabled()) logger.info("d= " + d + "; style= " + style + ";");

				BasicShape s = new BasicShape(d, style, ObjectType.SPAWN);
				String spawn_name = spawn.item(i).getAttributes().getNamedItem("id").getNodeValue();
				addSpawnToWorld(modelBag, wm, daBoxRef, s, spawn_name);

				nrElements+=1;
			}

			// Get Goals
			if (logger.isInfoEnabled()) logger.info("Goals...");
			expr = xpath.compile("//svg/g/path[contains(@id,'goal')]");
			NodeList goals = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < goals.getLength(); i++) {
				String d = goals.item(i).getAttributes().getNamedItem("d").getNodeValue();
				String style = goals.item(i).getAttributes().getNamedItem("style").getNodeValue();
				if (logger.isInfoEnabled()) logger.info("d= " + d + "; style= " + style + ";");

				BasicShape s = new BasicShape(d, style, ObjectType.GOAL);
				String goal_name = goals.item(i).getAttributes().getNamedItem("id").getNodeValue();
				addGoalToWorld(modelBag, wm, s, goal_name);

				nrElements+=1;
			}

			// Get Grounds
			if (logger.isInfoEnabled()) logger.info("Grounds...");
			expr = xpath.compile("//svg/g/path[contains(@id,'ground')]");
			NodeList grounds = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < grounds.getLength(); i++) {
				String d = grounds.item(i).getAttributes().getNamedItem("d").getNodeValue();
				String style = grounds.item(i).getAttributes().getNamedItem("style").getNodeValue();
				if (logger.isInfoEnabled()) logger.info("d= " + d + "; style= " + style + ";");

				BasicShape s = new BasicShape(d, style, ObjectType.GROUND);
				String ground_name = grounds.item(i).getAttributes().getNamedItem("id").getNodeValue();
				addGroundToWorld(modelBag, wm, s, ground_name);

				nrElements+=1;
			}

			// Get portals
			if (logger.isInfoEnabled()) logger.info("Portals...");
			//expr = xpath.compile("//svg/g/path[contains(@id,'portal')]/@d");
			expr = xpath.compile("//svg/g/path[contains(@id,'portal')]");
			NodeList portals = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < portals.getLength(); i++) {
				String d = portals.item(i).getAttributes().getNamedItem("d").getNodeValue();
				String style = portals.item(i).getAttributes().getNamedItem("style").getNodeValue();
				if (logger.isInfoEnabled()) logger.info("d= " + d + "; style= " + style + ";");

				BasicShape s = new BasicShape(d, style, ObjectType.PORTAL);
				String portal_name = portals.item(i).getAttributes().getNamedItem("id").getNodeValue();
				addPortalToWorld(modelBag, wm, s, portal_name);

				nrElements+=1;
			}

			// Get walls
			if (logger.isInfoEnabled()) logger.info("Walls...");
			//expr = xpath.compile("//svg/g/path[contains(@id,'portal')]/@d");
			expr = xpath.compile("//svg/g/path[contains(@id,'wall')]");
			NodeList walls = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < walls.getLength(); i++) {
				String d = walls.item(i).getAttributes().getNamedItem("d").getNodeValue();
				String style = walls.item(i).getAttributes().getNamedItem("style").getNodeValue();
				if (logger.isInfoEnabled()) logger.info("d= " + d + "; style= " + style + ";");

				BasicShape s = new BasicShape(d, style, ObjectType.WALL);
				String wall_name = walls.item(i).getAttributes().getNamedItem("id").getNodeValue();
				addWallToWorld(modelBag, wm, s, wall_name);

				nrElements+=1;
			}

			// Get stars
			if (logger.isInfoEnabled()) logger.info("Stars...");
			//expr = xpath.compile("//svg/g/path[contains(@id,'portal')]/@d");
			expr = xpath.compile("//svg/g/path[contains(@id,'star')]");
			NodeList stars = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < stars.getLength(); i++) {
				String d = stars.item(i).getAttributes().getNamedItem("d").getNodeValue();
				String style = stars.item(i).getAttributes().getNamedItem("style").getNodeValue();
				if (logger.isInfoEnabled()) logger.info("d= " + d + "; style= " + style + ";");

				BasicShape s = new BasicShape(d, style, ObjectType.STAR);
				String star_name = stars.item(i).getAttributes().getNamedItem("id").getNodeValue();
				addStarToWorld(modelBag, wm, s, star_name);

				nrElements+=1;
			}


			// Get text
			if (logger.isInfoEnabled()) logger.info("Text...");
			expr = xpath.compile("//svg/g/text[contains(@id,'text')]/tspan");
			NodeList text = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < text.getLength(); i++) {
				String id = text.item(i).getAttributes().getNamedItem("id").getNodeValue();
				if (logger.isInfoEnabled()) logger.info("[" + id + "]");

				String x = text.item(i).getAttributes().getNamedItem("x").getNodeValue();
				String y = text.item(i).getAttributes().getNamedItem("y").getNodeValue();
				String s = text.item(i).getTextContent();

				if (logger.isInfoEnabled()) logger.info("[" + id + "] = x: " + x + "; y: " + y + "; ==> '" + s + "'" );
				BasicShape sh = new BasicShape("m " + x + "," + y, "", ObjectType.TEXT);
				if (logger.isInfoEnabled()) logger.info(".:.:.:. " + sh.getCentroid() + " .:.:.:.");

				addTextToWorld(modelBag, wm, sh, id, s);

				nrElements+=1;
			}


			// Get triggers
			if (logger.isInfoEnabled()) logger.info("Triggers...");
			expr = xpath.compile("//svg/g/path[contains(@id,'trigger')]");
			NodeList triggers = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < triggers.getLength(); i++) {
				String d = triggers.item(i).getAttributes().getNamedItem("d").getNodeValue();
				String style = triggers.item(i).getAttributes().getNamedItem("style").getNodeValue();
				if (logger.isInfoEnabled()) logger.info("d= " + d + "; style= " + style + ";");

				BasicShape s = new BasicShape(d, style, ObjectType.TRIGGER);
				String trigger_name = triggers.item(i).getAttributes().getNamedItem("id").getNodeValue();
				String script = triggers.item(i).getAttributes().getNamedItem("custom-script").getNodeValue();
				addTriggerToWorld(modelBag, wm, s, trigger_name, script);

				nrElements+=1;
			}
			
			if (logger.isInfoEnabled()) logger.info("Finished Loading level: " + h.name());
		
		} catch (ParserConfigurationException e) {
			if (logger.isErrorEnabled()) logger.error(e.getMessage());
			e.printStackTrace();
		} catch (SAXException e) {
			if (logger.isErrorEnabled()) logger.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			if (logger.isErrorEnabled()) logger.error(e.getMessage());
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			if (logger.isErrorEnabled()) logger.error(e.getMessage());
			e.printStackTrace();
		}
		
		int check = 0;
		for (AbstractModel am : modelBag) {
			if (am instanceof DebugModel) check+=1;
		}
		// the (-1) is because the camera loading parameters doesn't create a new model
		if (logger.isDebugEnabled()) logger.debug("size of bag is {}; # of non debug point models is {}; # of debug points model is {}", modelBag.size(), nrElements-1, check);
		
		return modelBag;
	}
	
}
