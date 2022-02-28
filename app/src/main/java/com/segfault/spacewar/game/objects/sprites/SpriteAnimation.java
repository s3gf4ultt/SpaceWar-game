package com.segfault.spacewar.game.objects.sprites;
import com.segfault.spacewar.game.objects.GameObject;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Paint;

public class SpriteAnimation implements GameObject
{	
	private Sprite sprite;
	private long timer = 500, counter = -1;
	private Bitmap currFrame;
	private int x, y;
	private Paint p = new Paint();

	public SpriteAnimation(int spriteRes, int width, int height)
	{
		sprite = new Sprite(width, height, spriteRes);
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		if (counter == -1)
			counter = System.currentTimeMillis();
			
		if (System.currentTimeMillis() - counter >= timer)
		{
			sprite.next();
			currFrame = sprite.getFrame();
			counter = -1;
		}
		
		canvas.drawBitmap(currFrame, x, y, p);
	}

	@Override
	public void update()
	{
	}
	
	public void setCoords(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void setTimer(long timer)
	{
		this.timer = timer;
	}
	
	public void configureSprite(int startX, int startY, int marginX, int marginY, int framesPerColumn, int framesNumber)
	{
		sprite.setStartingPoint(startX, startY);
		sprite.marginX = marginX;
		sprite.marginY = marginY;
		sprite.setFramesPerColumn(framesPerColumn);
		sprite.setFramesNumber(framesNumber);
		
		currFrame = sprite.getFrame();
	}
}
