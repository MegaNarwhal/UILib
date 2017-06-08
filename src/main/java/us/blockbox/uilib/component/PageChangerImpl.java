package us.blockbox.uilib.component;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import us.blockbox.uilib.Consumer;
import us.blockbox.uilib.UIPlugin;
import us.blockbox.uilib.ViewManager;
import us.blockbox.uilib.view.View;

public final class PageChangerImpl extends AbstractItem implements PageChanger{
	private static final ViewManager viewManager = UIPlugin.getViewManager();
	private View link;

	public PageChangerImpl(String name,String id,String description,ItemStack stack){
		this(name,id,description,stack,null,null);
	}

	public PageChangerImpl(String name,String id,String description,ItemStack stack,View link){
		this(name,id,description,stack,null,link);
	}

	public PageChangerImpl(String name,String id,String description,ItemStack stack,Consumer<Player> onClick){
		this(name,id,description,stack,onClick,null);
	}

	public PageChangerImpl(String name,String id,String description,ItemStack stack,Consumer<Player> onClick,View link){
		super(name,id,description,stack,onClick);
		this.link = link;
	}

	@Override
	public boolean select(Player viewer,ClickType clickType){
		if(link == null) return false;
		if(onClick != null){
			onClick.accept(viewer);
		}
		viewManager.setView(viewer,link,true,true);//todo test
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
