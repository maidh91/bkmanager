package com.cvteam.bkmanager.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cvteam.bkmanager.R;
import com.cvteam.bkmanager.model.DI__ThoiKhoaBieu;
import com.cvteam.bkmanager.service.LogService;

public class ThoiKhoaBieuAdapter extends BaseAdapter {
	
	private List<DI__ThoiKhoaBieu> lstTKB;
	private Context mContext;
	private LogService logService = new LogService("ThoiKhoaBieuAdapter");

    public ThoiKhoaBieuAdapter(Context context, List<DI__ThoiKhoaBieu> lstTKB) {
    	logService.functionTag("ThoiKhoaBieuAdapter", "lstTKB.size(): " + lstTKB.size());
    	mContext = context;
        this.lstTKB = lstTKB;
    }
    
	public void setLstTKB(List<DI__ThoiKhoaBieu> lstTKB) {
        this.lstTKB = lstTKB;
    }
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
        return lstTKB.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lstTKB.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		logService.functionTag("getView", "position: " + position + " and lstTKB.size(): " + lstTKB.size());
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) mContext
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View inflate = inflater.inflate(R.layout.thoikhoabieu_item, parent, false);

		TextView txtTenMon = (TextView) inflate.findViewById(R.id.txt_tenmon);
		TextView txtMaMonHoc = (TextView) inflate.findViewById(R.id.txt_mamonhoc);

		TextView txtThu1 = (TextView) inflate.findViewById(R.id.txt_thu1);
		TextView txtTiet1 = (TextView) inflate.findViewById(R.id.txt_tiet1);
		TextView txtPhong1 = (TextView) inflate.findViewById(R.id.txt_phong1);

		TextView txtThu2 = (TextView) inflate.findViewById(R.id.txt_thu2);
		TextView txtTiet2 = (TextView) inflate.findViewById(R.id.txt_tiet2);
		TextView txtPhong2 = (TextView) inflate.findViewById(R.id.txt_phong2);
		    
		DI__ThoiKhoaBieu tkbItem = lstTKB.get(position);
		txtTenMon.setText(tkbItem.tenmh);
		txtMaMonHoc.setText(tkbItem.mamh);

		// buoi dau tien
		if (tkbItem.thu1 != 0) {
			txtThu1.setText(tkbItem.thu1 + "");
			txtTiet1.setText(tkbItem.tiet1);
			txtPhong1.setText(tkbItem.phong1);
		} else {
			txtThu1.setText("--");
			txtTiet1.setText("--");
			txtPhong1.setText("--");
		}

		// buoi thu 2
		if (tkbItem.thu2 != 0) {
			txtThu2.setText(tkbItem.thu2 + "");
			txtTiet2.setText(tkbItem.tiet2);
			txtPhong2.setText(tkbItem.phong2);
		} else {
			txtThu2.setText("--");
			txtTiet2.setText("--");
			txtPhong2.setText("--");
		}
		
        return inflate;
	}

}
