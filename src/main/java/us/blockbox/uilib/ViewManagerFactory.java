package us.blockbox.uilib;

public class ViewManagerFactory{
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
