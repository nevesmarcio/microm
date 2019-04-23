package com.example.projectionviewportcamera;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProjectionViewportCamera implements ApplicationListener {

    private static final String TAG = ProjectionViewportCamera.class.getSimpleName();
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

    // Nice Tutorial: https://www.youtube.com/watch?v=-tonZsbHty8
    // Model to World
    // World to View
    // View to Projection
    // https://xoppa.github.io/blog/basic-3d-using-libgdx/

    // Depth testing
    // https://learnopengl.com/Advanced-OpenGL/Depth-testing

    private ShaderProgram shaderProgram;

    private Mesh square1Mesh;
    private Mesh square2Mesh;

    public ModelBatch modelBatch;
    public Model model;
    public ModelInstance instance;

    public Environment environment;

    private PerspectiveCamera camera;
    private float FOV_Y = 67;

    @Override
    public void create() {
        shaderProgram = new ShaderProgram(vertexShader, fragmentShader);

        if (shaderProgram.isCompiled() == false) {
            if (logger.isErrorEnabled()) logger.error("ShaderError: {}", new Object[]{shaderProgram.getLog()});
            System.exit(0);
        }


        if (square1Mesh == null) {
            square1Mesh = new Mesh(true, 4, 4,
                    new VertexAttribute(Usage.Position, 3, "a_position"),
                    new VertexAttribute(Usage.ColorPacked, 4, "a_color"));

            square1Mesh.setVertices(new float[]{
                    -0.5f, -0.5f, 0, Color.toFloatBits(128, 0, 0, 255),
                    0.5f, -0.5f, 0, Color.toFloatBits(192, 0, 0, 255),
                    -0.5f, 0.5f, 0, Color.toFloatBits(192, 0, 0, 255),
                    0.5f, 0.5f, 0, Color.toFloatBits(255, 0, 0, 255)});
            square1Mesh.setIndices(new short[]{0, 1, 2, 3});
        }

        if (square2Mesh == null) {
            square2Mesh = new Mesh(true, 4, 4,
                    new VertexAttribute(Usage.Position, 3, "a_position"),
                    new VertexAttribute(Usage.ColorPacked, 4, "a_color"));

            square2Mesh.setVertices(new float[]{
                    -0.5f, -0.5f, -0.5f, Color.toFloatBits(0, 0, 128, 255),
                    0.5f, -0.5f, -0.5f, Color.toFloatBits(0, 0, 192, 255),
                    -0.5f, 0.5f, -0.5f, Color.toFloatBits(0, 0, 192, 255),
                    0.5f, 0.5f, -0.5f, Color.toFloatBits(0, 0, 255, 255)});
            square2Mesh.setIndices(new short[]{0, 1, 2, 3});
        }

        modelBatch = new ModelBatch();
        ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createBox(3f, 3f, 3f,
                new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                Usage.Position | Usage.Normal);
        instance = new ModelInstance(model);

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new PerspectiveCamera(FOV_Y, (w / h), 1);
        adjustCam(camera, w, h);


        ExecutorService executor = Executors.newSingleThreadExecutor();
        Scanner in = new Scanner(System.in);
        executor.submit(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String s = in.nextLine();
                    logger.info(">>{}", s);
                    String[] splitResult;
                    splitResult = s.split(":");
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            logger.info("submitted");
                            camera.fieldOfView = Float.valueOf(splitResult[0]);
                            camera.viewportWidth = Float.valueOf(splitResult[1]);
                            camera.viewportHeight = Float.valueOf(splitResult[2]);
                            camera.update();
                            logger.info("updated");
                        }
                    });
                }
            }
        });

    }

    private void adjustCam(Camera cam, float w, float h) {
        camera.fieldOfView = FOV_Y;
        camera.viewportWidth = (w / h);
        camera.viewportHeight = 1;
        camera.near = 0.01f;
        camera.far = 1000f;
        camera.position.set(0f, 0f, 3.85f);
        camera.lookAt(0f, 0f, 0f);
        camera.update();
    }

    @Override
    public void dispose() {
    }

    @Override
    public void pause() {
    }

    private static Matrix4 tempProjectionMatrix = new Matrix4();
    private static Matrix4 tempViewMatrix = new Matrix4();

    @Override
    public void render() {
        Gdx.gl20.glEnable(GL20.GL_DEPTH_TEST);

        Gdx.gl.glClearColor(0, 0, 20, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);


        modelBatch.begin(camera);
        modelBatch.render(instance, environment);
        modelBatch.end();

        tempProjectionMatrix.set(camera.projection);
        tempViewMatrix.set(camera.view);

        shaderProgram.begin();
        shaderProgram.setUniformMatrix("mProjectionMatrix", tempProjectionMatrix);
        shaderProgram.setUniformMatrix("mModelViewMatrix", tempViewMatrix);
        square1Mesh.render(shaderProgram, GL20.GL_TRIANGLE_FAN);
        square2Mesh.render(shaderProgram, GL20.GL_TRIANGLE_STRIP);
        shaderProgram.end();

    }

    @Override
    public void resize(int width, int height) {
        adjustCam(camera, width, height);
    }

    @Override
    public void resume() {
    }
}