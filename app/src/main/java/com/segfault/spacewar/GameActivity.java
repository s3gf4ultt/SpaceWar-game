package com.segfault.spacewar;
import android.app.Activity;
import android.os.Bundle;
import com.segfault.spacewar.game.GameView;
import android.widget.ImageView;
import android.widget.FrameLayout;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.view.View;
import com.segfault.spacewar.game.event.KeyPress;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class GameActivity extends Activity implements OnTouchListener
{
	private GameView gameView;
	
	private ImageView left, right, shoot;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);

		gameView = new GameView(this);

		setContentView(R.layout.game);

		FrameLayout f = (FrameLayout) findViewById(R.id.gameFrameLayout);
		f.addView(gameView);
		
		//left = (ImageView) findViewById(R.id.left_arrow);
		//right = (ImageView) findViewById(R.id.right_arrow);
		shoot = (ImageView) findViewById(R.id.shoot);
		
		//left.setOnTouchListener(this);
		//right.setOnTouchListener(this);
		
		shoot.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View p1)
				{
					gameView.onKeyPress(KeyPress.SHOOT);
				}
		});
	}

	@Override
	public boolean onTouch(View p1, MotionEvent p2)
	{
		/*switch(p1.getId())
		{
			case R.id.left_arrow:
				gameView.onKeyPress(KeyPress.LEFT_ARROW);
				break;
			case R.id.right_arrow:
				gameView.onKeyPress(KeyPress.RIGHT_ARROW);
				break;
		}*/
		
		return true;
	}

	@Override
	protected void onResume()
	{
		gameView.resume();
		super.onResume();
	}

	@Override
	protected void onPause()
	{
		gameView.pause();
		super.onPause();
	}
}
