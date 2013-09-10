package com.cvteam.bkmanager.model;

import java.util.ArrayList;
import java.util.List;

public class HocPhiModel {
	public interface Listener {
		void handleDiemModelChanged(HocPhiModel sender, List<String> objs);
	}

	private List<Listener> listeners = new ArrayList<Listener>();

	public void addListener(Listener listener) {
		this.listeners.add(listener);
	}

	public void removeListener(Listener listener) {
		this.listeners.remove(listener);
	}

	public void notifyChanged() {
		for (Listener listener : listeners) {
			listener.handleDiemModelChanged(this, this.objs);
		}
	}

	private DI__HocPhi hh;
	private List<String> objs;
	public DI__NienHoc hocky;
	public String mssv;

	public void setObjects(List<String> value) {
		this.objs = value;
	}

	public void setHocPhi(DI__HocPhi value) {
		if (value == null)
			return;
		this.hh = value;
		notifyChanged();
	}

	public DI__HocPhi getHocPhi() {
		return this.hh;
	}

	public List<String> getObjs() {
		return this.objs;
	}

	public HocPhiModel() {
		objs = new ArrayList<String>();
		hh = new DI__HocPhi();
		hocky = new DI__NienHoc(0, 0);
		mssv = "";
	}
}
