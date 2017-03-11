package us.blockbox.uilib.component;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class CommandItem extends AbstractItem{
	private final String command;

	public CommandItem(String name,String id,ItemStack stack,String command){
		super(name,id,stack);
		this.command = command;
	}

	public CommandItem(String name,String id,String description,ItemStack stack,String command){
		super(name,id,description,stack);
		this.command = command;
	}

	@Override
	public boolean select(Player viewer,ClickType clickType){
		return viewer.performCommand(command);
	}
}
