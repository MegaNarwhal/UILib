package us.blockbox.uilib;

import org.bukkit.plugin.java.JavaPlugin;
import us.blockbox.uilib.api.ViewManager;
import us.blockbox.uilib.command.TestViewCommand;
import us.blockbox.uilib.viewmanager.ViewManagerFactory;
import us.blockbox.uilib.viewmanager.ViewManagerImpl;

public class UIPlugin extends JavaPlugin{

	private static UIPlugin plugin;

	@Override
	public void onEnable(){
		if(plugin == null){
			plugin = this;
		}
		if(ViewManagerFactory.getInstance() == null){
			ViewManagerFactory.setInstance(new ViewManagerImpl(this));
		}
		getCommand("view").setExecutor(new TestViewCommand());
		getServer().getPluginManager().registerEvents(new InventoryListener(plugin,ViewManagerFactory.getInstance()),this);
	}

	@Override
	public void onDisable(){
		ViewManager viewManager = ViewManagerFactory.getInstance();
		if(viewManager != null){
			viewManager.closeAll();
		}
	}

	public static UIPlugin getPlugin(){
		return plugin;
	}

	@Deprecated
	public static ViewManager getViewManager(){
		return ViewManagerFactory.getInstance();
	}
}