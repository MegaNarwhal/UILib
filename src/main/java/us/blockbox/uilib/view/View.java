package us.blockbox.uilib.view;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import us.blockbox.uilib.component.IComponent;

public interface View{
	String getName();

	IComponent getItem(int index);

	IComponent getItem(ItemStack stack);

	ItemStack[] asContents();

	Inventory asInventory();

	int size();
}
