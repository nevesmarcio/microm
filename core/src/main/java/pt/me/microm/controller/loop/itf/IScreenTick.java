package pt.me.microm.controller.loop.itf;

import pt.me.microm.controller.loop.event.ScreenTickEvent;


public interface IScreenTick {
	public void draw(ScreenTickEvent e);
	public void draw20(ScreenTickEvent e);
}
