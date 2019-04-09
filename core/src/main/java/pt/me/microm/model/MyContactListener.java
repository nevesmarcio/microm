package pt.me.microm.model;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.me.microm.infrastructure.event.CollisionEvent;
import pt.me.microm.infrastructure.event.dispatcher.EventDispatcher;
import pt.me.microm.model.trigger.SimpleTriggerModel;

/**
 * 
 * @author Márcio Neves
 * 
 * Esta classe deve ser registada no Physics World. Ela contém os
 * handlers para quando há choques entre objectos
 * 
 */
public class MyContactListener extends EventDispatcher implements ContactListener {
	private static final String TAG = MyContactListener.class.getSimpleName();
	private static final Logger logger = LoggerFactory.getLogger(TAG);
	
	@Override /* related to MyContactListener interface */
	public void beginContact(Contact contact) {
		// Por defeito, as classes do model só logam os contactos
		// Cada model deverá fazer o override a este método para tratar dos contactos		
		if (logger.isDebugEnabled()) logger.debug("[default-contact-handler] : BeginContact => ("+contact.getChildIndexA()+","+contact.getChildIndexB()+") => " + contact.getFixtureA().toString() +" :: "+ contact.getFixtureB() );
		
		int a=-1,b=-1;
		a=((IContact)contact.getFixtureA().getBody().getUserData()).addPointOfContactWith((IBody)contact.getFixtureB().getBody().getUserData());
		b=((IContact)contact.getFixtureB().getBody().getUserData()).addPointOfContactWith((IBody)contact.getFixtureA().getBody().getUserData());
		assert(a==b && a!=-1 && b!=-1); // bem, se a!=b ou a==-1 ou b==-1, há gato!
		
		//TODO: INVOCAR AQUI A CHAMADA AO MOTOR DE JAVASCRIPT ??
		if (a==1) {
			//call to notify specific behavior
			((IContact)contact.getFixtureA().getBody().getUserData()).beginContactWith((IBody)contact.getFixtureB().getBody().getUserData());
			((IContact)contact.getFixtureB().getBody().getUserData()).beginContactWith((IBody)contact.getFixtureA().getBody().getUserData());

			//call to notify general behavior
			CollisionEvent e = new CollisionEvent(EventType.ON_COLLISION_BEGIN);
			if (contact.getFixtureA().getBody().getUserData() instanceof SimpleTriggerModel)
				e.setScript(((SimpleTriggerModel)contact.getFixtureA().getBody().getUserData()).getScript());
			if (contact.getFixtureB().getBody().getUserData() instanceof SimpleTriggerModel)
				e.setScript(((SimpleTriggerModel)contact.getFixtureB().getBody().getUserData()).getScript());				

			e.setA(((IBody)contact.getFixtureA().getBody().getUserData()).getName());
			e.setB(((IBody)contact.getFixtureB().getBody().getUserData()).getName());
			this.dispatchEvent(e);

			if (logger.isDebugEnabled()) logger.debug((IContact)contact.getFixtureA().getBody().getUserData() + " -x- " + (IContact)contact.getFixtureB().getBody().getUserData());
		}
		
		
		
	}
	@Override /* related to MyContactListener interface */
	public void endContact(Contact contact) {
		// Por defeito, as classes do model só logam os contactos
		// Cada model deverá fazer o override a este método para tratar dos contactos		
		if (logger.isDebugEnabled()) logger.debug("[default-contact-handler] : EndContact => ("+contact.getChildIndexA()+","+contact.getChildIndexB()+") => " + contact.getFixtureA().toString() +" :: "+ contact.getFixtureB() );

		int a=-1,b=-1;
		a=((IContact)contact.getFixtureA().getBody().getUserData()).subtractPointOfContactWith((IBody)contact.getFixtureB().getBody().getUserData());
		b=((IContact)contact.getFixtureB().getBody().getUserData()).subtractPointOfContactWith((IBody)contact.getFixtureA().getBody().getUserData());
		assert(a==b && a!=-1 && b!=-1); // bem, se a!=b ou a==-1 ou b==-1, há gato!
		
		//TODO: INVOCAR AQUI A CHAMADA AO MOTOR DE JAVASCRIPT ??
		if (a==0) {
			//call no notify specific behavior
			((IContact)contact.getFixtureA().getBody().getUserData()).endContactWith((IBody)contact.getFixtureB().getBody().getUserData());
			((IContact)contact.getFixtureB().getBody().getUserData()).endContactWith((IBody)contact.getFixtureA().getBody().getUserData());
			
			//call to notify general behavior
			CollisionEvent e = new CollisionEvent(EventType.ON_COLLISION_END);
			if (contact.getFixtureA().getBody().getUserData() instanceof SimpleTriggerModel)
				e.setScript(((SimpleTriggerModel)contact.getFixtureA().getBody().getUserData()).getScript());
			if (contact.getFixtureB().getBody().getUserData() instanceof SimpleTriggerModel)
				e.setScript(((SimpleTriggerModel)contact.getFixtureB().getBody().getUserData()).getScript());
			
			e.setA(((IBody)contact.getFixtureA().getBody().getUserData()).getName());
			e.setB(((IBody)contact.getFixtureB().getBody().getUserData()).getName());
			this.dispatchEvent(e);
			
			if (logger.isDebugEnabled()) logger.debug((IContact)contact.getFixtureA().getBody().getUserData() + " -o- " + (IBody)contact.getFixtureB().getBody().getUserData());
		}
		
	
	}
	
	@Override /* related to MyContactListener interface */
	public void preSolve(Contact contact, Manifold oldManifold) {
		//TODO: Auto-generated method stub
		
	}

	@Override /* related to MyContactListener interface */
	public void postSolve(Contact contact, ContactImpulse impulse) {
		//TODO: Auto-generated method stub
		
	}	

	
	public enum EventType {
		ON_COLLISION_BEGIN, // Event raised when 2 objects begin touching each other
		ON_COLLISION_END	// Event raised when 2 objects end touching each other
	};		
	
}
