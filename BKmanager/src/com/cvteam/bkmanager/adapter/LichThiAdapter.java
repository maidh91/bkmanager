package com.cvteam.bkmanager.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View inflate = inflater.inflate(R.layout.lichthi_item, parent, false);
		TextView txtTenMon = (TextView) inflate.findViewById(R.id.txt_tenmon);
		TextView txtMaMonHoc = (TextView) inflate
				.findViewById(R.id.txt_mamonhoc);
		TextView txtGioThiGK = (TextView) inflate
				.findViewById(R.id.txt_giothigk);
		TextView txtPhongThiGK = (TextView) inflate
				.findViewById(R.id.txt_phongthigk);
		TextView txtGioThiCK = (TextView) inflate
				.findViewById(R.id.txt_giothick);
		TextView txtPhongThiCK = (TextView) inflate
				.findViewById(R.id.txt_phongthick);
		
		DI__LichThi lichThiItem = lstlichThi.get(position);
		txtTenMon.setText(lichThiItem.tenmh);
        txtMaMonHoc.setText(lichThiItem.mamh);

        // final exam info is available
        if (lichThiItem.tietgk != 0) {
            txtGioThiGK.setText("  - Ngày " + lichThiItem.ngaygk);
            txtPhongThiGK.setText("  - Tiết " + lichThiItem.tietgk + ", P." + lichThiItem.phonggk);
        }
        else
        {
            txtGioThiGK.setText("   --");
            txtPhongThiGK.setText("   --");
        }

        // final exam info is not available => show mid-term exam
        if (lichThiItem.tietck != 0) {
            txtGioThiCK.setText("  - Ngày " + lichThiItem.ngayck);
            txtPhongThiCK.setText("  - Tiết " + lichThiItem.tietck + ", P." + lichThiItem.phongck);
        }
        else
        {
            txtGioThiCK.setText("   --");
            txtPhongThiCK.setText("   --");
        }
        
		return inflate;
	}

}
