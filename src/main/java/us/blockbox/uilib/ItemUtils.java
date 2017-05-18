package us.blockbox.uilib;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class ItemUtils{
	private ItemUtils(){}

	public static ItemStack nameStack(ItemStack stack,String name){
		ItemMeta itemMeta = stack.getItemMeta();
		itemMeta.setDisplayName(name);
		stack.setItemMeta(itemMeta);
		return stack;
	}
}
