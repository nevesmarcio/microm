package pt.me.microm.view.stuff;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.me.microm.GameMicroM;
import pt.me.microm.controller.loop.event.ScreenTickEvent;
import pt.me.microm.model.stuff.GroundModel;
import pt.me.microm.view.AbstractView;
import pt.me.microm.view.helper.SimpleRendererHelper;


public class GroundView extends AbstractView {
    private static final String TAG = GroundView.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(TAG);

    private GroundModel groundmSrc;

    private Mesh groundMesh;

    public GroundView(GroundModel groundmSrc) {
        super(groundmSrc);
        this.groundmSrc = groundmSrc;
    }

    @Override
    public void DelayedInit() {

        groundMesh = SimpleRendererHelper.buildMesh(groundmSrc.getBasicShape());
    }

    private Vector2 pointA = new Vector2();
    private Vector2 pointB = new Vector2();

    @Override
    public void draw(ScreenTickEvent e) {


        SimpleRendererHelper.drawMesh(e.getCamera(), groundmSrc, groundMesh);

        if (GameMicroM.FLAG_DISPLAY_ACTOR_SHAPES) {
            renderer.setProjectionMatrix(e.getCamera().getGameCamera().combined);

            Fixture fix = (groundmSrc.getBody().getFixtureList()).get(0);
            ChainShape cs = (ChainShape) fix.getShape();

            renderer.identity();
            renderer.translate(groundmSrc.getBody().getPosition().x, groundmSrc.getBody().getPosition().y, 0.0f);
            renderer.rotate(0.0f, 0.0f, 1.0f, (float) Math.toDegrees(groundmSrc.getBody().getAngle()));

            renderer.begin(ShapeType.Line);
            int vCnt = cs.getVertexCount();
            for (int i = 0; i < vCnt; i++) {
                cs.getVertex(i, pointA);
                cs.getVertex(i == vCnt - 1 ? 0 : i + 1, pointB);
                renderer.line(pointA.x, pointA.y, pointB.x, pointB.y);
            }
            renderer.end();
        }

    }

    @Override
    public void draw20(ScreenTickEvent e) {
        draw(e);
    }

}
