package pt.me.microm.view.helper;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.me.microm.model.IActorBody;
import pt.me.microm.model.base.CameraModel;
import pt.me.microm.tools.levelloader.BasicShape;

import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.Matrix4;

public class SimpleRendererHelper {

    private static final String TAG = SimpleRendererHelper.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(TAG);

    //create shader program
    private static String vertexShader =
            "uniform mat4 mModelViewMatrix;\n" +
                    "uniform mat4 mProjectionMatrix;\n" +
                    "attribute vec4 vPosition; 		\n" +
                    "void main()					\n" +
                    "{								\n" +
                    "	gl_Position = mProjectionMatrix * mModelViewMatrix * vPosition;\n" +
                    "}								\n";
    private static String fragmentShader =
            "#ifdef GL_ES 								\n" +
                    "precision mediump float;					\n" +
                    "#endif 									\n" +
                    "void main()								\n" +
                    "{											\n" +
                    "	gl_FragColor = vec4(1.0,0.0,0.0,1.0);	\n" +
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
		vertexes = new float[nr_points*4];

		indexes = new short[nr_points];
		
		for (int i = 0; i < nr_points; i++) {
			indexes[i] = (short)i;
			
			vertexes[i*4] = shape.getPointsArray()[i].x;
			vertexes[i*4+1] = shape.getPointsArray()[i].y;
			vertexes[i*4+2] = 0.0f;
			vertexes[i*4+3] = Color.toFloatBits(shape.getFillColor().r,
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
	public static void drawMesh(CameraModel camera, IActorBody model, Mesh mesh) {

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
