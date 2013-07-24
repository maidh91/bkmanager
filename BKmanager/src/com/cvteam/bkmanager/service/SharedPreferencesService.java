package com.cvteam.bkmanager.service;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;

import com.cvteam.bkmanager.Setting;
import com.cvteam.bkmanager.model.DI__NienHoc;

public final class SharedPreferencesService {

	public static boolean SaveCauHinh(SharedPreferences preferences) {
		try {
			SharedPreferences.Editor editor = preferences.edit();
			editor.putBoolean("_blackTheme", Setting._blackTheme);
			editor.putString("_mssv", Setting._mssv);
			editor.putString("_name", Setting._name);
			editor.commit();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public static boolean LoadCauHinh(SharedPreferences preferences) {
		try {
			Setting._blackTheme = preferences.getBoolean("_blackTheme", true);
			Setting._mssv = preferences.getString("_mssv", "");
			Setting._name = preferences.getString("_name", "");

			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public static String LoadCurrentSearch(SharedPreferences preferences) {
		try {
			return preferences.getString("currentSearch", "");
		} catch (Exception ex) {
			return "";
		}
	}

	public static boolean SaveCurrentSearch(String currentSearch,
			SharedPreferences preferences) {
		try {
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("currentSearch", currentSearch);
			editor.commit();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
	
	public static boolean SaveListNienHoc(List<DI__NienHoc> nienhocs, SharedPreferences preferences) {
        try {
            StringBuilder lstNienHoc = new StringBuilder();
            if(nienhocs.size() > 0)
                lstNienHoc.append(String.format("%s-%s", nienhocs.get(0).namhoc, nienhocs.get(0).hk));
            for(int i = 1;i < nienhocs.size();i++)
                lstNienHoc.append(String.format(",%s-%s", nienhocs.get(i).namhoc, nienhocs.get(i).hk));

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("listnienhoc", lstNienHoc.toString());
            editor.commit();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static List<DI__NienHoc> LoadListNienHoc(SharedPreferences preferences) {
        List<DI__NienHoc> nienhocs = new ArrayList<DI__NienHoc>();
        try {
            String lstNienHoc = preferences.getString("listnienhoc", "");
            if(lstNienHoc == "")
                return nienhocs;
            String[] lst = lstNienHoc.split(",");
            for(int i = 0;i < lst.length;i++)
            {
                DI__NienHoc nh = new DI__NienHoc(Integer.parseInt(lst[i].substring(0, 4)), Integer.parseInt(lst[i].substring(5)));
                nienhocs.add(nh);
            }
            return nienhocs;
        }catch (Exception ex) {
            return nienhocs;
        }
    }
}
