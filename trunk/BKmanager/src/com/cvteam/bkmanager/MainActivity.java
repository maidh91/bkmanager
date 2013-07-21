package com.cvteam.bkmanager;

import org.holoeverywhere.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.cvteam.bkmanager.service.LogService;
import com.cvteam.bkmanager.service.SharedPreferencesService;

public class MainActivity extends SherlockFragmentActivity implements
		SearchView.OnQueryTextListener {

	private LogService logService = new LogService("MainActivity");
	public static final String APP_PREFS = "CVTeam.BKmanager";
	private SharedPreferences sharedPrefs;
	public static String currentSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		logService.functionTag("onCreate", "");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sharedPrefs = getSharedPreferences(APP_PREFS, Activity.MODE_PRIVATE);
		SharedPreferencesService.LoadCauHinh(sharedPrefs);
		currentSearch = SharedPreferencesService.LoadCurrentSearch(sharedPrefs);
		if (Setting._mssv == "") {
			startActivityForResult(
					new Intent(this, AccountSetupActivity.class), 57);
		} else {
			getSupportActionBar().setDisplayShowTitleEnabled(true);
			getSupportActionBar().setTitle(Setting._name);
			getSupportActionBar().setSubtitle(Setting._mssv);
			getSupportActionBar().setHomeButtonEnabled(true);
		}
	}

	@Override
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
		logService.functionTag("onOptionsItemSelected", item.getTitle() + " selected");
		if (item.getTitle().toString().equals("Refresh")) {
		}
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		logService.functionTag("onQueryTextSubmit", "submit text: " + query);
		currentSearch = query;
		return true;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		logService.functionTag("onActivityResult", "");
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 57) {
			// logService.functionTag("onActivityResult", "requestCode == 57");
			if (resultCode == RESULT_OK) {
				SharedPreferencesService.SaveCauHinh(sharedPrefs);
				getSupportActionBar().setTitle(Setting._name);
				getSupportActionBar().setSubtitle(Setting._mssv);
			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
				this.finish();
			}
		}
	}

	@Override
	protected void onResume() {
		logService.functionTag("onResume", "");
		super.onResume();
	}

	@Override
	protected void onPause() {
		logService.functionTag("onPause", "");
		super.onPause();
	}

	@Override
	protected void onStop() {
		logService.functionTag("onStop", "");
		// TODO Auto-generated method stub
		SharedPreferencesService.SaveCauHinh(sharedPrefs);
		SharedPreferencesService.SaveCurrentSearch(currentSearch, sharedPrefs);
		super.onStop();
	}

	public void onClickFeature(View view) {
		logService.functionTag("onClickFeature", "id: " + view.getId());
		Intent myIntent;
		switch (view.getId()) {
		case R.id.txtviewThoiKhoaBieu:
			myIntent = new Intent(MainActivity.this,
					ThoiKhoaBieuActivity.class);
			MainActivity.this.startActivity(myIntent);
			break;
		case R.id.txtviewLichThi:
			myIntent = new Intent(MainActivity.this,
					LichThiActivity.class);
			MainActivity.this.startActivity(myIntent);
			break;
		}
	}
	
	
}
