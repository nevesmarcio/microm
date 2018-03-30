package marcio.batik.custom;

import marcio.transform.Coordinate;
import marcio.transform.Spline;
import marcio.transform.SplineVertex;
import org.apache.batik.parser.ParseException;
import org.apache.batik.parser.PathHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class MyPathHandler implements PathHandler {
    private static final Logger log = LoggerFactory.getLogger(MyPathHandler.class);

    //todo: this should be upgraded to a spline instead
    public Spline path;

    @Override
    public void startPath() throws ParseException {
        log.debug("startPath");
        path = new Spline();
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
        // 'm' or 'M' commands are always the first commands of a path, therefore no previous vertex
        path.addVertex(new SplineVertex(x, y));
    }

    /**
     * Parses a 'M' command.
     */
    @Override
    public void movetoAbs(float x, float y) throws ParseException {
        log.debug("movetoAbs (x,y)=('{}','{}')", x, y);
        // 'm' or 'M' commands are always the first commands of a path, therefore no previous vertex
        path.addVertex(new SplineVertex(x, y));
    }

    /**
     * Parses a 'Z' or 'z' command.
     */
    @Override
    public void closePath() throws ParseException {
        SplineVertex origin = path.getFirst();
        log.debug("closePath (x,y)=('{}','{}')", origin.p.x, origin.p.y);
        path.addVertex(origin);
    }

    /**
     * Parses a 'l' command.
     */
    @Override
    public void linetoRel(float x, float y) throws ParseException {
        log.debug("linetoRel (x,y)=('{}','{}')", x, y);
        SplineVertex toAdd = path.getLast()
                .getCopy()
                .offsetXYBy(x, y);

        path.addVertex(toAdd);
    }

    /**
     * Parses a 'L' command.
     */
    @Override
    public void linetoAbs(float x, float y) throws ParseException {
        log.debug("linetoAbs (x,y)=('{}','{}')", x, y);
        SplineVertex toAdd = path.getLast()
                .getCopy()
                .setXYTo(x,y);

        path.addVertex(toAdd);
    }

    /**
     * Parses a 'h' command.
     */
    @Override
    public void linetoHorizontalRel(float x) throws ParseException {
        log.debug("linetoHorizontalRel x='{}'", x);
        SplineVertex toAdd = path.getLast()
                .getCopy()
                .offsetXBy(x);

        path.addVertex(toAdd);
    }

    /**
     * Parses a 'H' command.
     */
    @Override
    public void linetoHorizontalAbs(float x) throws ParseException {
        log.debug("linetoHorizontalAbs x='{}'", x);
        SplineVertex toAdd = path.getLast()
                .getCopy()
                .setXTo(x);

        path.addVertex(toAdd);
    }

    /**
     * Parses a 'v' command.
     */
    @Override
    public void linetoVerticalRel(float y) throws ParseException {
        log.debug("linetoVerticalRel y='{}'", y);
        SplineVertex toAdd = path.getLast()
                .getCopy()
                .offsetYBy(y);

        path.addVertex(toAdd);
    }

    /**
     * Parses a 'V' command.
     */
    @Override
    public void linetoVerticalAbs(float y) throws ParseException {
        log.debug("linetoVerticalAbs y='{}'", y);
        SplineVertex toAdd = path.getLast()
                .getCopy()
                .setYTo(y);

        path.addVertex(toAdd);
    }

    /**
     * Parses a 'c' command.
     */
    @Override
    public void curvetoCubicRel(float x1, float y1, float x2, float y2, float x, float y) throws ParseException {
        log.debug("curvetoCubicRel (x1, y1, x2, y2, x, y)=('{}','{}','{}','{}','{}','{}')", x1, y1, x2, y2, x, y);
        // adjust control point of previous point
        path.getLast().offsetCP2By(x1, y1);

        //Create a new point relative to the last
        SplineVertex toAdd = new SplineVertex(path.getLast().p);
        toAdd.offsetPBy(x, y);
        toAdd.offsetCP1By(x2, y2);
        toAdd.offsetCP2By(x, y);

        path.addVertex(toAdd);
    }

    /**
     * Parses a 'C' command.
     */
    @Override
    public void curvetoCubicAbs(float x1, float y1, float x2, float y2, float x, float y) throws ParseException {
        log.debug("curvetoCubicAbs (x1, y1, x2, y2, x, y)=('{}','{}','{}','{}','{}','{}')", x1, y1, x2, y2, x, y);
        // adjust control point of previous point
        path.getLast().setCP2To(x1, y1);

        //Create a new point
        SplineVertex toAdd = new SplineVertex(x, y, x2, y2, x, y);

        path.addVertex(toAdd);

    }

    /**
     * Parses a 's' command.
     */
    @Override
    public void curvetoCubicSmoothRel(float x2, float y2, float x, float y) throws ParseException {
        log.debug("curvetoCubicSmoothRel (x2, y2, x, y)=('{}','{}','{}','{}')", x2, y2, x, y);
        log.error("command not implemented!");
    }

    /**
     * Parses a 'S' command.
     */
    @Override
    public void curvetoCubicSmoothAbs(float x2, float y2, float x, float y) throws ParseException {
        log.debug("curvetoCubicSmoothAbs (x2, y2, x, y)=('{}','{}','{}','{}')", x2, y2, x, y);
        log.error("command not implemented!");
    }

    /**
     * Parses a 'q' command.
     */
    @Override
    public void curvetoQuadraticRel(float x1, float y1, float x, float y) throws ParseException {
        log.debug("curvetoQuadraticRel (x1, y1, x, y)=('{}','{}','{}','{}')", x1, y1, x, y);
        log.error("command not implemented!");
    }

    /**
     * Parses a 'Q' command.
     */
    @Override
    public void curvetoQuadraticAbs(float x1, float y1, float x, float y) throws ParseException {
        log.debug("curvetoQuadraticAbs (x1, y1, x, y)=('{}','{}','{}','{}')", x1, y1, x, y);
        log.error("command not implemented!");
    }

    /**
     * Parses a 't' command.
     */
    @Override
    public void curvetoQuadraticSmoothRel(float x, float y) throws ParseException {
        log.debug("curvetoQuadraticSmoothRel (x, y)=('{}','{}')", x, y);
        log.error("command not implemented!");
    }

    /**
     * Parses a 'T' command.
     */
    @Override
    public void curvetoQuadraticSmoothAbs(float x, float y) throws ParseException {
        log.debug("curvetoQuadraticSmoothAbs (x, y)=('{}','{}')", x, y);
        log.error("command not implemented!");
    }

    /**
     * Parses a 'a' command.
     */
    @Override
    public void arcRel(float rx, float ry, float xAxisRotation, boolean largeArcFlag, boolean sweepFlag, float x, float y) throws ParseException {
        log.debug("arcRel (rx, ry, xAxisRotation, largeArcFlag, sweepFlag, x, y)=('{}','{}','{}','{}','{}','{}','{}')", rx, ry, xAxisRotation, largeArcFlag, sweepFlag, x, y);
        log.error("command not implemented!");
    }

    /**
     * Parses a 'A' command.
     */
    @Override
    public void arcAbs(float rx, float ry, float xAxisRotation, boolean largeArcFlag, boolean sweepFlag, float x, float y) throws ParseException {
        log.debug("arcAbs (rx, ry, xAxisRotation, largeArcFlag, sweepFlag, x, y)=('{}','{}','{}','{}','{}','{}','{}')", rx, ry, xAxisRotation, largeArcFlag, sweepFlag, x, y);
        log.error("command not implemented!");
    }
}
