package us.blockbox.uilib.component;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import us.blockbox.uilib.UIPlugin;
import us.blockbox.uilib.ViewManager;
import us.blockbox.uilib.view.View;

public class PageChangerImpl implements PageChanger{
	private static final ViewManager viewManager = UIPlugin.getViewManager();
	private final String name;
	private final String id;
	private final String description;
	private final ItemStack stack;
	private View link;

	public PageChangerImpl(String name,String id,String description,ItemStack stack){
		this.name = name;
		this.id = id;
		this.description = description;
		this.stack = stack;
	}

	public PageChangerImpl(String name,String id,String description,ItemStack stack,View link){
		this.name = name;
		this.id = id;
		this.description = description;
		this.stack = stack;
		this.link = link;
	}

	@Override
	public String getId(){
		return id;
	}

	@Override
	public String getName(){
		return name;
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
	public boolean select(final Player viewer,ClickType clickType){
		if(link == null) return false;
		viewManager.setView(viewer,link);
		return true;
	}

	@Override
	public View getLink(){
		return link;
	}

	@Override
	public void setLink(View view){
		this.link = view;
	}
}
