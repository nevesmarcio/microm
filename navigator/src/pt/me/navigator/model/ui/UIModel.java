package pt.me.navigator.model.ui;

import pt.me.navigator.infrastructure.GAME_CONSTANTS;
import pt.me.navigator.infrastructure.events.GameTickEvent;
import pt.me.navigator.model.AbstractModel;
import pt.me.navigator.model.AbstractModel.EventType;
import pt.me.navigator.model.base.CameraModel;
import pt.me.navigator.model.base.WorldModel;
import pt.me.navigator.model.events.SimpleEvent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;

public class UIModel extends AbstractModel {
	// Esta classe deverá ter um objecto visual independente da camera sobre o mundo.
	// O UI deverá permanecer inalterado independentemente do zoom/ pan, etc.
	private static final String TAG = UIModel.class.getSimpleName();
	
	

	public UIModel() {
		
		// Sinaliza os subscritores de que a construção do modelo terminou.
		this.dispatchEvent(new SimpleEvent(EventType.ON_MODEL_INSTANTIATED));		
	}
	
	
	
	@Override
	public void handleGameTick(GameTickEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	
}
