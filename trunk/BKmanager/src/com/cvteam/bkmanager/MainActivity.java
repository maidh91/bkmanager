package com.cvteam.bkmanager;

import org.holoeverywhere.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.cvteam.bkmanager.service.SharedPreferencesService;

public class MainActivity extends SherlockActivity implements
		SearchView.OnQueryTextListener {

	public static final String APP_PREFS = "CVTeam.BKmanager";
	private SharedPreferences sharedPrefs;
	private String currentSearch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		sharedPrefs = getSharedPreferences(APP_PREFS, Activity.MODE_PRIVATE);
		SharedPreferencesService.LoadCauHinh(sharedPrefs);
        currentSearch = SharedPreferencesService.LoadCurrentSearch(sharedPrefs);
        if (Setting._mssv == "") {
            startActivityForResult(new Intent(this, AccountSetupActivity.class),
                    57);
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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
		if (item.getTitle().toString().equals("Refresh")) {
		}
		return false;
	}
	
	@Override
	public boolean onQueryTextSubmit(String query) {
		Toast.makeText(this, "You searched for: " + query, Toast.LENGTH_LONG)
				.show();
		return true;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    
	@Override
    protected void onStop() {
        // TODO Auto-generated method stub
        SharedPreferencesService.SaveCauHinh(sharedPrefs);
        SharedPreferencesService.SaveCurrentSearch(currentSearch, sharedPrefs);
        super.onStop();
    }
}
