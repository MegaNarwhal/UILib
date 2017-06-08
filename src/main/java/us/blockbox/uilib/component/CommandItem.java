package us.blockbox.uilib.component;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import us.blockbox.uilib.Consumer;

public class CommandItem extends AbstractItem{
	private final String command;

	public CommandItem(String name,String id,String description,ItemStack stack,Consumer<Player> onClick,String command){
		super(name,id,description,stack,onClick);
		this.command = command;
	}

	public CommandItem(String name,String id,ItemStack stack,String command){
		this(name,id,null,stack,null,command);
	}

	public CommandItem(String name,String id,String description,ItemStack stack,String command){
		this(name,id,description,stack,null,command);
	}

	@Override
	public boolean select(Player viewer,ClickType clickType){
		if(onClick != null){
			onClick.accept(viewer);
		}
		return viewer.performCommand(command);
	}

	public String getCommand(){
		return command;
	}
}