package com.cvteam.bkmanager.adapter;

import java.util.List;

import org.holoeverywhere.widget.TextView;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cvteam.bkmanager.R;
import com.cvteam.bkmanager.model.DI__LichThi;
import com.cvteam.bkmanager.service.LogService;

public class LichThiAdapter extends BaseAdapter {

	private List<DI__LichThi> lstlichThi;
	private Context mContext;
	private LogService logService = new LogService("LichThiAdapter");

	public LichThiAdapter(Context context, List<DI__LichThi> lichThis) {
		mContext = context;
		lstlichThi = lichThis;
	}

	public void setLstLichThi(List<DI__LichThi> lstlichThi) {
		this.lstlichThi = lstlichThi;
	}

	@Override
	public int getCount() {
		return lstlichThi.size();
	}

	@Override
	public Object getItem(int position) {
		return lstlichThi.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		logService.functionTag("getView", "position: " + position
				+ " and lstlichThi.size(): " + lstlichThi.size());

		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View inflate = inflater.inflate(R.layout.lichthi_item, parent, false);
		
		TextView txtTenMon = (TextView) inflate.findViewById(R.id.txt_tenmon);
		if (position % 2 == 0)
			txtTenMon.setBackgroundColor(Color.parseColor("#e0992d"));
		else
			txtTenMon.setBackgroundColor(Color.parseColor("#02bd86"));

		TextView txtNgay = (TextView) inflate.findViewById(R.id.headerLT_ngay);
		TextView txtTiet = (TextView) inflate.findViewById(R.id.headerLT_tiet);
		TextView txtPhong = (TextView) inflate
				.findViewById(R.id.headerLT_phong);
		if (position % 2 == 0) {
			txtNgay.setBackgroundColor(Color.parseColor("#bc8125"));
			txtTiet.setBackgroundColor(Color.parseColor("#bc8125"));
			txtPhong.setBackgroundColor(Color.parseColor("#bc8125"));
		} else {
			txtNgay.setBackgroundColor(Color.parseColor("#01a775"));
			txtTiet.setBackgroundColor(Color.parseColor("#01a775"));
			txtPhong.setBackgroundColor(Color.parseColor("#01a775"));
		}

		TextView txtNgay1 = (TextView) inflate.findViewById(R.id.txt_ngay_gk);
		TextView txtTiet1 = (TextView) inflate.findViewById(R.id.txt_tiet_gk);
		TextView txtPhong1 = (TextView) inflate.findViewById(R.id.txt_phong_gk);

		TextView txtNgay2 = (TextView) inflate.findViewById(R.id.txt_ngay_ck);
		TextView txtTiet2 = (TextView) inflate.findViewById(R.id.txt_tiet_ck);
		TextView txtPhong2 = (TextView) inflate.findViewById(R.id.txt_phong_ck);

		DI__LichThi ltItem = lstlichThi.get(position);
		txtTenMon.setText(ltItem.mamh + " - " + ltItem.tenmh);

		// thi giua ki
		if (!ltItem.ngaygk.equals("/")) {
			txtNgay1.setText(ltItem.ngaygk + "");
			txtTiet1.setText(ltItem.tietgk + "");
			txtPhong1.setText(ltItem.phonggk);
		} else {
			txtNgay1.setText("--");
			txtTiet1.setText("--");
			txtPhong1.setText("--");
		}

		// buoi thu 2
		if (!ltItem.ngayck.equals("/")) {
			txtNgay2.setText(ltItem.ngayck + "");
			txtTiet2.setText(ltItem.tietck + "");
			txtPhong2.setText(ltItem.phongck);
		} else {
			txtNgay2.setText("--");
			txtTiet2.setText("--");
			txtPhong2.setText("--");
		}

		return inflate;
	}

}
