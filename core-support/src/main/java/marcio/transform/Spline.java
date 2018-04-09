package marcio.transform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Conceptually a spline is a path
 */
public class Spline extends Path {
    private static final Logger log = LoggerFactory.getLogger(Spline.class);

    public ArrayList<SplineVertex> splineVertices = new ArrayList<SplineVertex>();
    private ArrayList<SplineVertex> newArray = new ArrayList<SplineVertex>();

    public Spline(ArrayList<SplineVertex> v) {
        splineVertices = v;
    }

    public Spline() {
    }

    public void addVertex(SplineVertex v) {
        splineVertices.add(v);
    }

    private SplineVertex newPoint(SplineVertex sp1, SplineVertex sp2) {

        /*           x----v2----x
         *          /  .      .  \
         *         / v12--ns--v23 \
         *        /.              .\
         *       v1                 v3
         *      /                    \
         *     /                      \
         *    /                        \
         *  sp1                        sp2
         *
         *      ns - new spline point
         *      x - control point
         */

        Coordinate v1 = midPoint(sp1.p, sp1.cp2);
        Coordinate v2 = midPoint(sp1.cp2, sp2.cp1);
        Coordinate v3 = midPoint(sp2.cp1, sp2.p);

        Coordinate v12 = midPoint(v1, v2);
        Coordinate v23 = midPoint(v2, v3);

        sp1.cp2 = v1;
        sp2.cp1 = v3;

        return new SplineVertex(midPoint(v12, v23), v12, v23);
    }

    public void refine(double tol) {
        boolean complete = false;

        if (tol == 0)
            tol = 0.01f;
        // Loop until the curvature is within
        // tolerance
        while (complete == false) {

            complete = true;
            int size = splineVertices.size() - 1;

            // Loop over the spline points and create a new point
            // as per diagram for each point. Then test the curvature
            // if the curvature is greater than the tolerance add the
            // point to our new refined line
            newArray = new ArrayList<SplineVertex>();
            for (int i = 0; i < size; i++) {
                SplineVertex sp1 = splineVertices.get(i);
                SplineVertex sp2 = splineVertices.get(i + 1);
                SplineVertex n1 = newPoint(sp1, sp2);

                newArray.add(sp1);

                if (curvature(sp1.p, sp2.p, n1.p) > tol) {
                    newArray.add(n1);
                    complete = false;
                }

                // Because we're working on a i+1 basis
                // it's necessary to add the last point
                // at the end
                if (i == size - 1) {
                    newArray.add(sp2);
                }
            }
            splineVertices = newArray;
        }
    }

    // Complying with the path interface - return
    // a list of vertices
    public ArrayList<Coordinate> getVertices() {

        ArrayList<Coordinate> v = new ArrayList<Coordinate>();
        for (SplineVertex sp : splineVertices) {
            v.add(sp.p);
        }

        return v;
    }

    public ArrayList<Coordinate> getPolygon() {
        ArrayList<Coordinate> v = getVertices();


        log.debug("eucledean dist between first and last points is: {}", v.get(0).distance(v.get(v.size()-1)));
        if (v.get(0).equals(v.get(v.size() - 1))) {
            log.debug("removed last point of the polygon");
            v.remove(v.size() - 1);
        }

        return v;
    }


    private Coordinate midPoint(Coordinate v1, Coordinate v2) {
        return new Coordinate((v1.x + v2.x) / 2, (v1.y + v2.y) / 2);
    }

    private double curvature(Coordinate v1, Coordinate v2, Coordinate vc) {
        Coordinate midPoint = midPoint(v1, v2);
        return sq(midPoint.x - vc.x) + sq(midPoint.y - vc.y);
    }

    private double sq(double f1) {
        return f1 * f1;
    }

    // Standard path type functions

    public int size() {
        return splineVertices.size();
    }

    public SplineVertex getFirst() {
        return splineVertices.get(0);
    }

    public SplineVertex getLast() {
        return splineVertices.get(splineVertices.size() - 1);
    }

    public String toString() {
        String s = new String();
        for (Coordinate v : getVertices()) {
            s += v.toString() + "\n";
        }
        return s;
    }


}
