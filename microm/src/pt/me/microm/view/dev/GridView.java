package pt.me.microm.view.dev;

import pt.me.microm.controller.loop.event.ScreenTickEvent;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.model.dev.GridModel;
import pt.me.microm.view.AbstractView;
import pt.me.microm.view.helper.MeshHelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Logger;

public class GridView  extends AbstractView {
	private static final String TAG = GridView.class.getSimpleName();
	private static final Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private GridModel gridSrc;
	private Mesh myMesh;
	private MeshHelper meshHelper;
	
	private int nr_points;
	
	public GridView(GridModel gridSrc) {
		super(gridSrc);
		this.gridSrc = gridSrc;
	}

	@Override
	public void DelayedInit() {
		this.gridSrc.setGridSpacing(1);
		this.gridSrc.setBoxSize(16);
		
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

		meshHelper = new MeshHelper();
		meshHelper.createMesh(points);
		
	}
	

	@Override
	public void draw(ScreenTickEvent e) {
		
		// aplica os parâmetros da camera ao contexto GL - equivalente ao set das "Projection Matrix"?
		e.getCamera().getGameCamera().apply(Gdx.gl10);
		
		Gdx.graphics.getGL10().glPointSize(1.5f);	
		if (myMesh != null)
				myMesh.render(GL10.GL_POINTS, 0, (int)Math.pow(nr_points,3));
			
		//myMesh.render(GL10.GL_LINE_STRIP, 0, (int)Math.pow(nr_points,3)*4);
	}

	
	Matrix4 prj = new Matrix4();
	Matrix4 vw = new Matrix4();
	Matrix4 mdl = new Matrix4();
	@Override
	public void draw20(ScreenTickEvent e) {
		prj.set(e.getCamera().getGameCamera().projection);
		vw.set(e.getCamera().getGameCamera().view);
		mdl.idt();
		
		meshHelper.drawMesh(prj, vw, mdl, Color.GREEN, true);
	}
	
	@Override
	public void dispose() {
		meshHelper.dispose();
		super.dispose();
	}	
	
}
