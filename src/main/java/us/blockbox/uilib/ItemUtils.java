package us.blockbox.uilib;

import org.bukkit.inventory.ItemStack;

@Deprecated
public final class ItemUtils{
	private ItemUtils(){}

	public static ItemStack nameStack(ItemStack stack,String name){
		return new ItemBuilder(stack).name(name).build();
	}
}
