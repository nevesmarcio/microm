package pt.me.microm.tools.levelloader;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import marcio.LibgdxSvgLoader;
import marcio.batik.game1.LoadedActor;
import marcio.transform.AffineTransformation;
import marcio.transform.Coordinate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import pt.me.microm.GameMicroM;
import pt.me.microm.model.AbstractModel;
import pt.me.microm.model.base.CameraModel;
import pt.me.microm.model.base.WorldModel;
import pt.me.microm.model.collectible.StarModel;
import pt.me.microm.model.dev.DebugModel;
import pt.me.microm.model.stuff.*;
import pt.me.microm.model.trigger.SimpleTriggerModel;
import pt.me.microm.model.ui.TextModel;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
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

    private LevelLoader() {
    }


    /**
     * @param wm
     * @param dabox
     * @param dabox_name
     * @return
     */
    private static DaBoxModel addDaBoxToWorld(ArrayList<AbstractModel> modelBag, WorldModel wm, BasicShape dabox, String dabox_name) {
        DaBoxModel dbm = DaBoxModel.getNewInstance(wm, dabox, dabox_name);
        wm.setPlayer(dbm);

        modelBag.add(dbm);

        return dbm;
    }

    /**
     * @param spawn
     * @param wm
     */
    private static SpawnModel addSpawnToWorld(ArrayList<AbstractModel> modelBag, WorldModel wm, DaBoxModel dbm, BasicShape spawn, String spawn_name) {
        SpawnModel sm = SpawnModel.getNewInstance(wm, dbm, spawn, spawn_name);
        wm.setSpawnModel(sm);

        modelBag.add(sm);

        return sm;
    }

    private static TextModel addTextToWorld(ArrayList<AbstractModel> modelBag, WorldModel wm, BasicShape sh, String text_name, final String content) {
        TextModel tm = TextModel.getNewInstance(wm, sh, text_name, content);
        modelBag.add(tm);
        return tm;

    }

    private static void addDebugPoints(LoadedActor loadedActor, WorldModel wm, Color color){
        if (GameMicroM.FLAG_DEBUG_POINTS) {
            DebugModel m;
            for (Coordinate c : loadedActor.path) {
                m = DebugModel.getNewInstance(wm, (float) c.x, (float) c.y);
                m.setColor(color);
            }
        }
    }

    /**
     * This function loads a level from a SVG file. It makes a lot of
     * assumptions about the elements contained on that SVG. No safeguards are
     * implemented... yet...
     *
     * @param h        The handle to the SVG file that represents a level
     * @param camWidth The box2d World object
     * @return number of assets loaded
     */
    public static ArrayList<AbstractModel> LoadLevel(FileHandle h, final WorldModel wm, final CameraModel cm) {

        final AtomicInteger nrElements = new AtomicInteger(0);
        final ArrayList<AbstractModel> modelBag = new ArrayList<AbstractModel>();


        LibgdxSvgLoader svgLoader = new LibgdxSvgLoader(AffineTransformation.scaleInstance(1D / 85.3D, -1D / 85.3D), new AppendToGame1() {


            @Override
            public void addCamera(LoadedActor loadedActor) {
                if (logger.isInfoEnabled()) logger.info("type='{}' loadedActor='{}'", "Camera", loadedActor);

                addDebugPoints(loadedActor, wm,Color.GOLD);

                BasicShape s = new BasicShape(loadedActor.path, loadedActor.style, ObjectType.CAMERA);

                // here we configure the camera
                cm.adjustCamera(s.getWidth(), s.getHeight(), s.getCentroid().x, s.getCentroid().y);

                nrElements.incrementAndGet();
            }

            @Override
            public void addBoard(LoadedActor loadedActor) {
                if (logger.isInfoEnabled()) logger.info("type='{}' loadedActor='{}'", "Board", loadedActor);

                addDebugPoints(loadedActor, wm,Color.WHITE);

                BasicShape s = new BasicShape(loadedActor.path, loadedActor.style, ObjectType.BOARD);

                BoardModel wam = BoardModel.getNewInstance(wm, s, loadedActor.id);
                modelBag.add(wam);

                nrElements.incrementAndGet();
            }

            @Override
            public void addDaBox(LoadedActor loadedActor) {
                addDebugPoints(loadedActor, wm,Color.WHITE);
            }

            @Override
            public void addSpawn(LoadedActor loadedActor) {
                addDebugPoints(loadedActor, wm,Color.BLUE);
            }

            @Override
            public void addGoal(LoadedActor loadedActor) {
                if (logger.isInfoEnabled()) logger.info("type='{}' loadedActor='{}'", "Goal", loadedActor);

                addDebugPoints(loadedActor, wm,Color.GREEN);

                BasicShape s = new BasicShape(loadedActor.path, loadedActor.style, ObjectType.GOAL);

                GoalModel wam = GoalModel.getNewInstance(wm, s, loadedActor.id);
                modelBag.add(wam);

                nrElements.incrementAndGet();

            }

            @Override
            public void addGround(LoadedActor loadedActor) {
                if (logger.isInfoEnabled()) logger.info("type='{}' loadedActor='{}'", "Ground", loadedActor);

                addDebugPoints(loadedActor, wm,Color.RED);

                BasicShape s = new BasicShape(loadedActor.path, loadedActor.style, ObjectType.GROUND);

                GroundModel wam = GroundModel.getNewInstance(wm, s, loadedActor.id);
                modelBag.add(wam);

                nrElements.incrementAndGet();
            }

            @Override
            public void addPortal(LoadedActor loadedActor) {
                if (logger.isInfoEnabled()) logger.info("type='{}' loadedActor='{}'", "Portal", loadedActor);

                addDebugPoints(loadedActor, wm,Color.WHITE);

                BasicShape s = new BasicShape(loadedActor.path, loadedActor.style, ObjectType.PORTAL);

                PortalModel wam = PortalModel.getNewInstance(wm, s, loadedActor.id);
                modelBag.add(wam);

                nrElements.incrementAndGet();

            }

            @Override
            public void addWall(LoadedActor loadedActor) {
                if (logger.isInfoEnabled()) logger.info("type='{}' loadedActor='{}'", "Wall", loadedActor);

                addDebugPoints(loadedActor, wm,Color.GRAY);

                BasicShape s = new BasicShape(loadedActor.path, loadedActor.style, ObjectType.WALL);

                WallModel wam = WallModel.getNewInstance(wm, s, loadedActor.id);
                modelBag.add(wam);

                nrElements.incrementAndGet();
            }

            @Override
            public void addStar(LoadedActor loadedActor) {
                if (logger.isInfoEnabled()) logger.info("type='{}' loadedActor='{}'", "Star", loadedActor);

                addDebugPoints(loadedActor, wm,Color.WHITE);

                BasicShape s = new BasicShape(loadedActor.path, loadedActor.style, ObjectType.STAR);

                StarModel wam = StarModel.getNewInstance(wm, s, loadedActor.id);
                modelBag.add(wam);

                nrElements.incrementAndGet();

            }

            @Override
            public void addText(LoadedActor loadedActor) {
                addDebugPoints(loadedActor, wm,Color.YELLOW);
            }

            @Override
            public void addTrigger(LoadedActor loadedActor) {
                if (logger.isInfoEnabled()) logger.info("type='{}' loadedActor='{}'", "Trigger", loadedActor);

                addDebugPoints(loadedActor, wm,Color.BROWN);

                BasicShape s = new BasicShape(loadedActor.path, loadedActor.style, ObjectType.TRIGGER);

                SimpleTriggerModel wam = SimpleTriggerModel.getNewInstance(wm, s, loadedActor.id);
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


        //////////////



        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            //factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            //FileHandle h = Gdx.files.internal("data/levels/sample.xml");
            Document doc = builder.parse(h.read());
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
            //XPathExpression expr = xpath.compile("//book[author='Neal Stephenson']/title/text()");
            //expr.evaluate(doc, XPathConstants.STRING);

            XPathExpression expr;


            // Get DaBox
            DaBoxModel daBoxRef = null;
            if (logger.isInfoEnabled()) logger.info("DaBox...");
            expr = xpath.compile("//svg/g/path[contains(@id,'daBox')]");
            NodeList dabox = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < dabox.getLength(); i++) {
                String d = dabox.item(i).getAttributes().getNamedItem("d").getNodeValue();
                String style = dabox.item(i).getAttributes().getNamedItem("style").getNodeValue();
                if (logger.isInfoEnabled()) logger.info("d= " + d + "; style= " + style + ";");

                BasicShape s = new BasicShape(d, style, ObjectType.DABOX);
                String dabox_name = dabox.item(i).getAttributes().getNamedItem("id").getNodeValue();
                daBoxRef = addDaBoxToWorld(modelBag, wm, s, dabox_name);

                nrElements.incrementAndGet();
            }

            // Get Spawn
            if (logger.isInfoEnabled()) logger.info("Spawn...");
            expr = xpath.compile("//svg/g/path[contains(@id,'spawn')]");
            NodeList spawn = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < spawn.getLength(); i++) {
                String d = spawn.item(i).getAttributes().getNamedItem("d").getNodeValue();
                String style = spawn.item(i).getAttributes().getNamedItem("style").getNodeValue();
                if (logger.isInfoEnabled()) logger.info("d= " + d + "; style= " + style + ";");

                BasicShape s = new BasicShape(d, style, ObjectType.SPAWN);
                String spawn_name = spawn.item(i).getAttributes().getNamedItem("id").getNodeValue();
                addSpawnToWorld(modelBag, wm, daBoxRef, s, spawn_name);

                nrElements.incrementAndGet();
            }

            // Get text
            if (logger.isInfoEnabled()) logger.info("Text...");
            expr = xpath.compile("//svg/g/text[contains(@id,'text')]/tspan");
            NodeList text = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < text.getLength(); i++) {
                String id = text.item(i).getAttributes().getNamedItem("id").getNodeValue();
                if (logger.isInfoEnabled()) logger.info("[" + id + "]");

                String x = text.item(i).getAttributes().getNamedItem("x").getNodeValue();
                String y = text.item(i).getAttributes().getNamedItem("y").getNodeValue();
                String s = text.item(i).getTextContent();

                if (logger.isInfoEnabled()) logger.info("[" + id + "] = x: " + x + "; y: " + y + "; ==> '" + s + "'");
                BasicShape sh = new BasicShape("m " + x + "," + y, "", ObjectType.TEXT);
                if (logger.isInfoEnabled()) logger.info(".:.:.:. " + sh.getCentroid() + " .:.:.:.");

                addTextToWorld(modelBag, wm, sh, id, s);

                nrElements.incrementAndGet();
            }

            if (logger.isInfoEnabled()) logger.info("Finished Loading level: " + h.name());

        } catch (ParserConfigurationException e) {
            if (logger.isErrorEnabled()) logger.error(e.getMessage());
            e.printStackTrace();
        } catch (SAXException e) {
            if (logger.isErrorEnabled()) logger.error(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            if (logger.isErrorEnabled()) logger.error(e.getMessage());
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            if (logger.isErrorEnabled()) logger.error(e.getMessage());
            e.printStackTrace();
        }

        int check = 0;
        for (AbstractModel am : modelBag) {
            if (am instanceof DebugModel) check += 1;
        }
        // the (-1) is because the camera loading parameters doesn't create a new model
        if (logger.isDebugEnabled())
            logger.debug("size of bag is {}; # of non debug point models is {}; # of debug points model is {}", modelBag.size(), nrElements.get() - 1, check);



        //todo: remove this
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return modelBag;
    }

}
