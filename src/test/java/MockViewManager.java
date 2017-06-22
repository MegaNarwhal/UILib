import org.bukkit.entity.Player;
import us.blockbox.uilib.api.View;
import us.blockbox.uilib.api.ViewHistory;
import us.blockbox.uilib.api.ViewHistoryMutable;
import us.blockbox.uilib.api.ViewManager;
import us.blockbox.uilib.viewmanager.ViewHistoryMutableImpl;

import java.util.*;

class MockViewManager implements ViewManager{
	private final Map<UUID,ViewHistoryMutable> viewMap = new HashMap<>();

	@Override
	public View getView(Player p){
		return getView(p.getUniqueId());
	}

	@Override
	public View getView(UUID uuid){
		ViewHistoryMutable h = viewMap.get(uuid);
		return h == null ? null : h.current();
	}

	@Override
	public boolean hasView(Player p){
		return false;
	}

	@Override
	public boolean hasView(UUID uuid){
		return false;
	}

	@Override
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

	@Override
	public Set<UUID> getViewers(View view){
		Set<UUID> viewers = new HashSet<>(getInitialCapacity());
		for(Map.Entry<UUID,ViewHistoryMutable> e : viewMap.entrySet()){
			if(e.getValue().current().equals(view)){
				viewers.add(e.getKey());
			}
		}
		return viewers;
	}

	@Override
	public View setView(Player p,View v){
		return setView(p,v,false);
	}

	@Override
	public View setView(Player p,View v,boolean ignoreNext){
		return setView(p,v,ignoreNext,false);
	}

	@Override
	public View setView(Player p,View v,boolean ignoreNext,boolean preserveHistory){
		UUID uuid = p.getUniqueId();
		ViewHistoryMutable put;
		if(preserveHistory){
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
		return prev;
	}

	@Override
	public boolean descendView(Player p,View v){
		if(v == null){
			return false;
		}
		UUID uuid = p.getUniqueId();
		ViewHistoryMutable h = getHistoryAndCreateIfAbsent(uuid);
		h.add(v);
		return true;
	}

	@Override
	public boolean openSuperview(Player p,boolean ignoreNext){
		UUID uuid = p.getUniqueId();
//		ViewHistoryMutable h = getHistoryAndCreateIfAbsent(uuid);
		ViewHistoryMutable h = viewMap.get(uuid);
		if(h == null || h.getPrevious() == null){
//			System.out.println("history is empty, closing.");
			return false;
		}else{
			View back = h.back();
			//openView causes an InventoryCloseEvent that should be ignored in cases of a player clicking in the gui
			return true;
		}
	}

	@Override
	public ViewHistory getViewHistory(UUID uuid){
		return null;
	}

	@Override
	public View getPreviousView(Player p){
		return null;
	}

	@Override
	public boolean closeView(Player p){
		return false;
	}

	@Override
	public void closeAll(){

	}

	private ViewHistoryMutable getHistoryAndCreateIfAbsent(UUID uuid){
		ViewHistoryMutable h = viewMap.get(uuid);
		if(h == null){
			ViewHistoryMutable value = new ViewHistoryMutableImpl();
			viewMap.put(uuid,value);
			return value;
		}
		return h;
	}

	@Override
	public View exit(Player p){
		return null;
	}
}
