package pt.me.microm.model.ui.utils;

import aurelienribon.tweenengine.TweenAccessor;

public class FlashMessageAccessor implements TweenAccessor<FlashMessage> {
	public static final int SCALE = 1;

	@Override
	public int getValues(FlashMessage target, int tweenType, float[] returnValues) {
		switch (tweenType) {
			case SCALE:
				float scale = target.scale;
				returnValues[0] = scale;
				return 1;
			default:
				assert false;
				return -1;					
		}
	}

	@Override
	public void setValues(FlashMessage target, int tweenType, float[] newValues) {
		switch (tweenType) {				
			case SCALE:
				target.scale = newValues[0];
				break;
		}
	}
	
}