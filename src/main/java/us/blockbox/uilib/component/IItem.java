package us.blockbox.uilib.component;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public interface IItem extends IComponent{
	@Override
	boolean select(Player viewer,ClickType clickType);

	@Override
	String getName();

	@Override
	String getId();

	String getDescription();

	@Override
	ItemStack getItemStack();
}
