package com.segfault.spacewar.game;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import com.segfault.spacewar.R;
import com.segfault.spacewar.game.event.KeyPress;
import com.segfault.spacewar.game.objects.GameObject;
import com.segfault.spacewar.game.objects.SpaceDust;
import com.segfault.spacewar.game.objects.ships.BossShip;
import com.segfault.spacewar.game.objects.ships.EnemyShip;
import com.segfault.spacewar.game.objects.ships.PlayerShip;
import java.util.ArrayList;
import java.util.List;
import com.segfault.spacewar.game.ui.LeftArrow;
import com.segfault.spacewar.game.ui.RightArrow;
import android.content.res.Resources;
import android.util.TypedValue;

public class GameView extends SurfaceView implements Runnable, KeyPress
{
	public static int SCREEN_WIDTH, SCREEN_HEIGTH;

	public static Activity ctx;
	private volatile boolean running = false;
	private SurfaceHolder holder;
	private Canvas canvas;
	private Thread t;

	private List<GameObject> objects = new ArrayList<>();
	private PlayerShip player;
	private BossShip boss = null;
	private boolean bossDefeated;

	private Paint p = new Paint();
	private int centerX, centerY;

	private LeftArrow left;
	private RightArrow right;

	public GameView(Activity ctx)
	{
		super(ctx);
		this.ctx = ctx;
		p.setColor(Color.WHITE);
		holder = getHolder();

		Point size = new Point();
		ctx.getWindowManager().getDefaultDisplay().getSize(size);
		SCREEN_WIDTH = size.x;
		SCREEN_HEIGTH = size.y;
		setLayoutParams(new LinearLayout.LayoutParams(SCREEN_WIDTH, SCREEN_HEIGTH));
		centerX = SCREEN_WIDTH / 2;
		centerY = SCREEN_HEIGTH / 2;

		startGame();
		left = new LeftArrow(this);
		right = new RightArrow(this);

		left.x = 0;
		left.y = SCREEN_HEIGTH - left.height;

		right.x = left.x + left.width + 10;
		right.y = SCREEN_HEIGTH - right.height;
	}

	public void draw()
	{
		if (holder.getSurface().isValid())
		{
			canvas = holder.lockCanvas();
			canvas.drawColor(Color.BLACK);

			if (player.getShields() <= 0)
			{
				//Game over
				p.setTextSize(60);
				canvas.drawText("GAME OVER", centerX - (int)p.measureText("GAME OVER") / 2, centerY / 2, p);
				p.setTextSize(25);
				canvas.drawText("Tap to replay!", centerX - (int)p.measureText("Tap to replay!") / 2, centerY / 2 + 40, p);

				player.setShields(0);

				holder.unlockCanvasAndPost(canvas);
				return;
			}

			if (bossDefeated)
			{
				// player won
				player.setDistance(1); // BUG, prevent spawn new boss when player starts game again
				p.setTextSize(60);
				canvas.drawText("YOU WON !", centerX - (int)p.measureText("YOU WON !") / 2, centerY / 2, p);
				p.setTextSize(25);
				canvas.drawText("Tap to replay!", centerX - (int)p.measureText("Tap to replay!") / 2, centerY / 2 + 40, p);

				holder.unlockCanvasAndPost(canvas);
				return;
			}

			for (int i = 0; i < objects.size(); i++)
				objects.get(i).draw(canvas);

			left.draw(canvas);
			right.draw(canvas);

			holder.unlockCanvasAndPost(canvas);
		}
	}

	public void update()
	{
		left.update();
		right.update();

		bossDefeated = boss == null ? false : boss.playerDefeated();
		if (player.getShields() <= 0 || bossDefeated) return;

		if (player.getDistance() <= 0 && boss == null && !bossDefeated)
		{
			// add boss
			boss = new BossShip(R.drawable.boss, player);
			objects.add(boss);

			for (int i = 0; i < objects.size(); i++)
			{
				if (objects.get(i) instanceof EnemyShip) objects.remove(i--);
			}
			player.getLaserBullets().clear();
		}

		for (int i = 0; i < objects.size(); i++)
			objects.get(i).update();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		left.onTouch((int)event.getX(), (int)event.getY(), event.getAction());
		right.onTouch((int)event.getX(), (int)event.getY(), event.getAction());

		switch (event.getAction())
		{
			case MotionEvent.ACTION_UP:
				if (player.getShields() <= 0 || bossDefeated) // game ended
					startGame();

				break;
		}

		return true;
	}

	@Override
	public void onKeyPress(int keyCode)
	{
		for (Object obj : objects)
			if (obj instanceof KeyPress)
				((KeyPress) obj).onKeyPress(keyCode);
	}

	public void control()
	{
		try
		{
			t.sleep(10);
		}
		catch (InterruptedException e)
		{}
	}

	public void startGameLoop()
	{
		t = new Thread(this);
		running = true;
		t.start();
	}

	public void startGame()
	{
		bossDefeated = false;
		objects.clear();
		boss = null;

		for (int i = 1; i <= 35; i++)
			objects.add(new SpaceDust());

		player = new PlayerShip(R.drawable.playership);
		objects.add(player);

		for (int i = 0; i < 3; i++)
			objects.add(new EnemyShip(R.drawable.enemy, player));
	}

	public void resume()
	{
		startGameLoop();
	}

	public void pause()
	{
		running = false;

		try
		{
			t.join();
		}
		catch (InterruptedException e)
		{
			Log.e("GameView", "Could not stop thread");
		}
	}

	@Override
	public void run()
	{
		while (running)
		{
			draw();
			update();
			control();
		}
	}
}
