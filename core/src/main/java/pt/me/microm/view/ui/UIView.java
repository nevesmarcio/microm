package pt.me.microm.view.ui;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.me.microm.controller.loop.event.ScreenTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.model.ui.UIModel;
import pt.me.microm.view.AbstractView;


public class UIView extends AbstractView {
    private static final String TAG = UIView.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(TAG);

    private UIModel uiSrc;

    SpriteBatch batch;

    public UIView(UIModel uiSrc) {
        super(uiSrc, 100); // FIXME: arranjar umas constantes para definir o zOrder
        this.uiSrc = uiSrc;
    }

    @Override
    public void DelayedInit() {
        batch = new SpriteBatch();

    }

    private Vector3 intersection_point = new Vector3();
    private Plane intersect_plane = new Plane(new Vector3(0.0f, 0.0f, 1.0f), 0.0f);
    private Ray rr;

    @Override
    public void draw(ScreenTickEvent e) {

		/* RED : intersecção calculada pelo RAY */
        for (int i = 0; i < GAME_CONSTANTS.MAX_TOUCH_POINTS; i++) {

            if (uiSrc.getWindowCoordTestPoint()[i] != null) {
                rr = e.getCamera().getGameCamera().getPickRay(uiSrc.getWindowCoordTestPoint()[i].x, uiSrc.getWindowCoordTestPoint()[i].y);
                if (logger.isDebugEnabled()) logger.debug("PickingTest - ray: " + rr);

                Intersector.intersectRayPlane(rr, intersect_plane, intersection_point);
                if (logger.isDebugEnabled()) logger.debug("Intersect: " + intersection_point);

                renderer.setProjectionMatrix(e.getCamera().getGameCamera().combined);

                renderer.begin(ShapeType.Line);
                renderer.identity();
                renderer.translate(intersection_point.x, intersection_point.y, intersection_point.z);
                renderer.setColor(Color.RED);
                renderer.circle(0.0f, 0.0f, 0.300f, 20);
                renderer.circle(0.0f, 0.0f, 0.280f, 20);
                renderer.end();
            }
        }
	
		
		/* GREEN : renderização do local onde o rato está a apontar - 2D */
        OrthographicCamera c;
        renderer.setProjectionMatrix((c = e.getCamera().getUiCamera()).combined);

        renderer.begin(ShapeType.Line);
        for (int i = 0; i < GAME_CONSTANTS.MAX_TOUCH_POINTS; i++) {
            if (uiSrc.getWorldCoordTestPoint()[i] != null) {
                renderer.identity();

                Vector3 vec = uiSrc.getWindowCoordTestPoint()[i].cpy();
                c.unproject(vec);

                renderer.translate(vec.x, vec.y, vec.z);
                renderer.setColor(Color.GREEN);
                renderer.circle(0.0f, 0.0f, 30.0f, 20);
                renderer.circle(0.0f, 0.0f, 28.0f, 20);
            }
        }
        renderer.end();


    }

    @Override
    public void draw20(ScreenTickEvent e) {
        draw(e);

    }

}
