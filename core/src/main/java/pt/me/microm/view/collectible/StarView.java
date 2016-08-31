package pt.me.microm.view.collectible;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.me.microm.GameMicroM;
import pt.me.microm.controller.loop.event.ScreenTickEvent;
import pt.me.microm.model.collectible.StarModel;
import pt.me.microm.view.AbstractView;
import pt.me.microm.view.helper.SimpleRendererHelper;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;


public class StarView extends AbstractView {
	private static final String TAG = StarView.class.getSimpleName();
	private static final Logger logger = LoggerFactory.getLogger(TAG);
	
	private StarModel starmSrc;
	
	private Mesh starMesh;
	
	public StarView(StarModel starmSrc) {
		super(starmSrc, 1); // FIXME: arranjar umas constantes para definir o zOrder
		this.starmSrc = starmSrc;
	}

	@Override
	public void DelayedInit() {

		starMesh = SimpleRendererHelper.buildMesh(starmSrc.getBasicShape());
	}
	
	
	Vector2 pointA = new Vector2();
	Vector2 pointB = new Vector2();
	@Override
	public void draw(ScreenTickEvent e) {

		SimpleRendererHelper.drawMesh(e.getCamera(), starmSrc, starMesh);

		if (GameMicroM.FLAG_DISPLAY_ACTOR_SHAPES) {
			renderer.setProjectionMatrix(e.getCamera().getGameCamera().combined);
			
			Iterator<Fixture> it = starmSrc.getBody().getFixtureList().iterator();
			Fixture aux = null;
			while (it.hasNext()) {
				aux = it.next();
				
				renderer.identity();
				renderer.translate(aux.getBody().getPosition().x, aux.getBody().getPosition().y, 0.0f);
				renderer.rotate(0.0f, 0.0f, 1.0f, (float)Math.toDegrees(aux.getBody().getAngle()));
				
				ChainShape cs = (ChainShape)aux.getShape();
	
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
	
	@Override
	public void draw20(ScreenTickEvent e) {
		draw(e);
	}

}
