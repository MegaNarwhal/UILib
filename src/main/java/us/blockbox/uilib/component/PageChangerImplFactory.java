package us.blockbox.uilib.component;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.blockbox.uilib.api.Consumer;
import us.blockbox.uilib.api.PageChanger;
import us.blockbox.uilib.api.PageChangerFactory;

public class PageChangerImplFactory implements PageChangerFactory{
	private ItemStack previous;
	private ItemStack next;
	private Consumer<Player> onClick;

	public PageChangerImplFactory(ItemStack previous,ItemStack next,Consumer<Player> onClick){
		this.previous = previous;
		this.next = next;
		this.onClick = onClick;
	}

	@Override
	public PageChanger createNext(int page){
		return new PageChangerImpl("Next","pagenext",null,next,onClick);
	}

	@Override
	public PageChanger createPrevious(int page){
		return new PageChangerImpl("Previous","pageprev",null,previous,onClick);
	}
}