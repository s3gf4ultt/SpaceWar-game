package com.segfault.spacewar.game.ui;
import com.segfault.spacewar.game.GameView;
import com.segfault.spacewar.R;
import android.view.MotionEvent;
import com.segfault.spacewar.game.event.KeyPress;

public class RightArrow extends ArrowButton
{
	private boolean playerPressing;

	public RightArrow(GameView g)
	{
		super(g, R.drawable.arrow_right, 74, 80);
	}

	@Override
	public void update()
	{
		super.update();
		
		if (playerPressing)
			game.onKeyPress(KeyPress.RIGHT_ARROW);
	}
	
	@Override
	public void onTouch(int x, int y, int action)
	{
		if (hitBox.intersect(x, y, x, y) && action == MotionEvent.ACTION_DOWN)
			playerPressing = true;
		if (action == MotionEvent.ACTION_UP)
			playerPressing = false;
	}
}
