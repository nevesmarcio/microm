package pt.me.microm.model;

import pt.me.microm.infrastructure.GAME_CONSTANTS;

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
public class MyContactListener implements ContactListener {
	private static final String TAG = MyContactListener.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	@Override /* related to MyContactListener interface */
	public void beginContact(Contact contact) {
		// Por defeito, as classes do model só logam os contactos
		// Cada model deverá fazer o override a este método para tratar dos contactos		
		if (logger.getLevel() >= Logger.DEBUG) logger.debug("[default-contact-handler] : BeginContact => ("+contact.getChildIndexA()+","+contact.getChildIndexB()+") => " + contact.getFixtureA().toString() +" :: "+ contact.getFixtureB() );
		
		((ContactInterface)contact.getFixtureA().getBody().getUserData()).beginContactWith((BodyInterface)contact.getFixtureB().getBody().getUserData());
		((ContactInterface)contact.getFixtureB().getBody().getUserData()).beginContactWith((BodyInterface)contact.getFixtureA().getBody().getUserData()); 
				
	}
	@Override /* related to MyContactListener interface */
	public void endContact(Contact contact) {
		// Por defeito, as classes do model só logam os contactos
		// Cada model deverá fazer o override a este método para tratar dos contactos		
		if (logger.getLevel() >= Logger.DEBUG) logger.debug("[default-contact-handler] : EndContact => ("+contact.getChildIndexA()+","+contact.getChildIndexB()+") => " + contact.getFixtureA().toString() +" :: "+ contact.getFixtureB() );

		((ContactInterface)contact.getFixtureA().getBody().getUserData()).endContactWith((BodyInterface)contact.getFixtureB().getBody().getUserData());
		((ContactInterface)contact.getFixtureB().getBody().getUserData()).endContactWith((BodyInterface)contact.getFixtureA().getBody().getUserData()); 
	}
	
	@Override /* related to MyContactListener interface */
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override /* related to MyContactListener interface */
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}	

}
