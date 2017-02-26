package pt.me.microm.controller.loop.itf;

import pt.me.microm.controller.loop.event.ScreenTickEvent;


public interface IScreenTick {
	void draw(ScreenTickEvent e);
	void draw20(ScreenTickEvent e);
}
