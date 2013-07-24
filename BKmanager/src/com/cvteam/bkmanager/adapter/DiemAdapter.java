package com.cvteam.bkmanager.adapter;

import java.util.List;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.TextView;

import com.cvteam.bkmanager.R;
import com.cvteam.bkmanager.model.DI__Diem;
import com.cvteam.bkmanager.service.LogService;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class DiemAdapter extends BaseAdapter {
	private LogService logService = new LogService("DiemViewAdapter");
	private List<DI__Diem> lstDiem;
	private Context mContext;

	public DiemAdapter(Context context, List<DI__Diem> l) {
		mContext = context;
		lstDiem = l;
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
		TextView txtMaMonHoc = (TextView) inflate
				.findViewById(R.id.txt_mamonhoc);
		TextView txtSoTC = (TextView) inflate.findViewById(R.id.txt_sotc);
		TextView txtNhomTo = (TextView) inflate.findViewById(R.id.txt_nhomto);
		TextView txtGiuaKy = (TextView) inflate.findViewById(R.id.txt_diemgk);
		TextView txtCuoiKy = (TextView) inflate.findViewById(R.id.txt_diemck);
		TextView txtTongKet = (TextView) inflate.findViewById(R.id.txt_diemtk);

		DI__Diem diemItem = lstDiem.get(position);

		if (diemItem != null) {
			txtTenMon.setText(diemItem.tenmh);
			txtMaMonHoc.setText("Mã MH: " + diemItem.mamh);
			txtSoTC.setText("Số TC: " + diemItem.sotc);
			txtNhomTo.setText("Nhóm tổ: " + diemItem.nhomto);

			if (diemItem.diemkt < 0)
				txtGiuaKy.setText("  - Kiểm tra: --");
			else
				txtGiuaKy.setText("  - Kiểm tra: "
						+ Float.toString(diemItem.diemkt));

			if (diemItem.diemthi < 0)
				txtCuoiKy.setText("  - Cuối kỳ: --");
			else
				txtCuoiKy.setText("  - Cuối kỳ: "
						+ Float.toString(diemItem.diemthi));

			if (diemItem.diemtk < 0)
				txtTongKet.setText("  - Tổng kết: --");
			else
				txtTongKet.setText("  - Tổng kết: "
						+ Float.toString(diemItem.diemtk));
		}
		
		return inflate;
	}

}
