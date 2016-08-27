package pt.me.microm.model.ui.utils;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import pt.me.microm.controller.loop.event.GameTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.event.SimpleEvent;
import pt.me.microm.model.AbstractModel;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.math.Vector2;


public class FlashMessageManagerModel extends AbstractModel {
	
	private static FlashMessageManagerModel instance = null;
	
	private TweenManager tweenManager = new TweenManager();
	
	private FlashMessageManagerModel() { // Exists only to defeat instantiation.
		
		// regista no tween manager o accessor para as FlashMessages
		Tween.registerAccessor(FlashMessage.class, new FlashMessageAccessor());		
		
		// Sinaliza os subscritores de que a construção do modelo terminou.
		this.dispatchEvent(new SimpleEvent(AbstractModel.EventType.ON_MODEL_INSTANTIATED));
	}
	
	public static FlashMessageManagerModel getInstance() {
		if (instance == null) {
			instance = new FlashMessageManagerModel();
			
		}
		return instance;
	}
		

	private static int MAX_FLASH_MESSAGES = 5;
	private Queue<FlashMessage> afm = new ArrayBlockingQueue<FlashMessage>(MAX_FLASH_MESSAGES);
	
	public Queue<FlashMessage> getAfm() {
		return afm;
	}

	public void addFlashMessage(IDataSourceObject<?> a, Vector2 position, float duration, boolean worldCoord) {
		
		final FlashMessage fm = new FlashMessage();
		fm.dataSource = a;
		fm.position = position;
		fm.scale = 1.0f;
		fm.worldCoord = worldCoord;
		
		afm.offer(fm);
		
		Tween.to(fm, FlashMessageAccessor.SCALE, duration).target(3.0f)
				.ease(aurelienribon.tweenengine.equations.Elastic.INOUT)
				.setCallback(new TweenCallback() {
					@Override
					public void onEvent(int type, BaseTween<?> source) {
						afm.remove(fm);
					}
				})
				.start(tweenManager);
		
	}
	
	
	@Override
	public void handleGameTick(GameTickEvent e) {
		float elapsedNanoTime = e.getElapsedNanoTime();
		
		// Faz o step das "animações"
		tweenManager.update(elapsedNanoTime/(float)GAME_CONSTANTS.ONE_SECOND_TO_NANO);
	}
	
	@Override
	public void dispose() {
		Tween.registerAccessor(FlashMessage.class, null);
		
		instance = null;
		super.dispose();
	}
	
	
	
}
