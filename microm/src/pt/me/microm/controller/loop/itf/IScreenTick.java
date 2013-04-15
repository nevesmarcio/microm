package pt.me.microm.controller.loop.itf;

import pt.me.microm.controller.loop.event.ScreenTickEvent;


public interface IScreenTick {
	public void draw(ScreenTickEvent e);
}
