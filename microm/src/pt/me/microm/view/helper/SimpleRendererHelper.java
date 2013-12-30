package pt.me.microm.view.helper;

import pt.me.microm.model.IActorBody;
import pt.me.microm.model.base.CameraModel;
import pt.me.microm.tools.levelloader.BasicShape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.Matrix4;

public class SimpleRendererHelper {
	
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
		
		Gdx.gl10.glPushMatrix();
		Gdx.gl10.glMatrixMode(GL10.GL_PROJECTION);
		Gdx.gl10.glLoadMatrixf(tempProjectionMatrix.translate(0.0f, 0.0f, 0.0f).val, 0);
		
		Gdx.gl10.glPushMatrix();
		Gdx.gl10.glMatrixMode(GL10.GL_MODELVIEW);
		Gdx.gl10.glLoadMatrixf(tempViewMatrix
				.translate(model.getPosition().x, model.getPosition().y, 0.0f)
				.rotate(0.0f, 0.0f, 1.0f, (float)Math.toDegrees(model.getAngle()))
				.val, 0);
	    //mesh.render(GL10.GL_TRIANGLES, 0, 3);
	    if (mesh != null)
	    	mesh.render(GL10.GL_TRIANGLE_FAN); //GL10.GL_TRIANGLE_FAN //GL10.GL_TRIANGLES //GL10.GL_TRIANGLE_STRIP  
		
	    Gdx.gl10.glPopMatrix();
	    Gdx.gl10.glPopMatrix();
		
	}

}
