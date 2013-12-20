package pt.me.navigator.infrastructure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class GAME_CONSTANTS {
	public static final long ONE_SECOND_TO_NANO = 1000000000L; // 1 second = 1 x 10^9 nanoSeconds
	public static final long ONE_SECOND_TO_MILI = 1000L; // 1 second = 1x10^3 miliSeconds
	public static final long ONE_MILISECOND_TO_NANO = 1000000L; // 1 miliSecond = 1x10^6 nanoSeconds
	
	public static final int GAME_TICK_MILI = 16;
	
	public static final float MODEL_SCREEN_WIDTH_CAPACITY = 15.0f; // must be able to represent 15 units on full width
//	public static final float MODEL_GAME_ASPECT_RATIO = 4.0f/4.0f; // 4:3 game
//	public static final float MODEL_MAX_HEIGHT_TO_CONSIDER = MODEL_SCREEN_WIDTH_CAPACITY * MODEL_GAME_ASPECT_RATIO; // 16.0f
	
	public static final int MAX_TOUCH_POINTS = 5;
	
	// Textures LOAD

	// Sounds LOAD
	//public static final Sound SOUND_DROP = Gdx.audio.newSound(Gdx.files.internal("data/sound/Utopia Critical Stop.wav"));
	
	// Musics LOAD
	//public static final Music MUSIC_BACKGROUND = Gdx.audio.newMusic(Gdx.files.internal("data/music/01 me and my social anxiety.mp3"));
	
	public static void DisposeAllObjects() {

	}
}
