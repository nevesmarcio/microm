package pt.me.microm._package_by_feature_.screen;

import pt.me.microm.infrastructure.GAME_CONSTANTS;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

public class ItemSelector extends Widget {

	private int activeIndex = 0; 
	private Object[] list;
	
	private Sprite s1;
	private Sprite s2;
	
	public ItemSelector(Object [] list, int bulletSize) {
		this.list = list;
		
		s1 = GAME_CONSTANTS.devAtlas.createSprite("txr_wall");
		s2 = GAME_CONSTANTS.devAtlas.createSprite("txr_daBox");
		
	}
	
	/**
	 * Choose what index should be active
	 * @param index
	 */
	public void setActive(int index) {
		this.activeIndex = index;
		
		invalidate();
		// TODO animate stuff?
		
	
	}
	
	/**
	 * 
	 * @return the active index
	 */
	public int getActive() {
		return activeIndex;
	}

	
	@Override
	public void layout() {
		super.layout();
	}
	
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		validate();
		
		float x = getX();
		float y = getY();
		float w = getWidth();
		float h = getHeight();

		batch.draw(s2, x, y, w, h);
		for (int i = 0; i < list.length; i++) {
			batch.draw(s1,  x+i*(w/list.length)+(w/list.length/4),
							y,
							w/(2*list.length) + (activeIndex==i ? w/(2*list.length)/3 : 0),
							w/(2*list.length) + (activeIndex==i ? w/(2*list.length)/3 : 0));	
		}
			
		
		
		
		//super.draw(batch, parentAlpha);
	}

	
	
	
}
