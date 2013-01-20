package pt.me.microm.view.dev;

import java.util.Iterator;

import pt.me.microm.infrastructure.events.ScreenTickEvent;
import pt.me.microm.model.dev.DebugModel;
import pt.me.microm.view.AbstractView;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.physics.box2d.Fixture;

public class DebugView extends AbstractView {
	private static final String TAG = DebugView.class.getSimpleName();
	
	private DebugModel debugmSrc;
	
	ShapeRenderer renderer;
	
	public DebugView(DebugModel debugmSrc) {
		super(debugmSrc);
		this.debugmSrc = debugmSrc;
		
		renderer = new ShapeRenderer();
	}
	

	@Override
	public void draw(ScreenTickEvent e) {
		
		renderer.setProjectionMatrix(e.getCamera().combined);
		
		Iterator<Fixture> it = debugmSrc.debugBody.getFixtureList().iterator(); 
		while (it.hasNext()){
			Fixture aux = it.next();
			
			renderer.identity();
			renderer.translate(aux.getBody().getPosition().x, aux.getBody().getPosition().y, 0.0f);
			renderer.rotate(0.0f, 0.0f, 1.0f, (float)Math.toDegrees(aux.getBody().getAngle()));			
				
			renderer.begin(ShapeType.FilledCircle);
				
				renderer.setColor(debugmSrc.getColor());
				renderer.filledCircle(0.0f, 0.0f, debugmSrc.getRadius(), 50);
			renderer.end();

			renderer.begin(ShapeType.Line);
				renderer.setColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
				renderer.line(0.0f, 0.0f, 0.0f, debugmSrc.getRadius());
			renderer.end();		
		
		}

	}

}
