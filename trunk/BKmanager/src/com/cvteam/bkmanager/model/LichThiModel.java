package com.cvteam.bkmanager.model;

import java.util.ArrayList;
import java.util.List;

public class LichThiModel {

	public interface Listener {
		void handleLichThiChanged(LichThiModel sender);
	}

	private List<Listener> listeners = new ArrayList<Listener>();

	private List<DI__LichThi> lichThis;
	private List<String> objs;
	public String mssv;

	public void addListener(Listener listener) {
		this.listeners.add(listener);
	}

	public void removeListener(Listener listener) {
		this.listeners.remove(listener);
	}

	private void notifyChanged() {
		for (Listener listener : listeners) {
			listener.handleLichThiChanged(this);
		}
	}

	public LichThiModel() {
		lichThis = new ArrayList<DI__LichThi>();
		objs = new ArrayList<String>();
		mssv = "";
	}

	/**
	 * LichThi accessor
	 * 
	 * @return list of all DI__LichThi in this model
	 */
	public List<DI__LichThi> getLichThis() {
		return lichThis;
	}

	public List<String> getObjs() {
		return this.objs;
	}

	/**
	 * LichThi mutator
	 * 
	 * @param value
	 *            : new list of DI__LichThi objects
	 */
	public void setLichThis(List<DI__LichThi> value) {
		lichThis = value;
		notifyChanged();
	}

	/**
	 * 
	 * @param objs
	 */
	public void setObject(List<String> objs) {
		this.objs = objs;
	}
}
