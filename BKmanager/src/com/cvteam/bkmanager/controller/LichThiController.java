package com.cvteam.bkmanager.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.cvteam.bkmanager.model.DI__LichThi;
import com.cvteam.bkmanager.model.LichThiModel;
import com.cvteam.bkmanager.service.AAOService;
import com.cvteam.bkmanager.service.DatabaseService;
import com.cvteam.bkmanager.service.LogService;

public class LichThiController implements IDataSource {
	private SQLiteDatabase database;
	private DatabaseService databaseService;
	private LogService logService = new LogService("LichThiController");

	private LichThiModel lichThi;

	public void setModel(LichThiModel lichThi) {
		this.lichThi = lichThi;
	}

	public LichThiController(Context context) {
		databaseService = new DatabaseService(context);
	}

	public void open() throws SQLException {
		database = databaseService.getWritableDatabase();
	}

	public void close() {
		databaseService.close();
	}

	public void getLichThi() {
		// logService.functionTag("getAllLichThi", "Select all lich thi");
		Map<String, String> searchParams = new HashMap<String, String>();
		searchParams.put("mssv", this.lichThi.mssv);
		databaseService.selectTable("lichthi", searchParams, this);
	}

	public void getLichThi(Map<String, String> searchParams) {
		// logService.functionTag("getAllLichThi", "Select all lich thi where "
		// + searchParams.toString());
		databaseService.selectTable("lichthi", searchParams, this);
	}

	private DI__LichThi cursorToLichThi(Cursor cursor) {
		// logService.functionTag("cursorToLichThi", "cursor to model");
		if (cursor == null)
			return new DI__LichThi();
		DI__LichThi lt = new DI__LichThi();

		lt.mssv = cursor.getString(1);
		lt.namhoc = cursor.getInt(2);
		lt.hocky = cursor.getInt(3);
		lt.mamh = cursor.getString(4);
		lt.tenmh = cursor.getString(5);
		lt.nhomto = cursor.getString(6);
		lt.ngaygk = cursor.getString(7);
		lt.tietgk = cursor.getInt(8);
		lt.phonggk = cursor.getString(9);
		lt.ngayck = cursor.getString(10);
		lt.tietck = cursor.getInt(11);
		lt.phongck = cursor.getString(12);

		return lt;
	}

	/**
	 * Cap nhat Model LichThi tu du lieu lay tu DB
	 * 
	 * @param Object
	 *            : cursor lay tu DB
	 */
	public void updateDataSource(Object obj) {
		// logService.functionTag("UpdateDataSource", "Update model LichThi");
		List<DI__LichThi> lstLichThi = new ArrayList<DI__LichThi>();
		if (obj != null) {
			Cursor cursor = (Cursor) obj;
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				lstLichThi.add(cursorToLichThi(cursor));
				cursor.moveToNext();
			}
			cursor.close();
		}
		if (lstLichThi.size() == 0) {
			requestLichThiFromAao(lichThi.mssv);
		} else {
			this.lichThi.setLichThis(lstLichThi);
		}
	}

	/**
	 * Cap nhat Model LichThi tu du lieu lay tu AAO (qua mang)
	 * 
	 * @param lstLichThi
	 *            : list cac lich thi lay tu AAO
	 * @param objs
	 *            : objs[0] is updateDate or error message if result is empty
	 */
	public void updateDataSource(List<DI__LichThi> lstLichThi, List<String> objs) {
		logService.functionTag("updateLichThi",
				"Set new list for LichThi Model. Size = " + lstLichThi.size());

		this.lichThi.setObject(objs);
		this.lichThi.setLichThis(lstLichThi);

		if (lstLichThi.size() == 0)
			return;
		
		for (int i = 0; i < lstLichThi.size(); i++) {
			DI__LichThi temp = lstLichThi.get(i);

			ContentValues values = new ContentValues();

			values.put("mssv", temp.mssv);
			values.put("namhoc", temp.namhoc);
			values.put("hocky", temp.hocky);
			values.put("mamh", temp.mamh);
			values.put("tenmh", temp.tenmh);
			values.put("nhomto", temp.nhomto);

			values.put("ngaygk", temp.ngaygk);
			values.put("tietgk", temp.tietgk);
			values.put("phonggk", temp.phonggk);

			values.put("ngayck", temp.ngayck);
			values.put("tietck", temp.tietck);
			values.put("phongck", temp.phongck);

			try {
				int affected = database.update("lichthi", values, "mssv = '"
						+ temp.mssv + "' AND namhoc = " + temp.namhoc
						+ " AND hocky = " + temp.hocky + " AND mamh = '"
						+ temp.mamh + "'", null);
				if (affected == 0)
					database.insert("lichthi", null, values);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		// insert hocky entry into database
		DI__LichThi temp = lstLichThi.get(0);

		// logService.functionTag("updateLichThi", "Insert into hocky hocky = "
		// + temp.namhoc + temp.hocky + " lichthistt = " + objs.get(0)
		// + " for: " + temp.mssv);

		ContentValues values = new ContentValues();

		values.put("mssv", temp.mssv);
		values.put("namhoc", temp.namhoc);
		values.put("hocky", temp.hocky);
		values.put("lichthistt", objs.get(0));

		try {
			int affected = database.update("hocky", values, "mssv = '"
					+ temp.mssv + "' AND namhoc = " + temp.namhoc
					+ " AND hocky = " + temp.hocky, null);
			if (affected == 0)
				database.insert("hocky", null, values);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void requestLichThiFromAao(String mssv) {
		// RequestLichThiFromAaoAsynTask asynTask = new
		// RequestLichThiFromAaoAsynTask(
		// mssv, this);
		// asynTask.execute();
		RequestLichThiFromAaoAsynTask asynTask = new RequestLichThiFromAaoAsynTask(
				mssv, this);
		asynTask.execute();
	}

	class RequestLichThiFromAaoAsynTask extends AsyncTask<Void, Void, Void> {
		private String mssv;
		private LichThiController ltController;
		private List<DI__LichThi> lstLichThi;
		private List<String> objs;

		public RequestLichThiFromAaoAsynTask(String mssv,
				LichThiController ltController) {
			this.mssv = mssv;
			this.ltController = ltController;
		}

		@Override
		protected Void doInBackground(Void... params) {
			// logService.functionTag("doInBackground", "AAOService.getLichThi:"
			// + mssv);
			objs = new ArrayList<String>();
			lstLichThi = AAOService.getLichThi(mssv, "", objs);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// logService.functionTag("onPostExecute", "AAOService.getLichThi:"
			// + mssv + " done");
			ltController.updateDataSource(lstLichThi, objs);
		}

	}
}
