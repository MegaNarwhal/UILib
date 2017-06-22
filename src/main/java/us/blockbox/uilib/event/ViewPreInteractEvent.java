package us.blockbox.uilib.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import us.blockbox.uilib.api.View;

public class ViewPreInteractEvent extends ViewEvent implements Cancellable{
	private boolean cancelled;
	private final int slot;

	public ViewPreInteractEvent(Player viewer,View view,int slot){
		super(viewer,view);
		this.slot = slot;
	}

	public int getSlot(){
		return slot;
	}

	@Override
	public boolean isCancelled(){
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel){
		cancelled = cancel;
	}
}
