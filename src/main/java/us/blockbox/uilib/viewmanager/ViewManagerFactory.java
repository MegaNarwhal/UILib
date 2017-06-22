package us.blockbox.uilib.viewmanager;

import us.blockbox.uilib.api.ViewManager;

public final class ViewManagerFactory{
	private ViewManagerFactory(){
	}

	private static ViewManager instance;

	public static ViewManager getInstance(){
		return instance;
	}

	public static void setInstance(ViewManager instance){
		ViewManagerFactory.instance = instance;
	}
}