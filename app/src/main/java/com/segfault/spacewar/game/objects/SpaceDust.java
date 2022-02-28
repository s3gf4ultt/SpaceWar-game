package com.segfault.spacewar.game.objects;
import android.graphics.Canvas;
import java.util.Random;
import com.segfault.spacewar.game.GameView;
import android.graphics.Paint;
import android.graphics.Color;

public class SpaceDust implements GameObject
{
	private int x, y, speed;
	private Paint p;
	private Random gen;
	
	public SpaceDust()
	{
		p = new Paint();
		p.setColor(Color.WHITE);
		
		gen = new Random();
		
		x = gen.nextInt(GameView.SCREEN_WIDTH);
		y = gen.nextInt(GameView.SCREEN_HEIGTH);
		speed = gen.nextInt(8) + 2; 
	}

	@Override
	public void draw(Canvas canvas)
	{
		canvas.drawPoint(x, y, p);
	}

	@Override
	public void update()
	{
		y += speed;
		if (y > GameView.SCREEN_HEIGTH)
		{
			y = -1;
			x = gen.nextInt(GameView.SCREEN_WIDTH);
		}
	}
	
}
