import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import us.blockbox.uilib.Consumer;
import us.blockbox.uilib.ViewManager;
import us.blockbox.uilib.component.PageChanger;
import us.blockbox.uilib.view.View;

public class MockPageChangerImpl implements PageChanger{
	private final ViewManager viewManager;
	private final String name;
	private final String id;
	private final String description;
	private final ItemStack stack;
	private final Consumer<Player> onClick;
	private View link;

	MockPageChangerImpl(ViewManager viewManager,String name,String id,String description,ItemStack stack,Consumer<Player> onClick){
		this.name = name;
		this.id = id;
		this.description = description;
		this.stack = stack.clone();
		this.viewManager = viewManager;
		this.onClick = onClick;
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
