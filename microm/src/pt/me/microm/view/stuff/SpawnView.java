package pt.me.microm.view.stuff;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.events.ScreenTickEvent;
import pt.me.microm.model.stuff.BoardModel;
import pt.me.microm.model.stuff.GoalModel;
import pt.me.microm.model.stuff.SpawnModel;
import pt.me.microm.view.AbstractView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;

public class SpawnView extends AbstractView {
	private static final String TAG = SpawnView.class.getSimpleName();
	
	private SpawnModel spawnmSrc;

	ShapeRenderer renderer;
	
	public SpawnView(SpawnModel spawnmSrc) {
		super(spawnmSrc);
		this.spawnmSrc = spawnmSrc;
		
		renderer = new ShapeRenderer();
	}
	
	private Vector2 pointA = new Vector2();
	private Vector2 pointB = new Vector2();
	@Override
	public void draw(ScreenTickEvent e) {
		
		renderer.setProjectionMatrix(e.getCamera().getGameCamera().combined);
		
		Fixture fix = (spawnmSrc.getBody().getFixtureList()).get(0);
		ChainShape cs = (ChainShape)fix.getShape();
		
		renderer.begin(ShapeType.Line);
			int vCnt = cs.getVertexCount();
			for (int i = 0; i < vCnt; i++) {
				cs.getVertex(i, pointA); pointA.add(spawnmSrc.getBody().getPosition());
				cs.getVertex(i==vCnt-1 ? 0 : i + 1, pointB); pointB.add(spawnmSrc.getBody().getPosition());
				renderer.line(pointA.x, pointA.y, pointB.x, pointB.y);
			}
		renderer.end();

		
		
	}

}
