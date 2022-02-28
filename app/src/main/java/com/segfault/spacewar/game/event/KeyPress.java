package com.segfault.spacewar.game.event;

public interface KeyPress
{
	public static final int LEFT_ARROW = 0, RIGHT_ARROW = 1, SHOOT = 3;

	public void onKeyPress(int keyCode);
}
