package pt.me.microm.view.dev;

import java.util.Iterator;

import pt.me.microm.controller.loop.event.ScreenTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.model.dev.DebugModel;
import pt.me.microm.view.AbstractView;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Logger;

public class DebugView extends AbstractView {
	private static final String TAG = DebugView.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private DebugModel debugmSrc;
	
	ShapeRenderer renderer;
	
	public DebugView(DebugModel debugmSrc) {
		super(debugmSrc, 2);
		this.debugmSrc = debugmSrc;
	}
	
	@Override
	public void DelayedInit() {
		renderer = new ShapeRenderer();
	}
	
	
	private Iterator<Fixture> it;
	private Fixture aux;
	@Override
	public void draw(ScreenTickEvent e) {
		
		renderer.setProjectionMatrix(e.getCamera().getGameCamera().combined);
		
		it = debugmSrc.getBody().getFixtureList().iterator(); 
		while (it.hasNext()){
			aux = it.next();
			
			renderer.identity();
			renderer.translate(aux.getBody().getPosition().x, aux.getBody().getPosition().y, 0.0f);
			renderer.rotate(0.0f, 0.0f, 1.0f, (float)Math.toDegrees(aux.getBody().getAngle()));			
				
			renderer.begin(ShapeType.FilledCircle);
				renderer.setColor(debugmSrc.getColor());
				renderer.filledCircle(0.0f, 0.0f, debugmSrc.getRadius(), 50);
			renderer.end();

			renderer.begin(ShapeType.Line);
				renderer.setColor(1.0f, 1.0f, 1.0f, 1.0f);
				renderer.line(0.0f, 0.0f, 0.0f, debugmSrc.getRadius());
			renderer.end();		
		
		}

	}

}
