package pt.me.microm;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainActivity extends AndroidApplication {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();

        Logger logger = LoggerFactory.getLogger(MainActivity.class.getSimpleName());
        logger.debug("Hello world.");

        initialize(new GameMicroM(), cfg);
    }
}