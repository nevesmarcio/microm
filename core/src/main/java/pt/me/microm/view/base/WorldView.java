package pt.me.microm.view.base;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.utils.Array;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.me.microm.controller.loop.event.ScreenTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.view.AbstractView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class WorldView extends AbstractView {
    private static final String TAG = WorldView.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(TAG);

    private WorldModel wmSrc;

    private Sprite worldSprite;
    private SpriteBatch batch = new SpriteBatch();

    public WorldView(WorldModel wmSrc) {
        super(wmSrc, 0);
        this.wmSrc = wmSrc;

    }

    @Override
    public void DelayedInit() {

        worldSprite = GAME_CONSTANTS.devAtlas.createSprite("bg");

        worldSprite.setSize(15.0f, 15.0f);
        worldSprite.setOrigin(0.0f, 0.0f);

        batch = new SpriteBatch();
    }


    private List<Contact> temp = new ArrayList<Contact>();

    @Override
    public void draw(ScreenTickEvent e) {
//FIXME:Uma catreifada dos estouros fora da VM bate aqui 
//      Em principio este pedaço de código provoca os estouros fora da VM!! (devido ao array estar a ser manipulado sem cópia?)		
//
//		if (GameMicroM.FLAG_DEV_ELEMENTS_A) {
//			/* renderização do world sprite... nem faz mto sentido isto, mas pronto */
//			batch.setProjectionMatrix(e.getCamera().getGameCamera().combined);
//			batch.begin();
//				worldSprite.draw(batch);
//			batch.end();		
//		
//			/* renderização dos contactos */ 
//			temp.clear();
//			temp.addAll(wmSrc.getWorldPhysicsManager().getPhysicsWorld().getContactList());
//			for (int i=0; i < temp.size(); i++) {
//			
//				Contact aux = temp.get(i);
//				
//				WorldManifold wmfold = aux.getWorldManifold();
//
//				for (int j = 0; j<wmfold.getNumberOfContactPoints(); j++) {
//					Vector2 cpt = wmfold.getPoints()[j];
//					
//					renderer.identity();
//					renderer.translate(cpt.x, cpt.y, 0.0f);
//
//					renderer.begin(ShapeType.FilledCircle);
//					renderer.setColor(1.0f, 1.0f, 1.0f, 1.0f);
//					renderer.filledCircle(0.0f, 0.0f, 0.1f, 10);
//					renderer.end();
//				}
//			}		
//		
//		}

        renderer.setProjectionMatrix(e.getCamera().getGameCamera().combined);
        /* renderização dos joints */
        Array<Joint> joints = new Array<Joint>();
        wmSrc.getWorldPhysicsManager().getPhysicsWorld().getJoints(joints);
        Iterator<Joint> it = joints.iterator();
        while (it.hasNext()) {
            Joint aux = it.next();

            renderer.identity();
            renderer.translate(aux.getAnchorA().x, aux.getAnchorA().y, 0.0f);

            renderer.begin(ShapeType.Line);
            renderer.setColor(1.0f, 0.5f, 0.5f, 1.0f);
            renderer.line(0.0f, 0.0f, aux.getAnchorB().x - aux.getAnchorA().x, aux.getAnchorB().y - aux.getAnchorA().y);
            renderer.end();
        }


    }

    @Override
    public void draw20(ScreenTickEvent e) {
        draw(e);
    }

}
