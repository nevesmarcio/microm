package pt.me.microm.model;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.model.events.SimpleEvent;
import pt.me.microm.model.events.dispatcher.EventDispatcher;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Logger;

/**
 * 
 * @author Márcio Neves
 * 
 *         Esta classe deve ser registada no Physics World. Ela contém os
 *         handlers para quando há choques entre objectos
 * 
 */
public class MyContactListener extends EventDispatcher implements ContactListener {
	private static final String TAG = MyContactListener.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	@Override /* related to MyContactListener interface */
	public void beginContact(Contact contact) {
		// Por defeito, as classes do model só logam os contactos
		// Cada model deverá fazer o override a este método para tratar dos contactos		
		if (logger.getLevel() >= Logger.DEBUG) logger.debug("[default-contact-handler] : BeginContact => ("+contact.getChildIndexA()+","+contact.getChildIndexB()+") => " + contact.getFixtureA().toString() +" :: "+ contact.getFixtureB() );
		
		((ContactInterface)contact.getFixtureA().getBody().getUserData()).beginContactWith((IBodyProperties)contact.getFixtureB().getBody().getUserData());
		((ContactInterface)contact.getFixtureB().getBody().getUserData()).beginContactWith((IBodyProperties)contact.getFixtureA().getBody().getUserData()); 

		//TODO: INVOCAR AQUI A CHAMADA AO MOTOR DE JAVASCRIPT ??
		this.dispatchEvent(new SimpleEvent(EventType.ON_COLLISION_BEGIN));
		
		System.out.println((ContactInterface)contact.getFixtureA().getBody().getUserData() + " -x- " + (ContactInterface)contact.getFixtureB().getBody().getUserData());
		
	}
	@Override /* related to MyContactListener interface */
	public void endContact(Contact contact) {
		// Por defeito, as classes do model só logam os contactos
		// Cada model deverá fazer o override a este método para tratar dos contactos		
		if (logger.getLevel() >= Logger.DEBUG) logger.debug("[default-contact-handler] : EndContact => ("+contact.getChildIndexA()+","+contact.getChildIndexB()+") => " + contact.getFixtureA().toString() +" :: "+ contact.getFixtureB() );

		((ContactInterface)contact.getFixtureA().getBody().getUserData()).endContactWith((IBodyProperties)contact.getFixtureB().getBody().getUserData());
		((ContactInterface)contact.getFixtureB().getBody().getUserData()).endContactWith((IBodyProperties)contact.getFixtureA().getBody().getUserData()); 

		//TODO: INVOCAR AQUI A CHAMADA AO MOTOR DE JAVASCRIPT ??
		this.dispatchEvent(new SimpleEvent(EventType.ON_COLLISION_END));
		
		System.out.println((ContactInterface)contact.getFixtureA().getBody().getUserData() + " -o- " + (ContactInterface)contact.getFixtureB().getBody().getUserData());
	
	}
	
	@Override /* related to MyContactListener interface */
	public void preSolve(Contact contact, Manifold oldManifold) {
		//TODO: Auto-generated method stub
		
	}

	@Override /* related to MyContactListener interface */
	public void postSolve(Contact contact, ContactImpulse impulse) {
		//TODO: Auto-generated method stub
		
	}	

	
	public static enum EventType {
		ON_COLLISION_BEGIN, // Event raised when 2 objects begin touching each other
		ON_COLLISION_END	// Event raised when 2 objects end touching each other
	};		
	
}
