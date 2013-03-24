package pt.me.microm;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public abstract class ScreenAbstract implements Screen {

	protected Game g;
	
	public ScreenAbstract(Game g) {
		this.g = g;
	}
	
}
