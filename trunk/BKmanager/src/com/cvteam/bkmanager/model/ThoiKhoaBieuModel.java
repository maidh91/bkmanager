package com.cvteam.bkmanager.model;

import java.util.ArrayList;
import java.util.List;

public class ThoiKhoaBieuModel {

	public interface Listener {
		void handleThoiKhoaBieuChanged(ThoiKhoaBieuModel sender);
	}

	private List<Listener> listeners = new ArrayList<Listener>();

	public void addListener(Listener listener) {
		this.listeners.add(listener);
	}

	public void removeListener(Listener listener) {
		this.listeners.remove(listener);
	}

	public String mssv;
	private List<DI__ThoiKhoaBieu> thoiKhoaBieus;
	private List<String> objs;
	public boolean notify;

	public ThoiKhoaBieuModel() {
		thoiKhoaBieus = new ArrayList<DI__ThoiKhoaBieu>();
		objs = new ArrayList<String>();
		mssv = "";
		notify = true;
	}

	public void addThoiKhoaBieu(DI__ThoiKhoaBieu value) {
		thoiKhoaBieus.add(value);
		if (notify)
			notifyChanged();
	}

	public void setObjects(List<String> objs) {
		this.objs = objs;
	}

	public void setThoiKhoaBieus(List<DI__ThoiKhoaBieu> value) {
		thoiKhoaBieus = value;
		if (notify)
			notifyChanged();
	}

	public List<DI__ThoiKhoaBieu> getThoiKhoaBieus() {
		return thoiKhoaBieus;
	}

	public List<String> getObjs() {
		return this.objs;
	}

	public void notifyChanged() {
		for (Listener listener : listeners) {
			listener.handleThoiKhoaBieuChanged(this);
		}
	}
}
