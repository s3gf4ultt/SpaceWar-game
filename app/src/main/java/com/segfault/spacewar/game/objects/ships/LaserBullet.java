package com.segfault.spacewar.game.objects.ships;
import com.segfault.spacewar.game.objects.GameObject;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Rect;

public class LaserBullet implements GameObject
{
	private int x, y, speed;
	private Paint p;
	private Rect hitBox = new Rect();
	
	public LaserBullet(int x, int y, int direction)
	{
		this.x = x;
		this.y = y;
		this.speed = 12 * direction;
		this.p = new Paint();
		p.setColor(Color.RED);
	}

	@Override
	public void draw(Canvas canvas)
	{
		canvas.drawRect(hitBox, p);
	}

	@Override
	public void update()
	{
		hitBox.left = x;
		hitBox.right = x + 3;
		hitBox.top = y + 25;
		hitBox.bottom = y;
		y += speed;
	}
	
	public Rect getHitbox()
	{
		return hitBox;
	}
	
	public LaserBullet setColor(int color)
	{
		p.setColor(color);
		return this;
	}
}
