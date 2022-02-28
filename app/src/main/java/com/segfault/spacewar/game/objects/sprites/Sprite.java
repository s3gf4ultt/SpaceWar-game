package com.segfault.spacewar.game.objects.sprites;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.segfault.spacewar.game.GameView;
import android.util.Log;

public class Sprite
{
	private int frameWidth, frameHeight;
	private int startX, startY;
	private Bitmap sprite;
	private int currentFrame = 1;
	private int framesPerColumn, framesNumber;
	public int marginX, marginY;

	public Sprite(int frameWidth, int frameHeight, int spriteRes)
	{
		this.frameHeight = frameHeight;
		this.frameWidth = frameWidth;
		sprite = BitmapFactory.decodeResource(GameView.ctx.getResources(), spriteRes);
	}

	public Bitmap getFrame()
	{
		int y = startY;
		int x = startX; //+ (currentFrame > 1 ? (frameWidth * currentFrame + marginX) : 0);
		
		if (currentFrame > framesNumber)
			currentFrame = 1;

		int column = currentFrame / framesPerColumn;
		int rest = currentFrame % framesPerColumn;
		if (rest >= 1 || currentFrame < framesPerColumn) ++column;
		int frame = currentFrame > framesPerColumn ? currentFrame - (framesPerColumn * (column - 1)) : currentFrame;

		y = startY + frameHeight * (column - 1) + marginY * (column - 1);
		x = startX + (frame > 1 ? (frameWidth * frame + marginX * (column - 1)) : 0);
		
		if (x + frameWidth > sprite.getWidth())
			x -= (x + frameWidth) - sprite.getWidth();
		if (y + frameHeight > sprite.getHeight())
			y -= (y + frameHeight) - sprite.getHeight();
		
		//Log.e("Sprite", "x = " + x + ", y = " + y + "\ncurrentFrame = " + currentFrame + "\ncolumn = " + column);

		return sprite.createBitmap(sprite, x, y, frameWidth, frameHeight);
	}
	
	public void setFramesNumber(int n)
	{
		framesNumber = n;
	}

	public void next()
	{
		currentFrame++;
	}
	
	public void setStartingPoint(int x, int y)
	{
		startX = x;
		startY = y;
	}
	
	public void setFrame(int frame)
	{
		currentFrame = frame;
	}

	public void setFramesPerColumn(int n)
	{
		this.framesPerColumn = n;
	}
}
