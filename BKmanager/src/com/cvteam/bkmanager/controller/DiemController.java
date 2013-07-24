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
import com.cvteam.bkmanager.model.DI__NienHoc;
import com.cvteam.bkmanager.model.DiemModel;
import com.cvteam.bkmanager.service.AAOService;
import com.cvteam.bkmanager.service.DatabaseService;
import com.cvteam.bkmanager.service.LogService;

public class DiemController implements IDataSource {

	private LogService logService = new LogService("DiemController");

	private SQLiteDatabase database;
	private DatabaseService databaseService;

	private DiemModel diem;
	public List<DI__HocKy> hockys;

	public void setModel(DiemModel diem) {
		this.diem = diem;
	}

	public DiemController(Context context) {
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

		// logService.functionTag("getByHocKy", "Select all diem, mssv = " +
		// diem.mssv);
		Map<String, String> params = new HashMap<String, String>();
		params.put("mssv", diem.mssv);

		diem.hocky.namhoc = namhoc;
		diem.hocky.hk = hocky;

		if (namhoc != 0 && hocky != 0) {
			params.put("namhoc", namhoc + "");
			params.put("hocky", hocky + "");

			logService.functionTag("getByHocKy",
					Integer.toString(diem.hocky.namhoc * 10 + diem.hocky.hk));

			databaseService.selectTable("diem", params, this);
		} else {
			// case all
			StringBuilder sql = new StringBuilder(
					"SELECT diemstt FROM hocky WHERE mssv ='");
			sql.append(diem.mssv);
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
					databaseService.selectTable("diem", params, this);
				else {
					requestDiemFromAao(diem.mssv);
				}
			} else {
				requestDiemFromAao(diem.mssv);
			}
		}
	}

	private DI__Diem cursorToDiem(Cursor cursor) {
		// logService.functionTag("cursorToDiem", "cursor to model");
		if (cursor == null)
			return new DI__Diem();
		DI__Diem diem = new DI__Diem();
		diem.mssv = cursor.getString(1);
		diem.namhoc = cursor.getInt(2);
		diem.hocky = cursor.getInt(3);
		diem.mamh = cursor.getString(4);
		diem.tenmh = cursor.getString(5);
		diem.nhomto = cursor.getString(6);
		diem.sotc = cursor.getInt(7);
		diem.diemkt = cursor.getFloat(8);
		diem.diemthi = cursor.getFloat(9);
		diem.diemtk = cursor.getFloat(10);
		return diem;
	}

	@Override
	public void updateDataSource(Object obj) {
		// logService.functionTag("UpdateDataSource", "Update model Diem");
		List<DI__Diem> diems = new ArrayList<DI__Diem>();
		if (obj != null) {
			Cursor cursor = (Cursor) obj;
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				diems.add(cursorToDiem(cursor));
				cursor.moveToNext();
			}
			cursor.close();
		}
		// logService.functionTag("UpdateDataSource",
		// "Update model Diem successfully");
		if (diems.size() == 0) {
			requestDiemFromAao(diem.mssv);
		} else {
			StringBuilder sql = new StringBuilder(
					"SELECT tcdkhk, tctlhk, tongsotc, diemtbhk, diemtbtl FROM hocky WHERE mssv ='");
			sql.append(diem.mssv);
			sql.append("' AND namhoc = ");
			sql.append(diem.hocky.namhoc);
			sql.append(" AND hocky = ");
			sql.append(diem.hocky.hk);
			logService.functionTag("updateDataSource", sql.toString());
			Cursor csr = database.rawQuery(sql.toString(), null);
			if (csr != null) {
				csr.moveToFirst();
				List<Object> objs = new ArrayList<Object>();
				objs.add("");
				// tcdkhk
				if (csr.getInt(0) > 0)
					objs.add(csr.getInt(0));
				// tctlhk
				if (csr.getInt(1) > 0)
					objs.add(csr.getInt(1));
				// tongsotc
				if (csr.getInt(2) > 0)
					objs.add(csr.getInt(2));
				// diemtbhk
				if (csr.getFloat(3) > 0)
					objs.add(csr.getFloat(3));
				// diemtbtl
				if (csr.getFloat(4) > 0)
					objs.add(csr.getFloat(4));

				diem.setObjects(objs);

				csr.close();
			}
			diem.setDiems(diems);
			// logService.functionTag("UpdateDataSource",
			// "Update model Diem successfully");
		}
	}

	public void requestDiemFromAao(String mssv) {
		RequestDiemFromAaoAsynTask asynTask = new RequestDiemFromAaoAsynTask(
				mssv, this);
		asynTask.execute();
	}

	public void updateDataSource(List<DI__Diem> lstDiem, List<Object> objs) {
		// logService.functionTag("updateDiem", "Size is: " + lstDiem.size());
		diem.setObjects(objs);
		diem.setDiems(lstDiem);

		if (lstDiem.size() == 0)
			return;

		for (int i = 0; i < lstDiem.size(); i++) {
			DI__Diem temp = lstDiem.get(i);

			// insert diem into database
			logService.functionTag("updateDataSource",
					"Insert into diem mamh = " + temp.mamh + " for: "
							+ temp.mssv);
			ContentValues values = new ContentValues();

			values.put("mssv", temp.mssv);
			values.put("namhoc", temp.namhoc);
			values.put("hocky", temp.hocky);

			values.put("mamh", temp.mamh);
			values.put("tenmh", temp.tenmh);
			values.put("nhomto", temp.nhomto);
			values.put("sotc", temp.sotc);

			values.put("diemkt", temp.diemkt);
			values.put("diemthi", temp.diemthi);
			values.put("diemtk", temp.diemtk);

			try {
				int affected = database.update("diem", values, "mssv = '"
						+ temp.mssv + "' AND namhoc = " + temp.namhoc
						+ " AND hocky = " + temp.hocky + " AND mamh = '"
						+ temp.mamh + "'", null);
				if (affected == 0)
					database.insert("diem", null, values);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		ContentValues values;

		// for all hocky
		int currentNamhoc = 0;
		int currentHocky = 0;
		int lastestNienHoc = 0;
		for (int i = 0; i < lstDiem.size(); i++) {
			if (currentHocky != lstDiem.get(i).hocky
					|| currentNamhoc != lstDiem.get(i).namhoc) {
				currentHocky = lstDiem.get(i).hocky;
				currentNamhoc = lstDiem.get(i).namhoc;

				if (lastestNienHoc == 0)
					lastestNienHoc = currentNamhoc * 10 + currentHocky;

				values = new ContentValues();

				values.put("mssv", lstDiem.get(i).mssv);
				values.put("namhoc", currentNamhoc);
				values.put("hocky", currentHocky);

				// logService.functionTag("updateDiem", "objs.size() = " +
				// objs.size());

				if (objs.size() > 1)
					values.put("tcdkhk",
							Integer.parseInt(objs.get(1).toString().trim()));
				if (objs.size() > 2)
					values.put("tctlhk",
							Integer.parseInt(objs.get(2).toString().trim()));
				if (objs.size() > 3)
					values.put("tongsotc",
							Integer.parseInt(objs.get(3).toString().trim()));
				if (objs.size() > 4)
					values.put("diemtbhk",
							Double.parseDouble(objs.get(4).toString().trim()));
				if (objs.size() > 5)
					values.put("diemtbtl",
							Double.parseDouble(objs.get(5).toString().trim()));
				values.put("diemstt", objs.get(0).toString());

				try {
					// int affected = database.update("hocky", values,
					// "mssv = '?' AND namhoc = '?' AND hocky = '?'", new
					// String[] {temp.mssv, temp.namhoc + "", temp.hocky + ""});
					int affected = database.update("hocky", values, "mssv = '"
							+ lstDiem.get(i).mssv + "' AND namhoc = "
							+ currentNamhoc + " AND hocky = " + currentHocky,
							null);
					if (affected == 0)
						database.insert("hocky", null, values);

					logService.functionTag(
							"updateDiem",
							"Insert into hocky hocky = " + currentNamhoc
									+ currentHocky + " diemstt = "
									+ objs.get(0) + " for: "
									+ lstDiem.get(i).mssv);

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		// insert hocky entry into database
		if (diem.hocky.namhoc == 0) {
			DI__Diem temp = lstDiem.get(0);

			values = new ContentValues();

			values.put("mssv", temp.mssv);
			// values.put("namhoc", temp.namhoc);
			// values.put("hocky", temp.hocky);
			values.put("namhoc", diem.hocky.namhoc);
			values.put("hocky", diem.hocky.hk);

			// logService.functionTag("updateDiem", "objs.size() = " +
			// objs.size());

			if (objs.size() > 1)
				values.put("tcdkhk",
						Integer.parseInt(objs.get(1).toString().trim()));
			if (objs.size() > 2)
				values.put("tctlhk",
						Integer.parseInt(objs.get(2).toString().trim()));
			if (objs.size() > 3)
				values.put("tongsotc",
						Integer.parseInt(objs.get(3).toString().trim()));
			if (objs.size() > 4)
				values.put("diemtbhk",
						Double.parseDouble(objs.get(4).toString().trim()));
			if (objs.size() > 5)
				values.put("diemtbtl",
						Double.parseDouble(objs.get(5).toString().trim()));
			// values.put("diemstt", objs.get(0).toString());
			values.put("diemstt", Integer.toString(lastestNienHoc));

			try {
				// int affected = database.update("hocky", values,
				// "mssv = '?' AND namhoc = '?' AND hocky = '?'", new String[]
				// {temp.mssv, temp.namhoc + "", temp.hocky + ""});
				int affected = database.update("hocky", values, "mssv = '"
						+ temp.mssv + "' AND namhoc = " + diem.hocky.namhoc
						+ " AND hocky = " + diem.hocky.hk, null);
				if (affected == 0)
					database.insert("hocky", null, values);

				logService.functionTag(
						"updateDiem",
						"Insert into hocky hocky = " + diem.hocky.namhoc
								+ diem.hocky.hk + " diemstt = "
								+ Integer.toString(lastestNienHoc) + " for: "
								+ temp.mssv);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	class RequestDiemFromAaoAsynTask extends AsyncTask<Void, Void, Void> {
		private String mssv;
		private DiemController ltController;
		private List<DI__Diem> lstDiem;
		private List<Object> objs;

		public RequestDiemFromAaoAsynTask(String mssv,
				DiemController ltController) {
			this.mssv = mssv;
			this.ltController = ltController;
			this.objs = new ArrayList<Object>();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// logService.functionTag("doInBackground", "AAOService.getDiem:"
			// + mssv + " " + Integer.toString(ltController.diem.hocky.namhoc *
			// 10 + ltController.diem.hocky.hk));
			lstDiem = AAOService.getDiem(
					mssv,
					Integer.toString(ltController.diem.hocky.namhoc * 10
							+ ltController.diem.hocky.hk), objs);
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
