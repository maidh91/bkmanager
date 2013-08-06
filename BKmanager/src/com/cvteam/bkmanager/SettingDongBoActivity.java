package com.cvteam.bkmanager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;


import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.NumberPicker;
import org.holoeverywhere.widget.Switch;
import org.holoeverywhere.widget.TextView;

import com.actionbarsherlock.view.Menu;

public class SettingDongBoActivity extends Activity {
	
	private RelativeLayout ckcn;
	private RelativeLayout nnlt;
	private Switch dblt;
	private TextView ckcnTV;
	private TextView nnltTV;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_dong_bo);
		View backMenu = getLayoutInflater().inflate(R.layout.back_menu);
		RelativeLayout backlayout = (RelativeLayout) backMenu.findViewById(R.id.backmenu);
		backlayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SettingDongBoActivity.this.finish();
			}
		});
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setCustomView(backMenu);
		
		dblt = (Switch) findViewById(R.id.SwitchDBLT);
		dblt.setChecked(Setting._dongBoLichThi);
		dblt.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
				Setting._dongBoLichThi =  isChecked;
			}
		});
		
		ckcn = (RelativeLayout) findViewById(R.id.RelativeLayoutCKCN);
		ckcn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(
						SettingDongBoActivity.this);
	  			LayoutInflater inflater = SettingDongBoActivity.this
	  					.getLayoutInflater();
	  			final View viewdialog = inflater.inflate(
	  					R.layout.ckcn_dialog, null);

	  			builder.setView(viewdialog);
	  			
	  			final android.app.AlertDialog dialog = builder.create();

	  			dialog.setTitle("Chọn chu kì cập nhật");
	  			Button ok = (Button)  viewdialog.findViewById(R.id.buttonOK);
	  			Button cancel = (Button)  viewdialog.findViewById(R.id.buttonCancel);
	  			cancel.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
	  			final NumberPicker nP = (NumberPicker) viewdialog.findViewById(R.id.numberPicker1);
	  			nP.setMaxValue(7);
	  			nP.setMinValue(1);
	  			nP.setValue(Setting._chuKiCapNhat);
	  			ok.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Setting._chuKiCapNhat = nP.getValue();
						ckcnTV.setText(Setting._chuKiCapNhat+" ngày");
						dialog.dismiss();
					}
				});
	  			dialog.show();
			}
		});
		nnlt = (RelativeLayout) findViewById(R.id.RelativeLayoutNNLT);
		ckcnTV = (TextView) findViewById(R.id.TextViewCKCNValue);
		ckcnTV.setText(Setting._chuKiCapNhat+" ngày");
		nnltTV = (TextView) findViewById(R.id.TextViewNNLTValue);
		switch (Setting._nhacNhoLichThi) {
		case 0:
			nnltTV.setText("Trước một ngày");
			break;
		case 1:
			nnltTV.setText("Đầu ngày");
			break;
		case 2:
			nnltTV.setText("Trước thi 1 tiếng");
			break;

		default:
			break;
		}
		nnlt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(
						SettingDongBoActivity.this);
	  			LayoutInflater inflater = SettingDongBoActivity.this
	  					.getLayoutInflater();
	  			final View viewdialog = inflater.inflate(
	  					R.layout.nnlt_dialog, null);

	  			builder.setView(viewdialog);
	  			
	  			final android.app.AlertDialog dialog = builder.create();

	  			dialog.setTitle("Chọn nhắc nhở lịch thi");
	  			Button ok = (Button)  viewdialog.findViewById(R.id.buttonOK);
	  			Button cancel = (Button)  viewdialog.findViewById(R.id.buttonCancel);
	  			cancel.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
	  			final NumberPicker nP = (NumberPicker) viewdialog.findViewById(R.id.numberPicker1);
	  			nP.setMaxValue(2);
	  			nP.setMinValue(0);
	  			nP.setValue(Setting._nhacNhoLichThi);
	  			nP.setDisplayedValues( new String[] { "Trước một ngày", "Đầu ngày", "Trước thi 1 tiếng" }); 
	  			ok.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Setting._nhacNhoLichThi = nP.getValue();
						switch (Setting._nhacNhoLichThi) {
						case 0:
							nnltTV.setText("Trước một ngày");
							break;
						case 1:
							nnltTV.setText("Đầu ngày");
							break;
						case 2:
							nnltTV.setText("Trước thi 1 tiếng");
							break;

						default:
							break;
						}
						dialog.dismiss();
					}
	  			});
	  			dialog.show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		return true;
	}

}
