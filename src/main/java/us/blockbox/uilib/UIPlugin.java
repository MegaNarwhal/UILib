package us.blockbox.uilib;

import org.bukkit.plugin.java.JavaPlugin;

public class UIPlugin extends JavaPlugin{

	private static UIPlugin plugin;
	private static ViewManagerImpl viewManager;

	@Override
	public void onEnable(){
		if(plugin == null){
			plugin = this;
		}
		if(viewManager == null){
			viewManager = new ViewManagerImpl(this);
		}
		getCommand("view").setExecutor(new TestViewCommand());
//		ConfigurationSerialization.registerClass(AbstractItem.class);
		getServer().getPluginManager().registerEvents(new InventoryListener(plugin,viewManager),this);
	}

	@Override
	public void onDisable(){
		viewManager.closeAll();
	}

	public static UIPlugin getPlugin(){
		return plugin;
	}

	public static ViewManager getViewManager(){
		return viewManager;
	}
}
