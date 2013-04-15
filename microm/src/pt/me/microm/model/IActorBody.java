package pt.me.microm.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import pt.me.microm.tools.levelloader.BasicShape;

/**
 * This interface represents a Body that has certain properties:
 *  - it has a shape
 *  - can be described by a position
 *  - it has an angle
 * @author Márcio Neves
 *
 */
public interface IActorBody {
	public String getName();
	
	public BasicShape getBasicShape();
	public Vector2 getPosition();
	public float getAngle();
	
	//FIXME:: não quero expor o body!! - temporário!
	public Body getBody();
	
}
