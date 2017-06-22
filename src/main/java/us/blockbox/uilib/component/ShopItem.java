package us.blockbox.uilib.component;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import us.blockbox.uilib.api.AbstractItem;
import us.blockbox.uilib.api.util.WordWrapper;

public class ShopItem extends AbstractItem{
	private final double priceBuy;
	private final double priceSell;
	private final ItemStack original;

	public static ShopItem create(String name,String id,String description,ItemStack stack,double priceBuy,double priceSell){
		ItemStack stackWithMeta = stack.clone();
		setMeta(stackWithMeta,description);
		return new ShopItem(name,id,description,stack,stackWithMeta,priceBuy,priceSell);
	}

	private ShopItem(String name,String id,String description,ItemStack icon,ItemStack original,double priceBuy,double priceSell){
		super(name,id,description,icon);
		this.original = original;
		this.priceBuy = priceBuy;
		this.priceSell = priceSell;
	}

	private static void setMeta(ItemStack stack,String description){
		if(description == null){
			return;
		}
		ItemMeta m = stack.getItemMeta();
		if(!m.hasLore()){
			m.setLore(new WordWrapper(description).wrap(45));
		}
		stack.setItemMeta(m);
	}

	public ItemStack getOriginal(){
		return original.clone();
	}

	@Override
	public boolean select(Player viewer,ClickType clickType){
		boolean result;
		if(clickType == ClickType.LEFT){
			result = buySingle(viewer);
		}else if(clickType == ClickType.RIGHT){
			result = sellSingle(viewer);
		}else if(clickType == ClickType.SHIFT_LEFT){
			result = buyBulk(viewer);
		}else if(clickType == ClickType.SHIFT_RIGHT){
			result = sellBulk(viewer);
		}else{
			result = false;
		}
		return result;
	}

	protected boolean buySingle(Player p){
		p.sendMessage("Buy " + priceBuy);
		return true;
	}

	protected boolean buyBulk(Player p){
		p.sendMessage("Bulk Buy " + priceBuy);
		return true;
	}

	protected boolean sellSingle(Player p){
		p.sendMessage("Sell " + priceSell);
		return true;
	}

	protected boolean sellBulk(Player p){
		p.sendMessage("Bulk Sell " + priceSell);
		return true;
	}
}
