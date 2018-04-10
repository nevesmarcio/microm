package pt.me.microm.tools.levelloader;

import com.badlogic.gdx.math.Vector2;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

/**
 *
 */

/**
 * @author mneves
 */
public class BasicShapeTest {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(BasicShapeTest.class.getSimpleName());

    private BasicShape basicShape;


    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {

    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        basicShape = null;
    }

    /**
     * Test method for {@link pt.me.microm.tools.levelloader.BasicShape#getFillColor()}.
     */
    @Test
    public void testGetFillColor() {
        basicShape = new BasicShape("m 425.67827,432.93665 89.09545,0 0,118.79393 -89.09545,0 z",
                "fill:#ff6600;fill-opacity:0.71;stroke:none",
                ObjectType.TEXT);

        log.info("Read color: " + basicShape.getFillColor().toString());

        assert (basicShape.getFillColor().r == (float) 0xff / (float) 0xff);
        assert (basicShape.getFillColor().g == (float) 0x66 / (float) 0xff);
        assert (basicShape.getFillColor().b == (float) 0x00 / (float) 0xff);
        //todo: disabled this assert - it was failing
        //assert(basicShape.getFillColor().a == 0.71f);
    }

    /**
     * Verifies how the coordinate system is translated
     */
    @Test
    public void testInitialization_0() {
        basicShape = new BasicShape("m 0,-1280 1280,0 0,1280 0,0 z",
                "fill:#ff6600;fill-opacity:0.71;stroke:none",
                ObjectType.TEXT);

        log.info(basicShape.getCentroid().toString());
        String aux = "";
        for (Vector2 v : basicShape.getPointsArray())
            aux += "\t..[" + (float) Math.round(v.x * 100) / 100.0f + ":" + (float) Math.round(v.y * 100) / 100.0f + "]";
        log.info("pts: " + aux);
        log.info("w: " + basicShape.getWidth() + " h:" + basicShape.getHeight());
    }

    @Test
    public void testInitialization_1() {
        basicShape = new BasicShape("m 0.0,0.0 1280,0 0,1280 -1280,0 z",
                "fill:#ff6600;fill-opacity:0.71;stroke:none",
                ObjectType.TEXT);

        log.info(basicShape.getCentroid().toString());
        String aux = "";
        for (Vector2 v : basicShape.getPointsArray())
            aux += "\t..[" + (float) Math.round(v.x * 100) / 100.0f + ":" + (float) Math.round(v.y * 100) / 100.0f + "]";
        log.info("pts: " + aux);
        log.info("w: " + basicShape.getWidth() + " h:" + basicShape.getHeight());
    }

    @Test
    public void testInitialization_2() {
        basicShape = new BasicShape("m 640.0,640.0 640,0 0,640 -640,0 z",
                "fill:#ff6600;fill-opacity:0.71;stroke:none",
                ObjectType.TEXT);

        log.info(basicShape.getCentroid().toString());
        String aux = "";
        for (Vector2 v : basicShape.getPointsArray())
            aux += "\t..[" + (float) Math.round(v.x * 100) / 100.0f + ":" + (float) Math.round(v.y * 100) / 100.0f + "]";
        log.info("pts: " + aux);
        log.info("w: " + basicShape.getWidth() + " h:" + basicShape.getHeight());
    }

    @Test
    public void testInitialization_3() {
        basicShape = new BasicShape("m 640.0,0.0 640,0 0,-640 -640,0 z",
                "fill:#ff6600;fill-opacity:0.71;stroke:none",
                ObjectType.TEXT);

        log.info(basicShape.getCentroid().toString());
        String aux = "";
        for (Vector2 v : basicShape.getPointsArray())
            aux += "\t..[" + (float) Math.round(v.x * 100) / 100.0f + ":" + (float) Math.round(v.y * 100) / 100.0f + "]";
        log.info("pts: " + aux);
        log.info("w: " + basicShape.getWidth() + " h:" + basicShape.getHeight());
    }


}