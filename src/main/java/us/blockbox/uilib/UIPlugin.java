package us.blockbox.uilib;

import org.bukkit.plugin.java.JavaPlugin;

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
//		ConfigurationSerialization.registerClass(AbstractItem.class);
		getServer().getPluginManager().registerEvents(new InventoryListener(plugin,ViewManagerFactory.getInstance()),this);
	}

	@Override
	public void onDisable(){
		ViewManagerFactory.getInstance().closeAll();
	}

	public static UIPlugin getPlugin(){
		return plugin;
	}

	public static ViewManager getViewManager(){
		return ViewManagerFactory.getInstance();
	}
}
