package com.cvteam.bkmanager;

import java.util.List;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.TextView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.cvteam.bkmanager.controller.HocPhiController;
import com.cvteam.bkmanager.model.*;
import com.cvteam.bkmanager.model.HocPhiModel.Listener;
import com.cvteam.bkmanager.service.DialogService;

public class TienIchActivity extends Activity implements Listener {

	private HocPhiController hpC = null;
	private HocPhiModel hpModel = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tien_ich);
		RelativeLayout hocphiRL = (RelativeLayout) findViewById(R.id.RelativeLayoutHocPhi);
		hocphiRL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				DialogService.openProgressDialog(TienIchActivity.this,
						"Loading...");
				hpC = new HocPhiController(TienIchActivity.this);
				hpModel = new HocPhiModel();
				hpModel.mssv = Setting._mssv;
				hpModel.addListener(TienIchActivity.this);
				hpC.open();
				hpC.setModel(hpModel);
				hpC.getByHocKy(0, 0);

			}
		});
	}

	@Override
	protected void onPause() {
		if (hpC != null)
			hpC.close();
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public void handleDiemModelChanged(HocPhiModel sender, List<String> objs) {
		DI__HocPhi hh = sender.getHocPhi();
		
		DialogService.closeProgressDialog();
		
		LayoutInflater inflater = TienIchActivity.this.getLayoutInflater();
		final View viewdialog = inflater.inflate(R.layout.hoc_phi_dialog, null);		

		TextView txtHK = (TextView) viewdialog.findViewById(R.id.txt_HK);
		TextView txtUpdate = (TextView) viewdialog.findViewById(R.id.txt_update);
		TextView txtOwed = (TextView) viewdialog.findViewById(R.id.txt_owed);
		TextView txtTotal = (TextView) viewdialog.findViewById(R.id.txt_total);
		Button btnOK = (Button) viewdialog.findViewById(R.id.buttonOK);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(
				TienIchActivity.this);

		builder.setView(viewdialog);

		final android.app.AlertDialog dialog = builder.create();

		dialog.setTitle("Thông tin học phí");
		
		txtHK.setText("Học kỳ " + hh.hocky + " năm học " + hh.namhoc + " - " + (hh.namhoc + 1));
		txtUpdate.setText("Cập nhật ngày: " + hh.updateDay);
		txtOwed.setText(hh.owedFee);
		txtTotal.setText(hh.totalFee);
		
		btnOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		System.out.println("owedFee = " + hh.owedFee);
		System.out.println("totalFee = " + hh.totalFee);
		
		dialog.show();
	}

}
