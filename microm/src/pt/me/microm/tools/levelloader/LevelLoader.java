package pt.me.microm.tools.levelloader;

import java.io.File;
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

import pt.me.microm.MicroMGame;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.model.dev.DebugModel;
import pt.me.microm.model.stuff.BoardModel;
import pt.me.microm.model.stuff.DaBoxModel;
import pt.me.microm.model.stuff.GoalModel;
import pt.me.microm.model.stuff.GroundModel;
import pt.me.microm.model.stuff.PortalModel;
import pt.me.microm.model.stuff.SpawnModel;
import pt.me.microm.model.stuff.WallModel;

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
	private static final Logger logger = new Logger(TAG);
	
	private static float xOffset;
	private static float yOffset;
	private static float maxWidth;
	private static float maxHeight;
	private static float scale;
	
	private LevelLoader() {}
	

	
	/**
	 * 
	 * @param board
	 * @param wm
	 */
	private static void addBoardToWorld(BasicShape board, WorldModel wm) {
		if (MicroMGame.ISDEV) {
			DebugModel m;
			for (Vector2 ap : board.getPoints()) {
				m = DebugModel.getNewInstance(wm, ap.x, ap.y);
				m.setColor(Color.WHITE);
			}
			m = DebugModel.getNewInstance(wm, board.getCentroid().x, board.getCentroid().y);
			m.setColor(Color.BLACK);
		}		

		wm.setBoard(BoardModel.getNewInstance(wm, board, board.getPoints()));
	}
	
	/**
	 * 
	 * @param spawn
	 * @param wm
	 */
	private static void addSpawnToWorld(BasicShape spawn, WorldModel wm) {

		if (MicroMGame.ISDEV) {
			DebugModel m;
			for (Vector2 ap : spawn.getPoints()) {
				m = DebugModel.getNewInstance(wm, ap.x, ap.y);
				m.setColor(Color.BLUE);
			}

			m = DebugModel.getNewInstance(wm, spawn.getCentroid().x, spawn.getCentroid().y);
			m.setColor(Color.CYAN);
		}

		wm.spawnModel = SpawnModel.getNewInstance(wm, spawn, spawn.getPoints());
	}

	/**
	 * 
	 * @param goal
	 * @param wm
	 */
	private static void addGoalToWorld(BasicShape goal, WorldModel wm) {
		if (MicroMGame.ISDEV) {
			DebugModel m;
			for (Vector2 ap : goal.getPoints()) {
				m = DebugModel.getNewInstance(wm, ap.x, ap.y);
				m.setColor(Color.GREEN);
			}
			m = DebugModel.getNewInstance(wm, goal.getCentroid().x, goal.getCentroid().y);
			m.setColor(Color.GRAY);

		}
		
		GoalModel.getNewInstance(wm, goal, goal.getPoints());			
	}
	
	/**
	 * 
	 * @param ground
	 * @param wm
	 */
	private static void addGroundToWorld(BasicShape ground, WorldModel wm) {
		if (MicroMGame.ISDEV)
			for (Vector2 ap : ground.getPoints()) {
				DebugModel.getNewInstance(wm, ap.x, ap.y);
			}
		
		GroundModel.getNewInstance(wm, ground.getPoints());
	}	
	
	
	/**
	 * 
	 * @param portal
	 * @param wm
	 */
	private static void addPortalToWorld(BasicShape portal, WorldModel wm, String portal_name) {
		if (MicroMGame.ISDEV) {
			for (Vector2 ap : portal.getPoints()) {
				DebugModel.getNewInstance(wm, ap.x, ap.y);
			}
			DebugModel.getNewInstance(wm, portal.getCentroid().x, portal.getCentroid().y);
		}

		PortalModel.getNewInstance(wm, portal, portal_name);
	}


	
	/**
	 * 
	 * @param wall
	 * @param wm
	 */
	private static void addWallToWorld(BasicShape wall, WorldModel wm, String wall_name) {
		if (MicroMGame.ISDEV)
		for (Vector2 ap : wall.getPoints()) {
				DebugModel.getNewInstance(wm, ap.x, ap.y);
		}
		
		
		WallModel.getNewInstance(wm, wall);
		
	}
	
	
	private static void addDaBoxToWorld(WorldModel wm) {
		
		wm.setPlayer(DaBoxModel.getNewInstance(wm, scale, 0.75f/2, 12.0f));
	}
	
	
	/**
	 * This function loads a level from a SVG file. It makes a lot of
	 * assumptions about the elements contained on that SVG. No safeguards are
	 * implemented... yet...
	 * 
	 * @param h
	 *            The handle to the SVG file that represents a level
	 * @param w
	 *            The box2d World object
	 * @return number of assets loaded
	 */
	public static int LoadLevel(FileHandle h, WorldModel wm) {
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
			// Get Board
			if (logger.getLevel() == logger.INFO) logger.info("Board...");
			expr = xpath.compile("//svg/g/path[contains(@id,'board')]/@d");
			NodeList board = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < board.getLength(); i++) {
				String d = board.item(i).getNodeValue();
				if (logger.getLevel() == logger.INFO) logger.info(d);
				BasicShape s = new BasicShape(d);
				// O offset do nível é igual ao offset do 1º ponto da "board"
				xOffset = s.getPoints().get(0).x;
				yOffset = s.getPoints().get(0).y;

				// O offset do elemento "board" agora já pode ser zero
				s.offsetShape(new Vector2(xOffset, yOffset));			
				
				// Largura e Altura limites do nivel para efeitos de scaling
				for (Vector2 p : s.getPoints()) {
					if (p.x > maxWidth) maxWidth = p.x;
					if (p.y > maxHeight) maxHeight = p.y;
				}

				// Calculo do scale com base no tamanho do board
				scale = GAME_CONSTANTS.MODEL_SCREEN_WIDTH_CAPACITY / maxWidth;
				
				s.setType(ObjectType.BOARD);
				
				// scaling
				for (Vector2 ap : s.getPoints()) {
					ap.x = ap.x*scale;
					ap.y = (maxHeight - ap.y)*scale;			
				}
				s.getCentroid().x = s.getCentroid().x*scale;
				s.getCentroid().y = (maxHeight - s.getCentroid().y)*scale;
				
				if (logger.getLevel() == logger.INFO) logger.info(s.toString());
				addBoardToWorld(s, wm);
				
				nrElements+=1;
			}
			
			// Get Spawn
			if (logger.getLevel() == logger.INFO) logger.info("Spawn...");
			expr = xpath.compile("//svg/g/path[contains(@id,'spawn')]/@d");
			NodeList spawn = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < spawn.getLength(); i++) {
				String d = spawn.item(i).getNodeValue();
				if (logger.getLevel() == logger.INFO) logger.info(d);
				BasicShape s = new BasicShape(d);
				s.offsetShape(new Vector2(xOffset, yOffset));
				
				s.setType(ObjectType.SPAWN);
				if (logger.getLevel() == logger.INFO) logger.info(s.toString());

				// scaling
				for (Vector2 ap : s.getPoints()) {
					ap.x = ap.x*scale;
					ap.y = (maxHeight - ap.y)*scale;
				}
				s.getCentroid().x = s.getCentroid().x*scale;
				s.getCentroid().y = (maxHeight - s.getCentroid().y)*scale;
				
				addSpawnToWorld(s, wm);
				nrElements+=1;
			}
			
			// Get Goals
			if (logger.getLevel() == logger.INFO) logger.info("Goals...");
			expr = xpath.compile("//svg/g/path[contains(@id,'goal')]/@d");
			NodeList goals = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < goals.getLength(); i++) {
				String d = goals.item(i).getNodeValue();
				if (logger.getLevel() == logger.INFO) logger.info(d);
				BasicShape s = new BasicShape(d);
				s.offsetShape(new Vector2(xOffset, yOffset));
				
				s.setType(ObjectType.GOAL);
				if (logger.getLevel() == logger.INFO) logger.info(s.toString());

				// scaling				
				for (Vector2 ap : s.getPoints()) {
					ap.x = ap.x*scale;
					ap.y = (maxHeight - ap.y)*scale;			
				}
				s.getCentroid().x = s.getCentroid().x*scale;
				s.getCentroid().y = (maxHeight - s.getCentroid().y)*scale;				
				
				addGoalToWorld(s, wm);
				nrElements+=1;
			}

			// Get Grounds
			if (logger.getLevel() == logger.INFO) logger.info("Grounds...");
			expr = xpath.compile("//svg/g/path[contains(@id,'ground')]/@d");
			NodeList grounds = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < grounds.getLength(); i++) {
				String d = grounds.item(i).getNodeValue();
				if (logger.getLevel() == logger.INFO) logger.info(d);
				BasicShape s = new BasicShape(d);
				s.offsetShape(new Vector2(xOffset, yOffset));
				
				s.setType(ObjectType.GROUND);
				if (logger.getLevel() == logger.INFO) logger.info(s.toString());
				
				// scaling				
				for (Vector2 ap : s.getPoints()) {
					ap.x = ap.x*scale;
					ap.y = (maxHeight - ap.y)*scale;
				}				
				s.getCentroid().x = s.getCentroid().x*scale;
				s.getCentroid().y = (maxHeight - s.getCentroid().y)*scale;
				
				addGroundToWorld(s, wm);
				nrElements+=1;
			}

			// Get portals
			if (logger.getLevel() == logger.INFO) logger.info("Portals...");
			//expr = xpath.compile("//svg/g/path[contains(@id,'portal')]/@d");
			expr = xpath.compile("//svg/g/path[contains(@id,'portal')]");
			NodeList portals = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < portals.getLength(); i++) {
				String d = portals.item(i).getAttributes().getNamedItem("d").getNodeValue();
				if (logger.getLevel() == logger.INFO) logger.info(d);
				BasicShape s = new BasicShape(d);
				s.offsetShape(new Vector2(xOffset, yOffset));
				
				s.setType(ObjectType.PORTAL);
				if (logger.getLevel() == logger.INFO) logger.info(s.toString());
				String portal_name = portals.item(i).getAttributes().getNamedItem("id").getNodeValue();
				
				// scaling
				for (Vector2 ap : s.getPoints()) {
					ap.x = ap.x*scale;
					ap.y = (maxHeight - ap.y)*scale;
				}				
				s.getCentroid().x = s.getCentroid().x*scale;
				s.getCentroid().y = (maxHeight - s.getCentroid().y)*scale;				
				
				addPortalToWorld(s, wm, portal_name);
				nrElements+=1;
			}

			// Get walls
			if (logger.getLevel() == logger.INFO) logger.info("Walls...");
			//expr = xpath.compile("//svg/g/path[contains(@id,'portal')]/@d");
			expr = xpath.compile("//svg/g/path[contains(@id,'wall')]");
			NodeList walls = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < walls.getLength(); i++) {
				String d = walls.item(i).getAttributes().getNamedItem("d").getNodeValue();
				if (logger.getLevel() == logger.INFO) logger.info(d);
				BasicShape s = new BasicShape(d);
				s.offsetShape(new Vector2(xOffset, yOffset));
				
				s.setType(ObjectType.WALL);
				if (logger.getLevel() == logger.INFO) logger.info(s.toString());
				String wall_name = walls.item(i).getAttributes().getNamedItem("id").getNodeValue();
				
				// scaling				
				for (Vector2 ap : s.getPoints()) {
					ap.x = ap.x*scale;
					ap.y = (maxHeight - ap.y)*scale;
				}				
				s.getCentroid().x = s.getCentroid().x*scale;
				s.getCentroid().y = (maxHeight - s.getCentroid().y)*scale;				
				
				addWallToWorld(s, wm, wall_name);
				nrElements+=1;
			}
			
			addDaBoxToWorld(wm);
			
			if (logger.getLevel() == logger.INFO) logger.info("Finished Loading level: " + h.name());
		
		} catch (ParserConfigurationException e) {
			if (logger.getLevel() == logger.ERROR) logger.error(e.getMessage());
			e.printStackTrace();
		} catch (SAXException e) {
			if (logger.getLevel() == logger.ERROR) logger.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			if (logger.getLevel() == logger.ERROR) logger.error(e.getMessage());
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			if (logger.getLevel() == logger.ERROR) logger.error(e.getMessage());
			e.printStackTrace();
		}
		return nrElements;
	}
	
}
