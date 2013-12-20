package pt.me.navigator.infrastructure.interfaces;

import pt.me.navigator.infrastructure.events.GameTickEvent;


public interface GameTickInterface {
    public void handleGameTick(GameTickEvent e);
}
