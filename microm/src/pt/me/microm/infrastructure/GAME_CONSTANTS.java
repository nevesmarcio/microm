package pt.me.microm.infrastructure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Logger;

public class GAME_CONSTANTS {
	public static final int LOG_LEVEL = Logger.INFO;
	
	public static final long ONE_SECOND_TO_NANO = 1000000000L; // 1 second = 1 x 10^9 nanoSeconds
	public static final long ONE_SECOND_TO_MILI = 1000L; // 1 second = 1x10^3 miliSeconds
	public static final long ONE_MILISECOND_TO_NANO = 1000000L; // 1 miliSecond = 1x10^6 nanoSeconds
	
	public static final int GAME_TICK_MILI = 16;//32
	
	public static final float DIPIXELS_PER_METER = 85.3f; 	// device independent pixels per meter:: these pixels are the ones used on editor
														  	// then, the camera has to scale accordingly, but that's another story
	
	public static final int MAX_TOUCH_POINTS = 5;
	
	public static final String SAVEGAME_FILE_V1 = "PlayerProgress_v1.save";
	
	// Texture Atlas as it should be
	public static final TextureAtlas simpleAtlas = new TextureAtlas(Gdx.files.internal("data/textures/simple/output/simple.atlas"));
	public static final TextureAtlas devAtlas = new TextureAtlas(Gdx.files.internal("data/textures/dev/output/dev.atlas"));
	
	// Sounds LOAD
	//public static final Sound SOUND_DROP = Gdx.audio.newSound(Gdx.files.internal("data/sound/Utopia Critical Stop.wav"));
	
	// Musics LOAD
	//public static final Music MUSIC_BACKGROUND = Gdx.audio.newMusic(Gdx.files.internal("data/music/01 me and my social anxiety.mp3"));
	
	public static void dispose() {
	
	}
}
