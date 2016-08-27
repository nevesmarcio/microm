package pt.me.microm.view.dev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.me.microm.controller.loop.event.ScreenTickEvent;
import pt.me.microm.model.dev.DebugModel;
import pt.me.microm.view.AbstractView;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;


public class DebugView extends AbstractView {
	private static final String TAG = DebugView.class.getSimpleName();
	private static final Logger logger = LoggerFactory.getLogger(TAG);
	
	private DebugModel debugmSrc;
	
	public DebugView(DebugModel debugmSrc) {
		super(debugmSrc, 2);
		this.debugmSrc = debugmSrc;
	}
	
	@Override
	public void DelayedInit() {

	}
	
	@Override
	public void draw(ScreenTickEvent e) {

		renderer.setProjectionMatrix(e.getCamera().getGameCamera().combined);
		
		renderer.identity();
		renderer.translate(debugmSrc.getPosition().x, debugmSrc.getPosition().y, 0.0f);
			
		renderer.begin(ShapeType.FilledCircle);
			renderer.setColor(debugmSrc.getColor());
			renderer.filledCircle(0.0f, 0.0f, debugmSrc.getRadius(), 50);
		renderer.end();

		renderer.begin(ShapeType.Line);
			renderer.setColor(1.0f, 1.0f, 1.0f, 1.0f);
			renderer.line(0.0f, 0.0f, 0.0f, debugmSrc.getRadius());
		renderer.end();
		
	}
	
	@Override
	public void draw20(ScreenTickEvent e) {
	}
	
}
