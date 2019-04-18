package pt.me.microm.tools.levelloader;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import marcio.transform.Coordinate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * BasicShape is an object that is initialized with an SVG "d" string.
 * It decodes this string and translates the shape to a more workable coordinate system and units: box2d coordinate system and meters
 * <p>
 * Adopted convention to define the shapes:
 * # calculate the minimum rectangle that can be considered to inscribe the shape into
 * # calculate the centroid of this rectangle
 * # all the points that represent the shape are defined around this centroid (centroid is considered 0,0 coordinate)
 * # the position of the shape is considered to be this centroid coordinate
 *
 * @author mneves
 */
public class BasicShape {
    private static final String TAG = BasicShape.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(TAG);

    private ArrayList<Vector2> points;
    private float[] meshValues;
    private ObjectType type;
    private Vector2 centroid;
    private Vector2 rotationPivot;

    private Color color;

    private float width;
    private float height;

    private BasicShape() {
        points = new ArrayList<Vector2>();
    }

    /**
     * This constructor builds a shape using the points specified by a SVG 'd'
     * element
     *
     * @param d
     */
    public BasicShape(ArrayList<Coordinate> d, String style, ObjectType type) {
        this();

        Pattern pattern;
        Matcher matcher;

        Vector2 pt;
        for (int i = 0; i < d.size(); i++) // beware of too small distance between points
        {
            pt = new Vector2((float) d.get(i).x, (float) d.get(i).y);
            this.points.add(pt);
        }

        // calc centroid of the shape, considering the minimum rectangle that can be created to inscribe the shape into
        centroid = inscribedPolygonCenter();
        rotationPivot = centroid.cpy();

        //coordenadas do objecto definidas em torno do ponto 0.0f, 0.0f -- mais fácil para a renderização e rotações?
        for (Vector2 v : points) {
            v.sub(getCentroid());
        }

        //scaling and Y-invert
//        float scale = 1.0f/GAME_CONSTANTS.DIPIXELS_PER_METER;
//        scaleAndYInvertShape(scale);

        this.type = type;

        // calc width and height
        width = calcWidth();
        height = calcHeight();

        // converted mesh values init
        meshValues = new float[points.size() * 3];
        for (int ii = 0; ii < points.size(); ii++) {
            meshValues[ii * 3] = points.get(ii).x;
            meshValues[ii * 3 + 1] = points.get(ii).y;
            meshValues[ii * 3 + 2] = 0.0f;
        }

        // color fill
        color = new Color();
        pattern = Pattern.compile("fill:#([0-9a-fA-F]){6};*");
        matcher = pattern.matcher(style);
        while (matcher.find()) {
            String aux = matcher.group();
            aux = aux.replace("fill:#", "");
            aux = aux.replace(";", "");
            color.r = (float) Integer.parseInt(aux.substring(0, 2), 16) / (float) 0xFF;
            color.g = (float) Integer.parseInt(aux.substring(2, 4), 16) / (float) 0xFF;
            color.b = (float) Integer.parseInt(aux.substring(4, 6), 16) / (float) 0xFF;
        }
        // color fill opacity
        pattern = Pattern.compile("(?<!fill-)opacity:[01][\\.0-9]*;*"); // positive lookbehind
        matcher = pattern.matcher(style);
        while (matcher.find()) {
            String aux = matcher.group();
            aux = aux.replace("opacity:", "");
            aux = aux.replace(";", "");
            color.a = Float.parseFloat(aux);
        }


        if (logger.isDebugEnabled()) logger.debug("new shape - " + this.toString());
    }

    /**
     * this point is "world" coordinates
     *
     * @return meters
     */
    public Vector2 getCentroid() {
        return centroid;
    }

    public Vector2 getRotationPivot() {
        return rotationPivot;
    }

    public ObjectType getType() {
        return type;
    }

    /**
     * @return the width of the minimal rectangle in meters
     */
    public float getWidth() {
        return width;
    }

    /**
     * @return the height of the minimal rectangle in meters
     */
    public float getHeight() {
        return height;
    }

    /**
     * these points are "local" coordinates (centroid = [0.0, 0.0] )
     *
     * @return meters
     */
    public Vector2[] getPointsArray() {
        return Collections.unmodifiableList(points).toArray(new Vector2[]{});
    }

    public Color getFillColor() {
        return color;
    }

    public float[] getMeshValues() {
        return meshValues;
    }

    /////////////// Auxiliar functions ///////////////////

    /**
     * @return the center of the smallest rectangle that can be used to inscribe
     * the polygon into
     */
    private Vector2 inscribedPolygonCenter() {
        Float minX = null, maxX = null;
        Float minY = null, maxY = null;

        for (Vector2 point : points) {
            if ((minX == null) || (point.x < minX)) minX = point.x;
            if ((maxX == null) || (point.x > maxX)) maxX = point.x;
            if ((minY == null) || (point.y < minY)) minY = point.y;
            if ((maxY == null) || (point.y > maxY)) maxY = point.y;
        }

        return new Vector2(minX + (maxX - minX) / 2, minY + (maxY - minY) / 2);
    }

    /**
     * @return the width of the smallest rectangle that can be used to inscribe the polygon into
     */
    private float calcWidth() {
        Float minX = null, maxX = null;

        for (Vector2 point : points) {
            if ((minX == null) || (point.x < minX)) minX = point.x;
            if ((maxX == null) || (point.x > maxX)) maxX = point.x;
        }
        return maxX - minX;
    }

    /**
     * @return the height of the smallest rectangle that can be used to inscribe the polygon into
     */
    private float calcHeight() {
        Float minY = null, maxY = null;

        for (Vector2 point : points) {
            if ((minY == null) || (point.y < minY)) minY = point.y;
            if ((maxY == null) || (point.y > maxY)) maxY = point.y;
        }
        return maxY - minY;
    }


}
