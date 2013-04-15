package pt.me.microm.controller.loop.itf;

import pt.me.microm.controller.loop.event.GameTickEvent;


public interface IGameTick {
    public void handleGameTick(GameTickEvent e);
}
