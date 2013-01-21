package pt.me.microm.tools.levelloader;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import pt.me.microm.model.stuff.GroundModel;
import pt.me.microm.tools.levelloader.shape.BasicShape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
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
	 * This function fetches the points from a SVG 'd' element
	 * @param d
	 * @return
	 */
	private static BasicShape getPointsFromD(String d) {
		BasicShape newShape = new BasicShape();
		newShape.setD(d);
		
		//Pattern pattern = Pattern.compile("[-\\d]+[\\.\\d]*");//Detecta numero a numero (x ou y)
		Pattern pattern = Pattern.compile("[-\\d]+[-\\.\\d,]*");//Detecta coordenada (par x,y)
		
		Matcher matcher;		
		matcher = pattern.matcher(d);
		
		String s;
		String [] ssplit;
		Vector2 pt;
		int i = 0;
		while (matcher.find()) {
			s = matcher.group();
			Gdx.app.log(TAG, "val: " + s);
			ssplit = s.split(",");
			
			pt = new Vector2();
			if (i==0) {			
				pt.x = Float.parseFloat(ssplit[0]);
				pt.y = Float.parseFloat(ssplit[1]);
			}
			else {
				pt.x = Float.parseFloat(ssplit[0]) + newShape.getPoints().get(i-1).x;
				pt.y = Float.parseFloat(ssplit[1]) + newShape.getPoints().get(i-1).y;
			}
			
			newShape.getPoints().add(pt);
			i+=1;
		}
	
		return newShape;
	}
	
	/**
	 * 
	 * @param board
	 * @param wm
	 */
	private static void addBoardToWorld(BasicShape board, WorldModel wm) {
		for (Vector2 ap : board.getPoints()) {
			DebugModel.getNewInstance(wm, ap.x*scale, (maxHeight - ap.y)*scale);
		}
	}
	
	/**
	 * 
	 * @param spawn
	 * @param wm
	 */
	private static void addSpawnToWorld(BasicShape spawn, WorldModel wm) {
		for (Vector2 ap : spawn.getPoints()) {
			DebugModel.getNewInstance(wm, ap.x*scale, (maxHeight - ap.y)*scale);
		}
	}

	/**
	 * 
	 * @param goal
	 * @param wm
	 */
	private static void addGoalToWorld(BasicShape goal, WorldModel wm) {
		for (Vector2 ap : goal.getPoints()) {
			DebugModel.getNewInstance(wm, ap.x*scale, (maxHeight - ap.y)*scale);
		}				
	}
	
	/**
	 * 
	 * @param portal
	 * @param wm
	 */
	private static void addPortalToWorld(BasicShape portal, WorldModel wm) {
		for (Vector2 ap : portal.getPoints()) {
			DebugModel.getNewInstance(wm, ap.x*scale, (maxHeight - ap.y)*scale);
		}
	}
	
	/**
	 * 
	 * @param ground
	 * @param wm
	 */
	private static void addGroundToWorld(BasicShape ground, WorldModel wm) {
		for (Vector2 ap : ground.getPoints()) {
			DebugModel.getNewInstance(wm, ap.x*scale, (maxHeight - ap.y)*scale);
			ap.x = ap.x*scale;
			ap.y = (maxHeight - ap.y)*scale;
		}
		GroundModel.getNewInstance(wm, ground.getPoints());
	}
	
	/**
	 * 
	 * @param shape
	 * @param offset
	 */
	private static void offsetShape(BasicShape shape, Vector2 offset) {
		for (Vector2 p : shape.getPoints()) {
			p.x = p.x - offset.x;
			p.y = p.y - offset.y;
		}
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
				BasicShape s = getPointsFromD(d);
				// O offset do nível é igual ao offset do elemento "board"
				xOffset = s.getPoints().get(0).x;
				yOffset = s.getPoints().get(0).y;

				// O offset do elemento "board" agora já pode ser zero
				offsetShape(s, new Vector2(xOffset, yOffset));			
				
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
				BasicShape s = getPointsFromD(d);
				offsetShape(s, new Vector2(xOffset, yOffset));
				
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
				BasicShape s = getPointsFromD(d);
				offsetShape(s, new Vector2(xOffset, yOffset));
				
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
				BasicShape s = getPointsFromD(d);
				offsetShape(s, new Vector2(xOffset, yOffset));
				
				s.setType(ObjectType.GROUND);
				Gdx.app.log(TAG, s.toString());
				addGroundToWorld(s, wm);
				nrElements+=1;
			}

			// Get portals
			Gdx.app.log(TAG, "Portals...");
			expr = xpath.compile("//svg/g/path[contains(@id,'portal')]/@d");
			NodeList portals = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < portals.getLength(); i++) {
				String d;
				Gdx.app.log(TAG, d = portals.item(i).getNodeValue());
				BasicShape s = getPointsFromD(d);
				offsetShape(s, new Vector2(xOffset, yOffset));
				
				s.setType(ObjectType.PORTAL);
				Gdx.app.log(TAG, s.toString());
				addPortalToWorld(s, wm);
				nrElements+=1;
			}
			
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
