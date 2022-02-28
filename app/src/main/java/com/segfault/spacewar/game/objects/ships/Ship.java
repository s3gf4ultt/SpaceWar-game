package com.segfault.spacewar.game.objects.ships;
import com.segfault.spacewar.game.objects.GameObject;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.BitmapFactory;
import com.segfault.spacewar.game.GameView;
import com.segfault.spacewar.R;
import android.graphics.Paint;
import android.media.SoundPool;
import android.content.Context;
import android.media.AudioManager;
import android.content.res.AssetManager;
import android.content.res.AssetFileDescriptor;
import java.io.IOException;
import android.os.Build;

public class Ship implements GameObject
{
	protected Bitmap texture;
	protected int x, y, speed;
	protected Rect hitBox;
	protected Paint p;
	protected SoundPool soundPool;

	public int bump, win, destroyed, explosion, laserShoot, impact;

	public Ship(int textureID)
	{
		p = new Paint();
		hitBox = new Rect();
		texture = BitmapFactory.decodeResource(GameView.ctx.getResources(), textureID);

		if (GameView.SCREEN_WIDTH < 760)
			texture = texture.createScaledBitmap(texture, texture.getWidth() / 2, texture.getHeight() / 2, false);

		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		//soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		
		AssetManager assets = GameView.ctx.getAssets();
		try
		{
			AssetFileDescriptor fd = assets.openFd("bump.mp3");
			bump = soundPool.load(fd, 0);

			fd = assets.openFd("win.mp3");
			win = soundPool.load(fd, 0);

			fd = assets.openFd("destroyed.mp3");
			destroyed = soundPool.load(fd, 0);

			fd = assets.openFd("explosion.mp3");
			explosion = soundPool.load(fd, 0);

			fd = assets.openFd("laser_shoot.mp3");
			laserShoot = soundPool.load(fd, 0);

			fd = assets.openFd("impact.mp3");
			impact = soundPool.load(fd, 0);
		}
		catch (IOException e)
		{}
	}

	@Override
	public void draw(Canvas canvas)
	{
	}

	@Override
	public void update()
	{
	}

	public int getY()
	{
		return y;
	}
}
