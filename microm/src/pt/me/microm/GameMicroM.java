package pt.me.microm;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import pt.me.microm.api.ClassicSingleton;
import pt.me.microm.infrastructure.GAME_CONSTANTS;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.Logger;

public class GameMicroM extends Game/*implements ApplicationListener*/ { // it extends the Game so it can handle Screens
	// FLAGS
	public static final boolean FLAG_DEV_ELEMENTS = true; 				// "pre-compiler" equivalent for branching development-only code
	public static final boolean FLAG_DISPLAY_ACTOR_SHAPES = true;		// mostra o desenho das shapes dos actores: walls, dabox, etc.
	public static final boolean FLAG_DISPLAY_ACTOR_TEXTURES = true;		// liga a texturização dos actores
	public static final boolean FLAG_DISPLAY_PARTICLES = true;			// liga o desenho de particulas
	
	private static final String TAG = GameMicroM.class.getSimpleName();
	private static Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	

	public ScreenAbstract splash;
	public ScreenAbstract menu;
	public ScreenAbstract worldSelect;
	public ScreenAbstract levelSelect;
	public ScreenTheJuice theJuice;
	public ScreenAbstract pausePopUp;
	
	@Override
	public void create() {		
		splash = new ScreenSplash(this);
		menu = new ScreenMenu(this);
		worldSelect = new ScreenWorldSelect(this);
		levelSelect = new ScreenLevelSelect(this);
		theJuice = new ScreenTheJuice(this);
		pausePopUp = new ScreenPause(this);
		
		
//		setScreen(splash);
		setScreen(theJuice);

		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				// javascript engine
				Context cx = Context.enter();
				cx.setOptimizationLevel(-1); // do not compile - it won't run on dalvik vm
				Scriptable scope = cx.initStandardObjects();
				
				Object wrappedOut = Context.javaToJS(ClassicSingleton.getInstance(), scope);
				ScriptableObject.putProperty(scope, "cs", wrappedOut);		
				
				Object result = cx.evaluateString(scope, "function f(x){return x+1}; f(7);", "somescript.js", 1, null); // 1 is the line number!
				System.out.println(">>>>>>>" + Context.toString(result));
				
				
				InputStreamReader inputStreamReader = new InputStreamReader(System.in);
				BufferedReader stdin = new BufferedReader (inputStreamReader);
				int i = 0;
				try {
					while (true) {
						i+=1;
						String s = stdin.readLine();
						if (s.equals("exit"))
							break;
						result = cx.evaluateString(scope, s, "<<from console>>", i, null); // 1 is the line number!
						System.out.println(">>>>>>>" + Context.toString(result));	
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				Context.exit();
				/////////////////				
				
			}
		});
		
		t.start();


	}
	
}
