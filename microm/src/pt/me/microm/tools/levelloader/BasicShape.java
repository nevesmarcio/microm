package pt.me.microm.tools.levelloader;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * Esta BasicShape deverá receber as coordenadas do SVG, mas após a construcção
 * do objecto, deverá manter apenas coordenadas compativeis com o nível e com o
 * Box2D.
 * 
 * @author mneves
 * 
 */
public class BasicShape {
	private static final String TAG = BasicShape.class.getSimpleName();
	
	private String d; 
	private ArrayList<Vector2> points;
	private ObjectType type;
	private Vector2 centroid;

	public BasicShape() {
		points = new ArrayList<Vector2>();
	}

	/**
	 * This constructor builds a shape using the points specified by a SVG 'd'
	 * element 
	 * @param d
	 */
	public BasicShape(String d) {
		this();
		this.setD(d);
		
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
				pt.x = Float.parseFloat(ssplit[0]) + this.getPoints().get(i-1).x;
				pt.y = Float.parseFloat(ssplit[1]) + this.getPoints().get(i-1).y;
			}
			
			this.getPoints().add(pt);
			i+=1;
		}
	
		centroid = inscribedPolygonCenter();
//		//deslocamento do centroid
//		for (Vector2 v : getPoints()) {
//			v.sub(getCentroid());
//		}		
		
	}	
		
	
	public Vector2 getCentroid() {
		return centroid;
	}
	
	public String getD() {
		return d;
	}
	private void setD(String d) {
		this.d = d;
	}

	public ArrayList<Vector2> getPoints() {
		return points;
	}

	public ObjectType getType() {
		return type;
	}
	public void setType(ObjectType type) {
		this.type = type;
	}
	
	
	/**
	 * This function offsets the coordinates of a shape to stay inside of the
	 * board
	 * 
	 * @param offset
	 */
	public void offsetShape(Vector2 offset) {
		// offset de todos os pontos
		for (Vector2 p : this.getPoints()) {
			p.x = p.x - offset.x;
			p.y = p.y - offset.y;
		}
		// offset do centroid
		centroid.x = centroid.x - offset.x;
		centroid.y = centroid.y - offset.y;
		
	}	
		
	

	/**
	 * 
	 * @return the center of the smallest square that can be used to inscribe
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

   
}
