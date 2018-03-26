package marcio.batik.custom;

import marcio.transform.Coordinate;
import org.apache.batik.parser.ParseException;
import org.apache.batik.parser.PathHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class MyPathHandler implements PathHandler {
    private static final Logger log = LoggerFactory.getLogger(MyPathHandler.class);

    //todo: this should be upgraded to a spline instead
    public ArrayList<Coordinate> path;

    private Coordinate penTip = new Coordinate();

    @Override
    public void startPath() throws ParseException {
        log.debug("startPath");
        path = new ArrayList<>();
    }

    @Override
    public void endPath() throws ParseException {
        log.debug("endPath");
    }

    /**
     * Parses a 'm' command.
     */
    @Override
    public void movetoRel(float x, float y) throws ParseException {
        log.debug("movetoRel (x,y)=('{}','{}')", x, y);
        penTip.x += x;
        penTip.y += y;
        path.add(new Coordinate(penTip.x, penTip.y));
    }

    /**
     * Parses a 'M' command.
     */
    @Override
    public void movetoAbs(float x, float y) throws ParseException {
        log.debug("movetoAbs (x,y)=('{}','{}')", x, y);
        penTip.x = x;
        penTip.y = y;
        path.add(new Coordinate(penTip.x, penTip.y));
    }

    /**
     * Parses a 'Z' or 'z' command.
     */
    @Override
    public void closePath() throws ParseException {
        Coordinate origin = path.get(0);
        log.debug("closePath (x,y)=('{}','{}'", origin.x, origin.y);
        penTip.x = origin.x;
        penTip.y = origin.y;
        path.add(new Coordinate(penTip.x, penTip.y));
    }

    /**
     * Parses a 'l' command.
     */
    @Override
    public void linetoRel(float x, float y) throws ParseException {
        log.debug("linetoRel (x,y)=('{}','{}')", x, y);
        penTip.x += x;
        penTip.y += y;
        path.add(new Coordinate(penTip.x, penTip.y));
    }

    /**
     * Parses a 'L' command.
     */
    @Override
    public void linetoAbs(float x, float y) throws ParseException {
        log.debug("linetoAbs (x,y)=('{}','{}')", x, y);
        penTip.x = x;
        penTip.y = y;
        path.add(new Coordinate(penTip.x, penTip.y));
    }

    /**
     * Parses a 'h' command.
     */
    @Override
    public void linetoHorizontalRel(float x) throws ParseException {
        log.debug("linetoHorizontalRel x='{}'", x);
        penTip.x += x;
        path.add(new Coordinate(penTip.x, penTip.y));
    }

    /**
     * Parses a 'H' command.
     */
    @Override
    public void linetoHorizontalAbs(float x) throws ParseException {
        log.debug("linetoHorizontalAbs x='{}'", x);
        penTip.x = x;
        path.add(new Coordinate(penTip.x, penTip.y));
    }

    /**
     * Parses a 'v' command.
     */
    @Override
    public void linetoVerticalRel(float y) throws ParseException {
        log.debug("linetoVerticalRel y='{}'", y);
        penTip.y += y;
        path.add(new Coordinate(penTip.x, penTip.y));
    }

    /**
     * Parses a 'V' command.
     */
    @Override
    public void linetoVerticalAbs(float y) throws ParseException {
        log.debug("linetoVerticalAbs y='{}'", y);
        penTip.y = y;
        path.add(new Coordinate(penTip.x, penTip.y));
    }

    /**
     * Parses a 'c' command.
     */
    @Override
    public void curvetoCubicRel(float x1, float y1, float x2, float y2, float x, float y) throws ParseException {
        log.debug("curvetoCubicRel (x1, y1, x2, y2, x, y)=('{}','{}','{}','{}','{}','{}')", x1, y1, x2, y2, x, y);
        log.warn("not implemented!");
    }

    /**
     * Parses a 'C' command.
     */
    @Override
    public void curvetoCubicAbs(float x1, float y1, float x2, float y2, float x, float y) throws ParseException {
        log.debug("curvetoCubicAbs (x1, y1, x2, y2, x, y)=('{}','{}','{}','{}','{}','{}')", x1, y1, x2, y2, x, y);
        log.warn("not implemented!");
    }

    /**
     * Parses a 's' command.
     */
    @Override
    public void curvetoCubicSmoothRel(float x2, float y2, float x, float y) throws ParseException {
        log.debug("curvetoCubicSmoothRel (x2, y2, x, y)=('{}','{}','{}','{}')", x2, y2, x, y);
        log.warn("not implemented!");
    }

    /**
     * Parses a 'S' command.
     */
    @Override
    public void curvetoCubicSmoothAbs(float x2, float y2, float x, float y) throws ParseException {
        log.debug("curvetoCubicSmoothAbs (x2, y2, x, y)=('{}','{}','{}','{}')", x2, y2, x, y);
        log.warn("not implemented!");
    }

    /**
     * Parses a 'q' command.
     */
    @Override
    public void curvetoQuadraticRel(float x1, float y1, float x, float y) throws ParseException {
        log.debug("curvetoQuadraticRel (x1, y1, x, y)=('{}','{}','{}','{}')", x1, y1, x, y);
        log.warn("not implemented!");
    }

    /**
     * Parses a 'Q' command.
     */
    @Override
    public void curvetoQuadraticAbs(float x1, float y1, float x, float y) throws ParseException {
        log.debug("curvetoQuadraticAbs (x1, y1, x, y)=('{}','{}','{}','{}')", x1, y1, x, y);
        log.warn("not implemented!");
    }

    /**
     * Parses a 't' command.
     */
    @Override
    public void curvetoQuadraticSmoothRel(float x, float y) throws ParseException {
        log.debug("curvetoQuadraticSmoothRel (x, y)=('{}','{}')", x, y);
        log.warn("not implemented!");
    }

    /**
     * Parses a 'T' command.
     */
    @Override
    public void curvetoQuadraticSmoothAbs(float x, float y) throws ParseException {
        log.debug("curvetoQuadraticSmoothAbs (x, y)=('{}','{}')", x, y);
        log.warn("not implemented!");
    }

    /**
     * Parses a 'a' command.
     */
    @Override
    public void arcRel(float rx, float ry, float xAxisRotation, boolean largeArcFlag, boolean sweepFlag, float x, float y) throws ParseException {
        log.debug("arcRel (rx, ry, xAxisRotation, largeArcFlag, sweepFlag, x, y)=('{}','{}','{}','{}','{}','{}','{}')", rx, ry, xAxisRotation, largeArcFlag, sweepFlag, x, y);
        log.warn("not implemented!");
    }

    /**
     * Parses a 'A' command.
     */
    @Override
    public void arcAbs(float rx, float ry, float xAxisRotation, boolean largeArcFlag, boolean sweepFlag, float x, float y) throws ParseException {
        log.debug("arcAbs (rx, ry, xAxisRotation, largeArcFlag, sweepFlag, x, y)=('{}','{}','{}','{}','{}','{}','{}')", rx, ry, xAxisRotation, largeArcFlag, sweepFlag, x, y);
        log.warn("not implemented!");
    }
}
