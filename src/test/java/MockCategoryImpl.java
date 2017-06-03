import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import us.blockbox.uilib.ViewManager;
import us.blockbox.uilib.component.Category;
import us.blockbox.uilib.view.View;

public class MockCategoryImpl implements Category{
	private final ViewManager viewManager;
	private final String name;
	private final String id;
	private final String description;
	private final ItemStack stack;
	private final View viewSub;

	MockCategoryImpl(ViewManager viewManager,String name,String id,String description,ItemStack stack,View viewSub){
		this.viewManager = viewManager;
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