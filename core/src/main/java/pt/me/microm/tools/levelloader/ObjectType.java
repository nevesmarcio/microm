package pt.me.microm.tools.levelloader;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public enum ObjectType {
	NONE(null, 0.0f, 0.0f, 0.0f, false),
	DEBUG(BodyType.StaticBody, 1.0f, 1.0f, 0.75f, false),
	TEXT(null, 0.0f, 0.0f, 0.0f, false),
	CAMERA(null, 0.0f, 0.0f, 0.0f, false), 
	BOARD(BodyType.StaticBody, 1.0f, 0.0f, 0.0f, false), 
	SPAWN(BodyType.StaticBody, 1.0f, 0.0f, 0.0f, true), 
	GOAL(BodyType.StaticBody, 1.0f, 0.0f, 0.0f, true), 
	GROUND(BodyType.StaticBody, 1.0f, 0.9f, 0.0f, false), 
	PORTAL(BodyType.StaticBody, 1.0f, 0.0f, 0.0f, true), 
	WALL(BodyType.StaticBody, 1.0f, 0.0f, 0.0f, false), 
	DABOX(BodyType.DynamicBody, 1.0f, 0.0f, 0.0f, false), 
	STAR(BodyType.KinematicBody, 1.0f, 0.5f, 0.3f, true), 
	TRIGGER(BodyType.StaticBody, 1.0f, 0.0f, 0.0f, true);
	
	private BodyType bodyType;
	private float density;
	private float friction;
	private float restitution;
	private boolean isSensor;
	ObjectType (BodyType bodyType, float density, float friction, float restitution, boolean isSensor) {
		this.bodyType = bodyType;
		this.density = density;
		this.friction = friction;
		this.restitution = restitution;
		this.isSensor = isSensor;
	}
	
	public BodyType getBodyType() {
		return bodyType;
	}
	public float getDensity() {
		return density;
	}
	public float getFriction() {
		return friction;
	}
	public float getRestitution() {
		return restitution;
	}
	public boolean isSensor() {
		return isSensor;
	}
	
	
}
