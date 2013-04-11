package pt.me.microm.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.badlogic.gdx.Gdx;

import pt.me.microm.infrastructure.GameTickGenerator;
import pt.me.microm.model.events.IEvent;
import pt.me.microm.model.events.listener.IEventListener;
import pt.me.microm.model.stuff.DaBoxModel;


/**
 * Da forma que tenho esta classe implementada estou a contar com o thread do UI para pendurar o contexto do javascript, o que não me parece fixe!
 * 
 * @author mneves
 *
 */
public class ClassicSingleton implements IEventListener {
	private static ClassicSingleton instance = null;


	private Context cx;
	private Scriptable scope;
	private Object result;
	protected ClassicSingleton() {
		// Exists only to defeat instantiation.

		
		GameTickGenerator.PostRunnable(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// javascript engine
				cx = Context.enter();
				cx.setOptimizationLevel(-1); // do not compile - it won't run on
												// dalvik vm
				scope = cx.initStandardObjects();

				Object wrappedOut = Context.javaToJS(/*
													 * ClassicSingleton.getInstance
													 * ()
													 */ClassicSingleton.this, scope);
				ScriptableObject.putProperty(scope, "cs", wrappedOut);

				result = cx.evaluateString(scope,
						"function f(x){return x+1}; f(7);", "somescript.js", 1,
						null); // 1 is the line number!

				System.out.println(">>>>>>>" + Context.toString(result));

			}
		});

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
								// TODO Auto-generated method stub
								result = cx.evaluateString(scope, s, "<<from console>>", in,
										null); // 1 is the line number!
								System.out.println(">>>>>>>" + Context.toString(result));
								
							}
						});

						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				Context.exit();
				// ///////////////

			}
		});
		
		t.start();
		
		
		
		
	}

	public static ClassicSingleton getInstance() {
		if (instance == null) {
			instance = new ClassicSingleton();

		}
		return instance;
	}
	



	   
	   
	   
	   
	   
	   
	public DaBoxModel m;

	public void out(String s) {
		System.out.println(s);
	}
	   
	   
	@Override
	public void onEvent(IEvent event) {

		System.out.println("Event received :: __"+event.getType());
		GameTickGenerator.PostRunnable(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ClassicSingleton.this.cx.evaluateString(scope, "cs.out(\"puff! e fez-se o chocapic!!\");", "external", 1, null);		
			}
		});
		
	}
	   

	   
	/**
	 * write down a method that should be called from the collisions handler?
	 * No, i rather prefer to invert the dependency. this is the method that
	 * should depend on the models and not the other way around!
	 */	   

	   
	/**
	 * method to lookup for an object given its name
	 */	   

	   
	/**
	 * there should be auxiliar methods to interface from js with the model
	 * layer
	 */	   

	   
	}
