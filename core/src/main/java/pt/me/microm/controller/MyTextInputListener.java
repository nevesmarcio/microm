package pt.me.microm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Input.TextInputListener;

public class MyTextInputListener implements TextInputListener {
	private static final String TAG = MyTextInputListener.class.getSimpleName();
	private static final Logger logger = LoggerFactory.getLogger(TAG);
	
	@Override
	public void input(String text) {
		logger.info("[text]: " + text);
	}

	@Override
	public void canceled() {
	
	}
}