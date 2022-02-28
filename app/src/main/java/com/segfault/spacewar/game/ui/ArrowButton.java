package com.segfault.spacewar.game.ui;
import com.segfault.spacewar.game.objects.GameObject;
import android.graphics.Canvas;
import com.segfault.spacewar.game.GameView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.TypedValue;

public class ArrowButton implements GameObject
{
	protected GameView game;
	public int x, y, width, height;
	protected Bitmap bitmap;
	protected Paint p = new Paint();
	public Rect hitBox = new Rect();
	
	public ArrowButton(GameView game, int imageRes, int awidth, int aheight)
	{
		this.game = game;
		bitmap = BitmapFactory.decodeResource(GameView.ctx.getResources(), imageRes);
		bitmap = bitmap.createScaledBitmap(bitmap, dpToPx(awidth), dpToPx(aheight), false);
		
		width = bitmap.getWidth();
		height = bitmap.getHeight();
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		canvas.drawBitmap(bitmap, x, y, p);
	}

	@Override
	public void update()
	{
		hitBox.left = x;
		hitBox.right = x + width;
		hitBox.top = y;
		hitBox.bottom = y + height;
	}
	
	public void onTouch(int x, int y, int action)
	{
	}
	
	private int dpToPx(int dp)
	{
		return (int) TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_DIP,
			dp,
			GameView.ctx.getResources().getDisplayMetrics()
		);
	}
}
