package pt.me.microm.view.helper;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.me.microm.model.IBody;
import pt.me.microm.model.base.CameraModel;
import pt.me.microm.tools.levelloader.BasicShape;

public class SimpleRendererHelper {

    private static final String TAG = SimpleRendererHelper.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(TAG);

    //create shader program
    private static String vertexShader =
            "#ifdef GL_ES\n" +
                    "precision mediump float;\n" +
                    "#endif\n" +
                    "uniform mat4 mModelViewMatrix;\n" +
                    "uniform mat4 mProjectionMatrix;\n" +
                    "attribute vec4 a_position; 		\n" +
                    "attribute vec4 a_color;\n" +
                    "varying vec4 v_color;\n" +
                    "void main()					\n" +
                    "{								\n" +
                    "v_color = a_color; \n" +
                    "	gl_Position = mProjectionMatrix * mModelViewMatrix * a_position;\n" +
                    "}								\n";
    private static String fragmentShader =
            "#ifdef GL_ES 								\n" +
                    "precision mediump float;					\n" +
                    "#endif 									\n" +
                    "varying vec4 v_color;\n" +
                    "void main()								\n" +
                    "{											\n" +
                    "	gl_FragColor = v_color;	\n" +
                    "}											\n";
    private static ShaderProgram shaderProgram = new ShaderProgram(vertexShader, fragmentShader);

    static {
        if (shaderProgram.isCompiled() == false) {
            if (logger.isErrorEnabled()) logger.error("ShaderError: {}", new Object[]{shaderProgram.getLog()});
            System.exit(0);
        }
    }

    public static Mesh buildMesh(BasicShape shape) {
        Mesh newMesh;

        float[] vertexes;
        short[] indexes;
        int nr_points = shape.getPointsArray().length;
        vertexes = new float[nr_points * 4];

        indexes = new short[nr_points];

        for (int i = 0; i < nr_points; i++) {
            indexes[i] = (short) i;

            vertexes[i * 4] = shape.getPointsArray()[i].x;
            vertexes[i * 4 + 1] = shape.getPointsArray()[i].y;
            vertexes[i * 4 + 2] = 0.0f;
            vertexes[i * 4 + 3] = Color.toFloatBits(shape.getFillColor().r,
                    shape.getFillColor().g,
                    shape.getFillColor().b,
                    shape.getFillColor().a);
        }

        newMesh = new Mesh(true, nr_points, nr_points,
                new VertexAttribute(Usage.Position, 3, "a_position"),
                new VertexAttribute(Usage.ColorPacked, 4, "a_color"));

        newMesh.setVertices(vertexes);
        newMesh.setIndices(indexes);

        return newMesh;
    }

    private static Matrix4 tempProjectionMatrix = new Matrix4();
    private static Matrix4 tempViewMatrix = new Matrix4();

    public static void drawMesh(CameraModel camera, IBody model, Mesh mesh) {

        tempProjectionMatrix.set(camera.getGameCamera().projection);
        tempViewMatrix.set(camera.getGameCamera().view);

        shaderProgram.begin();
        shaderProgram.setUniformMatrix("mProjectionMatrix", tempProjectionMatrix
                .translate(0.0f, 0.0f, 0.0f));
        shaderProgram.setUniformMatrix("mModelViewMatrix", tempViewMatrix
                .translate(model.getPosition().x, model.getPosition().y, 0.0f)
                .rotate(0.0f, 0.0f, 1.0f, (float) Math.toDegrees(model.getAngle())));
        if (mesh != null) {
            mesh.render(shaderProgram, GL20.GL_TRIANGLE_FAN); //GL20.GL_TRIANGLE_FAN //GL20.GL_TRIANGLES //GL20.GL_TRIANGLE_STRIP
        }
        shaderProgram.end();

    }

}
