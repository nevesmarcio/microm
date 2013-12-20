package pt.me.microm._package_by_feature_.screen;

import pt.me.microm.infrastructure.event.dispatcher.EventDispatcher;
import pt.me.microm.session.PlayerProgress;

public class WorldNavigatorModel extends EventDispatcher {
	public static enum EventType {
		EXAMPLE_MODEL_EVENT
	};
	
	private PlayerProgress playerProgress;
	
	
	public PlayerProgress getPlayerProgress() {
		return playerProgress;
	}

	public WorldNavigatorModel(PlayerProgress playerProgress) {
		this.playerProgress = playerProgress;
	}
	
	
	
	
}
