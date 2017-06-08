package us.blockbox.uilib;

import org.bukkit.ChatColor;
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
		////Create the icon for the dirt giving component
		//ItemStack giveDirtIcon = new ItemBuilder(new ItemStack(Material.COMMAND))
		//		.name(ChatColor.GREEN + "Free Dirt!")
		//		.build();
		////Create the component that runs our give command
		//Component giveDirt = new ConsoleCommandItem("Command","cmd",giveDirtIcon,"give %s dirt 10");
		////Create a close button
		//Component closeButton = new CloseButtonItem(
		//		"Close",
		//		"close",
		//		null,
		//		new ItemBuilder(new ItemStack(Material.BARRIER))
		//				.name("Close")
		//				.build()
		//);
		////Center the close button in our subview
		//View subView = InventoryView.createCentered(ChatColor.DARK_PURPLE + "Centered Subview!",new Component[]{closeButton});
		////Create a category component that links to our subview
		//Category categoryItem = new CategoryImpl("Subview","subview",null,new ItemStack(Material.GOLD_BLOCK),subView);
		////Put the components for the main view in an array
		//Component[] components = new Component[]{giveDirt,categoryItem};
		////Create the main view with 2 components
		//View superView = InventoryView.create("Main View",components);
		////Set the player's view to the main view, opening it on their screen
		//UIPlugin.getViewManager().setView(((Player)sender),superView);

		Item filler = FillerItem.create(new ItemStack(Material.GOLD_BLOCK));
//		Item shopItem = new ShopItem("Test ShopItem","test1",ipsum,new ItemStack(Material.DIAMOND_PICKAXE),10.0,1.0);
		CloseButtonItem close = new CloseButtonItem("Close","close",new ItemBuilder(
				new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)1)
		).name(ChatColor.RED + "Close").build());
		Component[] subc = new Component[23];
		Random random = new Random();
		for(int i = 0; i < subc.length - 1; i++){
			subc[i] = FillerItem.create(new ItemStack(Material.values()[random.nextInt(Material.values().length)]));
		}
		subc[subc.length - 1] = close;//
		View sub = InventoryView.createPaginated("Paginated View",subc,1);
		Category cat = new CategoryImpl("Sub","sub",null,new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)5),sub);
		Component[] superc = new Component[]{filler,cat,close,new ConsoleCommandItem("Command","cmd",new ItemStack(Material.COMMAND),"give %s dirt 10"),filler};
		View superv = InventoryView.createCentered("Super",superc);
		viewManager.setView(((Player)sender),superv);
		return true;
	}
}