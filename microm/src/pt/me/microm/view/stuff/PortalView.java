package pt.me.microm.view.stuff;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.me.microm.GameMicroM;
import pt.me.microm.controller.loop.event.ScreenTickEvent;
import pt.me.microm.model.stuff.PortalModel;
import pt.me.microm.view.AbstractView;
import pt.me.microm.view.helper.SimpleRendererHelper;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;


public class PortalView extends AbstractView {
	private static final String TAG = PortalView.class.getSimpleName();
	private static final Logger logger = LoggerFactory.getLogger(TAG);
	
	private PortalModel portalmSrc;
	
	private Mesh portalMesh;
	
	public PortalView(PortalModel portalmSrc) {
		super(portalmSrc);
		this.portalmSrc = portalmSrc;
	}

	@Override
	public void DelayedInit() {

		portalMesh = SimpleRendererHelper.buildMesh(portalmSrc.getBasicShape());
	}
	
	
	
	private Vector2 pointA = new Vector2();
	private Vector2 pointB = new Vector2();
	@Override
	public void draw(ScreenTickEvent e) {

		SimpleRendererHelper.drawMesh(e.getCamera(), portalmSrc, portalMesh);
	    
	    //DEBUG STYLE 			-- desenha a "Shape" do portal
		if (GameMicroM.FLAG_DISPLAY_ACTOR_SHAPES) {
		    renderer.setProjectionMatrix(e.getCamera().getGameCamera().combined);
			
			Iterator<Fixture> it2 = portalmSrc.getBody().getFixtureList().iterator();
			
			while (it2.hasNext()) {
				Fixture aux = it2.next();
				
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
		
	}	
	
}
