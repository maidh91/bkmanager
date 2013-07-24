package com.cvteam.bkmanager.model;

import java.util.ArrayList;
import java.util.List;

public class NienHocModel {
	private List<DI__NienHoc> hks;

	public void setHKs(List<DI__NienHoc> value) {
		// all
		value.add(0, new DI__NienHoc(0, 0));
		// to choose
		value.add(0, new DI__NienHoc(-1, -1));

		this.hks = value;
		// logService.functionTag("setHKs", "" + value.size());
		// logService.functionTag("setHKs", "Size of listener: " +
		// listeners.size());
		notifyChanged();
	}

	public List<DI__NienHoc> getHKs() {
		return this.hks;
	}

	public NienHocModel() {
		hks = new ArrayList<DI__NienHoc>();
	}

	public interface Listener {
		void handleNienHocModelChanged(NienHocModel sender);
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
			listener.handleNienHocModelChanged(this);
		}
	}
}
