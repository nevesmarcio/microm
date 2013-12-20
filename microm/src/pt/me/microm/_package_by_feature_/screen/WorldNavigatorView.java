package pt.me.microm._package_by_feature_.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.SnapshotArray;

import pt.me.microm.GameMicroM;
import pt.me.microm._package_by_feature_.screen.WorldNavigatorModel.EventType;
import pt.me.microm.controller.loop.ScreenTickManager;
import pt.me.microm.infrastructure.GAME_CONSTANTS;
import pt.me.microm.infrastructure.event.IEvent;
import pt.me.microm.infrastructure.event.listener.IEventListener;
import pt.me.microm.session.MyWorld;



public class WorldNavigatorView {
	private static final String TAG = WorldNavigatorView.class.getSimpleName();
	private static Logger logger = new Logger(TAG, GAME_CONSTANTS.LOG_LEVEL);	
	
	public Stage stage;
	
	private Table mainTable;
	private Table lTable;
	private Table rTable;
	
	private Widget wNav;
	private Label btnGo;

	public static interface ViewListener {
		void go(String aWorld);
		void nextWorld();
		void previousWorld();
		void levelClicked(String levelId);
	}
	
	private final ViewListener viewListener;
	private WorldNavigatorModel model;
	private void bindModel(WorldNavigatorModel model) {
		this.model = model;
		
		IEventListener el = new IEventListener() {
			@Override
			public void onEvent(IEvent event) {
				// TODO Auto-generated method stub
				
			}
		}; 
		
		this.model.addListener(EventType.EXAMPLE_MODEL_EVENT, el);
	}
	
	
	public WorldNavigatorView(ViewListener vl, WorldNavigatorModel mdl) {
		this.viewListener = vl;
		bindModel(mdl);
		
		TextureAtlas t = new TextureAtlas(Gdx.files.internal("data/scene2d/uiskin.atlas"), Gdx.files.internal("data/scene2d/"));
		Skin skin = new Skin(Gdx.files.internal("data/scene2d/uiskin.json"), t);		
		
		stage = new Stage();

		mainTable = new Table();
		lTable = new Table();
		rTable = new Table();
		if (GameMicroM.FLAG_DEV_ELEMENTS_B) {
			mainTable.debug();
			lTable.debug();
			rTable.debug();
		}
	
		mainTable.setFillParent(true);
		
		mainTable.row().expand();

		mainTable.add(lTable)/*.width(1280f/2f)*/.expand().fill();
		mainTable.add(rTable).width(1280f/2f);//.fill();

		stage.addActor(mainTable);

		lTable.align(Align.left);
		rTable.align(Align.center);
		
		// Add widgets to the table here
		

		
		Actor a;
//		table.add(a = new TextButton("SPECIAL-btn",skin));
//		
//		a.addListener(new EventListener() {
//			@Override
//			public boolean handle(Event event) {
////				Gdx.app.log(TAG, event.getClass().getSimpleName() + " >> " + event.toString());
//				if (event instanceof ChangeEvent)
//					 ScreenWorldSelect.this.g.setScreen(((GameMicroM)ScreenWorldSelect.this.g).getLevelSelect());
//				return false;
//			}
//		});
		
		// World Navigator
		wNav = new ItemSelector(new Object[] {"A","B","C","D","E"}, 0);//new Label("navigator here...", skin);
		wNav.setWidth(100f);
		wNav.setHeight(30f);

		stage.addActor(wNav);
		
		btnGo = new Label("GO",skin);
		stage.addActor(btnGo);

		
		// Left Table
		lTable.row().height(800f/4f).left().fill();
			Image title_img = new Image(GAME_CONSTANTS.devAtlas.createSprite("txr_daBox"));
			lTable.add(title_img);
		lTable.row().height(800f/4f).width(1280f/2f).left();
			Label desc_lbl = new Label("Lorem ipsum dolor sit amet,consectetuer adipiscing elit. Nam cursus. Morbi ut mi. Nullam enim leo, egestas id, condimentum at, laoreet mattis, massa. Sed eleifend nonummy diam. Praesent mauris ante, elementum et, bibendum at, posuere sit amet, nibh. Duis tincidunt lectus quis dui viverra vestibulum. Suspendisse vulputate aliquam dui. Nulla elementum dui ut augue. Aliquam vehicula mi at mauris. Maecenas placerat, nisl at consequat rhoncus, sem nunc gravida justo, quis eleifend arcu velit quis lacus. Morbi magna magna, tincidunt a, mattis non, imperdiet vitae, tellus. Sed odio est, auctor ac, sollicitudin in, consequat vitae, orci. Fusce id felis. Vivamus sollicitudin metus eget eros", skin);// 
			desc_lbl.setWrap(true);
			lTable.add(desc_lbl);
		lTable.row().expandY().width(1280f/2f).left().top();
			Table tagTbl = new Table();
			lTable.add(tagTbl);
			tagTbl.left().top();
			float totalW = 0.0f;
			
			tagTbl.row();
			
			Image tag_img1 = new Image(GAME_CONSTANTS.devAtlas.createSprite("txr_daBox"));
			//tag_img1.setWidth(10f);
			if (tag_img1.getWidth() + totalW >= 1280f/2f) {tagTbl.row();totalW=tag_img1.getWidth();}else{totalW+=tag_img1.getWidth();}
			tagTbl.add(tag_img1);

			Label lblA = new Label("1-lblAadfasdfasdfasdfasdf", skin);
			//lblA.setWidth(90f);
			if (lblA.getWidth() + totalW >= 1280f/2f) {tagTbl.row();totalW=lblA.getWidth();}else{totalW+=lblA.getWidth();}
			tagTbl.add(lblA);
			
			Image tag_img2 = new Image(GAME_CONSTANTS.devAtlas.createSprite("txr_daBox"));
			//tag_img2.setWidth(20f);
			if (tag_img2.getWidth() + totalW >= 1280f/2f) {tagTbl.row();totalW=tag_img2.getWidth();}else{totalW+=tag_img2.getWidth();}
			tagTbl.add(tag_img2);
			
			Label lblB = new Label("2-lblA", skin);
			//lblB.setWidth(90f);
			if (lblB.getWidth() + totalW >= 1280f/2f) {tagTbl.row();totalW=lblB.getWidth();}else{totalW+=lblB.getWidth();}
			tagTbl.add(lblB);
			
			Image tag_img3 = new Image(GAME_CONSTANTS.devAtlas.createSprite("txr_daBox"));
			//tag_img3.setWidth(100f);
			if (tag_img3.getWidth() + totalW >= 1280f/2f) {tagTbl.row();totalW=tag_img3.getWidth();}else{totalW+=tag_img3.getWidth();}
			tagTbl.add(tag_img3);

			Label lblC = new Label("3-lblasdfasdf asdf asdfA", skin);
			if (lblC.getWidth() + totalW >= 1280f/2f) {tagTbl.row();totalW=lblC.getWidth();}else{totalW+=lblC.getWidth();}
			tagTbl.add(lblC);			
			
			Image tag_img4 = new Image(GAME_CONSTANTS.devAtlas.createSprite("txr_daBox"));
			if (tag_img4.getWidth() + totalW >= 1280f/2f) {tagTbl.row();totalW=tag_img4.getWidth();}else{totalW+=tag_img4.getWidth();}
			tagTbl.add(tag_img4);
			
			Label lblD = new Label("4-l asdf asd fasd fablA", skin);
			if (lblD.getWidth() + totalW >= 1280f/2f) {tagTbl.row();totalW=lblD.getWidth();}else{totalW+=lblD.getWidth();}
			tagTbl.add(lblD);
			
			Image tag_img5 = new Image(GAME_CONSTANTS.devAtlas.createSprite("txr_daBox"));
			if (tag_img5.getWidth() + totalW >= 1280f/2f) {tagTbl.row();totalW=tag_img5.getWidth();}else{totalW+=tag_img5.getWidth();}
			tagTbl.add(tag_img5);
		
			Label lblE = new Label("5-la dfasdfasdfasdblA", skin);
			if (lblE.getWidth() + totalW >= 1280f/2f) {tagTbl.row();totalW=lblE.getWidth();}else{totalW+=lblE.getWidth();}
			tagTbl.add(lblE);
			
			
		// Right Table 
		for (final MyWorld aWorld : model.getPlayerProgress().getScreenFlowService().getAllWorlds()) {
			
			rTable.row();
			rTable.add(a = new TextButton(aWorld.getName(), skin));
			
			a.addListener(new EventListener() {
				@Override
				public boolean handle(Event event) {
					if (event instanceof ChangeEvent) {
						 logger.info("[**] Selected world: " + aWorld.getName());
						 viewListener.go(aWorld.getName());
					}
					return false;
				}				
			});			
		}
		
	}
	
	
	public void render(float delta) {
        	
        Table.drawDebug(stage); // This is optional, but enables debug lines for tables.
        
        stage.act(delta);
        stage.draw();
        
	}
	
	
	public void resize(int width, int height) { 
		
		stage.setViewport(1280, 800, true);
		
		wNav.setPosition(stage.getWidth()/2f-wNav.getWidth()/2f, 0.0f);
		btnGo.setPosition(stage.getWidth()-btnGo.getWidth(), 0.0f);
		
	}

	
	public void dispose() {
		// clean tables actor listeners
		SnapshotArray<Actor> items; 
		//ltable
		items = lTable.getChildren();
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
		lTable.clear();
		
		//rtable
		items = rTable.getChildren();
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
		rTable.clear();
		
		//maintable
		items = mainTable.getChildren();
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
		mainTable.clear();
		
		stage.clear();
		stage.dispose();	
		
	}
}
