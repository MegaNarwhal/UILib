package us.blockbox.uilib.event;

import org.bukkit.entity.Player;
import us.blockbox.uilib.view.View;

/**
 * This Event is called when a Player's View changes as a result of a click.
 * This is currently not called when the Player closes their own Inventory or leaves the game.
 */
public class ViewChangeEvent extends ViewEvent{
	private final View viewNew;

	public ViewChangeEvent(Player viewer,View viewOld,View viewNew){
		super(viewer,viewOld);
		this.viewNew = viewNew;
	}

	/**
	 * @return The Player's new View.
	 */
	public View getViewNew(){
		return viewNew;
	}
}