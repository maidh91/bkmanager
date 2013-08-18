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
import com.cvteam.bkmanager.adapter.LichThiAdapter;
import com.cvteam.bkmanager.controller.LichThiController;
import com.cvteam.bkmanager.model.DI__LichThi;
import com.cvteam.bkmanager.model.LichThiModel;
import com.cvteam.bkmanager.service.Constant;
import com.cvteam.bkmanager.service.DialogService;
import com.cvteam.bkmanager.service.LogService;

public class LichThiActivity extends Activity implements
SearchView.OnQueryTextListener, LichThiModel.Listener {

	ListView lstLichThi;
	private String currentSearch;
	private LogService logService = new LogService("LichThiActivity");
	private LichThiModel model;
	private LichThiController controller;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lich_thi);
		
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setTitle(Setting._name);
		getSupportActionBar().setSubtitle(Setting._mssv);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		onLoad();
	}
	private void onLoad() {
		logService.functionTag("onLoad", "");
		currentSearch = MainActivity.currentSearch;
		lstLichThi = (ListView)findViewById(R.id.list_lich_thi);
		model = new LichThiModel();
		controller = new LichThiController(this);
		controller.setModel(model);
		controller.open();
		setModel(model);
		loadLichThi();
	}
	
	public void setModel(LichThiModel model) {
        if (model == null) {
            throw new NullPointerException("model");
        }

        LichThiModel oldModel = this.model;
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
			            loadLichThi();
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
		logService.functionTag("onOptionsItemSelected", item.getTitle().toString() + " selected");
		switch(item.getItemId())
		{
		case android.R.id.home:
			this.finish();
			break;
		}
		if(item.getTitle().toString().equals("Reload")) {
			loadLichThi();
		}
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		logService.functionTag("onQueryTextSubmit", "submit text: " + query);
		currentSearch = query;
		loadLichThi();
		return true;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handleLichThiChanged(LichThiModel sender) {
		// TODO Auto-generated method stub
		logService.functionTag("handleLichThiChanged", sender.mssv);
		
		List<DI__LichThi> lichthis = new ArrayList<DI__LichThi>();
		lichthis = sender.getLichThis();
		
		LichThiAdapter dvAdapter = new LichThiAdapter(this, lichthis);
		lstLichThi.setAdapter(dvAdapter);
        this.lstLichThi.invalidateViews();

		DialogService.closeProgressDialog();
        
        if (lichthis.size() == 0)
        {
            final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
            dialogBuilder.setMessage(model.getObjs().get(0));
            dialogBuilder.setTitle("BKmanager");
            dialogBuilder.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

                  public void onClick(DialogInterface dialog, int id) {

                      dialogBuilder.dismiss();

                } });
            dialogBuilder.show();
        }
	}
	public void loadLichThi() {
		logService.functionTag("loadLichThi", "");
		
		if (!currentSearch.equals("")) {
            if (!model.mssv.equals(currentSearch)) {
            	model.mssv = currentSearch;
            }
            Map<String, String> searchParams = new HashMap<String, String>();
            searchParams.put("mssv", currentSearch);
        	DialogService.openProgressDialog(this, Constant.progress_lichthi);
            controller.getLichThi(searchParams);
        } else {
        	if (!model.mssv.equals(Setting._mssv)) {
        		model.mssv = Setting._mssv;
        	}
        	DialogService.openProgressDialog(this, Constant.progress_lichthi);
        	controller.getLichThi();
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
