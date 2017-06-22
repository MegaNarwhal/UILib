package us.blockbox.uilib.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import us.blockbox.uilib.api.View;

/**
 * Abstract base class for events involving Views.
 */
public abstract class ViewEvent extends Event{
	private static final HandlerList handlers = new HandlerList();

	private final Player viewer;
	private final View view;

	public ViewEvent(Player viewer,View view){
		this.viewer = viewer;
		this.view = view;
	}

	/**
	 * @return The Player involved in this event.
	 */
	public Player getViewer(){
		return viewer;
	}


	/**
	 * @return The View involved in this event.
	 */
	public View getView(){
		return view;
	}

	public HandlerList getHandlers(){
		return handlers;
	}

	public static HandlerList getHandlerList(){
		return handlers;
	}
}