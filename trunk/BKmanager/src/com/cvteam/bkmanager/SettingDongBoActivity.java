package com.cvteam.bkmanager;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.NumberPicker;
import org.holoeverywhere.widget.Switch;
import org.holoeverywhere.widget.TextView;
import org.holoeverywhere.widget.ViewPager;

import com.actionbarsherlock.view.Menu;

public class SettingDongBoActivity extends FragmentActivity {

	// private RelativeLayout ckcn;
	// private RelativeLayout nnlt;
	private Switch dblt;
	// private TextView ckcnTV;
	// private TextView nnltTV;
	private ViewPager vpckcn;
	private ViewPager vpckcn_l;
	private ViewPager vpckcn_r;
	private ViewPager vpnnlt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_dong_bo);
		View backMenu = getLayoutInflater().inflate(R.layout.back_menu, null,
				false);
		RelativeLayout backlayout = (RelativeLayout) backMenu
				.findViewById(R.id.backmenu);
		backlayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SettingDongBoActivity.this.finish();
			}
		});
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayShowCustomEnabled(true);
		getActionBar().setCustomView(backMenu);

		dblt = (Switch) findViewById(R.id.SwitchDBLT);
		dblt.setChecked(Setting._dongBoLichThi);
		dblt.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
				Setting._dongBoLichThi = isChecked;
			}
		});
		vpckcn = (ViewPager) findViewById(R.id.viewpager_CKCN);
		FragmentAdapter FAdapter_ckcn = new FragmentAdapter(
				getSupportFragmentManager(), 5, new String[] { "1", "2", "3",
						"4", "5" },true);
		vpckcn.setAdapter(FAdapter_ckcn);
		vpckcn.setOffscreenPageLimit(5);
		vpckcn.setCurrentItem(Setting._chuKiCapNhat);

		vpckcn_l = (ViewPager) findViewById(R.id.viewpager_CKCN_l);
		FragmentAdapter FAdapter_ckcn_l = new FragmentAdapter(
				getSupportFragmentManager(), 5, new String[] { " ", "1", "2",
						"3", "4" },false);
		vpckcn_l.setAdapter(FAdapter_ckcn_l);
		vpckcn_l.setOffscreenPageLimit(5);
		vpckcn_l.setCurrentItem(Setting._chuKiCapNhat);

		vpckcn_r = (ViewPager) findViewById(R.id.viewpager_CKCN_r);
		FragmentAdapter FAdapter_ckcn_r = new FragmentAdapter(
				getSupportFragmentManager(), 5, new String[] { "2", "3", "4",
						"5", " " },false);
		vpckcn_r.setAdapter(FAdapter_ckcn_r);
		vpckcn_r.setOffscreenPageLimit(5);
		vpckcn_r.setCurrentItem(Setting._chuKiCapNhat);

		vpnnlt = (ViewPager) findViewById(R.id.viewpager_NNLT);
		FragmentAdapter FAdapter_nnlt = new FragmentAdapter(
				getSupportFragmentManager(), 3, new String[] {
						"Trước một ngày", "Đầu ngày", "Trước khi thi 1 tiếng" },false);
		vpnnlt.setAdapter(FAdapter_nnlt);
		vpnnlt.setOffscreenPageLimit(3);
		vpnnlt.setCurrentItem(Setting._nhacNhoLichThi);
		vpckcn.setOnPageChangeListener(new OnPageChangeListener() {
			private int mLastScrollPosition;
		    private int mLastPagePosition;
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				mLastPagePosition = position;
			}

			@Override
			public void onPageScrolled(int position, float arg1, int positionOffsetPixels) {
				// TODO Auto-generated method stub
				if(vpckcn_l.isFakeDragging()&&vpckcn_r.isFakeDragging()) {
		            int absoluteOffsetPixels = positionOffsetPixels;
		            if(mLastPagePosition!=position) {
		                absoluteOffsetPixels += (position - mLastPagePosition) * vpckcn_l.getWidth();
		                mLastPagePosition = position;
		            }
		            vpckcn_l.fakeDragBy(mLastScrollPosition - absoluteOffsetPixels);
	            	vpckcn_r.fakeDragBy(mLastScrollPosition - absoluteOffsetPixels);
		            
		            mLastScrollPosition = positionOffsetPixels;
		            
		        }
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				Setting._chuKiCapNhat = state;
				if(!vpckcn.isFakeDragging()) {
		            switch(state) {
		            case ViewPager.SCROLL_STATE_DRAGGING:
		                if(!vpckcn_l.isFakeDragging())
		                	vpckcn_l.beginFakeDrag();
		                if(!vpckcn_r.isFakeDragging())
		                	vpckcn_r.beginFakeDrag();
		                break;
		            case ViewPager.SCROLL_STATE_SETTLING:
		            case ViewPager.SCROLL_STATE_IDLE:
		                if(vpckcn_l.isFakeDragging()) {
		                	vpckcn_l.endFakeDrag();
		                    mLastScrollPosition = 0;
		                }
		                if(vpckcn_r.isFakeDragging()) {
		                	vpckcn_r.endFakeDrag();
		                }
		                break;
		            }
		        }
			}
		});

		vpckcn_l.setOnPageChangeListener(new OnPageChangeListener() {

			private int mLastScrollPosition;
		    private int mLastPagePosition;
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				mLastPagePosition = position;
			}

			@Override
			public void onPageScrolled(int position, float arg1, int positionOffsetPixels) {
				// TODO Auto-generated method stub
				if(vpckcn.isFakeDragging()&&vpckcn_r.isFakeDragging()) {
		            int absoluteOffsetPixels = positionOffsetPixels;
		            if(mLastPagePosition!=position) {
		                absoluteOffsetPixels += (position - mLastPagePosition) * vpckcn.getWidth();
		                mLastPagePosition = position;
		            }
		            vpckcn.fakeDragBy(mLastScrollPosition - absoluteOffsetPixels);
		            vpckcn_r.fakeDragBy(mLastScrollPosition - absoluteOffsetPixels);
		            mLastScrollPosition = positionOffsetPixels;
		        }
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				if(!vpckcn_l.isFakeDragging()) {
		            switch(state) {
		            case ViewPager.SCROLL_STATE_DRAGGING:
		                if(!vpckcn.isFakeDragging())
		                	vpckcn.beginFakeDrag();
		                if(!vpckcn_r.isFakeDragging())
		                	vpckcn_r.beginFakeDrag();
		                break;
		            case ViewPager.SCROLL_STATE_SETTLING:
		            case ViewPager.SCROLL_STATE_IDLE:
		                if(vpckcn.isFakeDragging()) {
		                	vpckcn.endFakeDrag();
		                    mLastScrollPosition = 0;
		                }
		                if(vpckcn_r.isFakeDragging()) {
		                	vpckcn_r.endFakeDrag();
		                }
		                break;
		            }
		        }
			}
		});

		vpckcn_r.setOnPageChangeListener(new OnPageChangeListener() {

			private int mLastScrollPosition;
		    private int mLastPagePosition;
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				mLastPagePosition = position;
			}

			@Override
			public void onPageScrolled(int position, float arg1, int positionOffsetPixels) {
				// TODO Auto-generated method stub
				if(vpckcn.isFakeDragging()&&vpckcn_l.isFakeDragging()) {
		            int absoluteOffsetPixels = positionOffsetPixels;
		            if(mLastPagePosition!=position) {
		                absoluteOffsetPixels += (position - mLastPagePosition) * vpckcn.getWidth();
		                mLastPagePosition = position;
		            }
		            vpckcn.fakeDragBy(mLastScrollPosition - absoluteOffsetPixels);
		            vpckcn_l.fakeDragBy(mLastScrollPosition - absoluteOffsetPixels);
		            mLastScrollPosition = positionOffsetPixels;
		        }
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				if(!vpckcn_r.isFakeDragging()) {
		            switch(state) {
		            case ViewPager.SCROLL_STATE_DRAGGING:
		                if(!vpckcn.isFakeDragging())
		                	vpckcn.beginFakeDrag();
		                if(!vpckcn_l.isFakeDragging())
		                	vpckcn_l.beginFakeDrag();
		                break;
		            case ViewPager.SCROLL_STATE_SETTLING:
		            case ViewPager.SCROLL_STATE_IDLE:
		                if(vpckcn.isFakeDragging()) {
		                	vpckcn.endFakeDrag();
		                    mLastScrollPosition = 0;
		                }
		                if(vpckcn_l.isFakeDragging()) {
		                	vpckcn_l.endFakeDrag();
		                }
		                break;
		            }
		        }
			}
		});

		vpnnlt.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onPageScrollStateChanged(int position) {
				// TODO Auto-generated method stub
				Setting._nhacNhoLichThi = position;
			}
		});

		/*
		 * ckcn = (RelativeLayout) findViewById(R.id.RelativeLayoutCKCN);
		 * ckcn.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { // TODO Auto-generated
		 * method stub AlertDialog.Builder builder = new
		 * AlertDialog.Builder(SettingDongBoActivity.this); LayoutInflater
		 * inflater = SettingDongBoActivity.this.getLayoutInflater(); final View
		 * viewdialog = inflater.inflate(R.layout.ckcn_dialog, null);
		 * 
		 * builder.setView(viewdialog);
		 * 
		 * final android.app.AlertDialog dialog = builder.create();
		 * 
		 * dialog.setTitle("Chọn chu kỳ cập nhật"); Button ok = (Button)
		 * viewdialog.findViewById(R.id.buttonOK); Button cancel = (Button)
		 * viewdialog.findViewById(R.id.buttonCancel);
		 * cancel.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { // TODO Auto-generated
		 * method stub dialog.dismiss(); } }); final NumberPicker nP =
		 * (NumberPicker) viewdialog.findViewById(R.id.numberPicker1);
		 * nP.setMaxValue(7); nP.setMinValue(1);
		 * nP.setValue(Setting._chuKiCapNhat); ok.setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub Setting._chuKiCapNhat = nP.getValue();
		 * ckcnTV.setText(Setting._chuKiCapNhat + " ngày"); dialog.dismiss(); }
		 * }); dialog.show(); } }); nnlt = (RelativeLayout)
		 * findViewById(R.id.RelativeLayoutNNLT); ckcnTV = (TextView)
		 * findViewById(R.id.TextViewCKCNValue);
		 * ckcnTV.setText(Setting._chuKiCapNhat + " ngày"); nnltTV = (TextView)
		 * findViewById(R.id.TextViewNNLTValue); switch
		 * (Setting._nhacNhoLichThi) { case 0: nnltTV.setText("Trước một ngày");
		 * break; case 1: nnltTV.setText("Đầu ngày"); break; case 2:
		 * nnltTV.setText("Trước khi thi 1 tiếng"); break;
		 * 
		 * default: break; } nnlt.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub AlertDialog.Builder builder = new
		 * AlertDialog.Builder(SettingDongBoActivity.this); LayoutInflater
		 * inflater = SettingDongBoActivity.this.getLayoutInflater(); final View
		 * viewdialog = inflater.inflate(R.layout.nnlt_dialog, null);
		 * 
		 * builder.setView(viewdialog);
		 * 
		 * final android.app.AlertDialog dialog = builder.create();
		 * 
		 * dialog.setTitle("Chọn nhắc nhở thi"); Button ok = (Button)
		 * viewdialog.findViewById(R.id.buttonOK); Button cancel = (Button)
		 * viewdialog.findViewById(R.id.buttonCancel);
		 * cancel.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { // TODO Auto-generated
		 * method stub dialog.dismiss(); } }); final NumberPicker nP =
		 * (NumberPicker) viewdialog.findViewById(R.id.numberPicker1);
		 * nP.setMaxValue(2); nP.setMinValue(0);
		 * nP.setValue(Setting._nhacNhoLichThi); nP.setDisplayedValues(new
		 * String[] { "Trước một ngày", "Đầu ngày", "Trước khi thi 1 tiếng" });
		 * ok.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub Setting._nhacNhoLichThi = nP.getValue(); switch
		 * (Setting._nhacNhoLichThi) { case 0: nnltTV.setText("Trước một ngày");
		 * break; case 1: nnltTV.setText("Đầu ngày"); break; case 2:
		 * nnltTV.setText("Trước khi thi 1 tiếng"); break;
		 * 
		 * default: break; } dialog.dismiss(); } }); dialog.show(); } });
		 */
	}

	@Override
	protected void onStop() {
		InitializeService();
		super.onStop();
	}

	private class FragmentAdapter extends FragmentPagerAdapter {
		protected String[] CONTENT;
		protected int[] ICONS = new int[] {};

		private List<Fragment> fragments = new ArrayList<Fragment>();
		private int mCount;

		public FragmentAdapter(FragmentManager fm, int count, String[] content,Boolean flag) {
			super(fm);
			mCount = count;
			CONTENT = content;
			fragments = new ArrayList<Fragment>();
			for (int i = 0; i < mCount; i++) {
				myFragment f = new myFragment();
				f.setContent(CONTENT[i],flag);
				fragments.add(f);
			}
		}

		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}

		@Override
		public int getCount() {
			return mCount;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return this.CONTENT[position % CONTENT.length];
		}
	}

	// /////////////////////////////////////////
	// Notification //
	// /////////////////////////////////////////

	private void InitializeService() {
		Boolean isRunning = false;
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if ("com.cvteam.bkmanager.service.NotiService"
					.equals(service.service.getClassName())) {
				// System.out.println("isRunning");
				isRunning = true;
				break;
			}
		}

		if (!isRunning) {
			Intent intent = new Intent(this,
					com.cvteam.bkmanager.service.NotiService.class);
			intent.putExtra("mssv", Setting._mssv);
			intent.putExtra("dongbo", Setting._dongBoLichThi);
			intent.putExtra("noti_lichthi", Setting._thongBaoLichThi);
			intent.putExtra("noti_tkb", Setting._thongBaoTKB);
			intent.putExtra("noti_diem", Setting._thongBaoDiem);
			intent.putExtra("intervalTime", Setting._chuKiCapNhat);

			// System.out.println("start service " + mssv);
			startService(intent);
		}
	}

	// /////////////////////////////////////////
	// End Notification //
	// /////////////////////////////////////////
}
