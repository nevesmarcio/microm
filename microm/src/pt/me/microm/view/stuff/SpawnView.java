package pt.me.microm.view.stuff;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.me.microm.GameMicroM;
import pt.me.microm.controller.loop.event.ScreenTickEvent;
import pt.me.microm.model.stuff.SpawnModel;
import pt.me.microm.view.AbstractView;
import pt.me.microm.view.helper.SimpleRendererHelper;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;

public class SpawnView extends AbstractView {
	private static final String TAG = SpawnView.class.getSimpleName();
	private static final Logger logger = LoggerFactory.getLogger(TAG);
	
	private SpawnModel spawnmSrc;

	private Mesh spawnMesh;
	
	public SpawnView(SpawnModel spawnmSrc) {
		super(spawnmSrc);
		this.spawnmSrc = spawnmSrc;
	}

	@Override
	public void DelayedInit() {

		spawnMesh = SimpleRendererHelper.buildMesh(spawnmSrc.getBasicShape());
	}

	
	private Vector2 pointA = new Vector2();
	private Vector2 pointB = new Vector2();
	@Override
	public void draw(ScreenTickEvent e) {
		
		SimpleRendererHelper.drawMesh(e.getCamera(), spawnmSrc, spawnMesh);
		
		if (GameMicroM.FLAG_DISPLAY_ACTOR_SHAPES) {
			renderer.setProjectionMatrix(e.getCamera().getGameCamera().combined);
			
			Fixture fix = (spawnmSrc.getBody().getFixtureList()).get(0);
			ChainShape cs = (ChainShape)fix.getShape();
			
			renderer.identity();
			renderer.translate(spawnmSrc.getBody().getPosition().x, spawnmSrc.getBody().getPosition().y, 0.0f);
			renderer.rotate(0.0f, 0.0f, 1.0f, (float)Math.toDegrees(spawnmSrc.getBody().getAngle()));			
			
			renderer.begin(ShapeType.Line);
				int vCnt = cs.getVertexCount();
				for (int i = 0; i < vCnt; i++) {
					cs.getVertex(i, pointA); 
					cs.getVertex(i==vCnt-1 ? 0 : i + 1, pointB); 
					renderer.line(pointA.x, pointA.y, pointB.x, pointB.y);
				}
			renderer.end();
		}
		
	}
	
	
	
	@Override
	public void draw20(ScreenTickEvent e) {
		
	}	
}
