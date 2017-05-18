package us.blockbox.uilib;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import us.blockbox.uilib.event.ViewChangeEvent;
import us.blockbox.uilib.view.View;

import java.util.*;

/**
 * Manages each Player's current View. This may become inconsistent if there are multiple ViewManagers.
 */
public class ViewManager{
	private final Map<UUID,ViewHistory> viewMap = new HashMap<>();
	private final JavaPlugin plugin;
	private final Set<UUID> ignored = new HashSet<>();

	public ViewManager(JavaPlugin plugin){
		this.plugin = plugin;
	}

	public View getView(Player p){
		return getViewHistory(p).current();
	}

	public boolean hasView(Player p){
		return getView(p) != null;
	}

	/**
	 * Sets the player's current View, clearing any view history.
	 *
	 * @param p The Player whose View to set
	 * @param v The View to set
	 * @return The previous View, or null if the player did not have an open View
	 */
	public View setView(Player p,View v){
		return setView(p,v,false);
	}

	public View setView(Player p,View v,boolean ignoreNext){
		ViewHistory put = viewMap.put(p.getUniqueId(),new ViewHistory(new ArrayList<>(Collections.singletonList(v))));
		View prev = (put == null ? null : put.current());
		View view = getView(p);
		System.out.println(view == null ? "null" : view.getName());
		if(ignoreNext && hasView(p)){
			ignoreNextClose(p);
		}
		openView(p,v);
		return prev;
	}

	/**
	 * Remove a Player's ViewHistory and ignore lock if present. Care must be taken to close the Player's current View
	 * immediately after calling this method to avoid leakage of unprotected view contents.
	 *
	 * @param p The player to remove.
	 * @return The Player's current view.
	 * @see #closeView(Player)
	 */
	public View exit(Player p){
		ViewHistory remove = viewMap.remove(p.getUniqueId());
		removeIgnore(p);
//		System.out.println("Clearing view history for " + p.getName());
		if(remove == null){
			return null;
		}
		return remove.current();
	}

	private ViewHistory getViewHistory(Player p){
		UUID id = p.getUniqueId();
		if(viewMap.containsKey(id)){
			return viewMap.get(id);
		}else{
			ViewHistory value = new ViewHistory();
			viewMap.put(id,value);
			return value;
		}
	}

	private void ignoreNextClose(Player p){
//		System.out.println("Will ignore next close");
		ignored.add(p.getUniqueId());
	}

	private void removeIgnore(Player p){
		ignored.remove(p.getUniqueId());
	}

	private boolean isIgnored(Player p){
		if(ignored.contains(p.getUniqueId())){
//			System.out.println("Ignoring close");
			return true;
		}
		return false;
	}

	public boolean descendView(Player p,View v){
		if(v == null){
			return false;
		}
		ViewHistory h = getViewHistory(p);
		View prev = h.current();
		if(prev != null){
			ignoreNextClose(p);
		}
		openView(p,v);
		h.add(v);
		Bukkit.getPluginManager().callEvent(new ViewChangeEvent(p,prev,v));
		return true;
	}

	void openView(Player p,View v){
		if(v == null){
			removeIgnore(p); //todo is this enough to prevent ignoring closes that shouldn't be and still ignoring ones that should be?
		}else{
			p.openInventory(v.asInventory());
		}
	}

	private void delay(final Player p,final IAction<Player> action,long ticks){ //todo don't schedule task if no delay?
		new BukkitRunnable(){
			@Override
			public void run(){
				action.act(p);
			}
		}.runTaskLater(plugin,ticks);
	}

	/**
	 * Opens the superview of the player's current View if there is one. The opening will be done with a delay of one
	 * tick to avoid issues with InventoryCloseEvent.
	 *
	 * @return True if the player's View had a superview and it was opened.
	 */
	public boolean openSuperview(Player p,boolean ignoreNext){
		if(isIgnored(p)){
			removeIgnore(p);
			return false;
		}
		ViewHistory h = getViewHistory(p);
		if(h.getPrevious() == null){
//			System.out.println("history is empty, closing.");
			closeView(p);
			return false;
		}else{
			View back = h.back();
			//openView causes an InventoryCloseEvent that should be ignored in cases of a player clicking in the gui
			if(ignoreNext){
				ignoreNextClose(p);
			}
			openView(p,back);
			return true;
		}
	}

	public View getPreviousView(Player p){
		return getViewHistory(p).getPrevious();
	}

	public boolean closeView(final Player p){
		if(hasView(p)){
			new BukkitRunnable(){
				@Override
				public void run(){
					p.closeInventory();
					exit(p);
				}
			}.runTaskLater(plugin,1L);
			return true;
		}
		return false;
	}


	public void closeAll(){
		for(UUID id : viewMap.keySet()){
			Player p = Bukkit.getPlayer(id);
			if(p != null){
				p.closeInventory();
			}
		}
	}
}
