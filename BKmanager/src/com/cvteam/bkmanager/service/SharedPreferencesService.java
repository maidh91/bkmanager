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
			editor.putBoolean("_thongBaoLichThi", Setting._thongBaoLichThi);
			editor.putBoolean("_thongBaoDiem", Setting._thongBaoDiem);
			editor.putBoolean("_thongBaoHocPhi", Setting._thongBaoHocPhi);
			editor.putBoolean("_thongBaoHocBong", Setting._thongBaoHocBong);
			editor.putBoolean("_thongBaoSuKien", Setting._thongBaoSuKien);
			editor.putBoolean("_dongBoLichThi", Setting._dongBoLichThi);
			editor.putInt("_chuKiCapNhat", Setting._chuKiCapNhat);
			editor.putInt("_nhacNhoLichThi", Setting._nhacNhoLichThi);
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
			Setting._thongBaoLichThi = preferences.getBoolean("_thongBaoLichThi", true);
			Setting._thongBaoDiem = preferences.getBoolean("_thongBaoDiem", true);
			Setting._thongBaoHocPhi = preferences.getBoolean("_thongBaoHocPhi", true);
			Setting._thongBaoHocBong = preferences.getBoolean("_thongBaoHocBong", true);
			Setting._thongBaoSuKien = preferences.getBoolean("_thongBaoSuKien", true);
			Setting._dongBoLichThi = preferences.getBoolean("_dongBoLichThi", true);
			Setting._chuKiCapNhat = preferences.getInt("_chuKiCapNhat", 3);
			Setting._nhacNhoLichThi = preferences.getInt("_nhacNhoLichThi", 0);

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
            ex.printStackTrace();
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
            ex.printStackTrace();
            return nienhocs;
        }
    }
    
    public static boolean SaveListNienHocByType(List<DI__NienHoc> nienhocs, SharedPreferences preferences, String type) {
        try {
            StringBuilder lstNienHoc = new StringBuilder();
            if(nienhocs.size() > 0)
                lstNienHoc.append(String.format("%s-%s", nienhocs.get(0).namhoc, nienhocs.get(0).hk));
            for(int i = 1;i < nienhocs.size();i++)
                lstNienHoc.append(String.format(",%s-%s", nienhocs.get(i).namhoc, nienhocs.get(i).hk));

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("listnienhoc" + type, lstNienHoc.toString());
            editor.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static List<DI__NienHoc> LoadListNienHocByType(SharedPreferences preferences, String type) {
        List<DI__NienHoc> nienhocs = new ArrayList<DI__NienHoc>();
        try {
            String lstNienHoc = preferences.getString("listnienhoc" + type, "");
            
            System.out.println("LoadListNienHoc" + type + " = " + lstNienHoc);
            
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
            ex.printStackTrace();
            return nienhocs;
        }
    }
}
