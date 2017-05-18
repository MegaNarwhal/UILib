package us.blockbox.uilib.component;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * An object that may perform an action when its ItemStack representation is interacted with by a Player.
 * @since 1.0
 */
public interface Component{
	boolean select(Player viewer,ClickType clickType);
	String getName();
	String getId();
	String getDescription();
	ItemStack getItemStack();
}