package pt.me.microm.view.stuff;

import java.util.Iterator;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.events.ScreenTickEvent;
import pt.me.microm.model.stuff.BoardModel;
import pt.me.microm.model.stuff.GoalModel;
import pt.me.microm.model.stuff.PortalModel;
import pt.me.microm.model.stuff.SpawnModel;
import pt.me.microm.view.AbstractView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;

public class PortalView extends AbstractView {
	private static final String TAG = PortalView.class.getSimpleName();
	
	private PortalModel portalmSrc;
	Texture texture = GAME_CONSTANTS.TEXTURE_DROID;
	ShapeRenderer renderer;
	
	public PortalView(PortalModel portalmSrc) {
		super(portalmSrc);
		this.portalmSrc = portalmSrc;
		
		renderer = new ShapeRenderer();
	}
	
	@Override
	public void draw(ScreenTickEvent e) {
		
		renderer.setProjectionMatrix(e.getCamera().combined);
		
		Iterator<Fixture> it = portalmSrc.getPortalBody().getFixtureList().iterator();
		
		while (it.hasNext()) {
			Fixture aux = it.next();
			
			renderer.identity();
			renderer.translate(aux.getBody().getPosition().x, aux.getBody().getPosition().y, 0.0f);
			renderer.rotate(0.0f, 0.0f, 1.0f, (float)Math.toDegrees(aux.getBody().getAngle()));
			
			ChainShape cs = (ChainShape)aux.getShape();
			Vector2 pointA = new Vector2();
			Vector2 pointB = new Vector2();
			
			renderer.begin(ShapeType.Line);
				int vCnt = cs.getVertexCount();
				for (int i = 0; i < vCnt; i++) {
					cs.getVertex(i, pointA); //pointA.add(portalmSrc.getPortalBody().getPosition());
					cs.getVertex(i==vCnt-1 ? 0 : i + 1, pointB); //pointB.add(portalmSrc.getPortalBody().getPosition());
					renderer.line(pointA.x, pointA.y, pointB.x, pointB.y);
				}
			renderer.end();
		}
		
	}

}
