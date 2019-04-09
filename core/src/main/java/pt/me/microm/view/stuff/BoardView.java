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
import pt.me.microm.model.stuff.BoardModel;
import pt.me.microm.view.AbstractView;
import pt.me.microm.view.helper.SimpleRendererHelper;

import java.util.Iterator;

public class BoardView extends AbstractView {
    private static final String TAG = BoardView.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(TAG);

    private BoardModel boardmSrc;

    private Mesh boardMesh;

    public BoardView(BoardModel boardmSrc) {
        super(boardmSrc);
        this.boardmSrc = boardmSrc;
    }

    @Override
    public void DelayedInit() {

        boardMesh = SimpleRendererHelper.buildMesh(boardmSrc.getBasicShape());

    }


    private Vector2 pointA = new Vector2();
    private Vector2 pointB = new Vector2();

    @Override
    public void draw(ScreenTickEvent e) {

        SimpleRendererHelper.drawMesh(e.getCamera(), boardmSrc, boardMesh);
        if (boardmSrc.getBody() != null){

            if (GameMicroM.FLAG_DISPLAY_ACTOR_SHAPES) {
                renderer.setProjectionMatrix(e.getCamera().getGameCamera().combined);

                Iterator<Fixture> it = boardmSrc.getBody().getFixtureList().iterator();
                Fixture aux = null;
                while (it.hasNext()) {
                    aux = it.next();

                    renderer.identity();
                    renderer.translate(aux.getBody().getPosition().x, aux.getBody().getPosition().y, 0.0f);
                    renderer.rotate(0.0f, 0.0f, 1.0f, (float) Math.toDegrees(aux.getBody().getAngle()));

                    ChainShape cs = (ChainShape) aux.getShape();

                    renderer.begin(ShapeType.Line);
                    int vCnt = cs.getVertexCount();
                    for (int i = 0; i < vCnt; i++) {
                        cs.getVertex(i, pointA); //pointA.add(portalmSrc.getPortalBody().getPosition());
                        cs.getVertex(i == vCnt - 1 ? 0 : i + 1, pointB); //pointB.add(portalmSrc.getPortalBody().getPosition());
                        renderer.line(pointA.x, pointA.y, pointB.x, pointB.y);
                    }
                    renderer.end();
                }
            }
    }
    }

    @Override
    public void draw20(ScreenTickEvent e) {
        draw(e);
    }

}
