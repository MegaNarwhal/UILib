package us.blockbox.uilib;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.blockbox.uilib.component.*;
import us.blockbox.uilib.view.InventoryView;
import us.blockbox.uilib.view.View;

import java.util.Random;

public class TestViewCommand implements CommandExecutor{

	private static final ViewManager viewManager = UIPlugin.getViewManager();
	private static final String ipsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

	@Override
	public boolean onCommand(CommandSender sender,Command command,String label,String[] args){
		if(!sender.isOp()) return true;
		Item filler = FillerItem.create(new ItemStack(Material.GOLD_BLOCK));
//		Item shopItem = new ShopItem("Test ShopItem","test1",ipsum,new ItemStack(Material.DIAMOND_PICKAXE),10.0,1.0);
		CloseButtonItem close = new CloseButtonItem("Close","close",new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)2));
		Component[] subc = new Component[18];
		for(int i = 0;i < subc.length - 1;i++){
			subc[i] = FillerItem.create(new ItemStack(Material.values()[new Random().nextInt(Material.values().length)]));
		}
		subc[17] = close;
		View sub = InventoryView.padVertically("Sub",1,subc);
		Category cat = new CategoryImpl("Sub","sub",null,new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)5),sub);
		Component[] superc = new Component[]{filler,cat,close,new CommandItem("Command","cmd",new ItemStack(Material.COMMAND),"tp 0 70 0"),filler};
		View superv = InventoryView.createCentered("Super",superc);
		viewManager.setView(((Player)sender),superv);
//		viewManager.descendView(((Player)sender),superv);
		return true;
	}
}