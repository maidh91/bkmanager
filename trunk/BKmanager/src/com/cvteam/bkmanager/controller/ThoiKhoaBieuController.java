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

import com.cvteam.bkmanager.model.DI__ThoiKhoaBieu;
import com.cvteam.bkmanager.model.ThoiKhoaBieuModel;
import com.cvteam.bkmanager.service.AAOService;
import com.cvteam.bkmanager.service.DatabaseService;
import com.cvteam.bkmanager.service.LogService;

public class ThoiKhoaBieuController implements IDataSource{
	private LogService logService = new LogService("ThoiKhoaBieuController");
	private SQLiteDatabase database;
    private DatabaseService databaseService;

    private ThoiKhoaBieuModel thoikhoabieu;

    public void setModel(ThoiKhoaBieuModel v) {
        this.thoikhoabieu = v;
    }

    public ThoiKhoaBieuController(Context context) {
        databaseService = new DatabaseService(context);
    }

    public void open() throws SQLException {
        database = databaseService.getWritableDatabase();
    }

    public void close() {
        databaseService.close();
    }

    public void getThoiKhoaBieu() {
        // logService.functionTag("getThoiKhoaBieu",
        // "Select all Thoi Khoa Bieu");
        Map<String, String> searchParams = new HashMap<String, String>();
        searchParams.put("mssv", this.thoikhoabieu.mssv);
        databaseService.selectTable("thoikhoabieu", searchParams, this);
    }

    public void getThoiKhoaBieu(Map<String, String> searchParams) {
    	logService.functionTag("getThoiKhoaBieu", "");
        databaseService.selectTable("thoikhoabieu", searchParams, this);
    }

    private DI__ThoiKhoaBieu cursorToTKB(Cursor cursor) {
        // logService.functionTag("cursorToLichThi", "cursor to model");
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

        return tkb;
    }

    /**
     * Cap nhat Model TKB tu du lieu lay tu DB
     *
     * @param Object
     *            : cursor lay tu DB
     */
    public void updateDataSource(Object obj) {
        // logService.functionTag("UpdateDataSource", "Update model TKB");
    	logService.functionTag("updateDataSource", "");
        List<DI__ThoiKhoaBieu> lstTKB = new ArrayList<DI__ThoiKhoaBieu>();
        if (obj != null) {
            Cursor cursor = (Cursor) obj;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                lstTKB.add(cursorToTKB(cursor));
                cursor.moveToNext();
            }
            cursor.close();
            
            
        }
        if (lstTKB.size() == 0) {
            requestTKBFromAao(thoikhoabieu.mssv);
        } else {
            this.thoikhoabieu.setThoiKhoaBieus(lstTKB);
        }
    }

    /**
     * Cap nhat Model TKB tu du lieu lay tu AAO (qua mang)
     *
     * @param lstTKB
     *            : list cac tkb lay tu AAO
     * @param objs
     *            : objs[0] is updateDate
     */
    public void updateDataSource(List<DI__ThoiKhoaBieu> lstTKB,
            List<String> objs) {
        logService.functionTag("updateDataSource",
        			"Set new list for TKB Model. Size = " + lstTKB.size());
        this.thoikhoabieu.setObjects(objs);
        this.thoikhoabieu.setThoiKhoaBieus(lstTKB);

        if (lstTKB.size() == 0)
            return;

        for (int i = 0; i < lstTKB.size(); i++) {
            DI__ThoiKhoaBieu temp = lstTKB.get(i);

            ContentValues values = new ContentValues();

            values.put("mssv", temp.mssv);
            values.put("namhoc", temp.namhoc);
            values.put("hocky", temp.hocky);

            values.put("mamh", temp.mamh);
            values.put("tenmh", temp.tenmh);
            values.put("nhomto", temp.nhomto);

            values.put("thu1", temp.thu1);
            values.put("tiet1", temp.tiet1);
            values.put("phong1", temp.phong1);

            values.put("thu2", temp.thu2);
            values.put("tiet2", temp.tiet2);
            values.put("phong2", temp.phong2);

            try {
                int affected = database.update("thoikhoabieu", values,
                        "mssv = '" + temp.mssv + "' AND namhoc = "
                                + temp.namhoc + " AND hocky = " + temp.hocky
                                + " AND mamh ='" + temp.mamh + "'", null);
                if (affected == 0)
                    database.insert("thoikhoabieu", null, values);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // insert hocky entry into database
        DI__ThoiKhoaBieu temp = lstTKB.get(0);

        ContentValues values = new ContentValues();

        values.put("mssv", temp.mssv);
        values.put("namhoc", temp.namhoc);
        values.put("hocky", temp.hocky);
        values.put("tkbstt", objs.get(0));

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

    public void requestTKBFromAao(String mssv) {
        logService.functionTag("requestTKBFromAao", "From aao get TKB");
        RequestTKBFromAaoAsynTask asynTask = new RequestTKBFromAaoAsynTask(
                mssv, this);
        asynTask.execute();
    }

    /**
     * Get list of TKB records from AAO
     *
     */
    class RequestTKBFromAaoAsynTask extends AsyncTask<Void, Void, Void> {
        private String mssv;
        private ThoiKhoaBieuController tkbController;
        private List<DI__ThoiKhoaBieu> lstTKB;
        private List<String> objs;

        public RequestTKBFromAaoAsynTask(String mssv, ThoiKhoaBieuController v) {
            this.mssv = mssv;
            this.tkbController = v;
        }

        @Override
        protected Void doInBackground(Void... params) {
            logService.functionTag("doInBackground", "AAOService.getTKB:" + mssv);
            objs = new ArrayList<String>();
            lstTKB = AAOService.getTKB(mssv, "", objs);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            logService.functionTag("onPostExecute", "AAOService.getTKB:" + mssv + " done");
            tkbController.updateDataSource(lstTKB, objs);
        }

    }
}
