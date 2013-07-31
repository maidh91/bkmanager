package com.cvteam.bkmanager.service;

import org.holoeverywhere.app.ProgressDialog;

import android.content.Context;

public class DialogService {
	public static ProgressDialog pd;
	
	public static void openProgressDialog(Context context, String message) {
        // Log.d("openProgressDialog", "Progress open here");
        if (pd != null) {
            // Log.d("openProgressDialog", "pd.isShowing() = " +
            // pd.isShowing());
            if (pd.isShowing())
                return;
        }
        pd = new ProgressDialog(context);
        pd.setTitle("BKnoti: Thông báo");
        pd.setMessage(message);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();

    }

    public static void closeProgressDialog() {
        if (pd != null)
            if (pd.isShowing())
                pd.dismiss();
    }
}
