package com.cvteam.bkmanager.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.cvteam.bkmanager.MainActivity;
import com.cvteam.bkmanager.R;
import com.cvteam.bkmanager.model.DI__Diem;
import com.cvteam.bkmanager.model.DI__LichThi;
import com.cvteam.bkmanager.model.DI__NienHoc;
import com.cvteam.bkmanager.model.DI__ThoiKhoaBieu;

public class UpdateService extends BroadcastReceiver {
	// private LogService logService = new LogService("UpdateService");

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("onReceive()");
		// System.out.println("thread " + Thread.currentThread().getId());
		// Toast.makeText(context, "Checking BKnoti", Toast.LENGTH_LONG).show();

		// Init setting
		if (intent == null)
			return;
		String mssv = intent.getStringExtra("mssv");
		if (mssv.equals(""))
			return;
		int dongbo = intent.getIntExtra("dongbo", 0);
		Boolean notifications_lich_thi = intent.getBooleanExtra("noti_lichthi", true);
		Boolean notifications_tkb = intent.getBooleanExtra("noti_diem", true);
		Boolean notifications_diem = intent.getBooleanExtra("noti_tkb", true);
		Boolean notifications_hoc_phi = intent.getBooleanExtra("noti_hocphi", true);

		UpdateAsynTask asynTask = new UpdateAsynTask(context, mssv, dongbo, notifications_lich_thi,
				notifications_tkb, notifications_diem, notifications_hoc_phi);
		asynTask.execute();
	}

	public void createNewNotification(Context context, String title, String contentText,
			Class<?> desActivityClass) {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
		mBuilder.setSmallIcon(R.drawable.ic_launcher);
		mBuilder.setContentTitle(title);
		mBuilder.setContentText(contentText);

		Intent resultIntent = new Intent(context, desActivityClass);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(desActivityClass);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(1, mBuilder.build());
	}

	class UpdateAsynTask extends AsyncTask<Void, Void, Void> {
		private Context context;
		private String mssv;
		private int dongbo;
		private Boolean notifications_lich_thi;
		private Boolean notifications_tkb;
		private Boolean notifications_diem;
		private Boolean notifications_hoc_phi;

		private DatabaseService databaseService;
		private SQLiteDatabase database;

		private List<DI__LichThi> lichThiNew;
		private List<String> lichThiObjs;
		private List<DI__LichThi> lichThiCurr;
		private String lichthisttCurr;
		private String lichthisttNew;

		private List<DI__Diem> diemNew;
		private List<Object> diemObjs;
		private List<DI__Diem> diemCurr;
		private String diemsttCurr;
		private String diemsttNew;

		private List<DI__ThoiKhoaBieu> tkbNew;
		private List<String> tkbObjs;
		private List<DI__ThoiKhoaBieu> tkbCurr;
		private String tkbsttCurr;
		private String tkbsttNew;

		private Map<String, String> params;

		private Boolean isNewLichThi, isNewDiem, isNewTKB;

		public UpdateAsynTask(Context context, String mssv, int dongbo,
				Boolean notifications_lich_thi, Boolean notifications_tkb,
				Boolean notifications_diem, Boolean notifications_hoc_phi) {
			// System.out.println("thread " + Thread.currentThread().getId());
			this.context = context;
			this.mssv = mssv;
			this.dongbo = dongbo;
			this.notifications_lich_thi = notifications_lich_thi;
			this.notifications_diem = notifications_diem;
			this.notifications_tkb = notifications_tkb;
			this.notifications_hoc_phi = notifications_hoc_phi;

			isNewLichThi = isNewDiem = isNewTKB = false;
		}

		@Override
		protected Void doInBackground(Void... pa) {
			// logService.functionTag("doInBackground",
			// "UpdateService doInBackground:" + mssv);
			// System.out.println("thread " + Thread.currentThread().getId());

			// Get new lich thi from aao
			lichThiObjs = new ArrayList<String>();
			lichThiNew = new ArrayList<DI__LichThi>();
			if (notifications_lich_thi)
				lichThiNew = AAOService.getLichThi(mssv, "", lichThiObjs);

			diemObjs = new ArrayList<Object>();
			diemNew = new ArrayList<DI__Diem>();
			if (notifications_diem)
				diemNew = AAOService.getDiem(mssv, "", diemObjs);

			tkbObjs = new ArrayList<String>();
			tkbNew = new ArrayList<DI__ThoiKhoaBieu>();
			if (notifications_tkb)
				tkbNew = AAOService.getTKB(mssv, "", tkbObjs);

			lichthisttNew = lichThiNew.size() > 0 && lichThiObjs.size() > 0 ? lichThiObjs.get(0)
					: "";
			diemsttNew = diemNew.size() > 0 && diemObjs.size() > 0 ? diemObjs.get(0).toString()
					: "";
			tkbsttNew = tkbNew.size() > 0 && tkbObjs.size() > 0 ? tkbObjs.get(0) : "";
			// System.out.println("lichthisttNew " + lichthisttNew);
			// System.out.println("diemsttNew " + diemsttNew);
			// System.out.println("tkbsttNew " + tkbsttNew);

			// init db
			databaseService = new DatabaseService(context);
			open();

			List<DI__NienHoc> nienHocs = new ArrayList<DI__NienHoc>();
			try {
				nienHocs = AAOService.refreshListNienHoc();
				if (nienHocs.size() > 0) {
					int namhoc = nienHocs.get(0).namhoc;
					int hocky = nienHocs.get(0).hk;

					params = new HashMap<String, String>();
					params.put("mssv", mssv);
					params.put("namhoc", namhoc + "");
					params.put("hocky", hocky + "");

					if (!getHocKy()) {
						// System.out.println("getHocKy fail");
					} else {
						if (notifications_lich_thi && lichThiNew.size() > 0)
							updateLichThi();
						if (notifications_diem && diemNew.size() > 0)
							updateDiem();
						if (notifications_tkb && tkbNew.size() > 0)
							updateTKB();
					}
				}
			} catch (Exception e) {
			}

			close();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// logService.functionTag("onPostExecute",
			// "UpdateService onPostExecute:" + mssv + " done");

			if (isNewLichThi)
				createNewNotification(context, "BKnoti", "Ä�Ã£ cÃ³ lá»‹ch thi má»›i",
						MainActivity.class);
			if (isNewDiem)
				createNewNotification(context, "BKnoti", "Ä�Ã£ cÃ³ Ä‘iá»ƒm má»›i",
						MainActivity.class);
			if (isNewTKB)
				createNewNotification(context, "BKnoti", "Ä�Ã£ cÃ³ tkb má»›i", MainActivity.class);
		}

		public void open() throws SQLException {
			database = databaseService.getWritableDatabase();
		}

		public void close() {
			databaseService.close();
		}

		private Cursor selectTable(String table, Map<String, String> params) {
			// logService.functionTag("selectTable", "Select table " + table);
			try {
				StringBuilder sql = new StringBuilder("SELECT * FROM " + table);
				if (params != null) {
					sql.append(" WHERE ");
					Set<Entry<String, String>> entries = params.entrySet();

					Iterator<Entry<String, String>> it = entries.iterator();
					while (it.hasNext()) {
						Entry<String, String> entry = it.next();
						sql.append(entry.getKey() + " = '" + entry.getValue() + "'");
						if (it.hasNext())
							sql.append(" AND ");
					}
				}
				Cursor cursor = database.rawQuery(sql.toString(), null);
				// logService.functionTag("selectTable", "Select table " + table
				// + ", row count: " + cursor.getCount());
				return cursor;
			} catch (Exception e) {
				// logService.functionTag("selectTable", "Select table " + table
				// + " throws exception: " + e.getMessage());
			}
			return null;
		}

		public void updateDataSourceLichThi(List<DI__LichThi> lichThiNew, List<String> objs) {
			// logService.functionTag("updateLichThi", "Set new list. Size = " +
			// lichThiNew.size());

			if (lichThiNew.size() == 0)
				return;

			for (int i = 0; i < lichThiNew.size(); i++) {
				DI__LichThi temp = lichThiNew.get(i);

				long evenGkId = -1;
				long evenCkId = -1;

				// create a Calendar event for mid-term exam at 6am in exam day
				// and
				// logService.functionTag("updateLichThi",
				// "Create event mamonhoc = " + temp.mamh + " for: " +
				// temp.mssv);
				Calendar eventTime;
				int day, month;

				if (dongbo == 1) {
					if (temp.tietgk != 0) {
						// set alarm 1 day before
						eventTime = Calendar.getInstance();
						day = Integer.parseInt(temp.ngaygk.substring(0, 2));
						month = Integer.parseInt(temp.ngaygk.substring(3)) - 1;
						eventTime.set(eventTime.get(Calendar.YEAR), month, day, 6, 0, 0);

						if (Calendar.getInstance().getTimeInMillis() < eventTime.getTimeInMillis()) {
							evenGkId = CalendarService.NewEvent(context, "Thi giua hoc ky"
									+ temp.namhoc + temp.hocky + " mon " + temp.tenmh, "MSSV: "
									+ temp.mssv + ". Tiet " + temp.tietgk, temp.phonggk, eventTime,
									eventTime, 24 * 60);
						}
					}
					// create a Calendar event for final exam at 6am in exam day
					// and
					if (temp.tietck != 0) {
						// set alarm 1 day before
						eventTime = Calendar.getInstance();
						day = Integer.parseInt(temp.ngayck.substring(0, 2));
						month = Integer.parseInt(temp.ngayck.substring(3)) - 1;
						eventTime.set(eventTime.get(Calendar.YEAR), month, day, 6, 0, 0);

						if (Calendar.getInstance().getTimeInMillis() < eventTime.getTimeInMillis()) {
							evenCkId = CalendarService.NewEvent(context, "Thi cuá»‘i ká»³ "
									+ temp.namhoc + temp.hocky + " mÃ´n " + temp.tenmh, "MSSV: "
									+ temp.mssv + ". Tiáº¿t " + temp.tietck, temp.phongck,
									eventTime, eventTime, 24 * 60);
						}
					}
				}

				// logService.functionTag("updateDataSource", "Events created");

				// insert lichthi into database
				// logService.functionTag("updateLichThi",
				// "Insert into lichthi mamonhoc = " + temp.mamh + " for: " +
				// temp.mssv);
				ContentValues values = new ContentValues();

				values.put("mssv", temp.mssv);
				values.put("namhoc", temp.namhoc);
				values.put("hocky", temp.hocky);
				values.put("mamh", temp.mamh);
				values.put("tenmonhoc", temp.tenmh);
				values.put("nhomto", temp.nhomto);

				values.put("ngaygk", temp.ngaygk);
				values.put("tietgk", temp.tietgk);
				values.put("phonggk", temp.phonggk);

				values.put("ngayck", temp.ngayck);
				values.put("tietck", temp.tietck);
				values.put("phongck", temp.phongck);

				values.put("evenGkId", evenGkId);
				values.put("evenCkId", evenCkId);

				try {
					int affected = database.update("lichthi", values, "mssv = '" + temp.mssv
							+ "' AND namhoc = " + temp.namhoc + " AND hocky = " + temp.hocky
							+ " AND mamh = " + temp.mamh, null);
					if (affected == 0)
						database.insert("lichthi", null, values);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			// insert hocky entry into database
			DI__LichThi temp = lichThiNew.get(0);

			// logService.functionTag("updateHocKy",
			// "Insert into hocky hocky = " + temp.namhoc + temp.hocky +
			// " lichthistt = " + objs.get(0) + " for: " + temp.mssv);

			ContentValues values = new ContentValues();

			values.put("mssv", temp.mssv);
			values.put("namhoc", temp.namhoc);
			values.put("hocky", temp.hocky);
			values.put("lichthistt", objs.get(0));

			try {
				int affected = database.update("hocky", values, "mssv = '" + temp.mssv
						+ "' AND namhoc = " + temp.namhoc + " AND hocky = " + temp.hocky, null);
				if (affected == 0)
					database.insert("hocky", null, values);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		public void updateDataSourceDiem(List<DI__Diem> diemNew, List<Object> objs) {
			// logService.functionTag("updateDiem", "Size is: " +
			// diemNew.size());
			if (diemNew.size() == 0)
				return;

			for (int i = 0; i < diemNew.size(); i++) {
				DI__Diem temp = diemNew.get(i);

				// insert diem into database
				// logService.functionTag("updateDataSource",
				// "Insert into diem mamonhoc = " + temp.mamonhoc + " for: " +
				// temp.mssv);
				ContentValues values = new ContentValues();

				values.put("mssv", temp.mssv);
				values.put("namhoc", temp.namhoc);
				values.put("hocky", temp.hocky);

				values.put("mamh", temp.mamh);
				values.put("tenmonhoc", temp.tenmh);
				values.put("nhomto", temp.nhomto);
				values.put("sotc", temp.sotc);

				values.put("diemkiemtra", temp.diemkt);
				values.put("diemthi", temp.diemthi);
				values.put("diemtk", temp.diemtk);

				try {
					int affected = database.update("diem", values, "mssv = '" + temp.mssv
							+ "' AND namhoc = " + temp.namhoc + " AND hocky = " + temp.hocky
							+ " AND mamh = " + temp.mamh, null);
					if (affected == 0)
						database.insert("diem", null, values);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			// insert hocky entry into database
			DI__Diem temp = diemNew.get(0);

			// logService.functionTag("updateDiem", "Insert into hocky hocky = "
			// + temp.namhoc + temp.hocky + " diemstt = " + objs.get(0) +
			// " for: " + temp.mssv);

			ContentValues values = new ContentValues();

			values.put("mssv", temp.mssv);
			values.put("namhoc", temp.namhoc);
			values.put("hocky", temp.hocky);

			// logService.functionTag("updateDiem", "objs.size() = " +
			// objs.size());

			if (objs.size() > 1)
				values.put("tcdkhk", Integer.parseInt(objs.get(1).toString().trim()));
			if (objs.size() > 2)
				values.put("tctlhk", Integer.parseInt(objs.get(2).toString().trim()));
			if (objs.size() > 3)
				values.put("tongsotc", Integer.parseInt(objs.get(3).toString().trim()));
			if (objs.size() > 4)
				values.put("diemtbhk", Double.parseDouble(objs.get(4).toString().trim()));
			if (objs.size() > 5)
				values.put("diemtbtl", Double.parseDouble(objs.get(5).toString().trim()));
			values.put("diemstt", objs.get(0).toString());

			try {
				// int affected = database.update("hocky", values,
				// "mssv = '?' AND namhoc = '?' AND hocky = '?'", new String[]
				// {temp.mssv, temp.namhoc + "", temp.hocky + ""});
				int affected = database.update("hocky", values, "mssv = '" + temp.mssv
						+ "' AND namhoc = " + temp.namhoc + " AND hocky = " + temp.hocky, null);
				if (affected == 0)
					database.insert("hocky", null, values);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		public void updateDataSourceTKB(List<DI__ThoiKhoaBieu> tkbNew, List<String> objs) {
			// logService.functionTag("updateDataSource",
			// "Set new list for TKB Model. Size = " + tkbNew.size());
			if (tkbNew.size() == 0)
				return;

			for (int i = 0; i < tkbNew.size(); i++) {
				DI__ThoiKhoaBieu temp = tkbNew.get(i);

				// insert tkb into database
				// logService.functionTag("updateDataSource",
				// "Insert into thoikhoabieu mamonhoc = " + temp.mamh + " for: "
				// + temp.mssv);
				ContentValues values = new ContentValues();

				values.put("mssv", temp.mssv);
				values.put("namhoc", temp.namhoc);
				values.put("hocky", temp.hocky);

				values.put("mamh", temp.mamh);
				values.put("tenmonhoc", temp.tenmh);
				values.put("nhomto", temp.nhomto);

				values.put("thu1", temp.thu1);
				values.put("tiet1", temp.tiet1);
				values.put("phong1", temp.phong1);

				values.put("thu2", temp.thu2);
				values.put("tiet2", temp.tiet2);
				values.put("phong2", temp.phong2);

				values.put("notice", temp.notice);

				try {
					int affected = database.update("thoikhoabieu", values, "mssv = '" + temp.mssv
							+ "' AND namhoc = " + temp.namhoc + " AND hocky = " + temp.hocky
							+ " AND mamh = " + temp.mamh, null);
					if (affected == 0)
						database.insert("thoikhoabieu", null, values);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			// insert hocky entry into database
			DI__ThoiKhoaBieu temp = tkbNew.get(0);

			// logService.functionTag("updateDataSource",
			// "Insert into hocky hocky = " + temp.namhoc + temp.hocky +
			// " tkbstt = " + objs.get(0) + " for: " + temp.mssv);

			ContentValues values = new ContentValues();

			values.put("mssv", temp.mssv);
			values.put("namhoc", temp.namhoc);
			values.put("hocky", temp.hocky);
			values.put("tkbstt", objs.get(0));

			try {
				int affected = database.update("hocky", values, "mssv = '" + temp.mssv
						+ "' AND namhoc = " + temp.namhoc + " AND hocky = " + temp.hocky, null);
				if (affected == 0)
					database.insert("hocky", null, values);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		private ContentValues cursorToHocKy(Cursor cursor) {
			// logService.functionTag("cursorToHocKy", "cursor to hocky");
			if (cursor == null)
				return null;

			ContentValues statusValues = new ContentValues();
			statusValues.put("lichthistt", cursor.getString(9));
			statusValues.put("diemstt", cursor.getString(10));
			statusValues.put("tkbstt", cursor.getString(11));

			return statusValues;
		}

		private DI__LichThi cursorToLichThi(Cursor cursor) {
			// logService.functionTag("cursorToLichThi", "cursor to lichthi");
			DI__LichThi lt = new DI__LichThi();
			if (cursor == null)
				return lt;

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
			lt.eventgk = cursor.getLong(13);
			lt.eventck = cursor.getLong(14);
			return lt;
		}

		private DI__Diem cursorToDiem(Cursor cursor) {
			// logService.functionTag("cursorToLichThi", "cursor to lichthi");
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

		private DI__ThoiKhoaBieu cursorToTKB(Cursor cursor) {
			// logService.functionTag("cursorToLichThi", "cursor to tkb");
			if (cursor == null)
				return new DI__ThoiKhoaBieu();
			DI__ThoiKhoaBieu tkb = new DI__ThoiKhoaBieu();

			tkb.mssv = cursor.getString(1);
			tkb.namhoc = cursor.getInt(2);
			tkb.hocky = cursor.getInt(3);

			tkb.mamh = cursor.getString(4);
			tkb.tenmh = cursor.getString(5);
			tkb.nhomto = cursor.getString(6);

			tkb.thu1 = cursor.getInt(7);
			tkb.tiet1 = cursor.getString(8);
			tkb.phong1 = cursor.getString(9);

			tkb.thu2 = cursor.getInt(10);
			tkb.tiet2 = cursor.getString(11);
			tkb.phong2 = cursor.getString(12);

			tkb.notice = cursor.getString(13);
			return tkb;
		}

		private Boolean getHocKy() {
			// logService.functionTag("getAllHocKy", "Select all hoc ky where "
			// + params.toString());
			Cursor cursor = selectTable("hocky", params);
			if (cursor == null) {
				return false;
			} else {
				ContentValues statusValues = null;
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					statusValues = cursorToHocKy(cursor);
					cursor.moveToNext();
				}
				cursor.close();
				if (statusValues == null) {
					return false;
				} else {
					lichthisttCurr = statusValues.getAsString("lichthistt");
					diemsttCurr = statusValues.getAsString("diemstt");
					tkbsttCurr = statusValues.getAsString("tkbstt");

					// System.out.println("lichthistt " + lichthisttCurr +
					// " - ngayCapNhapLichThi " + lichthisttNew);
					// System.out.println("diemstt " + diemsttCurr +
					// " - ngayCapNhapDiem " + diemsttNew);
					// System.out.println("tkbstt " + tkbsttCurr +
					// " - ngayCapNhapTKB " + tkbsttNew);

					return true;
				}
			}
		}

		private void updateLichThi() {
			if (lichthisttCurr.equals(lichthisttNew) || lichthisttNew.equals("")) {
				return;
			}

			lichThiCurr = new ArrayList<DI__LichThi>();
			Cursor cursor = selectTable("lichthi", params);
			if (cursor == null)
				return;
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				lichThiCurr.add(cursorToLichThi(cursor));
				cursor.moveToNext();
			}
			cursor.close();

			// delete old event
			for (int i = 0; i < lichThiCurr.size(); i++) {
				DI__LichThi temp = lichThiCurr.get(i);
				CalendarService.DeleteEvent(context, temp.eventgk);
				CalendarService.DeleteEvent(context, temp.eventck);
			}

			updateDataSourceLichThi(lichThiNew, lichThiObjs);
			// System.out.println("New Lich thi" + lichThiObjs.get(0));
			isNewLichThi = true;
		}

		private void updateDiem() {
			if (diemsttCurr.equals(diemsttNew) || diemsttNew.equals("")) {
				return;
			}

			diemCurr = new ArrayList<DI__Diem>();
			Cursor cursor = selectTable("diem", params);
			if (cursor == null)
				return;
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				diemCurr.add(cursorToDiem(cursor));
				cursor.moveToNext();
			}
			cursor.close();

			updateDataSourceDiem(diemNew, diemObjs);
			// System.out.println("New Diem" + diemObjs.get(0));
			isNewDiem = true;
		}

		private void updateTKB() {
			if (tkbsttCurr.equals(tkbsttNew) || tkbsttNew.equals("")) {
				return;
			}

			tkbCurr = new ArrayList<DI__ThoiKhoaBieu>();
			Cursor cursor = selectTable("thoikhoabieu", params);
			if (cursor == null)
				return;
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				tkbCurr.add(cursorToTKB(cursor));
				cursor.moveToNext();
			}
			cursor.close();

			updateDataSourceTKB(tkbNew, tkbObjs);
			// System.out.println("New tkb" + tkbObjs.get(0));
			isNewTKB = true;
		}
	}
}
