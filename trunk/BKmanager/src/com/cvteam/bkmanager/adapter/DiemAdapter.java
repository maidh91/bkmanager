package com.cvteam.bkmanager.adapter;

import java.util.List;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.TextView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cvteam.bkmanager.R;
import com.cvteam.bkmanager.model.DI__Diem;
import com.cvteam.bkmanager.service.LogService;

public class DiemAdapter extends BaseAdapter {
	private LogService logService = new LogService("DiemViewAdapter");
	private List<DI__Diem> lstDiem;
	private Context mContext;

	public DiemAdapter(Context context, List<DI__Diem> l) {
		mContext = context;
		lstDiem = l;
	}

	public void setLstDiem(List<DI__Diem> lstDiem) {
		this.lstDiem = lstDiem;
	}
	
	@Override
	public int getCount() {
		return lstDiem.size();
	}

	@Override
	public Object getItem(int position) {
		return lstDiem.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		logService.functionTag("getView", String.valueOf(position));

		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View inflate = inflater.inflate(R.layout.diem_item, parent, false);

		TextView txtTenMon = (TextView) inflate.findViewById(R.id.txt_tenmon);
		TextView txtGiuaKy = (TextView) inflate.findViewById(R.id.txt_diemgk);
		TextView txtCuoiKy = (TextView) inflate.findViewById(R.id.txt_diemck);
		TextView txtTongKet = (TextView) inflate.findViewById(R.id.txt_diemtk);

		DI__Diem diemItem = lstDiem.get(position);

		if (diemItem != null) {
			txtTenMon.setText(diemItem.mamh + " - " + diemItem.tenmh);

			if (diemItem.diemkt < 0)
				txtGiuaKy.setText("--");
			else
				txtGiuaKy.setText(Float.toString(diemItem.diemkt));

			if (diemItem.diemthi < 0)
				txtCuoiKy.setText("--");
			else
				txtCuoiKy.setText(Float.toString(diemItem.diemthi));

			if (diemItem.diemtk < 0)
				txtTongKet.setText("--");
			else
				txtTongKet.setText(Float.toString(diemItem.diemtk));
		}
		
		return inflate;
	}

}
