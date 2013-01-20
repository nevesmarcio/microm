package pt.me.microm.tools.levelloader.shape;

public class APoint {
	private float x;
	private float y;
	
	public APoint() {
		this(0.0f, 0.0f);
	}
	
	public APoint(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	
}
