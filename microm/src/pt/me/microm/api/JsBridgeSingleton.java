package pt.me.microm.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Logger;

import pt.me.microm.ScreenTheJuice;
import pt.me.microm.controller.loop.GameTickGenerator;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.event.CollisionEvent;
import pt.me.microm.infrastructure.event.IEvent;
import pt.me.microm.infrastructure.event.listener.IEventListener;
import pt.me.microm.model.MyContactListener;
import pt.me.microm.model.stuff.DaBoxModel;


/**
 * Da forma que tenho esta classe implementada estou a contar com o thread do UI para pendurar o contexto do javascript, o que não me parece fixe!
 * 
 * @author mneves
 *
 */
public class JsBridgeSingleton implements IEventListener {
	
	private static final String TAG = JsBridgeSingleton.class.getSimpleName();
	private static Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);

	
	private static JsBridgeSingleton instance = null;


	private Context cx;
	private Scriptable scope;
	private Object result;
	protected JsBridgeSingleton() {
		// Exists only to defeat instantiation.

		/*Example invocation of javascript engine!*/
		GameTickGenerator.PostRunnable(new Runnable() {

			@Override
			public void run() {
				// javascript engine
				cx = Context.enter();
				cx.setOptimizationLevel(-1); // do not compile - it won't run on
												// dalvik vm
				scope = cx.initStandardObjects();

				Object wrappedOut = Context.javaToJS(/*
													 * ClassicSingleton.getInstance
													 * ()
													 */JsBridgeSingleton.this, scope);
				ScriptableObject.putProperty(scope, "cs", wrappedOut);

				result = cx.evaluateString(scope,
						"function f(x){return x+1}; f(7);", "somescript.js", 1,
						null); // 1 is the line number!

				logger.info(">>>>>>>" + Context.toString(result));

			}
		});
		
		/*Raises a thread that listens to console input and evaluates javascript with rhino engine*/
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				InputStreamReader inputStreamReader = new InputStreamReader(System.in);
				BufferedReader stdin = new BufferedReader(inputStreamReader);
				int i = 0;
				try {
					while (true) {
						i += 1;
						final String s = stdin.readLine();
						if (s.equals("exit"))
							break;
						final Integer in = new Integer(i);
						GameTickGenerator.PostRunnable(new Runnable() {
							
							@Override
							public void run() {
								result = cx.evaluateString(scope, s, "<<from console>>", in, null); // 1 is the line number!
								logger.info(">>>>>>>" + Context.toString(result));
								
							}
						});

					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				Context.exit();

			}
		});
		
		t.start();
		
	}

	public static JsBridgeSingleton getInstance() {
		if (instance == null) {
			instance = new JsBridgeSingleton();

		}
		return instance;
	}
	


	/**
	 * Method that should be called when the collisions handler dispatches events!
	 * This is the method that should depend on the models and not the other way around!!
	 */	
	@Override
	public void onEvent(final IEvent event) {

		logger.debug("event:: _" + event.getType() + "_ " + ((CollisionEvent)event).getA() + "<-->" + ((CollisionEvent)event).getB());
		
		GameTickGenerator.PostRunnable(new Runnable() {
			
			@Override
			public void run() {

				ScriptableObject.putProperty(scope, "__EVENT_TYPE", event.getType().toString());
				ScriptableObject.putProperty(scope, "__ACTOR_A", ((CollisionEvent)event).getA());
				ScriptableObject.putProperty(scope, "__ACTOR_B", ((CollisionEvent)event).getB());
				
				String script = ((CollisionEvent)event).getScript();
//				script = "function add(a,b){return a + '<--->' + b;}; " +
//						"cs.out(\"puff>>> \" + add(__ACTOR_A, __ACTOR_B));" +
//						"if (__EVENT_TYPE.indexOf(\"ON_COLLISION_BEGIN\") !== -1)" +
//						"if ((__ACTOR_A.indexOf(\"trigger\") !== -1) || (__ACTOR_B.indexOf(\"trigger\") !== -1)) cs.m.jump();";
				
				if (script != null)
					JsBridgeSingleton.this.cx.evaluateString(scope, script, "external", 1, null);		
			}
		});
		
	}
	   

	   
	/************ EXPOSED API ************/
	public DaBoxModel m;

	public void out(String s) {
		logger.info(s);
	}
	/******** END OF EXPOSED API *********/	   

	   
	/**
	 * method to lookup for an object given its name
	 */	   
	
	   

	   
	}
