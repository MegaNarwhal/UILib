package us.blockbox.uilib.component;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import us.blockbox.uilib.api.AbstractItem;
import us.blockbox.uilib.api.Consumer;

/**
 * Runs a command as console. Replaces %s in the commandFormat string with the name of the viewer who selects it. For
 * example "give %s diamond 5" would give the player 5 diamonds.
 *
 * @since 0.0.17
 */
public class ConsoleCommandItem extends AbstractItem{
	private final String commandFormat;

	public ConsoleCommandItem(String name,String id,ItemStack stack,String commandFormat){
		this(name,id,null,stack,null,commandFormat);
	}

	public ConsoleCommandItem(String name,String id,String description,ItemStack stack,String commandFormat){
		this(name,id,description,stack,null,commandFormat);
	}

	public ConsoleCommandItem(String name,String id,String description,ItemStack stack,Consumer<Player> onClick,String commandFormat){
		super(name,id,description,stack,onClick);
		this.commandFormat = commandFormat;
	}

	@Override
	public boolean select(Player viewer,ClickType clickType){
		String command = String.format(commandFormat,viewer.getName());
		return Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command);
	}

	public String getCommandFormat(){
		return commandFormat;
	}
}