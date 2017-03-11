package us.blockbox.uilib.component;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ShopItem extends AbstractItem{
	private final double priceBuy;
	private final double priceSell;
	private final ItemStack original;

	public ShopItem(String name,String id,ItemStack stack,double priceBuy,double priceSell){
		super(name,id,stack);
		this.original = stack.clone();
		this.priceBuy = priceBuy;
		this.priceSell = priceSell;
		setMeta(stack);
	}

	public ShopItem(String name,String id,String description,ItemStack stack,double priceBuy,double priceSell){
		super(name,id,description,stack);
		this.original = stack.clone();
		this.priceBuy = priceBuy;
		this.priceSell = priceSell;
		setMeta(stack);
	}

	private void setMeta(ItemStack stack){
		ItemMeta m = stack.getItemMeta();
		if(!m.hasLore()){
			String description = getDescription();
			if(description != null){
				List<String> lore = AbstractItem.wordWrap(description,45);
				m.setLore(lore);
			}
		}
		stack.setItemMeta(m);
	}

	public ItemStack getOriginal(){
		return original.clone();
	}

	@Override
	public boolean select(Player viewer,ClickType clickType){
		if(clickType == ClickType.LEFT){
			viewer.sendMessage("Buy " + priceBuy);
		}else if(clickType == ClickType.RIGHT){
			viewer.sendMessage("Sell " + priceSell);
		}else if(clickType == ClickType.SHIFT_LEFT){
			viewer.sendMessage("Bulk Buy " + priceBuy);
		}else if(clickType == ClickType.SHIFT_RIGHT){
			viewer.sendMessage("Bulk Sell " + priceSell);
		}
		return false; //todo
	}
}
