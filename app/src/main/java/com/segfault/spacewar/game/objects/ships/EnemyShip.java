package com.segfault.spacewar.game.objects.ships;
import android.graphics.Canvas;
import java.util.Random;
import com.segfault.spacewar.game.GameView;

public class EnemyShip extends Ship
{
	private PlayerShip player;
	private Random gen = new Random();
	
	public EnemyShip(int i, PlayerShip player)
	{
		super(i);
		this.player = player;
		speed = gen.nextInt(12) + 4;
		y = 0;
		x = gen.nextInt(GameView.SCREEN_WIDTH) - texture.getWidth();
	}

	@Override
	public void draw(Canvas canvas)
	{
		canvas.drawBitmap(texture, x, y, p);
	}

	@Override
	public void update()
	{
		y += speed;
		updateHitbox();
		
		if (player.getHitbox().intersect(hitBox))
		{
			player.bump();
			y = GameView.SCREEN_HEIGTH + 10;
		}
		
		if (y > GameView.SCREEN_HEIGTH)
		{
			y = 0;
			x = gen.nextInt(GameView.SCREEN_WIDTH) - texture.getWidth();
			speed = gen.nextInt(12) + 4;
		}
		
		for (int i = 0; i < player.getLaserBullets().size(); i++)
		{
			if (player.getLaserBullets().get(i).getHitbox().intersect(hitBox))
			{
				// the laser hites this enemy
				y = GameView.SCREEN_HEIGTH + 10;
				player.getLaserBullets().remove(player.getLaserBullets().get(i));
			}
		}
	}
	
	public void updateHitbox()
	{
		hitBox.left = x;
		hitBox.right = x + texture.getWidth();
		hitBox.top = y;
		hitBox.bottom = y + texture.getHeight();
	}
}
