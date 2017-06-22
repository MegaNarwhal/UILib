package us.blockbox.uilib.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractItem implements Component{
	private final String name;
	private final String id;
	private final String description;
	private final ItemStack stack;
	protected final Consumer<Player> onClick;

	public AbstractItem(String name,String id,String description,ItemStack stack,Consumer<Player> onClick){
		this.name = name;
		this.id = id;
		this.description = description;
		this.stack = stack.clone();
		this.onClick = onClick;
	}

	public AbstractItem(String name,String id,ItemStack stack){
		this(name,id,null,stack);
	}

	public AbstractItem(String name,String id,String description,ItemStack stack){
		this(name,id,description,stack,null);
	}

/*	public AbstractItem(Map<String,Object> map){
		this.name = ((String)map.get("name"));
		this.id = ((String)map.get("id"));
		this.description = ((String)map.get("desc"));
		this.stack = ((ItemStack)map.get("itemstack"));
	}*/

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

/*	@Override
	public Map<String,Object> serialize(){
		Map<String,Object> map = new HashMap<>();
		map.put("name",getName());
		map.put("id",getId());
		map.put("desc",getDescription());
		map.put("itemstack",getItemStack());
		return map;
	}*/
}
