package com.segfault.spacewar.game.objects.ships;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import com.segfault.spacewar.game.GameView;
import com.segfault.spacewar.game.event.KeyPress;
import java.util.ArrayList;
import java.util.List;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.segfault.spacewar.R;

public class PlayerShip extends Ship implements KeyPress
{
	private int speedX = 3, shields = 3;
	
	private int shieldX, shieldY, distance = 10000; // 10 KM
	private List<LaserBullet> bullets = new ArrayList<>();
	private Bitmap shield;
	
	private boolean isLocked = false;

	public PlayerShip(int t)
	{
		super(t);
		speed = 3;
		y = GameView.SCREEN_HEIGTH - 140 * 2;
		x = GameView.SCREEN_WIDTH / 2;
		p.setTextSize(25);
		p.setColor(Color.WHITE);
		shieldX = GameView.SCREEN_WIDTH - (int) p.measureText("Shields: " + shields) - 10;
		shieldY = 35;

		shield = BitmapFactory.decodeResource(GameView.ctx.getResources(), R.drawable.shield);
	}

	@Override
	public void draw(Canvas canvas)
	{
		for (int i = 0; i < bullets.size(); i++)
			bullets.get(i).draw(canvas);
		
		canvas.drawBitmap(texture, x, y, p);
		//canvas.drawText("Shields: " + shields, shieldX, shieldY, p);
		
		for (int i = 1; i <= shields; i++)
			canvas.drawBitmap(shield, GameView.SCREEN_WIDTH - (shield.getWidth() * i) - i * 10, shieldY, p);
		
		canvas.drawText("Distance: " + (float) distance / 1000 + " KM", 0, shieldY, p);
	}

	@Override
	public void update()
	{
		if (distance <= 0)
		{
			if (speed > 0)
				x = (GameView.SCREEN_WIDTH / 2) - texture.getWidth() / 2;
			speed = 0;
		}
		if (isLocked) return;
		for (int i = 0; i < bullets.size(); i++)
		{
			LaserBullet l = bullets.get(i);
			l.update();
			
			if (l.getHitbox().bottom < 0)
				bullets.remove(l);
			if (l.getHitbox().top > GameView.SCREEN_HEIGTH)
				bullets.remove(l);
		}
		
		updateHitbox();
		distance -= speed;
		
		if (x > GameView.SCREEN_WIDTH - texture.getWidth())
			x = GameView.SCREEN_WIDTH - texture.getWidth();
		if (x < 0)
			x = 0;
	}
	
	public void shoot()
	{
		if (isLocked) return;
		LaserBullet lb = new LaserBullet(x + texture.getWidth() / 2, y, -1);
		bullets.add(lb);
		
		soundPool.play(laserShoot, 1, 1, 1, 0, 1);
	}
	
	public void bump()
	{
		shields--;
		soundPool.play(bump, 1, 1, 1, 0, 1);
	}
	
	public void setDistance(int dist)
	{
		this.distance = dist;
	}
	
	@Override
	public void onKeyPress(int keyCode)
	{
		if (isLocked) return;
		switch(keyCode)
		{
			case KeyPress.LEFT_ARROW:
				x-= speedX;
				break;
			case KeyPress.RIGHT_ARROW:
				x += speedX;
				break;
			case KeyPress.SHOOT:
				shoot();
				break;
		}
	}
	
	public void lockPlayer()
	{
		isLocked = true;
	}
	
	public void unlockPlayer()
	{
		isLocked = false;
	}
	
	public int getShields()
	{
		return shields;
	}
	
	public int getDistance()
	{
		return distance;
	}
	
	public List<LaserBullet> getLaserBullets()
	{
		return bullets;
	}
	
	public void setShields(int shields)
	{
		this.shields = shields;
	}
	
	public Rect getHitbox()
	{
		return hitBox;
	}
	
	private void updateHitbox()
	{
		hitBox.left = x;
		hitBox.right = x + texture.getWidth();
		hitBox.top = y;
		hitBox.bottom = y + texture.getHeight();
	}
}
