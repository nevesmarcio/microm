package pt.me.microm.tools.levelloader.shape;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import pt.me.microm.tools.levelloader.ObjectType;

public class BasicShape {
	private String d; 
	private ArrayList<Vector2> points;
	private ObjectType type;
	private float w;
	private float h;

	public BasicShape() {
		points = new ArrayList<Vector2>();
	}
	
	public String getD() {
		return d;
	}
	public void setD(String d) {
		this.d = d;
	}

	public ArrayList<Vector2> getPoints() {
		return points;
	}
	public void setPoints(ArrayList<Vector2> points) {
		this.points = points;
	}	

	public float getW() {
		return w;
	}
	public void setW(float w) {
		this.w = w;
	}
	
	public float getH() {
		return h;
	}
	public void setH(float h) {
		this.h = h;
	}

	public ObjectType getType() {
		return type;
	}
	public void setType(ObjectType type) {
		this.type = type;
	}


}
