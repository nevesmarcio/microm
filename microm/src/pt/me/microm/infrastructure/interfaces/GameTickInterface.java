package pt.me.microm.infrastructure.interfaces;

import pt.me.microm.infrastructure.events.GameTickEvent;


public interface GameTickInterface {
    public void handleGameTick(GameTickEvent e);
}
