package com.segfault.spacewar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.content.Intent;
import android.view.LayoutInflater;
import android.app.AlertDialog;

public class MainActivity extends Activity 
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		Button play = (Button) findViewById(R.id.btPlay);
		Button credits = (Button) findViewById(R.id.btCredits);
		
		play.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View p1)
				{
					Intent i = new Intent(MainActivity.this, GameActivity.class);
					startActivity(i);
				}
			
		});
		
		credits.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View p1)
				{
					LayoutInflater inf = getLayoutInflater();
					View v = inf.inflate(R.layout.credits, null);
					
					new AlertDialog.Builder(MainActivity.this).setTitle("Credits").setView(v).show();
				}
		});
    }
}
