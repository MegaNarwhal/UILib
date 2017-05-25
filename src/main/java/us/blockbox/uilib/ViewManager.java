package us.blockbox.uilib;

import org.bukkit.Server;
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
	private final Map<UUID,ViewHistoryMutable> viewMap = new HashMap<>();
	private final JavaPlugin plugin;
	private final Set<UUID> ignored = new HashSet<>();

	ViewManager(JavaPlugin plugin){
		this.plugin = plugin;
	}

	public View getView(Player p){
		return getView(p.getUniqueId());
	}

	public View getView(UUID uuid){
		ViewHistoryMutable h = viewMap.get(uuid);
		return h == null ? null : h.current();
	}

	public boolean hasView(Player p){
		return hasView(p.getUniqueId());
	}

	public boolean hasView(UUID uuid){
		return getView(uuid) != null;
	}

	public Set<UUID> getViewers(String name){
		Set<UUID> viewers = new HashSet<>(getInitialCapacity());
		for(Map.Entry<UUID,ViewHistoryMutable> e : viewMap.entrySet()){
			View current = e.getValue().current();
			if(current != null && current.getName().equals(name)){
				viewers.add(e.getKey());
			}
		}
		return viewers;
	}

	private int getInitialCapacity(){
		return Math.max(16,viewMap.size() / 2);
	}

	public Set<UUID> getViewers(View view){
		Set<UUID> viewers = new HashSet<>(getInitialCapacity());
		for(Map.Entry<UUID,ViewHistoryMutable> e : viewMap.entrySet()){
			if(e.getValue().current().equals(view)){
				viewers.add(e.getKey());
			}
		}
		return viewers;
	}

	/**
	 * Sets the player's current {@link View}, clearing any view history.
	 *
	 * @param p The Player whose View to set
	 * @param v The View to set
	 * @return The previous View, or null if the player did not have an open View
	 */
	public View setView(Player p,View v){
		return setView(p,v,false);
	}

	public View setView(Player p,View v,boolean ignoreNext){ //todo option to preserve history and just change current view
		return setView(p,v,ignoreNext,false);
	}

	/**
	 * Sets the Player's current {@link View} without adding to their {@link ViewHistory}.
	 *
	 * @param p               The Player whose View to set
	 * @param v               The View to set
	 * @param ignoreNext      If the next InventoryCloseEvent should be ignored. This avoids opening the superview when
	 *                        switching Views.
	 * @param preserveHistory If the Player's {@link ViewHistory} should be preserved if there is any. This is useful
	 *                        for paginated subviews.
	 * @return The previous View, or null if the player did not have an open View
	 */
	public View setView(Player p,View v,boolean ignoreNext,boolean preserveHistory){
		UUID uuid = p.getUniqueId();
		ViewHistoryMutable put;
		if(preserveHistory){//todo make this cleaner
			put = viewMap.get(uuid);
		}else{
			put = viewMap.put(uuid,new ViewHistoryMutableImpl(new ArrayList<>(Collections.singletonList(v))));
		}
		View prev;
		if(put == null){
			prev = null;
			if(preserveHistory){
				viewMap.put(uuid,new ViewHistoryMutableImpl(new ArrayList<>(Collections.singletonList(v))));
			}
		}else{
			prev = put.current();
			if(preserveHistory){
				put.setCurrent(v);
			}
		}
		if(ignoreNext && hasView(p)){
			ignoreNextClose(uuid);
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
	View exit(Player p){
		UUID uuid = p.getUniqueId();
		ViewHistory remove = viewMap.remove(uuid);
		removeIgnore(uuid);
//		System.out.println("Clearing view history for " + p.getName());
		if(remove == null){
			return null;
		}
		return remove.current();
	}

	private ViewHistoryMutable getHistoryAndCreateIfAbsent(UUID uuid){
		ViewHistoryMutable h = viewMap.get(uuid);
		if(h == null){
			ViewHistoryMutable value = new ViewHistoryMutableImpl(); //todo any spots where creating history can be avoided?
			viewMap.put(uuid,value);
			return value;
		}
		return h;
	}

	private void ignoreNextClose(UUID uuid){
//		System.out.println("Will ignore next close");
		ignored.add(uuid);
	}

	private void removeIgnore(UUID uuid){
		ignored.remove(uuid);
	}

	private boolean isIgnored(UUID uuid){
		return ignored.contains(uuid);
	}

	/**
	 * Open a new {@link View}, appending it to the head of the Player's {@link ViewHistory}.
	 *
	 * @param p The Player whose View to set
	 * @param v The View to set
	 * @return True if the Player's View was set, false otherwise.
	 * @see #setView(Player,View)
	 */
	public boolean descendView(Player p,View v){
		if(v == null){
			return false;
		}
		UUID uuid = p.getUniqueId();
		ViewHistoryMutable h = getHistoryAndCreateIfAbsent(uuid);
		View prev = h.current();
		if(prev != null){
			ignoreNextClose(uuid);
		}
		openView(p,v);
		h.add(v);
		plugin.getServer().getPluginManager().callEvent(new ViewChangeEvent(p,prev,v));
		return true;
	}

	void openView(Player p,View v){
		if(v == null){
			removeIgnore(p.getUniqueId()); //todo is this enough to prevent ignoring closes that shouldn't be and still ignoring ones that should be?
		}else{
			p.openInventory(v.asInventory());
		}
	}

	/**
	 * Opens the superview of the player's current View if there is one.
	 *
	 * @return True if the player's View had a superview and it was opened.
	 */
	public boolean openSuperview(Player p,boolean ignoreNext){
		UUID uuid = p.getUniqueId();
		if(isIgnored(uuid)){
			removeIgnore(uuid);
			return false;
		}
//		ViewHistoryMutable h = getHistoryAndCreateIfAbsent(uuid);
		ViewHistoryMutable h = viewMap.get(uuid);
		if(h == null || h.getPrevious() == null){
//			System.out.println("history is empty, closing.");
			closeView(p);
			return false;
		}else{
			View back = h.back();
			//openView causes an InventoryCloseEvent that should be ignored in cases of a player clicking in the gui
			if(ignoreNext){
				ignoreNextClose(uuid);
			}
			openView(p,back);
			return true;
		}
	}

	public ViewHistory getViewHistory(UUID uuid){
		ViewHistoryMutable h = viewMap.get(uuid);
		return h == null ? null : new ViewHistoryImpl(h);
	}

	public View getPreviousView(Player p){
		ViewHistoryMutable h = viewMap.get(p.getUniqueId());
		return h == null ? null : h.getPrevious();
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
		Server server = plugin.getServer();
		for(UUID id : viewMap.keySet()){
			Player p = server.getPlayer(id);
			if(p != null){
				p.closeInventory();
			}
		}
	}
}