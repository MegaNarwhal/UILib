package us.blockbox.uilib.component;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import us.blockbox.uilib.api.AbstractItem;

public class FillerItem extends AbstractItem{
	private static final String FILLER = "filler";

	public FillerItem(String name,String id,String description,ItemStack stack){
		super(name,id,description,stack);
	}

	public FillerItem(String name,String id,ItemStack stack){
		super(name,id,stack);
	}

	public static FillerItem create(ItemStack stack){
		return new FillerItem(null,FILLER,stack);
	}

	@Override
	public boolean select(Player viewer,ClickType clickType){
		return false;
	}
}
