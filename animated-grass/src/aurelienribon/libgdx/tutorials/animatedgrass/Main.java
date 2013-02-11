package aurelienribon.libgdx.tutorials.animatedgrass;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import javax.swing.JOptionPane;

import org.lwjgl.util.Display;

/**
 * @author Aurelien Ribon | http://www.aurelienribon.com
 */
public class Main {
	public static void main(String[] args) {
		LwjglApplication app = new LwjglApplication(new MyAnimatedGrassApp1(), "Tutorial: Animated Grass", 480, 800, false);

		
		//new LwjglApplication(new AnimatedGrassApp3(), "Tutorial: Animated Grass", 404, 100, false);
		
		
//		String[] values = new String[] {
//			"v1 (no movement)",
//			"v2 (single sprite + wind)",
//			"v3 (multiple sprites + wind",
//			"my stuff"
//		};
//		
//		Object choice = JOptionPane.showInputDialog(null, "Select the implementation to show", "Initialization", JOptionPane.QUESTION_MESSAGE, null, values, values[0]);
//		
//		if (choice == values[0]) new LwjglApplication(new AnimatedGrassApp1(), "Tutorial: Animated Grass", 300, 60, false);
//		else if (choice == values[1]) new LwjglApplication(new AnimatedGrassApp2(), "Tutorial: Animated Grass", 300, 60, false);
//		else if (choice == values[2]) new LwjglApplication(new AnimatedGrassApp3(), "Tutorial: Animated Grass", 404, 100, false);
//		else if (choice == values[3]) new LwjglApplication(new MyAnimatedGrassApp1(), "Tutorial: Animated Grass", 404, 100, false);
	}
}
