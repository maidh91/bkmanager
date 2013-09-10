package com.cvteam.bkmanager;

import java.util.List;

import org.holoeverywhere.app.Activity;
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
				HocPhiController hC = new HocPhiController(TienIchActivity.this);
				HocPhiModel model = new HocPhiModel();
				hC.setModel(model);
				hC.getByHocKy(0, 0);

			}
		});
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
		
		AlertDialog.Builder builder = new AlertDialog.Builder(
				TienIchActivity.this);
		LayoutInflater inflater = TienIchActivity.this.getLayoutInflater();
		final View viewdialog = inflater.inflate(R.layout.hoc_phi_dialog, null);

		builder.setView(viewdialog);

		final android.app.AlertDialog dialog = builder.create();

		dialog.setTitle("Tuition fee");
		

		TextView txtHK = (TextView) findViewById(R.id.txt_HK);
		TextView txtUpdate = (TextView) findViewById(R.id.txt_update);
		TextView txtOwed = (TextView) findViewById(R.id.txt_owed);
		TextView txtTotal = (TextView) findViewById(R.id.txt_total);
		
		txtHK.setText(hh.mssv + "_" + hh.namhoc + hh.hocky);
		txtUpdate.setText(hh.updateDay);
		txtOwed.setText(hh.owedFee);
		txtTotal.setText(hh.totalFee);
	}

}
