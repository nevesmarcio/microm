package pt.me.microm.tools.levelloader;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.google.common.eventbus.EventBus;
import marcio.LibgdxSvgLoader;
import marcio.batik.game1.LoadedActor;
import marcio.transform.AffineTransformation;
import marcio.transform.Coordinate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.me.microm.GameMicroM;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.base.CameraModel;
import pt.me.microm.model.collectible.StarModel;
import pt.me.microm.model.dev.DebugModel;
import pt.me.microm.model.stuff.*;
import pt.me.microm.model.trigger.SimpleTriggerModel;
import pt.me.microm.model.ui.TextModel;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class exposes a static method that allows the reading of a Level from an
 * SVG file
 *
 * @author Márcio Neves
 */
public class LevelLoader {
    private static final String TAG = LevelLoader.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(TAG);

    private static void addDebugPoints(LoadedActor loadedActor, Color color, EventBus modelEventBus) {
        if (GameMicroM.FLAG_DEBUG_POINTS) {
            DebugModel m;
            for (Coordinate c : loadedActor.path) {
                m = DebugModel.getNewInstance(modelEventBus, (float) c.x, (float) c.y);
                m.setColor(color);
            }
        }
    }

    /**
     * This function loads a level from a SVG file. It makes a lot of
     * assumptions about the elements contained on that SVG. No safeguards are
     * implemented... yet...
     *
     * @param h             The handle to the SVG file that represents a level
     * @param modelEventBus The EventBus of the game
     * @return number of assets loaded
     */
    public static ArrayList<AbstractModel> LoadLevel(FileHandle h, final EventBus modelEventBus) {

        final AtomicInteger nrElements = new AtomicInteger(0);
        final ArrayList<AbstractModel> modelBag = new ArrayList<AbstractModel>();


        LibgdxSvgLoader svgLoader = new LibgdxSvgLoader(AffineTransformation.scaleInstance(1D / 85.3D, -1D / 85.3D), new AppendToGame1() {


            @Override
            public void addCamera(LoadedActor loadedActor) {
                if (logger.isInfoEnabled()) logger.info("type='{}' loadedActor='{}'", "Camera", loadedActor);

                addDebugPoints(loadedActor, Color.GOLD, modelEventBus);

                BasicShape s = new BasicShape(loadedActor.path, loadedActor.style, ObjectType.CAMERA);

                // here we configure the camera
                CameraModel.getInstance().adjustCamera(s.getWidth(), s.getHeight(), s.getCentroid().x, s.getCentroid().y);

                nrElements.incrementAndGet();
            }

            @Override
            public void addBoard(LoadedActor loadedActor) {
                if (logger.isInfoEnabled()) logger.info("type='{}' loadedActor='{}'", "Board", loadedActor);

                addDebugPoints(loadedActor, Color.WHITE, modelEventBus);

                BasicShape s = new BasicShape(loadedActor.path, loadedActor.style, ObjectType.BOARD);
                BoardModel wam = BoardModel.getNewInstance(modelEventBus, s, loadedActor.id);
                modelBag.add(wam);

                nrElements.incrementAndGet();
            }

            @Override
            public void addDaBox(LoadedActor loadedActor) {
                if (logger.isInfoEnabled()) logger.info("type='{}' loadedActor='{}'", "DaBox", loadedActor);

                addDebugPoints(loadedActor, Color.WHITE, modelEventBus);

                BasicShape s = new BasicShape(loadedActor.path, loadedActor.style, ObjectType.DABOX);
                DaBoxModel dbm = DaBoxModel.getNewInstance(modelEventBus, s, loadedActor.id);
                modelBag.add(dbm);

                nrElements.incrementAndGet();
            }

            @Override
            public void addSpawn(LoadedActor loadedActor) {
                if (logger.isInfoEnabled()) logger.info("type='{}' loadedActor='{}'", "Spawn", loadedActor);

                addDebugPoints(loadedActor, Color.BLUE, modelEventBus);

                BasicShape s = new BasicShape(loadedActor.path, loadedActor.style, ObjectType.SPAWN);
                SpawnModel sm = SpawnModel.getNewInstance(modelEventBus, s, loadedActor.id);
                modelBag.add(sm);

                nrElements.incrementAndGet();
            }

            @Override
            public void addGoal(LoadedActor loadedActor) {
                if (logger.isInfoEnabled()) logger.info("type='{}' loadedActor='{}'", "Goal", loadedActor);

                addDebugPoints(loadedActor, Color.GREEN, modelEventBus);

                BasicShape s = new BasicShape(loadedActor.path, loadedActor.style, ObjectType.GOAL);
                GoalModel wam = GoalModel.getNewInstance(modelEventBus, s, loadedActor.id);
                modelBag.add(wam);

                nrElements.incrementAndGet();
            }

            @Override
            public void addGround(LoadedActor loadedActor) {
                if (logger.isInfoEnabled()) logger.info("type='{}' loadedActor='{}'", "Ground", loadedActor);

                addDebugPoints(loadedActor, Color.RED, modelEventBus);

                BasicShape s = new BasicShape(loadedActor.path, loadedActor.style, ObjectType.GROUND);
                GroundModel wam = GroundModel.getNewInstance(modelEventBus, s, loadedActor.id);
                modelBag.add(wam);

                nrElements.incrementAndGet();
            }

            @Override
            public void addPortal(LoadedActor loadedActor) {
                if (logger.isInfoEnabled()) logger.info("type='{}' loadedActor='{}'", "Portal", loadedActor);

                addDebugPoints(loadedActor, Color.WHITE, modelEventBus);

                BasicShape s = new BasicShape(loadedActor.path, loadedActor.style, ObjectType.PORTAL);
                PortalModel wam = PortalModel.getNewInstance(modelEventBus, s, loadedActor.id);
                modelBag.add(wam);

                nrElements.incrementAndGet();
            }

            @Override
            public void addWall(LoadedActor loadedActor) {
                if (logger.isInfoEnabled()) logger.info("type='{}' loadedActor='{}'", "Wall", loadedActor);

                addDebugPoints(loadedActor, Color.GRAY, modelEventBus);

                BasicShape s = new BasicShape(loadedActor.path, loadedActor.style, ObjectType.WALL);
                WallModel wam = WallModel.getNewInstance(modelEventBus, s, loadedActor.id);
                modelBag.add(wam);

                nrElements.incrementAndGet();
            }

            @Override
            public void addStar(LoadedActor loadedActor) {
                if (logger.isInfoEnabled()) logger.info("type='{}' loadedActor='{}'", "Star", loadedActor);

                addDebugPoints(loadedActor, Color.WHITE, modelEventBus);

                BasicShape s = new BasicShape(loadedActor.path, loadedActor.style, ObjectType.STAR);
                StarModel wam = StarModel.getNewInstance(modelEventBus, s, loadedActor.id);
                modelBag.add(wam);

                nrElements.incrementAndGet();
            }

            @Override
            public void addText(LoadedActor loadedActor) {
                if (logger.isInfoEnabled()) logger.info("type='{}' loadedActor='{}'", "Text", loadedActor);

                addDebugPoints(loadedActor, Color.YELLOW, modelEventBus);

                String id = loadedActor.id;
                if (logger.isInfoEnabled()) logger.info("[" + id + "]");

                double x = loadedActor.path.get(0).x;
                double y = loadedActor.path.get(0).y;
                String s = loadedActor.behaviour;

                if (logger.isInfoEnabled()) logger.info("[" + id + "] = x: " + x + "; y: " + y + "; ==> '" + s + "'");
                BasicShape sh = new BasicShape(loadedActor.path, loadedActor.style, ObjectType.TEXT);
                if (logger.isInfoEnabled()) logger.info(".:.:.:. " + sh.getCentroid() + " .:.:.:.");

                TextModel tm = TextModel.getNewInstance(modelEventBus, sh, id, s);
                modelBag.add(tm);

                nrElements.incrementAndGet();

            }

            @Override
            public void addTrigger(LoadedActor loadedActor) {
                if (logger.isInfoEnabled()) logger.info("type='{}' loadedActor='{}'", "Trigger", loadedActor);

                addDebugPoints(loadedActor, Color.BROWN, modelEventBus);

                BasicShape s = new BasicShape(loadedActor.path, loadedActor.style, ObjectType.TRIGGER);

                SimpleTriggerModel wam = SimpleTriggerModel.getNewInstance(modelEventBus, s, loadedActor.id);
                wam.setScript(loadedActor.behaviour);
                modelBag.add(wam);

                nrElements.incrementAndGet();
            }
        });
        try {
            svgLoader.loadSvg(h);
        } catch (Exception e) {
            e.printStackTrace();
        }


        int check = 0;
        for (AbstractModel am : modelBag) {
            if (am instanceof DebugModel) check += 1;
        }
        // the (-1) is because the camera loading parameters doesn't create a new model
        if (logger.isDebugEnabled())
            logger.debug("size of bag is {}; # of non debug point models is {}; # of debug points model is {}", modelBag.size(), nrElements.get() - 1, check);


        return modelBag;
    }

}
