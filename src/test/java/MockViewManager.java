import org.bukkit.entity.Player;
import us.blockbox.uilib.ViewHistory;
import us.blockbox.uilib.ViewHistoryMutable;
import us.blockbox.uilib.ViewHistoryMutableImpl;
import us.blockbox.uilib.ViewManager;
import us.blockbox.uilib.view.View;

import java.util.*;

class MockViewManager implements ViewManager{
	private final Map<UUID,ViewHistoryMutable> viewMap = new HashMap<>();

	@Override
	public View getView(Player p){
		return null;
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
		return null;
	}

	@Override
	public Set<UUID> getViewers(View view){
		return null;
	}

	@Override
	public View setView(Player p,View v){
		UUID uuid = p.getUniqueId();
		ViewHistoryMutable put;
		put = viewMap.put(uuid,new ViewHistoryMutableImpl(new ArrayList<>(Collections.singletonList(v))));
		View prev;
		if(put == null){
			prev = null;
		}else{
			prev = put.current();
		}
		return prev;
	}

	@Override
	public View setView(Player p,View v,boolean ignoreNext){
		return null;
	}

	@Override
	public View setView(Player p,View v,boolean ignoreNext,boolean preserveHistory){
		return null;
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
}
