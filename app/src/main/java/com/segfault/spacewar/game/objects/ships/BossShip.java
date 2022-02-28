package com.segfault.spacewar.game.objects.ships;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Color;
import com.segfault.spacewar.game.GameView;
import java.util.List;
import java.util.ArrayList;
import com.segfault.spacewar.game.objects.sprites.SpriteAnimation;
import com.segfault.spacewar.R;
import java.util.Random;

public class BossShip extends Ship
{
	private int health, centerX;
	private Rect uiHealth = new Rect();
	private PlayerShip player;
	private List<LaserBullet> bullets = new ArrayList<>();
	private int speedX = 3;
	private int a = 0;

	private int leftLaserX, leftLaserY, rightLaserX, rightLaserY;
	private long cooldown = 2000; // every 2 seconds the boss shoots
	private long counter = -1, explosionTimer = 100, soundExplosionTimer = -1, explMoveTimer = -1;
	
	private long deathTimer = 6500, dCounter = -1;
	private boolean dead;
	
	private SpriteAnimation explosionAnim;
	
	public BossShip(int r, PlayerShip player)
	{
		super(r);
		health = 15;
		speed = 1;
		p.setColor(Color.BLUE);
		centerX = GameView.SCREEN_WIDTH / 2;
		this.player = player;
		y = 0 - texture.getHeight();
		x = centerX - (texture.getWidth() / 2);
	}

	@Override
	public void draw(Canvas canvas)
	{
		for (int i = 1; i <= health; i++)
		{
			uiHealth.left = centerX - (health * 5) + 10 * i;
			uiHealth.right = uiHealth.left - 5;
			uiHealth.top = 10;
			uiHealth.bottom = 20;

			canvas.drawRect(uiHealth, p);
		}
		
		for (int i = 0; i < bullets.size(); i++)
			bullets.get(i).draw(canvas);
		canvas.drawBitmap(texture, x, y, p);
		
		if (explosionAnim != null)
			explosionAnim.draw(canvas);
	}

	@Override
	public void update()
	{
		y += speed;
		updateCoords();
		
		if (explosionAnim != null)
		{
			explosionAnim.update();
			
			if (explMoveTimer == -1)
				explMoveTimer = System.currentTimeMillis();
			if (System.currentTimeMillis() - explMoveTimer >= explosionTimer * 10)
			{
				Random r = new Random();
				int ex = r.nextInt(texture.getWidth()) + x;
				int ey = r.nextInt(texture.getHeight() / 2) + y;
				explosionAnim.setCoords(ex, ey);
				
				explMoveTimer = -1;
			}
			
			if (soundExplosionTimer == -1)
				soundExplosionTimer = System.currentTimeMillis();
			if (System.currentTimeMillis() - soundExplosionTimer >= explosionTimer * 2)
			{
				soundPool.play(explosion, 1, 1, 1, 0, 1);
				soundExplosionTimer = -1;
			}
		}
		
		if (health <= 0)
		{
			if (dCounter == -1)
				dCounter = System.currentTimeMillis();
			if (System.currentTimeMillis() - dCounter >= deathTimer)
				dead = true;
		}
		
		if (health <= 0 && a++ == 2)
		{
			speedX = 0;
			counter = 0;
			// add explosions to ship
			explosionAnim = new SpriteAnimation(R.drawable.explosion, 50, 60);
			explosionAnim.configureSprite(5, 0, 10, 22, 4, 16);
			explosionAnim.setTimer(explosionTimer);

			Random r = new Random();
			int ex = r.nextInt(texture.getWidth()) + x;
			int ey = r.nextInt(texture.getHeight() / 2) + y;
			explosionAnim.setCoords(ex, ey);
		}
		
		if (health == 10 && a == 0)
		{
			if (speedX < 0)
				speedX -= 2;
			else
				speedX += 2;
			++a;
			cooldown -= 500;
		}
		if (health == 6 && a == 1)
		{
			speedX *= 2 * a++;
			cooldown -= 250;
		}
		
		if (x >= GameView.SCREEN_WIDTH - texture.getWidth())
			speedX = -speedX;
		if (x <= 0)
			speedX = Math.abs(speedX);

		for (int i = 0; i < bullets.size(); i++)
			bullets.get(i).update();
		
		if (y <= 18)
			player.lockPlayer();
		else
		{
			player.unlockPlayer();
			speed = 0;
		}
		
		if (speed == 0) // start attacking player
		{
			x += speedX;

			if (counter == -1)
			{
				counter = System.currentTimeMillis();
			}

			if ((System.currentTimeMillis() - counter) >= cooldown)
			{
				shoot();
				counter = -1;
			}
			
			List<LaserBullet> playerBullets = player.getLaserBullets();
			for (int i = 0; i < playerBullets.size(); i++)
			{
				if (playerBullets.get(i).getHitbox().intersect(hitBox))
				{
					health--;
					playerBullets.remove(i--);
					soundPool.play(impact, 1, 1, 1, 0, 1);
				}
			}
			
			for (int i = 0; i < bullets.size(); i++)
			{
				if (player.getHitbox().intersect(bullets.get(i).getHitbox()))
				{
					player.bump();
					bullets.remove(i--);
				}
			}
		}
	}
	
	public boolean playerDefeated()
	{
		return health <= 0 && dead;
	}
	
	private void shoot()
	{
		if (health <= 0) return;

		bullets.add(new LaserBullet(leftLaserX, leftLaserY, 1).setColor(Color.BLUE));
		bullets.add(new LaserBullet(rightLaserX, rightLaserY, 1).setColor(Color.BLUE));
		
		soundPool.play(laserShoot, 1, 1, 1, 0, 1);
	}
	
	private void updateCoords()
	{
		leftLaserX = x + 45;
		rightLaserX = (x + texture.getWidth()) - 40;
		leftLaserY = (y + texture.getHeight()) - 5;
		rightLaserY = (y + texture.getHeight()) - 5;
		
		hitBox.left = x;
		hitBox.right = x + texture.getWidth();
		hitBox.top = y;
		hitBox.bottom = y + texture.getHeight();
	}
}
