package com.cvteam.bkmanager;

import org.holoeverywhere.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.actionbarsherlock.view.Menu;
import com.cvteam.bkmanager.R.layout;

public class SettingActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layout.activity_setting);
		View backMenu = getLayoutInflater().inflate(R.layout.back_menu);
		RelativeLayout backlayout = (RelativeLayout) backMenu.findViewById(R.id.backmenu);
		backlayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SettingActivity.this.finish();
			}
		});
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setCustomView(backMenu);
		
		RelativeLayout thongbao = (RelativeLayout) findViewById(R.id.RelativeLayoutThongBao);
		thongbao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(SettingActivity.this, SettingThongBaoActivity.class)); 
			}
		});
		RelativeLayout dongbo = (RelativeLayout) findViewById(R.id.RelativeLayoutDongBo);
		dongbo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(SettingActivity.this, SettingDongBoActivity.class));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.setting, menu);
		return true;
	}

}
