package us.blockbox.uilib.api.util;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * A helper class for setting various attributes of an ItemStack.
 */
public class ItemBuilder{
	private final ItemStack stack;

	/**
	 * Creates an ItemBuilder with a clone of the given ItemStack.
	 */
	public ItemBuilder(ItemStack stack){
		Validate.notNull(stack);
		this.stack = stack.clone();
	}

	/**
	 * Creates an ItemBuilder with a stack of one of the specified material.
	 *
	 * @param material The material used to create the ItemStack
	 * @since 0.0.17
	 */
	public ItemBuilder(Material material){
		this(new ItemStack(material));
	}

	/**
	 * Sets the type of this item. This resets the item's MaterialData.
	 *
	 * @param type The Material to set.
	 * @return This ItemBuilder.
	 */
	public ItemBuilder type(Material type){
		stack.setType(type);
		return this;
	}

	/**
	 * Sets the amount of the item.
	 *
	 * @param amount The amount to set
	 * @return This ItemBuilder.
	 */
	public ItemBuilder amount(int amount){
		stack.setAmount(amount);
		return this;
	}

	/**
	 * Sets the item's durability.
	 *
	 * @param durability The durability to set
	 * @return This ItemBuilder.
	 */
	public ItemBuilder durability(short durability){
		stack.setDurability(durability);
		return this;
	}

	/**
	 * Sets the display name of the item.
	 *
	 * @param name The name to set
	 * @return This ItemBuilder.
	 */
	public ItemBuilder name(String name){
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(name);
		stack.setItemMeta(meta);
		return this;
	}

	/**
	 * Sets the item's lore.
	 *
	 * @param lore The lore to set
	 * @return This ItemBuilder.
	 */
	public ItemBuilder lore(List<String> lore){
		ItemMeta meta = stack.getItemMeta();
		meta.setLore(lore);
		stack.setItemMeta(meta);
		return this;
	}

	/**
	 * Sets the item's ItemFlags.
	 *
	 * @param flags The ItemFlags to set
	 * @return This ItemBuilder.
	 * @since 0.0.17
	 */
	public ItemBuilder flags(ItemFlag... flags){
		ItemMeta meta = stack.getItemMeta();
		meta.removeItemFlags(ItemFlag.values());
		meta.addItemFlags(flags);
		return this;
	}

	public ItemStack build(){
		return stack.clone();
	}
}
