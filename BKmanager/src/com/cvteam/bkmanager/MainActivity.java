
package com.cvteam.bkmanager;

import java.util.List;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.TextView;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.cvteam.bkmanager.model.DI__NienHoc;
import com.cvteam.bkmanager.model.NienHocModel;
import com.cvteam.bkmanager.service.AAOService;
import com.cvteam.bkmanager.service.LogService;
import com.cvteam.bkmanager.service.SharedPreferencesService;

public class MainActivity extends Activity implements SearchView.OnQueryTextListener {

    private LogService logService = new LogService("MainActivity");
    public static final String APP_PREFS = "CVTeam.BKmanager";
    private SharedPreferences sharedPrefs;
    public static String currentSearch;
    public static NienHocModel nienHocModel;
    public static NienHocModel nienHocHocPhiModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logService.functionTag("onCreate", "");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPrefs = getSharedPreferences(APP_PREFS, Activity.MODE_PRIVATE);
        SharedPreferencesService.LoadCauHinh(sharedPrefs);
        currentSearch = SharedPreferencesService.LoadCurrentSearch(sharedPrefs);
        if (Setting._mssv == "") {
            startActivityForResult(new Intent(this, AccountSetupActivity.class), 57);
        } else {
            // getSupportActionBar().setDisplayShowTitleEnabled(true);
            // getSupportActionBar().setTitle(Setting._name);
            // getSupportActionBar().setSubtitle(Setting._mssv);
            getSupportActionBar().setHomeButtonEnabled(false);

            TextView txt_tensv = (TextView) findViewById(R.id.txt_tensv);
            TextView txt_mssv = (TextView) findViewById(R.id.txt_mssv);
            txt_tensv.setText(Setting._name);
            txt_mssv.setText("MSSV: " + Setting._mssv);
        }
        
        nienHocModel = new NienHocModel();
        initNienHocModel(nienHocModel, "http://www.aao.hcmut.edu.vn/php/aao_tkb.php", "");
        
        nienHocHocPhiModel = new NienHocModel();
        initNienHocModel(nienHocHocPhiModel, "http://www.aao.hcmut.edu.vn/php/aao_hp.php", "hocphi");
        
        InitializeService(); // CalendarService.readCalendar(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        logService.functionTag("onCreateOptionsMenu", "");
        // Used to put dark icons on light action bar
        boolean isLight = !Setting._blackTheme;

        // Create the search view
        // SearchView searchView = new
        // SearchView(getSupportActionBar().getThemedContext());
        // searchView.setQueryHint("Tìm theo MSSV");
        // searchView.setOnQueryTextListener(this);

        // menu.add("Search")
        // .setIcon(
        // isLight ? R.drawable.ic_search_inverse
        // : R.drawable.abs__ic_search)
        // .setActionView(searchView)
        // .setShowAsAction(
        // MenuItem.SHOW_AS_ACTION_IF_ROOM
        // | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        menu.add("Reload")
                .setIcon(isLight ? R.drawable.ic_refresh_inverse : R.drawable.ic_refresh)
                .setShowAsAction(
                        MenuItem.SHOW_AS_ACTION_IF_ROOM
                                | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        getSupportMenuInflater().inflate(R.menu.my_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        logService.functionTag("onOptionsItemSelected", item.getTitle() + " selected");
        if (item.getTitle().toString().equals("Refresh")) {
        }
        switch (item.getItemId()) {
            case R.id.about:
                return true;
            case R.id.setting:
                startActivity(new Intent(this, SettingActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        // return false;
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
                // getSupportActionBar().setTitle(Setting._name);
                // getSupportActionBar().setSubtitle(Setting._mssv);
                TextView txt_tensv = (TextView) findViewById(R.id.txt_tensv);
                TextView txt_mssv = (TextView) findViewById(R.id.txt_mssv);
                txt_tensv.setText(Setting._name);
                txt_mssv.setText("MSSV: " + Setting._mssv);
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
        SharedPreferencesService.SaveCauHinh(sharedPrefs);
        SharedPreferencesService.SaveCurrentSearch(currentSearch, sharedPrefs);
        SharedPreferencesService.SaveListNienHocByType(nienHocModel.getHKs(), sharedPrefs, "");
        SharedPreferencesService.SaveListNienHocByType(nienHocHocPhiModel.getHKs(), sharedPrefs, "hocphi");
        super.onStop();
    }

    public void onClickFeature(View view) {
        logService.functionTag("onClickFeature", "id: " + view.getId());
        Intent myIntent;
        switch (view.getId()) {
            case R.id.imgTKB:
                myIntent = new Intent(MainActivity.this, ThoiKhoaBieuActivity.class);
                MainActivity.this.startActivity(myIntent);
                break;
            case R.id.imgLichThi:
                myIntent = new Intent(MainActivity.this, LichThiActivity.class);
                MainActivity.this.startActivity(myIntent);
                break;
            case R.id.imgDiem:
                myIntent = new Intent(MainActivity.this, DiemActivity.class);
                MainActivity.this.startActivity(myIntent);
                break;
        }
    }

    // /////////////////////////////////////////
    // Notification //
    // /////////////////////////////////////////

    private void InitializeService() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.cvteam.bkmanager.service.NotiService".equals(service.service.getClassName())) {
                System.out.println("isRunning");
                stopService(new Intent(this, com.cvteam.bkmanager.service.NotiService.class));
                break;
            }
        }

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

    // /////////////////////////////////////////
    // End Notification //
    // /////////////////////////////////////////

    public void tienichclick(View v) {
        startActivity(new Intent(this, TienIchActivity.class));
    }

    private void initNienHocModel(NienHocModel nienHocModel, String source, String type) {
        List<DI__NienHoc> lstNienHoc = SharedPreferencesService.LoadListNienHocByType(sharedPrefs, type);
        if (lstNienHoc.size() == 0)
            try {
                lstNienHoc = AAOService
                        .refreshListNienHocWithJSoup(source);
            } catch (Exception e) {
                e.printStackTrace();
                if (lstNienHoc.size() == 0) {

                    lstNienHoc.add(new DI__NienHoc(2013, 1));

                    lstNienHoc.add(new DI__NienHoc(2012, 3));
                    lstNienHoc.add(new DI__NienHoc(2012, 2));
                    lstNienHoc.add(new DI__NienHoc(2012, 1));

                    lstNienHoc.add(new DI__NienHoc(2011, 3));
                    lstNienHoc.add(new DI__NienHoc(2011, 2));
                    lstNienHoc.add(new DI__NienHoc(2011, 1));

                    lstNienHoc.add(new DI__NienHoc(2010, 3));
                    lstNienHoc.add(new DI__NienHoc(2010, 2));
                    lstNienHoc.add(new DI__NienHoc(2010, 1));

                    lstNienHoc.add(new DI__NienHoc(2009, 3));
                    lstNienHoc.add(new DI__NienHoc(2009, 2));
                    lstNienHoc.add(new DI__NienHoc(2009, 1));

                    lstNienHoc.add(new DI__NienHoc(2008, 3));
                    lstNienHoc.add(new DI__NienHoc(2008, 2));
                    lstNienHoc.add(new DI__NienHoc(2008, 1));

                    lstNienHoc.add(new DI__NienHoc(2007, 3));
                    lstNienHoc.add(new DI__NienHoc(2007, 2));
                    lstNienHoc.add(new DI__NienHoc(2007, 1));

                    lstNienHoc.add(new DI__NienHoc(2006, 3));
                    lstNienHoc.add(new DI__NienHoc(2006, 2));
                    lstNienHoc.add(new DI__NienHoc(2006, 1));

                    lstNienHoc.add(new DI__NienHoc(2005, 3));
                    lstNienHoc.add(new DI__NienHoc(2005, 2));
                    lstNienHoc.add(new DI__NienHoc(2005, 1));
                }
            }

        nienHocModel.setHKs(lstNienHoc);
    }
}
