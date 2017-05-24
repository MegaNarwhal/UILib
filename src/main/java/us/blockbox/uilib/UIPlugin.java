package us.blockbox.uilib;

import org.bukkit.plugin.java.JavaPlugin;

public class UIPlugin extends JavaPlugin{

	private static UIPlugin plugin;
	private static ViewManager viewManager;

	@Override
	public void onEnable(){
		if(plugin == null){
			plugin = this;
		}
		if(viewManager == null){
			viewManager = new ViewManager(this);
		}
		getCommand("view").setExecutor(new TestViewCommand());
//		ConfigurationSerialization.registerClass(AbstractItem.class);
		getServer().getPluginManager().registerEvents(new InventoryListener(),this);
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
