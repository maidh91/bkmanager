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

import com.cvteam.bkmanager.MainActivity;
import com.cvteam.bkmanager.model.DI__Diem;
import com.cvteam.bkmanager.model.DI__HocKy;
import com.cvteam.bkmanager.model.DI__HocPhi;
import com.cvteam.bkmanager.model.DI__NienHoc;
import com.cvteam.bkmanager.model.DiemModel;
import com.cvteam.bkmanager.model.HocPhiModel;
import com.cvteam.bkmanager.service.AAOService;
import com.cvteam.bkmanager.service.DatabaseService;
import com.cvteam.bkmanager.service.DialogService;
import com.cvteam.bkmanager.service.LogService;

public class HocPhiController implements IDataSource {

	private LogService logService = new LogService("HocPhiController");

	private SQLiteDatabase database;
	private DatabaseService databaseService;

	private HocPhiModel model;
	public List<DI__HocKy> hockys;

	public void setModel(HocPhiModel diem) {
		this.model = diem;
	}

	public HocPhiController(Context context) {
		databaseService = new DatabaseService(context);
		hockys = new ArrayList<DI__HocKy>();
	}

	public void open() throws SQLException {
		database = databaseService.getWritableDatabase();
		if (!database.isReadOnly()) {
			// Enable foreign key constraints
			database.execSQL("PRAGMA foreign_keys=ON;");
		}
	}

	public void close() {
		databaseService.close();
	}

	public void getByHocKy(int namhoc, int hocky) {

		// logService.functionTag("getByHocKy", model.mssv + " " + namhoc +
		// hocky);
		Map<String, String> params = new HashMap<String, String>();
		params.put("mssv", model.mssv);

		model.hocky.namhoc = namhoc;
		model.hocky.hk = hocky;

		if (namhoc != 0 && hocky != 0) {
			params.put("namhoc", namhoc + "");
			params.put("hocky", hocky + "");

			logService.functionTag("getByHocKy",
					Integer.toString(model.hocky.namhoc * 10 + model.hocky.hk));

			databaseService.selectTable("hocphi", params, this);
		} else {
			// case all
			StringBuilder sql = new StringBuilder(
					"SELECT hocphistt FROM hocky WHERE mssv ='");
			sql.append(model.mssv);
			sql.append("' AND namhoc = 0");
			sql.append(" AND hocky = 0");
			logService.functionTag("updateDataSource", sql.toString());
			Cursor csr = database.rawQuery(sql.toString(), null);
			if (csr != null && csr.getCount() > 0) {
				logService
						.functionTag("updateDataSource", "cursor is not null");
				csr.moveToFirst();
				String lastNienHoc = csr.getString(0);
				logService.functionTag("updateDataSource", "lastNienHoc = "
						+ lastNienHoc);
				csr.close();

				DI__NienHoc nh = MainActivity.nienHocModel.getHKs().get(2);
				logService.functionTag("updateDataSource",
						"MainActivity.nienHocModel.getHKs().get(2) = "
								+ nh.namhoc + "" + nh.hk);

				if (lastNienHoc.equals(nh.namhoc + "" + nh.hk))
					databaseService.selectTable("hocphi", params, this);
				else {
					requestDiemFromAao(model.mssv);
				}
			} else {
				requestDiemFromAao(model.mssv);
			}
		}
	}

	private DI__HocPhi cursorToHH(Cursor cursor) {
		// logService.functionTag("cursorToDiem", "cursor to model");
		if (cursor == null)
			return new DI__HocPhi();
		DI__HocPhi hh = new DI__HocPhi();
		hh.mssv = cursor.getString(1);
		hh.namhoc = cursor.getInt(2);
		hh.hocky = cursor.getInt(3);
		hh.totalFee = cursor.getString(4);
		hh.owedFee = cursor.getString(5);
		hh.updateDay = cursor.getString(6);

		return hh;
	}

	@Override
	public void updateDataSource(Object obj) {
		// logService.functionTag("UpdateDataSource", "Update model Diem");
		DI__HocPhi hh = null;
		if (obj != null) {
			Cursor cursor = (Cursor) obj;
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				hh = cursorToHH(cursor);
				cursor.moveToNext();
			}
			cursor.close();
		}
		// logService.functionTag("UpdateDataSource",
		// "Update model Diem successfully");
		if (hh == null) {
			requestDiemFromAao(model.mssv);
		} else {
			// StringBuilder sql = new StringBuilder(
			// "SELECT hocphistt FROM hocky WHERE mssv ='");
			// sql.append(model.mssv);
			// sql.append("' AND namhoc = ");
			// sql.append(model.hocky.namhoc);
			// sql.append(" AND hocky = ");
			// sql.append(model.hocky.hk);
			// logService.functionTag("updateDataSource", sql.toString());
			// Cursor csr = database.rawQuery(sql.toString(), null);
			// if (csr != null) {
			// csr.moveToFirst();
			// List<Object> objs = new ArrayList<Object>();
			// objs.add("");
			// // tcdkhk
			// if (csr.getInt(0) > 0)
			// objs.add(csr.getInt(0));
			// // tctlhk
			// if (csr.getInt(1) > 0)
			// objs.add(csr.getInt(1));
			// // tongsotc
			// if (csr.getInt(2) > 0)
			// objs.add(csr.getInt(2));
			// // diemtbhk
			// if (csr.getFloat(3) > 0)
			// objs.add(csr.getFloat(3));
			// // diemtbtl
			// if (csr.getFloat(4) > 0)
			// objs.add(csr.getFloat(4));
			//
			// model.setObjects(objs);
			//
			// csr.close();
			// }
			model.setHocPhi(hh);
			// logService.functionTag("UpdateDataSource",
			// "Update model Diem successfully");
		}
	}

	public void requestDiemFromAao(String mssv) {
		RequestDiemFromAaoAsynTask asynTask = new RequestDiemFromAaoAsynTask(
				mssv, this);
		asynTask.execute();
	}

	public void updateDataSource(DI__HocPhi lstDiem, List<String> objs) {
		// logService.functionTag("updateDiem", "Size is: " + lstmodel.size());
		model.setObjects(objs);
		model.setHocPhi(lstDiem);

		if (lstDiem == null) {
			return;
		}

		// for (int i = 0; i < lstDiem.size(); i++) {
		DI__HocPhi temp = lstDiem;

		// insert hh into database
		logService.functionTag("updateDataSource", "Insert into hocphi  for: "
				+ temp.mssv);
		ContentValues values = new ContentValues();

		values.put("mssv", temp.mssv);
		values.put("namhoc", temp.namhoc);
		values.put("hocky", temp.hocky);

		values.put("hptong", temp.totalFee);
		values.put("hpno", temp.owedFee);
		values.put("ngaycapnhat", temp.updateDay);

		try {
			int affected = database.update("hocphi", values, "mssv = '"
					+ temp.mssv + "' AND namhoc = " + temp.namhoc
					+ " AND hocky = " + temp.hocky, null);
			if (affected == 0)
				database.insert("hocphi", null, values);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// insert hocky entry into database
		if (model.hocky.namhoc == 0) {
			temp = lstDiem;

			values = new ContentValues();

			values.put("mssv", temp.mssv);
			values.put("namhoc", model.hocky.namhoc);
			values.put("hocky", model.hocky.hk);

			// logService.functionTag("updateDiem", "objs.size() = " +
			// objs.size());

			int lastestNienHoc = model.hocky.hk * 10 + model.hocky.hk;
			
			values.put("hocphistt", Integer.toString(lastestNienHoc));

			try {
				// int affected = database.update("hocky", values,
				// "mssv = '?' AND namhoc = '?' AND hocky = '?'", new String[]
				// {temp.mssv, temp.namhoc + "", temp.hocky + ""});
				int affected = database.update("hocky", values, "mssv = '"
						+ temp.mssv + "' AND namhoc = " + model.hocky.namhoc
						+ " AND hocky = " + model.hocky.hk, null);
				if (affected == 0)
					database.insert("hocky", null, values);

				logService.functionTag(
						"updateDiem",
						"Insert into hocky hocky = " + model.hocky.namhoc
								+ model.hocky.hk + " diemstt = "
								+ Integer.toString(lastestNienHoc) + " for: "
								+ temp.mssv);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		DialogService.closeProgressDialog();
	}

	class RequestDiemFromAaoAsynTask extends AsyncTask<Void, Void, Void> {
		private String mssv;
		private HocPhiController ltController;
		private DI__HocPhi lstDiem;
		private List<String> objs;

		public RequestDiemFromAaoAsynTask(String mssv,
				HocPhiController ltController) {
			this.mssv = mssv;
			this.ltController = ltController;
			this.objs = new ArrayList<String>();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// logService.functionTag("doInBackground", "AAOService.getDiem:"
			// + mssv + " " + Integer.toString(ltController.diem.hocky.namhoc *
			// 10 + ltController.diem.hocky.hk));
			lstDiem = AAOService.getHocPhi(
					mssv,
					Integer.toString(ltController.model.hocky.namhoc * 10
							+ ltController.model.hocky.hk), objs);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// logService.functionTag("onPostExecute", "AAOService.getDiem:"
			// + mssv + " done");
			ltController.updateDataSource(lstDiem, objs);
		}

	}

}
