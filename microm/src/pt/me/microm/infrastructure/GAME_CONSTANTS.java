package pt.me.microm.infrastructure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.utils.Logger;

public class GAME_CONSTANTS {
	public static final int LOG_LEVEL = Logger.INFO;
	
	public static final long ONE_SECOND_TO_NANO = 1000000000L; // 1 second = 1 x 10^9 nanoSeconds
	public static final long ONE_SECOND_TO_MILI = 1000L; // 1 second = 1x10^3 miliSeconds
	public static final long ONE_MILISECOND_TO_NANO = 1000000L; // 1 miliSecond = 1x10^6 nanoSeconds
	
	public static final int GAME_TICK_MILI = 16;
	
	public static final float MODEL_SCREEN_WIDTH_CAPACITY = 15.0f; // must be able to represent 15 units on full width
//	public static final float MODEL_GAME_ASPECT_RATIO = 4.0f/4.0f; // 4:3 game
//	public static final float MODEL_MAX_HEIGHT_TO_CONSIDER = MODEL_SCREEN_WIDTH_CAPACITY * MODEL_GAME_ASPECT_RATIO; // 16.0f
	
	public static final int MAX_TOUCH_POINTS = 5;
	
	// Textures LOAD
	public static final Texture TEXTURE_BG = new Texture(Gdx.files.internal("data/textures/bg.png"));
	public static final Texture TEXTURE_DROID = new Texture(Gdx.files.internal("data/textures/libgdx.png"));
	
	public static final Texture TEXTURE_BALL = new Texture(Gdx.files.internal("data/bodies/1st_example/ball.png"));
	public static final Texture TEXTURE_THING = new Texture(Gdx.files.internal("data/bodies/1st_example/thing.png"));

	public static final Texture TEXTURE_SQUARE1 = new Texture(Gdx.files.internal("data/textures/simple/input/square1.png"));
	public static final Texture TEXTURE_SQUARE2 = new Texture(Gdx.files.internal("data/textures/simple/input/square2.png"));
	public static final Texture TEXTURE_DABOX = new Texture(Gdx.files.internal("data/textures/simple/input/txr_daBox.png"));
	public static final Texture TEXTURE_WALL = new Texture(Gdx.files.internal("data/textures/simple/input/txr_wall.png"));
	public static final Texture TEXTURE_BOARD = new Texture(Gdx.files.internal("data/textures/simple/input/txr_full_board.png"));
	public static final Texture TEXTURE_GROUND = new Texture(Gdx.files.internal("data/textures/simple/input/txr_ground.png"));

	// Sounds LOAD
	//public static final Sound SOUND_DROP = Gdx.audio.newSound(Gdx.files.internal("data/sound/Utopia Critical Stop.wav"));
	
	// Musics LOAD
	//public static final Music MUSIC_BACKGROUND = Gdx.audio.newMusic(Gdx.files.internal("data/music/01 me and my social anxiety.mp3"));
	
	public static void DisposeAllObjects() {
		TEXTURE_DROID.dispose();
		//SOUND_DROP.dispose();
		//MUSIC_BACKGROUND.dispose();
	}
}
