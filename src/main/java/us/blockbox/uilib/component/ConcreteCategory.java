package us.blockbox.uilib.component;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import us.blockbox.uilib.UIPlugin;
import us.blockbox.uilib.view.View;
import us.blockbox.uilib.ViewManager;

public class ConcreteCategory implements ICategory{
	private static final ViewManager viewManager = UIPlugin.getViewManager();
	private String name;
	private String id;
	private String description;
	private ItemStack stack;
	private View viewSub;

	public ConcreteCategory(String name,String id,String description,ItemStack stack,View viewSub){
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

	public static class Builder{
		String name;
		String id;
		String description;
		ItemStack stack;
		View subview;

		public Builder(){
		}

		public Builder(String name,String id,String description,ItemStack stack){

		}

		public Builder setName(String name){
			this.name = name;
			return this;
		}

		public Builder setId(String id){
			this.id = id;
			return this;
		}

		public Builder setDescription(String description){
			this.description = description;
			return this;
		}

		public Builder setStack(ItemStack stack){
			this.stack = stack;
			return this;
		}

		public Builder setSubview(View subview){
			this.subview = subview;
			return this;
		}

		public ConcreteCategory build(){
			return new ConcreteCategory(name,id,description,stack,subview);
		}
	}
}
