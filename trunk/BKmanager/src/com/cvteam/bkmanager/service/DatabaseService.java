package com.cvteam.bkmanager.service;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.cvteam.bkmanager.controller.IDataSource;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

public class DatabaseService extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "bkmanager.db";

	private static final int DATABASE_VERSION = 2;

	private static final String CREATE_TABLE_LICHTHI = "create table lichthi ("
			+ "_id integer primary key autoincrement, "
			+ "mssv text not null, " + "namhoc integer not null, "
			+ "hocky integer not null, " + "mamh text not null, "
			+ "tenmh text not null, " + "nhomto text not null, "
			+ "ngaygk text, " + "tietgk integer, " + "phonggk text, "
			+ "ngayck text, " + "tietck integer, " + "phongck text, "
			+ "evenGkId integer, " + "evenCkId integer);";

	private static final String CREATE_TABLE_THOIKHOABIEU = "create table thoikhoabieu ("
			+ "_id integer primary key autoincrement, "
			+ "mssv text not null, "
			+ "namhoc integer not null, "
			+ "hocky integer not null, "
			+ "mamh text not null, "
			+ "tenmh text not null, "
			+ "nhomto text not null, "
			+ "thu1 text, "
			+ "tiet1 integer, "
			+ "phong1 text, "
			+ "thu2 text, "
			+ "tiet2 integer, "
			+ "phong2 text, "
			+ "notice text);";

	private static final String CREATE_TABLE_DIEM = "create table diem ("
			+ "_id integer primary key autoincrement, "
			+ "mssv text not null, " + "namhoc integer not null, "
			+ "hocky integer not null, " + "mamh text not null, "
			+ "tenmh text not null, " + "nhomto text not null, "
			+ "sotc integer, " + "diemkt real, " + "diemthi real, "
			+ "diemtk real);";

	private static final String CREATE_TABLE_HOCKY = "create table hocky ("
			+ "_id integer primary key autoincrement, "
			+ "mssv text not null, " + "namhoc integer not null, "
			+ "hocky integer not null, " + "tcdkhk integer, "
			+ "tctlhk integer, " + "tongsotc integer, " + "diemtbhk real, "
			+ "diemtbtl real, " + "lichthistt text, " + "diemstt text, "
			+ "tkbstt text);";

	public DatabaseService(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_TABLE_HOCKY);
		database.execSQL(CREATE_TABLE_DIEM);
		database.execSQL(CREATE_TABLE_LICHTHI);
		database.execSQL(CREATE_TABLE_THOIKHOABIEU);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS hocky");
		db.execSQL("DROP TABLE IF EXISTS diem");
		db.execSQL("DROP TABLE IF EXISTS lichthi");
		db.execSQL("DROP TABLE IF EXISTS thoikhoabieu");
		onCreate(db);
	}

	public void selectTable(String table, Map<String, String> params,
			IDataSource ds) {
		SQLiteDatabase database = this.getReadableDatabase();
		LoadGenericTask lgt = new LoadGenericTask(table, params, database, ds);
		lgt.execute();
	}

	class LoadGenericTask extends AsyncTask<Void, Void, Void> {
		private Map<String, String> params;
		private String table;
		private SQLiteDatabase database;
		private Cursor cursor;
		private IDataSource ds;

		public LoadGenericTask(String table, Map<String, String> params,
				SQLiteDatabase database, IDataSource ds) {
			this.params = params;
			this.table = table;
			this.database = database;
			this.ds = ds;
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				StringBuilder sql = new StringBuilder("SELECT * FROM " + table);
				if (this.params != null) {
					sql.append(" WHERE ");
					Set<Entry<String, String>> entries = this.params.entrySet();

					Iterator<Entry<String, String>> it = entries.iterator();
					while (it.hasNext()) {
						Entry<String, String> entry = it.next();
						sql.append(entry.getKey() + " = '" + entry.getValue()
								+ "'");
						if (it.hasNext())
							sql.append(" AND ");
					}
				}
				cursor = database.rawQuery(sql.toString(), null);
			} catch (Exception e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			ds.updateDataSource(cursor);
		}
	}
}
