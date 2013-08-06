package com.cvteam.bkmanager;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.CheckBox;

import com.actionbarsherlock.view.Menu;

public class SettingThongBaoActivity extends Activity {
	
	private CheckBox cblichthi;
	private CheckBox cbdiem;
	private CheckBox cbhocphi;
	private CheckBox cbhocbong;
	private CheckBox cbsukien;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_thong_bao);
		View backMenu = getLayoutInflater().inflate(R.layout.back_menu);
		RelativeLayout backlayout = (RelativeLayout) backMenu.findViewById(R.id.backmenu);
		backlayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SettingThongBaoActivity.this.finish();
			}
		});
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setCustomView(backMenu);
		
		
		RelativeLayout lichthi = (RelativeLayout) findViewById(R.id.RelativeLayoutLichThi);
		RelativeLayout diem = (RelativeLayout) findViewById(R.id.RelativeLayoutDiem);
		RelativeLayout hocphi = (RelativeLayout) findViewById(R.id.RelativeLayoutHocPhi);
		RelativeLayout hocbong = (RelativeLayout) findViewById(R.id.RelativeLayoutHocBong);
		RelativeLayout sukien = (RelativeLayout) findViewById(R.id.RelativeLayoutSuKien);
		
		cblichthi = (CheckBox) findViewById(R.id.CheckBoxLichThi);
		cbdiem = (CheckBox) findViewById(R.id.CheckBoxDiem);
		cbhocphi = (CheckBox) findViewById(R.id.CheckBoxHocPhi);
		cbhocbong = (CheckBox) findViewById(R.id.CheckBoxHocBong);
		cbsukien = (CheckBox) findViewById(R.id.CheckBoxSuKien);
		
		cblichthi.setChecked(Setting._thongBaoLichThi);
		cbdiem.setChecked(Setting._thongBaoDiem);
		cbhocphi.setChecked(Setting._thongBaoHocPhi);
		cbhocbong.setChecked(Setting._thongBaoHocBong);
		cbsukien.setChecked(Setting._thongBaoSuKien);
		
		lichthi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				cblichthi.toggle();
				Setting._thongBaoLichThi = !Setting._thongBaoLichThi;
			}
		});
		diem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				cbdiem.toggle();
				Setting._thongBaoDiem = !Setting._thongBaoDiem;
			}
		});
		hocphi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				cbhocphi.toggle();
				Setting._thongBaoHocPhi = !Setting._thongBaoHocPhi;
			}
		});
		hocbong.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				cbhocbong.toggle();
				Setting._thongBaoHocBong = !Setting._thongBaoHocBong;
			}
		});
		sukien.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				cbsukien.toggle();
				Setting._thongBaoSuKien = !Setting._thongBaoSuKien;
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

}
