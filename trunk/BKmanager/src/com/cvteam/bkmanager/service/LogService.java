package com.cvteam.bkmanager.service;

import android.util.Log;

public class LogService {
	public static final String APP_TAG = "CVTeam.BKmanager";
    private String classTag;

    public LogService(String className) {
        classTag = className;
    }


    public static void freeTag(String tag, String message) {
    	Log.d(APP_TAG + "." + tag, message);
    }
    
    public void functionTag(String function, String message) {
        Log.d(APP_TAG + "." + classTag + "." + function, message);
    }

    public void exceptionTag(String function, String message) {
        Log.d(APP_TAG + "." + classTag + "." + function + ".exception", message);
    }
}
