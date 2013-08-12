package com.cvteam.bkmanager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.cvteam.bkmanager.model.DI__ThoiKhoaBieu;
import com.cvteam.bkmanager.service.AAOService;

public class AccountSetupActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_setup);
		setTitle("Thiết lập tài khoản");
		final Button okButton = (Button) findViewById(R.id.buttonOK);

		okButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String name = ((EditText) findViewById(R.id.editTextName)).getText().toString();
				String mssv = ((EditText) findViewById(R.id.editTextMSSV)).getText().toString();
				if (name.equals("")) {
					((EditText) findViewById(R.id.editTextName))
							.setError("Bạn phải nhập tên người dùng.");
					return;
				}
				if (mssv.equals("")) {
					((EditText) findViewById(R.id.editTextMSSV))
							.setError("Bạn phải nhập mã số sinh viên.");
					return;
				}
				if (!mssv.matches("[A-Za-z1-9][0-9]{7}")) {
					((EditText) findViewById(R.id.editTextMSSV))
							.setError("Bạn nhập mã số sinh viên không hợp lệ.");
					return;
				}
				okButton.setText("Đang kiểm tra thông tin sinh viên...");
				okButton.setEnabled(false);
				List<String> checkvalid = new ArrayList<String>();
				List<DI__ThoiKhoaBieu> tkbs = AAOService.getTKB(mssv, "", checkvalid);
				if (tkbs.size() == 0 && checkvalid.get(0).equals("Mã số sinh viên không tồn tại.")) {
					((EditText) findViewById(R.id.editTextMSSV))
							.setError("Bạn nhập mã số sinh viên không tồn tại.");
					okButton.setText("Nhập");
					okButton.setEnabled(true);
					return;
				}
				okButton.setText("Nhập");
				okButton.setEnabled(true);
				if (checkvalid.get(0).contains("Vui lòng kiểm tra kết nối Internet và thử lại.")) {
					((EditText) findViewById(R.id.editTextMSSV))
							.setError("Vui lòng kiểm tra kết nối Internet và thử lại.");
					return;
				}

				Setting._mssv = mssv;
				Setting._name = name;
				Intent returnIntent = new Intent();
				setResult(RESULT_OK, returnIntent);
				finish();
			}

		});
		((EditText) findViewById(R.id.editTextMSSV))
				.setOnEditorActionListener(new OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
						if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
							InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

							in.hideSoftInputFromWindow(((EditText) findViewById(R.id.editTextMSSV))
									.getApplicationWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);
							// Must return true here to consume event
							return true;

						}
						return false;
					}
				});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.account_setup, menu);
		return true;
	}

	@Override
	protected void onStop() {
		InitializeService();
		super.onStop();
	}

	// /////////////////////////////////////////
	// Notification //
	// /////////////////////////////////////////

	private void InitializeService() {
		Boolean isRunning = false;
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if ("com.cvteam.bkmanager.service.NotiService".equals(service.service.getClassName())) {
				System.out.println("isRunning");
				isRunning = true;
				break;
			}
		}

		if (!isRunning) {
			Intent intent = new Intent(this, com.cvteam.bkmanager.service.NotiService.class);
			intent.putExtra("mssv", Setting._mssv);
			intent.putExtra("dongbo", Setting._dongBoLichThi);
			intent.putExtra("noti_lichthi", Setting._thongBaoLichThi);
			intent.putExtra("noti_tkb", Setting._thongBaoTKB);
			intent.putExtra("noti_diem", Setting._thongBaoDiem);
			intent.putExtra("intervalTime", Setting._chuKiCapNhat);

			System.out.println("start service " + Setting._mssv);
			startService(intent);
		}
	}

	// /////////////////////////////////////////
	// End Notification //
	// /////////////////////////////////////////
}
