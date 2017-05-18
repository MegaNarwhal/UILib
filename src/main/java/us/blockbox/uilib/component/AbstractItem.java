package us.blockbox.uilib.component;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractItem implements Item{
	private final String name;
	private final String id;
	private final String description;
	private final ItemStack stack;

	public AbstractItem(String name,String id,ItemStack stack){
		this.name = name;
		this.id = id;
		this.stack = stack;
		this.description = null;
	}

	public AbstractItem(String name,String id,String description,ItemStack stack){
		this.name = name;
		this.id = id;
		this.description = description;
		this.stack = stack;
	}

/*	public AbstractItem(Map<String,Object> map){
		this.name = ((String)map.get("name"));
		this.id = ((String)map.get("id"));
		this.description = ((String)map.get("desc"));
		this.stack = ((ItemStack)map.get("itemstack"));
	}*/

	public static List<String> wordWrap(String string,int lineMax){
		String[] split = string.split(" ");
		int i = 0;
		List<String> lines = new ArrayList<>();
		StringBuilder b = new StringBuilder();
		int initialLength = b.length();
		int c = 0;
		while(i < split.length){
			String word = split[i];
			i++;
			b.append(word);
			c += word.length() + 1;
			if(c >= lineMax){
				String line = b.toString();
				lines.add(line);
				b = new StringBuilder(ChatColor.getLastColors(line));
				initialLength = b.length();
				c = 0;
			}else{
				b.append(" ");
				c += 1;
			}
		}
		if(b.length() > initialLength){
			lines.add(b.toString());
		}
		return lines;
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
