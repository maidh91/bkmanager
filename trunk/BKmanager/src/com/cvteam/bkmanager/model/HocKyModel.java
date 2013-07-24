package com.cvteam.bkmanager.model;

import java.util.ArrayList;
import java.util.List;

public class HocKyModel {
	public interface Listener {
		void handleHocKyModelChanged(HocKyModel sender);
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
			listener.handleHocKyModelChanged(this);
		}
	}

	private List<DI__HocKy> hocKys;

	public void setHocKys(List<DI__HocKy> value) {
		this.hocKys = value;
	}

	public List<DI__HocKy> getHocKys() {
		return this.hocKys;
	}
}
