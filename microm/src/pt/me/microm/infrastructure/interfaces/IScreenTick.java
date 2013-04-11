package pt.me.microm.infrastructure.interfaces;

import pt.me.microm.infrastructure.events.ScreenTickEvent;


public interface IScreenTick {
	public void draw(ScreenTickEvent e);
}
