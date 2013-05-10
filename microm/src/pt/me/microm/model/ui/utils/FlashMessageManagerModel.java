package pt.me.microm.model.ui.utils;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.math.Vector2;


public class FlashMessageManagerModel extends AbstractModel {
	
	private static FlashMessageManagerModel instance = null;
	
	private TweenManager tm;
	
	private FlashMessageManagerModel(TweenManager tm) {
		// Exists only to defeat instantiation.
		this.tm = tm;
		
		
		// Sinaliza os subscritores de que a construção do modelo terminou.
		this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));
	}
	
	public static FlashMessageManagerModel getInstance(TweenManager tm) {
		if (instance == null) {
			instance = new FlashMessageManagerModel(tm);
			
		}
		return instance;
	}
		

	private static int MAX_FLASH_MESSAGES = 5;
	public Queue<FlashMessage> afm = new ArrayBlockingQueue<FlashMessage>(MAX_FLASH_MESSAGES);
	
	public void addFlashMessage(IDataSourceObject<?> a, Vector2 position, float duration, boolean worldCoord) {
		
		final FlashMessage fm = new FlashMessage();
		fm.dataSource = a;
		fm.position = position;
		fm.scale = 1.0f;
		
		afm.offer(fm);
		
		Tween.to(fm, FlashMessageAccessor.SCALE, duration).target(3.0f)
				.ease(aurelienribon.tweenengine.equations.Elastic.INOUT)
				.setCallback(new TweenCallback() {
					@Override
					public void onEvent(int type, BaseTween<?> source) {
						afm.remove(fm);
					}
				})
				.start(tm);
		
	}
	
	
	@Override
	public void handleGameTick(GameTickEvent e) {
		
	}
	
	
	
	
	
}
