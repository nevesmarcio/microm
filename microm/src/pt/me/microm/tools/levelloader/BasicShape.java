package pt.me.microm.tools.levelloader;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.me.microm.infrastructure.GAME_CONSTANTS;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Logger;

/**
 * Esta BasicShape deverá receber as coordenadas do SVG, mas após a construcção
 * do objecto, deverá manter apenas coordenadas compativeis com o nível e com o
 * Box2D.
 * 
 * Este objecto guarda a posição do shape no centroid e guarda a propria shape com coordenadas centradas em torno do 0.0f, 0.0f
 * Avaliar se pretendo que seja efectivamente o centroid a fornecer a posição, ou se quero definir um centroid, e guardar a posição noutra variável
 * 
 * @author mneves
 * 
 */
public class BasicShape {
	private static final String TAG = BasicShape.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private String d;
	private ArrayList<Vector2> points;
	private ObjectType type;
	private Vector2 centroid;

	private String style;
	private Color color;
	
	private float width;
	private float height;
	
	public BasicShape() {
		points = new ArrayList<Vector2>();
	}

	/**
	 * This constructor builds a shape using the points specified by a SVG 'd'
	 * element 
	 * @param d
	 */
	public BasicShape(String d, String style, Vector2 offset, Vector2 maxSize, ObjectType type) {
		this();
		this.d = d;
		
		Pattern pattern;
		Matcher matcher;
		
		//pattern = Pattern.compile("[-\\d]+[\\.\\d]*");//Detecta numero a numero (x ou y)
		pattern = Pattern.compile("[-\\d]+[-\\.\\d,]*");//Detecta coordenada (par x,y)
		matcher = pattern.matcher(d);
		
		String s;
		String [] ssplit;
		Vector2 pt;
		int i = 0;
		while (matcher.find()) {
			s = matcher.group();
			if (logger.getLevel() >= Logger.DEBUG) logger.debug("val: " + s);
			ssplit = s.split(",");
			
			pt = new Vector2();
			if (i==0) {			
				pt.x = Float.parseFloat(ssplit[0]);
				pt.y = Float.parseFloat(ssplit[1]);
			}
			else {
				pt.x = Float.parseFloat(ssplit[0]) + this.getPoints().get(i-1).x;
				pt.y = Float.parseFloat(ssplit[1]) + this.getPoints().get(i-1).y;
			}
			
			this.getPoints().add(pt);
			i+=1;
		}
	
		// offset the shape to make it independent from the position drawn on svg
		offsetShape(offset);
		
		// calc centroid of the shape, considering the minimum rectangle that can be created to inscribe the shape into
		centroid = inscribedPolygonCenter();

		//coordenadas do objecto definidas em torno do ponto 0.0f, 0.0f -- mais fácil para a renderização e rotações?
		for (Vector2 v : getPoints()) {
			v.sub(getCentroid());
		}		
		
		//scaling and Y-invert
		float scale = GAME_CONSTANTS.MODEL_SCREEN_WIDTH_CAPACITY / maxSize.x;
		scaleAndYInvertShape(scale, maxSize.y);

		this.type = type;
		
		// calc width and height
		width = calcWidth();
		height = calcHeight();
		
		
		color = new Color();
		// color fill
		pattern = Pattern.compile("fill:#([0-9a-fA-F]){6};*");
		matcher = pattern.matcher(style);
		while (matcher.find()) {
			String aux = matcher.group();
			aux = aux.replace("fill:#", "");
			aux = aux.replace(";", "");			
			color.r = (float)Integer.parseInt(aux.substring(0, 2), 16) / (float)0xFF;
			color.g = (float)Integer.parseInt(aux.substring(2, 4), 16) / (float)0xFF;
			color.b = (float)Integer.parseInt(aux.substring(4, 6), 16) / (float)0xFF;
		}
		// color fill opacity
		pattern = Pattern.compile("fill-opacity:[01][\\.0-9]*;*");
		matcher = pattern.matcher(style);
		while (matcher.find()) {
			String aux = matcher.group();
			aux = aux.replace("fill-opacity:", "");
			aux = aux.replace(";", "");
			color.a = Float.parseFloat(aux);
		}
		
		
	}	
		
	
	public Vector2 getCentroid() {
		return centroid;
	}
	
	public ObjectType getType() {
		return type;
	}

	
	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}
    	
	
	public Vector2[] getPointsArray() {
		return points.toArray(new Vector2[] {});
	}
	
	public Color getFillColor() {
		return color;
	}
	

	/////////////// Auxiliar functions /////////////////// 
	private ArrayList<Vector2> getPoints() {
		return points;
	}
    
    /**
	 * 
	 * @return the center of the smallest rectangle that can be used to inscribe
	 *         the polygon into
	 */
    private Vector2 inscribedPolygonCenter() {
    	Float minX = null, maxX = null;
    	Float minY = null, maxY = null;
    	
    	for (Vector2 point : getPoints()) {
			if ((minX == null) || (point.x < minX)) minX = point.x;
			if ((maxX == null) || (point.x > maxX)) maxX = point.x;
			if ((minY == null) || (point.y < minY)) minY = point.y;
			if ((maxY == null) || (point.y > maxY)) maxY = point.y;			
		}
    	
    	return new Vector2(minX+(maxX-minX)/2, minY+(maxY-minY)/2);
    }

	/**
	 * This function offsets the coordinates of a shape to stay inside of the
	 * board
	 * 
	 * @param offset
	 */
	private void offsetShape(Vector2 offset) {
		// offset de todos os pontos
		for (Vector2 p : this.getPoints()) {
			p.x = p.x - offset.x;
			p.y = p.y - offset.y;
		}
	}	
    
	private void scaleAndYInvertShape(float scale, float maxHeight) {
		// scaling and y-invert
		for (Vector2 ap : getPoints()) {
			ap.x = ap.x*scale;
			ap.y = - ap.y*scale;			
		}
		getCentroid().x = getCentroid().x*scale;
		getCentroid().y = (maxHeight - getCentroid().y)*scale;
	}

   /**
    * 
    * @return the width of the smallest rectangle that can be used to inscribe the polygon into
    */
    private float calcWidth() {
    	Float minX = null, maxX = null;
    	
    	for (Vector2 point : getPoints()) {
			if ((minX == null) || (point.x < minX)) minX = point.x;
			if ((maxX == null) || (point.x > maxX)) maxX = point.x;
		}
    	return maxX - minX;
   }
    
    /**
     * 
     * @return the height of the smallest rectangle that can be used to inscribe the polygon into
     */
    private float calcHeight() {
    	Float minY = null, maxY = null;
    	
    	for (Vector2 point : getPoints()) {
			if ((minY == null) || (point.y < minY)) minY = point.y;
			if ((maxY == null) || (point.y > maxY)) maxY = point.y;			
		}
    	return maxY - minY;
    }	
	
	
}
