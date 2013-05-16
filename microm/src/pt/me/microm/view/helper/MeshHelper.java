package pt.me.microm.view.helper;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;

public class MeshHelper {
	private Mesh mesh;
	private ShaderProgram meshShader;
	private Matrix4 combined;

	public MeshHelper() {
		createShader();
	}
	
	public void createMesh(float[] vertices) {
		mesh = new Mesh(true, vertices.length, 0, // We set this to 0 so that we
													// don’t have to specify an
													// indices array – the mesh
													// will just use each vertex
													// once in the order
													// specified. Indices can be
													// useful for more
													// complicated shapes, I
													// will explain these in
													// more detail in a separate
													// tutorial.
				new VertexAttribute(Usage.Position, 3, "a_position"));
		mesh.setVertices(vertices);
		
		combined = new Matrix4();
	}


	private void createShader() {
		// this shader tells opengl where to put things
		String vertexShader = "uniform mat4 u_mvpMatrix;                   \n" 
							+ "attribute vec4 a_position;                  \n"
							+ "uniform vec4 a_color;					   \n"
							+ "varying vec4 v_color;  					   \n"
							+ "void main()                                 \n" 
							+ "{                                           \n"
							+ "   gl_Position = u_mvpMatrix * a_position;  \n"
							+ "   v_color = a_color;					   \n"
							+ "}                            \n";

		// this one tells it what goes in between the points (i.e
		// colour/texture)
		String fragmentShader = "#ifdef GL_FRAGMENT_PRECISION_HIGH                \n"
							  + "precision highp float;      \n"
							  + "#else                       \n"
							  + "precision mediump float;     \n"
							  + "#endif                      \n"
							  + " 							 \n"
							  + "varying vec4 v_color;		 \n"
							  + "void main(void)             \n"
							  + "{                           \n"
							  + "  gl_FragColor = v_color;       // vec4(1.0,0.0,0.0,1.0);	\n"
							  + "}";

		// make an actual shader from our strings
		meshShader = new ShaderProgram(vertexShader, fragmentShader);

		// check there's no shader compile errors
		if (!meshShader.isCompiled())
			throw new IllegalStateException(meshShader.getLog());
	}

	
	public void drawMesh(Matrix4 projection, Matrix4 view, Matrix4 model, Color c) {
		// this should be called in render()
		if (mesh == null)
			throw new IllegalStateException("drawMesh called before a mesh has been created.");

		meshShader.begin();
		combined.set(projection).mul(view).mul(model);
		meshShader.setUniformMatrix("u_mvpMatrix", combined);
		meshShader.setUniformf("a_color", c.r, c.g, c.b, c.a);
		mesh.render(meshShader, GL20.GL_TRIANGLE_FAN);
		meshShader.end();
	}	
	
	
	public void dispose() {
		mesh.dispose();
		meshShader.dispose();
	}
}
