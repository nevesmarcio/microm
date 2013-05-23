package pt.me.microm._package_by_feature_.screen;

import pt.me.microm._package_by_feature_.screen.WorldNavigatorModel.EventType;
import pt.me.microm.infrastructure.event.IEvent;
import pt.me.microm.infrastructure.event.listener.IEventListener;



public class WorldNavigatorView {
	public static interface ViewListener {
		void go();
		void nextWorld();
		void previousWorld();
		void levelClicked(String levelId);
	}
	
	private ViewListener viewListener;
	public void setViewListener(ViewListener viewListener) {
		this.viewListener = viewListener;
	}
	
	private WorldNavigatorModel model;
	public void bindModel(WorldNavigatorModel model) {
		this.model = model;
		
		IEventListener el = new IEventListener() {
			@Override
			public void onEvent(IEvent event) {
				// TODO Auto-generated method stub
				
			}
		}; 
		
		this.model.addListener(EventType.EXAMPLE_MODEL_EVENT, el);
	}
	
	public WorldNavigatorView() {

	}
	
	
	
	
	
}
