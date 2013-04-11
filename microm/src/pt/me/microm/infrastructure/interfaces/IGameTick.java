package pt.me.microm.infrastructure.interfaces;

import pt.me.microm.infrastructure.events.GameTickEvent;


public interface IGameTick {
    public void handleGameTick(GameTickEvent e);
}
