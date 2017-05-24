package us.blockbox.uilib;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemBuilder{
	private final ItemStack stack;

	public ItemBuilder(ItemStack stack){
		this.stack = stack.clone();
	}

	public ItemBuilder type(Material type){
		stack.setType(type);
		return this;
	}

	public ItemBuilder amount(int amount){
		stack.setAmount(amount);
		return this;
	}

	public ItemBuilder durability(short durability){
		stack.setDurability(durability);
		return this;
	}

	public ItemBuilder name(String name){
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(name);
		stack.setItemMeta(meta);
		return this;
	}

	public ItemBuilder lore(List<String> lore){
		ItemMeta meta = stack.getItemMeta();
		meta.setLore(lore);
		stack.setItemMeta(meta);
		return this;
	}

	public ItemStack build(){
		return stack.clone();
	}
}
