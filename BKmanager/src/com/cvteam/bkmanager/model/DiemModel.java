package com.cvteam.bkmanager.model;

import java.util.ArrayList;
import java.util.List;

public class DiemModel {
	public interface Listener {
		void handleDiemModelChanged(DiemModel sender, List<Object> objs);
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

	private List<DI__Diem> diems;
	private List<Object> objs;
	public DI__NienHoc hocky;
	public String mssv;

	public void setObjects(List<Object> value) {
		this.objs = value;
	}

	public void setDiems(List<DI__Diem> value) {
		if (value == null)
			return;
		this.diems = value;
		notifyChanged();
	}

	public List<DI__Diem> getDiems() {
		return this.diems;
	}

	public List<Object> getObjs() {
		return this.objs;
	}

	public void addDiem(DI__Diem value) {
		this.diems.add(value);
		notifyChanged();
	}

	public DiemModel() {
		objs = new ArrayList<Object>();
		diems = new ArrayList<DI__Diem>();
		hocky = new DI__NienHoc(0, 0);
		mssv = "";
	}
}
