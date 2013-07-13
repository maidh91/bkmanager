package com.cvteam.bkmanager;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.app.ProgressDialog;

import android.app.Activity;
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

	public static ProgressDialog pd;

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
				String mssv = ((EditText) findViewById(R.id.editTextMSSV))
						.getText().toString();
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
				// openProgressDialog("Đang kiểm tra...");
				okButton.setText("Đang kiểm tra thông tin sinh viên...");
				okButton.setEnabled(false);
				List<String> checkvalid = new ArrayList<String>();
				List<DI__ThoiKhoaBieu> tkbs = AAOService.getTKB(mssv, "",
						checkvalid);
				if (tkbs.size() == 0
						&& checkvalid.get(0).equals(
								"Mã số sinh viên không tồn tại.")) {
					((EditText) findViewById(R.id.editTextMSSV))
							.setError("Bạn nhập mã số sinh viên không tồn tại.");
					okButton.setText("Nhập");
					okButton.setEnabled(true);
					// closeProgressDialog();
					return;
				}
				// closeProgressDialog();
				okButton.setText("Nhập");
				okButton.setEnabled(true);
				if (checkvalid.get(0).contains(
						"Vui lòng kiểm tra kết nối Internet và thử lại.")) {
					((EditText) findViewById(R.id.editTextMSSV))
							.setError("Vui lòng kiểm tra kết nối Internet và thử lại.");
					return;
				}

				Intent returnIntent = new Intent();
				setResult(RESULT_OK, returnIntent);
				finish();
			}

		});
		((EditText) findViewById(R.id.editTextMSSV))
				.setOnEditorActionListener(new OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (event != null
								&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
							InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

							// NOTE: In the author's example, he uses an
							// identifier
							// called searchBar. If setting this code on your
							// EditText
							// then use v.getWindowToken() as a reference to
							// your
							// EditText is passed into this callback as a
							// TextView

							in.hideSoftInputFromWindow(
									((EditText) findViewById(R.id.editTextMSSV))
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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public void openProgressDialog(String message) {
		if (pd != null) {
			if (pd.isShowing())
				return;
		}
		pd = new ProgressDialog(this);
		pd.setTitle("BKmanager: Thông báo");
		pd.setMessage(message);
		pd.setCancelable(false);
		pd.setCanceledOnTouchOutside(false);
		pd.show();

	}

	public void closeProgressDialog() {
		if (pd != null)
			if (pd.isShowing())
				pd.dismiss();
	}

}
