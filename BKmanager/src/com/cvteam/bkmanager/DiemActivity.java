package com.cvteam.bkmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.widget.AdapterView;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.Spinner;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.cvteam.bkmanager.adapter.DiemAdapter;
import com.cvteam.bkmanager.controller.DiemController;
import com.cvteam.bkmanager.model.DI__Diem;
import com.cvteam.bkmanager.model.DI__NienHoc;
import com.cvteam.bkmanager.model.DiemModel;
import com.cvteam.bkmanager.service.Constant;
import com.cvteam.bkmanager.service.DialogService;
import com.cvteam.bkmanager.service.LogService;

public class DiemActivity extends Activity implements
SearchView.OnQueryTextListener, DiemModel.Listener, 
org.holoeverywhere.widget.AdapterView.OnItemSelectedListener {

	ListView lstDiem;
	private String currentSearch;
	private LogService logService = new LogService("DiemActivity");
	private DiemModel model;
	private DiemController controller;
	private Spinner spinHocKy;
	private static ArrayAdapter<String> dataAdapter;
	private List<Object> objs = new ArrayList<Object>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diem);
		
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setTitle(Setting._name);
		getSupportActionBar().setSubtitle(Setting._mssv);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		spinHocKy = (Spinner) findViewById(R.id.spin_hocky);
        spinHocKy.setOnItemSelectedListener(this);
        // Creating adapter for spinner
        dataAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

        for (int i = 0; i < MainActivity.nienHocModel.getHKs().size(); i++) {
            // logService.functionTag("handleNienHocModelChanged", "add " + i);
            String lbl = "";
            if (MainActivity.nienHocModel.getHKs().get(i).hk == 0)
                lbl = "Tất cả";
            else if (MainActivity.nienHocModel.getHKs().get(i).hk == -1)
                lbl = "Chọn học kỳ...";
            else
                lbl = "HK" + MainActivity.nienHocModel.getHKs().get(i).hk + "("
                        + MainActivity.nienHocModel.getHKs().get(i).namhoc + "-"
                        + (MainActivity.nienHocModel.getHKs().get(i).namhoc + 1) + ")";
            dataAdapter.add(lbl);
        }
        spinHocKy.setAdapter(dataAdapter);

        Button btnMoreInfo = (Button) findViewById(R.id.btn_more_info);
        btnMoreInfo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // get correspondence item

                StringBuilder sb = new StringBuilder();

                // check case all
                if (model.hocky.namhoc != 0) {
                    sb.append("\tTín chỉ đăng kí hk: "
                            + (objs.size() > 1 ? objs.get(1) : "N/A"));
                    sb.append("\n");

                    sb.append("\tTín chỉ tích lũy hk: "
                            + (objs.size() > 2 ? objs.get(2) : "N/A"));
                    sb.append("\n");
                }
                sb.append("\tTổng số tín chỉ: "
                        + (objs.size() > 3 ? objs.get(3) : "N/A"));
                sb.append("\n");

                if (model.hocky.namhoc != 0) {
                    sb.append("\tĐiểm TB hk: "
                            + (objs.size() > 4 ? objs.get(4) : "N/A"));
                    sb.append("\n");
                }

                sb.append("\tĐiểm TB tích lũy: "
                        + (objs.size() > 5 ? objs.get(5) : "N/A"));
                sb.append("\n");
            }

        });
        
		onLoad();
	}
	private void onLoad() {
		logService.functionTag("onLoad", "");
		currentSearch = MainActivity.currentSearch;
		lstDiem = (ListView)findViewById(R.id.list_diem);
		model = new DiemModel();
		controller = new DiemController(this);
		controller.setModel(model);
		controller.open();
		setModel(model);
		//loadDiem();
	}
	
	public void setModel(DiemModel model) {
        if (model == null) {
            throw new NullPointerException("model");
        }

        DiemModel oldModel = this.model;
        if (oldModel != null) {
            oldModel.removeListener(this);
        }
        this.model = model;
        this.model.addListener(this);

    }
	
	public boolean onCreateOptionsMenu(Menu menu) {
		logService.functionTag("onCreateOptionsMenu", "");
		// Used to put dark icons on light action bar
		boolean isLight = !Setting._blackTheme;

		// Create the search view
		SearchView searchView = new SearchView(getSupportActionBar()
				.getThemedContext());
		searchView.setQueryHint("Tìm theo MSSV");
		searchView.setOnQueryTextListener(this);

		menu.add("Search")
				.setIcon(
						isLight ? R.drawable.ic_search_inverse
								: R.drawable.abs__ic_search)
				.setActionView(searchView)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		menu.add("Reload")
				.setIcon(
						isLight ? R.drawable.ic_refresh_inverse
								: R.drawable.ic_refresh)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		logService.functionTag("onOptionsItemSelected", item.getTitle().toString() + " selected");
		switch(item.getItemId())
		{
		case android.R.id.home:
			this.finish();
			break;
		}
		if(item.getTitle().toString().equals("Reload")) {
			//loadDiem();
		}
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		logService.functionTag("onQueryTextSubmit", "submit text: " + query);
		currentSearch = query;
		DI__NienHoc nienhoc = MainActivity.nienHocModel.getHKs().get(spinHocKy.getSelectedItemPosition());
		loadDiem(nienhoc.namhoc, nienhoc.hk);
		return true;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		return false;
	}

	public void loadDiem(int namhoc, int hocky) {
		logService.functionTag("loadDiem", namhoc + " - " + hocky);
		
		if (!currentSearch.equals("")
                && !model.mssv.equals(currentSearch)) {
			model.mssv = currentSearch;
            Map<String, String> searchParams = new HashMap<String, String>();
            searchParams.put("mssv", currentSearch);
            model.mssv = currentSearch;
        	DialogService.openProgressDialog(this, Constant.progress_diem);
            controller.getByHocKy(namhoc, hocky);
        } else if (currentSearch.equals("")
                && !model.mssv.equals(Setting._mssv)) {
        	model.mssv = Setting._mssv;
        	DialogService.openProgressDialog(this, Constant.progress_diem);
        	controller.getByHocKy(namhoc, hocky);
        }
	}
	
	@Override
	protected void onResume() {
		logService.functionTag("onResume", "");
		controller.open();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		logService.functionTag("onPause", "");
		controller.close();
		super.onPause();
	}
	@Override
	public void handleDiemModelChanged(DiemModel sender, List<Object> objs) {
		// TODO Auto-generated method stub
		List<DI__Diem> diems = new ArrayList<DI__Diem>();
		diems = sender.getDiems();
		
		DiemAdapter dvAdapter = new DiemAdapter(this, diems);
        lstDiem.setAdapter(dvAdapter);

        this.lstDiem.invalidateViews();
        
        if (diems.size() == 0)
        {
            final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
            dialogBuilder.setMessage(model.getObjs().get(0).toString());
            dialogBuilder.setTitle("BKmanager");
            dialogBuilder.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

                  public void onClick(DialogInterface dialog, int id) {

                      dialogBuilder.dismiss();

                } });
            dialogBuilder.show();
        }
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		// On selecting a spinner item
        String label = parent.getItemAtPosition(position).toString();
        logService.functionTag("onItemSelected", label);

        if (position == 0)
            return;

        if (MainActivity.nienHocModel.getHKs().size() != 0) {
            DI__NienHoc nienhoc = MainActivity.nienHocModel.getHKs().get(position);
            loadDiem(nienhoc.namhoc, nienhoc.hk);
            //controller.getByHocKy(nienhoc.namhoc, nienhoc.hk);
        }
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}

}
