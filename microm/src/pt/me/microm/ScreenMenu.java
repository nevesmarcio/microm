package pt.me.microm;

import java.util.UUID;

import pt.me.microm.controller.loop.GameTickGenerator;
import pt.me.microm.controller.loop.ScreenTickManager;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.ICommand;
import pt.me.microm.model.base.CameraControllerDrag;
import pt.me.microm.model.base.CameraControllerStrafe;
import pt.me.microm.model.base.CameraModel;
import pt.me.microm.model.dev.GridModel;
import pt.me.microm.model.ui.UIMetricsModel;
import pt.me.microm.model.ui.UIModel;
import pt.me.microm.session.PlayerProgress;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.SnapshotArray;
import com.esotericsoftware.tablelayout.Cell;

public class ScreenMenu implements Screen {
	
	private static final String TAG = ScreenMenu.class.getSimpleName();
	private static Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);
	
	private Stage stage;
	private Table menuTable;
	private Table titleTable;
	
	private PlayerProgress playerProgress;
	private ICommand callback;
	
	private Image imgNorth = new Image(GAME_CONSTANTS.devAtlas.createSprite("txr_daBox"));
	private Image imgSouth = new Image(GAME_CONSTANTS.devAtlas.createSprite("txr_daBox"));
	private Image imgWest = new Image(GAME_CONSTANTS.devAtlas.createSprite("txr_daBox")); 
	private Image imgEast = new Image(GAME_CONSTANTS.devAtlas.createSprite("txr_daBox")); 
	private Image imgTitle = new Image(GAME_CONSTANTS.devAtlas.createSprite("txr_wall"));
	
	private CameraModel cameraModel; 
	private GridModel gridModel;
	private UIMetricsModel uiMetricsModel;
	
	private CameraControllerDrag camcontrollerDrag;
	private CameraControllerStrafe camcontrollerStrafe;
	
	private Decal background;
	private Texture bg;
	private DecalBatch decalBatch;
	
	private UUID devID;
	private ScreenMenu(PlayerProgress playerProgress, ICommand callback) {
		logger.info("ALLOC:" + (devID = UUID.randomUUID()).toString());
		
		this.playerProgress = playerProgress;
		this.callback = callback;
		
		cameraModel = new CameraModel();
//		cameraModel.setWindowSize(Gdx.graphics.getWidth()/50, Gdx.graphics.getHeight()/50);
//		cameraModel.cameraXposition += cameraModel.camWidth/2;
//		cameraModel.cameraYposition += cameraModel.camHeight/2;
		
		gridModel = new GridModel();
		uiMetricsModel = new UIMetricsModel();

		GameTickGenerator.getInstance(); //responsável pela actualizacao dos modelos
		ScreenTickManager.getInstance(); //responsável pela actualizacao das views


		TextureAtlas t = new TextureAtlas(Gdx.files.internal("data/scene2d/uiskin.atlas"), Gdx.files.internal("data/scene2d/"));
		Skin skin = new Skin(Gdx.files.internal("data/scene2d/uiskin.json"), t);		
		
		stage = new Stage();
		
//		InputMultiplexer im = (InputMultiplexer) Gdx.input.getInputProcessor();
//		if (im == null) im = new InputMultiplexer();
//		im.addProcessor(stage);
//		Gdx.input.setInputProcessor(im);
		
		menuTable = new Table();
		menuTable.debugWidget();//table.debug();//
		
		menuTable.setFillParent(true);
		menuTable.bottom().right().padBottom(50.0f);//.padRight(120.0f).padBottom(50.0f);
		menuTable.setWidth(180f);

		stage.addActor(imgNorth);
		stage.addActor(imgSouth);
		stage.addActor(imgWest);
		stage.addActor(imgEast);
		
		
		titleTable = new Table();
		titleTable.setFillParent(true);
		titleTable.debug();
		titleTable.bottom().right().padBottom(150.0f).size(200f, 80f);
		//imgTitle.setSize(550f, 80f);
		titleTable.row().height(80f).width(200f);
		Stack stkTitle = new Stack();
		stkTitle.add(imgTitle);
		Label lblTitle = new Label("seeds ", skin);
		lblTitle.setAlignment(Align.bottom | Align.right);
		stkTitle.add(lblTitle);
		
		titleTable.add(stkTitle).align(Align.right);
		
		stage.addActor(titleTable);

		
		// Add widgets to the table here
		
		final Actor b;
		stage.addActor(b = new Label("#: "+playerProgress.getScreenFlowService().getWorldByName("world.1.justforkicks").getCurrentDeathCount(), skin));
		b.addAction(new Action() {
			@Override
			public boolean act(float delta) {
				String text = "#: "+ScreenMenu.this.playerProgress.getScreenFlowService().getWorldByName("world.1.justforkicks").getCurrentDeathCount();
				text+="\nstageW: " + stage.getWidth() + " stageH: " + stage.getHeight();
				text+="\nstageGW: " + stage.getGutterWidth()*2 + " stageGH: " + stage.getGutterHeight()*2;
				
				((Label)b).setText(text);
				return false;
			}
		});
		b.setPosition(0f, 100f);
		
		stage.addActor(menuTable);		
		
		
		//types of listeners
		//ActorGestureListener, ChangeListener, ClickListener, DragListener, DragScrollListener, FocusListener, InputListener
		menuTable.row().height(16.0f).padTop(5.0f);
			final Actor a_1;
			final Stack stk_1 = new Stack();
			final Label lbl_1;
			final Image img_1;
			final Cell<?> c_1;
			lbl_1 = new Label("PLAY ", skin);
			img_1 = new Image(GAME_CONSTANTS.devAtlas.createSprite("txr_daBox"));
			lbl_1.setAlignment(Align.right);
			a_1 = lbl_1;
			stk_1.add(img_1);
			stk_1.add(a_1);
			c_1 = menuTable.add(stk_1).width(120f).align(Align.right);
			a_1.addListener(new EventListener() {
				@Override
				public boolean handle(Event event) {
					CellWidthToAction aux = null;
					if (event instanceof InputEvent) {

						switch (((InputEvent)event).getType()) {
						case enter:
						case touchDown:
							aux = new CellWidthToAction();
							aux.setCell(c_1);
							aux.setWidget(lbl_1);
							aux.setWidth(160f);
							aux.setDuration(0.5f);
							aux.setInterpolation(Interpolation.bounceOut);
							
							a_1.addAction(aux);
							break;
						case exit:
							for (Action a: a_1.getActions()) {
								a_1.removeAction(a);
							}
							
							c_1.width(120f);
							lbl_1.invalidateHierarchy();
							
							break;
							
						case touchUp:
							ScreenMenu.this.playerProgress.getScreenFlowService().getSessionState().gameMode = 0;
							ScreenMenu.this.callback.handler(null);							
							break;
						default:
							break;
						}
						
					}
					
					return true;
				}
			});		
			
			
			
		menuTable.row().height(16.0f).padTop(5.0f);
			final Actor a_2;
			final Stack stk_2 = new Stack();
			final Label lbl_2;
			final Image img_2;
			final Cell<?> c_2;
			lbl_2 = new Label("SUPPORT US ",skin);
			img_2 = new Image(GAME_CONSTANTS.devAtlas.createSprite("txr_daBox"));
			lbl_2.setAlignment(Align.right);
			a_2 = lbl_2;
			stk_2.add(img_2);
			stk_2.add(a_2);
			c_2 = menuTable.add(stk_2).width(120f).align(Align.right);
			a_2.addListener(new EventListener() {
				@Override
				public boolean handle(Event event) {
					CellWidthToAction aux = null;
					if (event instanceof InputEvent) {
						switch (((InputEvent)event).getType()) {
						case enter:
						case touchDown:
							aux = new CellWidthToAction();
							aux.setCell(c_2);
							aux.setWidget(lbl_2);
							aux.setWidth(160f);
							aux.setDuration(0.5f);
							aux.setInterpolation(Interpolation.bounceOut);
							
							a_2.addAction(aux);
							
							break;
						case exit:
							for (Action a: a_2.getActions()) {
								a_2.removeAction(a);
							}
							
							c_2.width(120f);
							lbl_2.invalidateHierarchy();
							
							break;
						default:
							break;
						}
						
					}
					
					if (event instanceof ChangeEvent) {
						
					}
					return false;
				}
			});
		
		menuTable.row().height(16.0f).padTop(5.0f);
			final Actor a_3;
			final Stack stk_3 = new Stack();
			final Label lbl_3;
			final Image img_3;
			final Cell<?> c_3;
			lbl_3 = new Label("SHARE ",skin);
			img_3 = new Image(GAME_CONSTANTS.devAtlas.createSprite("txr_daBox"));
			lbl_3.setAlignment(Align.right);
			a_3 = lbl_3;
			stk_3.add(img_3);
			stk_3.add(a_3);
			c_3 = menuTable.add(stk_3).width(120f).align(Align.right);
			a_3.addListener(new EventListener() {
				@Override
				public boolean handle(Event event) {
					CellWidthToAction aux = null;
					if (event instanceof InputEvent) {
						switch (((InputEvent)event).getType()) {
						case enter:
						case touchDown:
							aux = new CellWidthToAction();
							aux.setCell(c_3);
							aux.setWidget(lbl_3);
							aux.setWidth(160f);
							aux.setDuration(0.5f);
							aux.setInterpolation(Interpolation.bounceOut);
							
							a_3.addAction(aux);
							
							break;
						case exit:
							for (Action a: a_3.getActions()) {
								a_3.removeAction(a);
							}
							
							c_3.width(120f);
							lbl_3.invalidateHierarchy();
							
							break;
						default:
							break;
						}
						
					}
					
					if (event instanceof ChangeEvent) {
						
					}
					return false;
				}
			});
		
			
		menuTable.row().height(16.0f).padTop(5.0f);
			final Actor a_4;
			final Stack stk_4 = new Stack();
			final Label lbl_4;
			final Image img_4;
			final Cell<?> c_4;
			lbl_4 = new Label("EXIT ",skin);
			img_4 = new Image(GAME_CONSTANTS.devAtlas.createSprite("txr_daBox"));
			lbl_4.setAlignment(Align.right);
			a_4 = lbl_4;
			stk_4.add(img_4);
			stk_4.add(a_4);
			c_4 = menuTable.add(stk_4).width(120f).align(Align.right);
			a_4.addListener(new EventListener() {
				@Override
				public boolean handle(Event event) {
					CellWidthToAction aux = null;
					if (event instanceof InputEvent) {
						switch (((InputEvent)event).getType()) {
						case enter:
						case touchDown:
							aux = new CellWidthToAction();
							aux.setCell(c_4);
							aux.setWidget(lbl_4);
							aux.setWidth(160f);
							aux.setDuration(0.5f);
							aux.setInterpolation(Interpolation.bounceOut);
							
							a_4.addAction(aux);
							
							break;
						case exit:
							for (Action a: a_4.getActions()) {
								a_4.removeAction(a);
							}
							
							c_4.width(120f);
							lbl_4.invalidateHierarchy();
							
							break;
						default:
							break;
						}
						
					}
					
					if (event instanceof ChangeEvent) {
						
					}
					return false;
				}
			});		

			camcontrollerDrag = new CameraControllerDrag(cameraModel);
			camcontrollerStrafe = new CameraControllerStrafe(cameraModel);
			
//			bg = new Texture(Gdx.files.internal("data/textures/dev/input/bg.png"));
			bg = new Texture(Gdx.files.internal("data/textures/menuEgg.png"));
			
			bg.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
			//bg.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
			//background = Decal.newDecal(GAME_CONSTANTS.devAtlas.createSprite("bg"), true);
//			background = Decal.newDecal(800f, 800f, new TextureRegion(bg), true);
//			background = Decal.newDecal(bg.getWidth()/2, bg.getHeight()/2, new TextureRegion(bg), true);
			background = Decal.newDecal(new TextureRegion(bg), true);
			background.setPosition(0f, 0f, 0f);
			
			decalBatch = new DecalBatch(new CameraGroupStrategy(cameraModel.getGameCamera()));
	}

	public static Screen showMenu(PlayerProgress playerProgress, ICommand callback) {
		logger.info("showMenu start!");
		return new ScreenMenu(playerProgress, callback);
	}
	
	
	private String clear_color = "ffffffff";//"0606020F";
	@Override
	public void render(float delta) {
		long elapsedNanoTime = (long)(Gdx.graphics.getDeltaTime()*GAME_CONSTANTS.ONE_SECOND_TO_NANO);
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		
		if (Gdx.input.isKeyPressed(Keys.BACKSPACE) || Gdx.input.isKeyPressed(Keys.BACK)) // use your own criterion here
			callback.handler("back");
		
		
//      background.lookAt(cameraModel.getGameCamera().position, cameraModel.getGameCamera().up);
      decalBatch.add(background);
      decalBatch.flush();		
    	
    	
    	if (GameMicroM.FLAG_DEV_ELEMENTS_B)
			Table.drawDebug(stage); // This is optional, but enables debug lines for tables.
        
        stage.act(delta);
        stage.draw();

    	if (Gdx.graphics.isGL20Available()) {
    		
//   		 Clean do gl context
//			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			Gdx.gl.glClearColor(Color.valueOf(clear_color).r, Color.valueOf(clear_color).g, Color.valueOf(clear_color).b, Color.valueOf(clear_color).a);
   		
			ScreenTickManager.getInstance().fireEvent(true, cameraModel, elapsedNanoTime);
			
   	} else {
			// Clean do gl context
//			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			Gdx.gl.glClearColor(Color.valueOf(clear_color).r, Color.valueOf(clear_color).g, Color.valueOf(clear_color).b, Color.valueOf(clear_color).a);
			
   		ScreenTickManager.getInstance().fireEvent(false, cameraModel, elapsedNanoTime);    		
   	}
        
        

	}

	@Override
	public void resize(int width, int height) {
		cameraModel.resize(width, height);
		
		//stage.setViewport(width, height, true);
		stage.setViewport(1280/2, 800/2, true);// fixando o viewport permite que se fique com um sistema de coordenadas independente da resolução
		
		imgNorth.setSize(stage.getWidth(), 10f);
		imgNorth.setPosition(0f, stage.getHeight()-imgNorth.getHeight());
		imgNorth.invalidate();
		
		imgSouth.setSize(stage.getWidth(), 10f);
		imgSouth.setPosition(0f, 0f);
		imgSouth.invalidate();
		
		imgWest.setSize(10f, stage.getHeight());
		imgWest.setPosition(0f, 0f);
		imgWest.invalidate();
		
		imgEast.setSize(10f, stage.getHeight());
		imgEast.setPosition(stage.getWidth()-imgEast.getWidth(), 0f);
		imgEast.invalidate();
	}

	@Override
	public void show() {
		if (logger.getLevel() == Logger.DEBUG) logger.debug("-->show()");
		
		InputMultiplexer im = new InputMultiplexer();
		im.addProcessor(stage);

		im.addProcessor(camcontrollerDrag);
		im.addProcessor(camcontrollerStrafe);
		
		Gdx.input.setInputProcessor(im);		
	}

	@Override
	public void hide() {
		if (logger.getLevel() == Logger.DEBUG) logger.debug("-->hide()");
		this.dispose();
	}

	@Override
	public void pause() {
		if (logger.getLevel() == Logger.DEBUG) logger.debug("-->pause()");
		
	}

	@Override
	public void resume() {
		if (logger.getLevel() == Logger.DEBUG) logger.debug("-->resume()");
		
	}


	@Override
	public void dispose() {
		// clean actor listeners
		SnapshotArray<Actor> items = menuTable.getChildren();
		for (int i = 0; i<items.size; i++) {
			Array<EventListener> el = items.get(i).getListeners();
			for (int j = 0; j<el.size; j++){
				items.get(i).removeListener(el.get(j));
			}
			Array<EventListener> ecl = items.get(i).getCaptureListeners();
			for (int j = 0; j<ecl.size; j++){
				items.get(i).removeCaptureListener(ecl.get(j));
			}
		}
		menuTable.clear();
		titleTable.clear();
		stage.clear();
		stage.dispose();
	
		decalBatch.dispose();
	}
	
	@Override
	protected void finalize() throws Throwable {
		logger.info("GC'ed:"+devID);
		super.finalize();
	}	

}
