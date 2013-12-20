package pt.me.navigator.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;

public class MyTextInputListener implements TextInputListener {
	private static final String TAG = MyTextInputListener.class.getSimpleName();
	
	@Override
	public void input(String text) {
		Gdx.app.log(TAG, "[text]: " + text);
	}

	@Override
	public void canceled() {
	
	}
}
