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

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.model.dev.DebugModel;
import pt.me.microm.model.stuff.BoardModel;
import pt.me.microm.model.stuff.DaBoxModel;
import pt.me.microm.model.stuff.GoalModel;
import pt.me.microm.model.stuff.GroundModel;
import pt.me.microm.model.stuff.PortalModel;
import pt.me.microm.model.stuff.SpawnModel;

import com.badlogic.gdx.Gdx;
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
		DebugModel m;
		for (Vector2 ap : board.getPoints()) {
			ap.x = ap.x*scale;
			ap.y = (maxHeight - ap.y)*scale;			
			m = DebugModel.getNewInstance(wm, ap.x, ap.y);
			m.setColor(Color.WHITE);
		}
		board.getCentroid().x = board.getCentroid().x*scale;
		board.getCentroid().y = (maxHeight - board.getCentroid().y)*scale;
		m = DebugModel.getNewInstance(wm, board.getCentroid().x, board.getCentroid().y);
		m.setColor(Color.BLACK);
		
		wm.setBoard(BoardModel.getNewInstance(wm, board, board.getPoints()));
		
	}
	
	/**
	 * 
	 * @param spawn
	 * @param wm
	 */
	private static void addSpawnToWorld(BasicShape spawn, WorldModel wm) {
		DebugModel m;
		for (Vector2 ap : spawn.getPoints()) {
			ap.x = ap.x*scale;
			ap.y = (maxHeight - ap.y)*scale;
			m = DebugModel.getNewInstance(wm, ap.x, ap.y);
			m.setColor(Color.BLUE);
		}
		spawn.getCentroid().x = spawn.getCentroid().x*scale;
		spawn.getCentroid().y = (maxHeight - spawn.getCentroid().y)*scale;
		m = DebugModel.getNewInstance(wm, spawn.getCentroid().x, spawn.getCentroid().y);
		m.setColor(Color.CYAN);
		
		SpawnModel.getNewInstance(wm, spawn, spawn.getPoints());
	}

	/**
	 * 
	 * @param goal
	 * @param wm
	 */
	private static void addGoalToWorld(BasicShape goal, WorldModel wm) {
		DebugModel m;
		for (Vector2 ap : goal.getPoints()) {
			ap.x = ap.x*scale;
			ap.y = (maxHeight - ap.y)*scale;			
			m = DebugModel.getNewInstance(wm, ap.x, ap.y);
			m.setColor(Color.GREEN);
		}
		goal.getCentroid().x = goal.getCentroid().x*scale;
		goal.getCentroid().y = (maxHeight - goal.getCentroid().y)*scale;
		m = DebugModel.getNewInstance(wm, goal.getCentroid().x, goal.getCentroid().y);
		m.setColor(Color.GRAY);		
		
		GoalModel.getNewInstance(wm, goal, goal.getPoints());			
	}
	
	/**
	 * 
	 * @param portal
	 * @param wm
	 */
	private static void addPortalToWorld(BasicShape portal, WorldModel wm, String portal_name) {
		for (Vector2 ap : portal.getPoints()) {
			ap.x = ap.x*scale;
			ap.y = (maxHeight - ap.y)*scale;	
			DebugModel.getNewInstance(wm, ap.x, ap.y);
		}
		portal.getCentroid().x = portal.getCentroid().x*scale;
		portal.getCentroid().y = (maxHeight - portal.getCentroid().y)*scale;
		DebugModel.getNewInstance(wm, portal.getCentroid().x, portal.getCentroid().y);
		
		PortalModel.getNewInstance(wm, portal, portal_name);
	}

	/**
	 * 
	 * @param ground
	 * @param wm
	 */
	private static void addGroundToWorld(BasicShape ground, WorldModel wm) {
		for (Vector2 ap : ground.getPoints()) {
			ap.x = ap.x*scale;
			ap.y = (maxHeight - ap.y)*scale;
			DebugModel.getNewInstance(wm, ap.x, ap.y);
		}
		GroundModel.getNewInstance(wm, ground.getPoints());
	}
	
	/**
	 * 
	 * @param wall
	 * @param wm
	 */
	private static void addWallToWorld(BasicShape wall, WorldModel wm, String wall_name) {
		for (Vector2 ap : wall.getPoints()) {
			ap.x = ap.x*scale;
			ap.y = (maxHeight - ap.y)*scale;
			DebugModel.getNewInstance(wm, ap.x, ap.y);
		}
		
		
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
			Document doc = builder.parse(h.file());
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			//XPathExpression expr = xpath.compile("//book[author='Neal Stephenson']/title/text()");
			//expr.evaluate(doc, XPathConstants.STRING);

			XPathExpression expr; 
			// Get Board
			Gdx.app.log(TAG, "Board...");
			expr = xpath.compile("//svg/g/path[contains(@id,'board')]/@d");
			NodeList board = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < board.getLength(); i++) {
				String d;
				Gdx.app.log(TAG, d = board.item(i).getNodeValue());
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

				// Calculo do scale
				scale = GAME_CONSTANTS.MODEL_SCREEN_WIDTH_CAPACITY / maxWidth;
				
				s.setType(ObjectType.BOARD);
				
				Gdx.app.log(TAG, s.toString());
				addBoardToWorld(s, wm);
				nrElements+=1;
			}
			
			// Get Spawn
			Gdx.app.log(TAG, "Spawn...");
			expr = xpath.compile("//svg/g/path[contains(@id,'spawn')]/@d");
			NodeList spawn = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < spawn.getLength(); i++) {
				String d;
				Gdx.app.log(TAG, d = spawn.item(i).getNodeValue());
				BasicShape s = new BasicShape(d);
				s.offsetShape(new Vector2(xOffset, yOffset));
				
				s.setType(ObjectType.SPAWN);
				Gdx.app.log(TAG, s.toString());
				addSpawnToWorld(s, wm);
				nrElements+=1;
			}
			
			// Get Goals
			Gdx.app.log(TAG, "Goals...");
			expr = xpath.compile("//svg/g/path[contains(@id,'goal')]/@d");
			NodeList goals = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < goals.getLength(); i++) {
				String d;
				Gdx.app.log(TAG, d = goals.item(i).getNodeValue());
				BasicShape s = new BasicShape(d);
				s.offsetShape(new Vector2(xOffset, yOffset));
				
				s.setType(ObjectType.GOAL);
				Gdx.app.log(TAG, s.toString());
				addGoalToWorld(s, wm);
				nrElements+=1;
			}

			// Get Grounds
			Gdx.app.log(TAG, "Grounds...");
			expr = xpath.compile("//svg/g/path[contains(@id,'ground')]/@d");
			NodeList grounds = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < grounds.getLength(); i++) {
				String d;
				Gdx.app.log(TAG, d = grounds.item(i).getNodeValue());
				BasicShape s = new BasicShape(d);
				s.offsetShape(new Vector2(xOffset, yOffset));
				
				s.setType(ObjectType.GROUND);
				Gdx.app.log(TAG, s.toString());
				addGroundToWorld(s, wm);
				nrElements+=1;
			}

			// Get portals
			Gdx.app.log(TAG, "Portals...");
			//expr = xpath.compile("//svg/g/path[contains(@id,'portal')]/@d");
			expr = xpath.compile("//svg/g/path[contains(@id,'portal')]");
			NodeList portals = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < portals.getLength(); i++) {
				String d;
				Gdx.app.log(TAG, d = portals.item(i).getAttributes().getNamedItem("d").getNodeValue());
				BasicShape s = new BasicShape(d);
				s.offsetShape(new Vector2(xOffset, yOffset));
				
				s.setType(ObjectType.PORTAL);
				Gdx.app.log(TAG, s.toString());
				String portal_name = portals.item(i).getAttributes().getNamedItem("id").getNodeValue();
				addPortalToWorld(s, wm, portal_name);
				nrElements+=1;
			}

			// Get walls
			Gdx.app.log(TAG, "Walls...");
			//expr = xpath.compile("//svg/g/path[contains(@id,'portal')]/@d");
			expr = xpath.compile("//svg/g/path[contains(@id,'wall')]");
			NodeList walls = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < walls.getLength(); i++) {
				String d;
				Gdx.app.log(TAG, d = walls.item(i).getAttributes().getNamedItem("d").getNodeValue());
				BasicShape s = new BasicShape(d);
				s.offsetShape(new Vector2(xOffset, yOffset));
				
				s.setType(ObjectType.WALL);
				Gdx.app.log(TAG, s.toString());
				String wall_name = walls.item(i).getAttributes().getNamedItem("id").getNodeValue();
				addWallToWorld(s, wm, wall_name);
				nrElements+=1;
			}
			
			addDaBoxToWorld(wm);
			
			Gdx.app.log(TAG, "Finished Loading level: " + h.name());
		
		} catch (ParserConfigurationException e) {
			Gdx.app.error(TAG, e.getMessage());
			e.printStackTrace();
		} catch (SAXException e) {
			Gdx.app.error(TAG, e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Gdx.app.error(TAG, e.getMessage());
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			Gdx.app.error(TAG, e.getMessage());
			e.printStackTrace();
		}
		return nrElements;
	}
	
}
