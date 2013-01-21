package pt.me.microm.view.stuff;

import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.events.ScreenTickEvent;
import pt.me.microm.model.stuff.GroundModel;
import pt.me.microm.view.AbstractView;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;

public class GroundView extends AbstractView {
	private static final String TAG = GroundView.class.getSimpleName();
	
	private GroundModel groundmSrc;
	Texture texture = GAME_CONSTANTS.TEXTURE_DROID;
	ShapeRenderer renderer;
	
	public GroundView(GroundModel groundmSrc) {
		super(groundmSrc);
		this.groundmSrc = groundmSrc;
		
		renderer = new ShapeRenderer();
	}
	
	
	@Override
	public void draw(ScreenTickEvent e) {
		
		renderer.setProjectionMatrix(e.getCamera().combined);
		
		Fixture fix = (groundmSrc.getPlayzoneBody().getFixtureList()).get(0);
		ChainShape cs = (ChainShape)fix.getShape();
		
		Vector2 pointA = new Vector2();
		Vector2 pointB = new Vector2();
		
		renderer.begin(ShapeType.Line);
		cs.getVertex(0, pointA); pointA.add(groundmSrc.getPlayzoneBody().getPosition());
		cs.getVertex(1, pointB); pointB.add(groundmSrc.getPlayzoneBody().getPosition());
		renderer.line(pointA.x, pointA.y, pointB.x, pointB.y);
		
		cs.getVertex(1, pointA); pointA.add(groundmSrc.getPlayzoneBody().getPosition());	
		cs.getVertex(2, pointB); pointB.add(groundmSrc.getPlayzoneBody().getPosition());
		renderer.line(pointA.x, pointA.y, pointB.x, pointB.y);
		
		cs.getVertex(2, pointA); pointA.add(groundmSrc.getPlayzoneBody().getPosition());	 
		cs.getVertex(3, pointB); pointB.add(groundmSrc.getPlayzoneBody().getPosition());
		renderer.line(pointA.x, pointA.y, pointB.x, pointB.y);
		
		cs.getVertex(3, pointA); pointA.add(groundmSrc.getPlayzoneBody().getPosition());	 
		cs.getVertex(0, pointB); pointB.add(groundmSrc.getPlayzoneBody().getPosition());
		renderer.line(pointA.x, pointA.y, pointB.x, pointB.y);		
		
		renderer.end();
		
		
	}

}
