package us.blockbox.uilib.component;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import us.blockbox.uilib.Consumer;
import us.blockbox.uilib.UIPlugin;
import us.blockbox.uilib.ViewManager;

public class CloseButtonItem extends AbstractItem{
	private static final ViewManager viewManager = UIPlugin.getViewManager();

	public CloseButtonItem(String name,String id,String description,ItemStack stack,Consumer<Player> onClick){
		super(name,id,description,stack,onClick);
	}

	public CloseButtonItem(String name,String id,String description,ItemStack stack){
		super(name,id,description,stack);
	}

	public CloseButtonItem(String name,String id,ItemStack stack){
		super(name,id,stack);
	}

	@Override
	public boolean select(Player viewer,ClickType clickType){
		if(onClick != null){
			onClick.accept(viewer);
		}
		viewManager.openSuperview(viewer,true);
		return true;
	}
}
