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
import com.cvteam.bkmanager.model.DI__HocKy;
import com.cvteam.bkmanager.model.DI__HocPhi;
import com.cvteam.bkmanager.model.DI__NienHoc;
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
			logService.functionTag("getByHocKy", sql.toString());
			Cursor csr = database.rawQuery(sql.toString(), null);
			if (csr != null && csr.getCount() > 0) {
				logService
						.functionTag("getByHocKy", "cursor is not null");
				csr.moveToFirst();
				String lastNienHoc = csr.getString(0);
				logService.functionTag("getByHocKy", "lastNienHoc = "
						+ lastNienHoc);
				csr.close();

				DI__NienHoc nh = MainActivity.nienHocModel.getHKs().get(2);
				logService.functionTag("getByHocKy",
						"MainActivity.nienHocModel.getHKs().get(2) = "
								+ nh.namhoc + "" + nh.hk);
				
				if (lastNienHoc!= null && lastNienHoc.equals(nh.namhoc + "" + nh.hk))
					databaseService.selectTable("hocphi", params, this);
				else {
				    requestHocPhiFromAao(model.mssv);
				}
			} else {
			    requestHocPhiFromAao(model.mssv);
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
	/**
	 * Update from DB
	 */
	public void updateDataSource(Object obj) {
		// logService.functionTag("UpdateDataSource", "Update model hocphi");
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
			requestHocPhiFromAao(model.mssv);
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

	public void requestHocPhiFromAao(String mssv) {
	    RequestHocPhiFromAaoAsynTask asynTask = new RequestHocPhiFromAaoAsynTask(
				mssv, this);
		asynTask.execute();
	}

	/**
	 * Update from AAO
	 * @param hp
	 * @param objs
	 */
	public void updateDataSource(DI__HocPhi hp, List<String> objs) {
		// logService.functionTag("updateDiem", "Size is: " + lstmodel.size());
		model.setObjects(objs);
		model.setHocPhi(hp);

		if (hp == null) {
			return;
		}

		// for (int i = 0; i < lstDiem.size(); i++) {
		DI__HocPhi temp = hp;

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

		// insert hocky entry into database for hocky(0, 0)
		if (model.hocky.namhoc == 0) {
			temp = hp;

			values = new ContentValues();

			values.put("mssv", temp.mssv);
			values.put("namhoc", model.hocky.namhoc);
			values.put("hocky", model.hocky.hk);

			int lastestNienHoc = temp.namhoc * 10 + temp.hocky;
			
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
						"updateDataSource",
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

	class RequestHocPhiFromAaoAsynTask extends AsyncTask<Void, Void, Void> {
		private String mssv;
		private HocPhiController controller;
		private DI__HocPhi hp;
		private List<String> objs;

		public RequestHocPhiFromAaoAsynTask(String mssv,
				HocPhiController controller) {
			this.mssv = mssv;
			this.controller = controller;
			this.objs = new ArrayList<String>();
		}

		@Override
		protected Void doInBackground(Void... params) {
			List<DI__NienHoc> nhs = null;
			DI__NienHoc nh = new DI__NienHoc(0, 0);
			
			try {
				nhs = AAOService.refreshListNienHocWithJSoup("http://www.aao.hcmut.edu.vn/php/aao_hp.php");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (nhs != null && nhs.size() > 2) {
				nh = nhs.get(2);				
			}
			
			 logService.functionTag("doInBackground", "AAOService.getHocPhi:"
			 + mssv + " " + nh.namhoc  + nh.hk);
			
			 hp = AAOService.getHocPhi(
					mssv,
					Integer.toString(nh.namhoc * 10
							+ nh.hk), objs);
			 
			 hp.namhoc = nh.namhoc;
			 hp.hocky = nh.hk;
			 
			 return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// logService.functionTag("onPostExecute", "AAOService.getDiem:"
			// + mssv + " done");
			System.out.println("objs.size() = " + objs.size());
			if (objs.size() > 0)
				System.out.println("objs.get(0) = " + objs.get(0));
			
			controller.updateDataSource(hp, objs);
		}

	}

}
