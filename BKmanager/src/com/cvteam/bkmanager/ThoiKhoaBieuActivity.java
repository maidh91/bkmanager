package com.cvteam.bkmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.AlertDialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ListView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnActionExpandListener;
import com.actionbarsherlock.widget.SearchView;
import com.cvteam.bkmanager.adapter.ThoiKhoaBieuAdapter;
import com.cvteam.bkmanager.controller.ThoiKhoaBieuController;
import com.cvteam.bkmanager.model.DI__ThoiKhoaBieu;
import com.cvteam.bkmanager.model.ThoiKhoaBieuModel;
import com.cvteam.bkmanager.service.Constant;
import com.cvteam.bkmanager.service.DialogService;
import com.cvteam.bkmanager.service.LogService;

public class ThoiKhoaBieuActivity extends Activity implements
		SearchView.OnQueryTextListener, ThoiKhoaBieuModel.Listener {

	ListView lstTKB;
	private String currentSearch;
	private LogService logService = new LogService("ThoiKhoaBieuActivity");
	private ThoiKhoaBieuModel model;
	private ThoiKhoaBieuController controller;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		logService.functionTag("onCreate", "");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_thoi_khoa_bieu);

		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setTitle(Setting._name);
		getSupportActionBar().setSubtitle(Setting._mssv);
		getSupportActionBar().setHomeButtonEnabled(true);

		onLoad();
	}

	private void onLoad() {
		logService.functionTag("onLoad", "");
		currentSearch = "";//MainActivity.currentSearch;
		lstTKB = (ListView) findViewById(R.id.list_thoi_khoa_bieu);
		model = new ThoiKhoaBieuModel();
		controller = new ThoiKhoaBieuController(this);
		controller.setModel(model);
		controller.open();
		setModel(model);
		loadThoiKhoaBieu();
	}

	public void setModel(ThoiKhoaBieuModel model) {
		if (model == null) {
			throw new NullPointerException("model");
		}

		ThoiKhoaBieuModel oldModel = this.model;
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
				.setOnActionExpandListener(new OnActionExpandListener() {
			        @Override
			        public boolean onMenuItemActionCollapse(MenuItem item) {
			            currentSearch = "";
			            loadThoiKhoaBieu();
			        	// Return true to collapse action view
			            return true;  
			        }

			        @Override
			        public boolean onMenuItemActionExpand(MenuItem item) {
			            // Do something when expanded
			        	// Return true to expand action view
			            return true;
			        }
			    })
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
		logService.functionTag("onOptionsItemSelected", item.getTitle()
				.toString() + " selected");
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		}
		if (item.getTitle().toString().equals("Reload")) {
			loadThoiKhoaBieu();
		}
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		logService.functionTag("onQueryTextSubmit", "submit text: " + query);
		currentSearch = query;
		loadThoiKhoaBieu();
		return true;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handleThoiKhoaBieuChanged(ThoiKhoaBieuModel sender) {
		// TODO Auto-generated method stub
		logService.functionTag("handleThoiKhoaBieuChanged", sender.mssv);

		List<DI__ThoiKhoaBieu> TKBs = new ArrayList<DI__ThoiKhoaBieu>();
		TKBs = sender.getThoiKhoaBieus();

		ThoiKhoaBieuAdapter dvAdapter = new ThoiKhoaBieuAdapter(this, TKBs);
		lstTKB.setAdapter(dvAdapter);
		this.lstTKB.invalidateViews();

		DialogService.closeProgressDialog();

		if (TKBs.size() == 0) {
			final AlertDialog dialogBuilder = new AlertDialog.Builder(this)
					.create();
			dialogBuilder.setMessage(model.getObjs().get(0));
			dialogBuilder.setTitle("BKmanager");
			dialogBuilder.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int id) {

							dialogBuilder.dismiss();

						}
					});
			dialogBuilder.show();
		}
	}

	public void loadThoiKhoaBieu() {
		logService.functionTag("loadThoiKhoaBieu", "");

		if (!currentSearch.equals("")) {
			if (!model.mssv.equals(currentSearch)) {
				model.mssv = currentSearch;
			}
			Map<String, String> searchParams = new HashMap<String, String>();
			searchParams.put("mssv", currentSearch);
			DialogService.openProgressDialog(this, Constant.progress_tkb);
			controller.getThoiKhoaBieu(searchParams);
		} else {
			if (!model.mssv.equals(Setting._mssv)) {
				model.mssv = Setting._mssv;
			}
			DialogService.openProgressDialog(this, Constant.progress_tkb);
			controller.getThoiKhoaBieu();
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
}
