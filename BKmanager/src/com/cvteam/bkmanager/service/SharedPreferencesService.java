package com.cvteam.bkmanager.service;

import android.content.SharedPreferences;

import com.cvteam.bkmanager.Setting;

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
}
