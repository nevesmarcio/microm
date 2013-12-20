package pt.me.navigator.view.dev;

import pt.me.navigator.infrastructure.events.ScreenTickEvent;
import pt.me.navigator.model.dev.GridModel;
import pt.me.navigator.view.AbstractView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.Matrix4;

public class GridView  extends AbstractView {
	private static final String TAG = GridView.class.getSimpleName();
	
	private GridModel gridSrc;
	private Mesh myMesh;
	
	private int nr_points;
	
	public GridView(GridModel gridSrc) {
		super(gridSrc);
		this.gridSrc = gridSrc;
		
		this.gridSrc.setGridSpacing(1);
		this.gridSrc.setBoxSize(5);
		
		nr_points = (int)(this.gridSrc.getBoxSize()/this.gridSrc.getGridSpacing());
		
		myMesh = new Mesh(true, (int)Math.pow(nr_points,3), (int)(Math.pow(nr_points,3)), 
									new VertexAttribute(Usage.Position, 3, "a_position"),
									new VertexAttribute(Usage.ColorPacked, 4, "a_color"));
		
		float points[] = new float[(int)Math.pow(nr_points,3)*4];
		short indexes[] = new short[(int)Math.pow(nr_points,3)];
		
		for (short z=0; z<nr_points; z++)	{
			for(short y=0; y<nr_points; y++) {
				for(short x=0; x<nr_points; x++) {
					int aux_points = 4*((int)Math.pow(nr_points, 2)*z + (int)Math.pow(nr_points, 1)*y + (int)Math.pow(nr_points, 0)*x);
					int aux_index = (int)Math.pow(nr_points, 2)*z + (int)Math.pow(nr_points, 1)*y + (int)Math.pow(nr_points, 0)*x; 

					points[aux_points+0] = x*this.gridSrc.getGridSpacing();
					points[aux_points+1] = y*this.gridSrc.getGridSpacing();
					points[aux_points+2] = -z*this.gridSrc.getGridSpacing();
					points[aux_points+3] = Color.toFloatBits(0, 192, 0, 255);
					
					indexes[aux_index] = (short)aux_index;
				}
			}
		}

		myMesh.setVertices(points);   
		myMesh.setIndices(indexes);				
		
		Gdx.graphics.getGL10().glPointSize(1.5f);		

	}
	
	Matrix4 tmp = new Matrix4();
	@Override
	public void draw(ScreenTickEvent e) {
		
		// aplica os parâmetros da camera ao contexto GL - equivalente ao set das "Projection Matrix"?
//		e.getCamera().getGameCamera().apply(Gdx.gl10);
		

			
		

			Gdx.gl10.glMatrixMode(GL10.GL_PROJECTION);
			Gdx.gl10.glLoadMatrixf(e.getCamera().getGameCamera().projection.cpy().translate(0.0f, 0.0f, -10.0f).val, 0);
		
			
			
			Gdx.gl10.glPushMatrix();
				Gdx.gl10.glMatrixMode(GL10.GL_MODELVIEW);
				Gdx.gl10.glLoadMatrixf(e.getCamera().getGameCamera().view.cpy().translate(15.0f,0.0f,0.0f).val, 0);
	
				if (myMesh != null)
						myMesh.render(GL10.GL_POINTS, 0, (int)Math.pow(nr_points,3));
			Gdx.gl10.glPopMatrix();

			
			
			Gdx.gl10.glPushMatrix();
				Gdx.gl10.glMatrixMode(GL10.GL_MODELVIEW);
				Gdx.gl10.glLoadMatrixf(e.getCamera().getGameCamera().view.cpy().translate(-17.0f,0.0f,0.0f).val, 0);
	
				if (myMesh != null)
						myMesh.render(GL10.GL_POINTS, 0, (int)Math.pow(nr_points,3));
			Gdx.gl10.glPopMatrix();			
			
			
			
			tmp.set(e.getCamera().rotationValues);
			if (tmp.det() == 0) tmp.idt();
			Gdx.gl10.glPushMatrix();
				Gdx.gl10.glMatrixMode(GL10.GL_MODELVIEW);
				Gdx.gl10.glLoadMatrixf(e.getCamera().getGameCamera().view.cpy().mul(tmp.inv()).translate(-2.0f, -2.0f, 2.0f).val, 0);
	
				if (myMesh != null)
						myMesh.render(GL10.GL_POINTS, 0, (int)Math.pow(nr_points,3));
			Gdx.gl10.glPopMatrix();

		//myMesh.render(GL10.GL_LINE_STRIP, 0, (int)Math.pow(nr_points,3)*4);
	}

}
