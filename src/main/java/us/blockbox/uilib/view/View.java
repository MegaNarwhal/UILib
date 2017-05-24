package us.blockbox.uilib.view;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import us.blockbox.uilib.component.Component;

public interface View{
	String getName();

	Component getItem(int index);

	Component getItem(ItemStack stack);

	ItemStack[] asContents();

	Inventory asInventory();

	int size();

	/**
	 * @param i         The index to set
	 * @param component The new Component to set at this index
	 * @return The previous Component at this index, possibly null
	 */
	Component set(int i,Component component);
}
