package us.blockbox.uilib;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import us.blockbox.uilib.api.Component;
import us.blockbox.uilib.api.View;
import us.blockbox.uilib.api.ViewManager;
import us.blockbox.uilib.event.ViewPreInteractEvent;

import java.util.Set;
import java.util.UUID;

public class InventoryListener implements Listener{
	private final JavaPlugin plugin;
	private final ViewManager viewManager;
	private final CooldownManager cooldownManager;

	public InventoryListener(JavaPlugin plugin,ViewManager viewManager){
		this.plugin = plugin;
		this.viewManager = viewManager;
		this.cooldownManager = new CooldownManager(plugin,3L);
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onClick(InventoryClickEvent e){
		if(!(e.getWhoClicked() instanceof Player)){
			System.out.println("Clicker was not player!");
			return;
		}
		int raw = e.getRawSlot();
		int topSize = e.getView().getTopInventory().getSize();
		Player p = (Player)e.getWhoClicked();
		View view = viewManager.getView(p);
		if(e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY && view != null){
			e.setCancelled(true);
		}
		if(!inView(raw,topSize)){
			return;
		}
		if(view != null && isView(view,e.getInventory())){//todo handle in view itself?
			e.setCancelled(true);
			Component item = view.getItem(raw);
			if(item != null){
				UUID uuid = p.getUniqueId();
				if(!cooldownManager.isCooling(uuid)){
					ViewPreInteractEvent preInteract = new ViewPreInteractEvent(p,view,raw);
					plugin.getServer().getPluginManager().callEvent(preInteract);
					if(!preInteract.isCancelled()){
						item.select(p,e.getClick());
						cooldownManager.add(uuid);
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onDrag(InventoryDragEvent e){
		if(!(e.getWhoClicked() instanceof Player)) return;
		Set<Integer> slots = e.getRawSlots();
		int ui = e.getView().getTopInventory().getSize();
		Player p = (Player)e.getWhoClicked();
		boolean inTopInventory = false;
		for(int raw : slots){
			if(inView(raw,ui)){
				inTopInventory = true;
				break;
			}
		}
		if(inTopInventory && viewManager.hasView(p)){
			e.setCancelled(true);
		}
	}

	private static boolean inView(int rawSlot,int viewSlots){
		return rawSlot >= 0 && rawSlot < viewSlots;
	}

	private static boolean isView(View view,Inventory inventory){
		return view.getName().equals(inventory.getName());
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e){
		HumanEntity entity = e.getPlayer();
		if(entity instanceof Player){
			final Player p = (Player)e.getPlayer();
			new BukkitRunnable(){
				@Override
				public void run(){
					viewManager.openSuperview(p,false); //Do not ignore the next close event
				}
			}.runTaskLater(plugin,1L);
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		viewManager.exit(e.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onTeleport(PlayerTeleportEvent e){
		viewManager.closeView(e.getPlayer());
	}

/*	@EventHandler
	public void onViewChange(ViewChangeEvent e){
		System.out.println("Old: " + (e.getView() == null ? "null" : e.getView().getName()) + ", New: " + e.getViewNew().getName());
	}*/
}
