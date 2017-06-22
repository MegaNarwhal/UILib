package us.blockbox.uilib.component;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import us.blockbox.uilib.api.Category;
import us.blockbox.uilib.api.View;
import us.blockbox.uilib.api.ViewManager;
import us.blockbox.uilib.viewmanager.ViewManagerFactory;

public class CategoryImpl implements Category{
	private static final ViewManager viewManager = ViewManagerFactory.getInstance();
	private final String name;
	private final String id;
	private final String description;
	private final ItemStack stack;
	private final View viewSub;

	public CategoryImpl(String name,String id,String description,ItemStack stack,View viewSub){
		this.name = name;
		this.id = id;
		this.description = description;
		this.stack = stack;
		this.viewSub = viewSub;
	}

	@Override
	public boolean select(Player viewer,ClickType clickType){
		return viewManager.descendView(viewer,viewSub);
	}

	@Override
	public String getName(){
		return name;
	}

	@Override
	public String getId(){
		return id;
	}

	@Override
	public String getDescription(){
		return description;
	}

	@Override
	public ItemStack getItemStack(){
		return stack.clone();
	}

	@Override
	public View getSubview(){
		return viewSub;
	}
}
