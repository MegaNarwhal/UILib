package us.blockbox.uilib;

import org.bukkit.entity.Player;
import us.blockbox.uilib.view.View;

import java.util.Set;
import java.util.UUID;

public interface ViewManager{
	View getView(Player p);

	View getView(UUID uuid);

	boolean hasView(Player p);

	boolean hasView(UUID uuid);

	Set<UUID> getViewers(String name);

	Set<UUID> getViewers(View view);

	/**
	 * Sets the player's current {@link View}, clearing any view history.
	 *
	 * @param p The Player whose View to set
	 * @param v The View to set
	 * @return The previous View, or null if the player did not have an open View
	 */
	View setView(Player p,View v);

	View setView(Player p,View v,boolean ignoreNext);

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
	View setView(Player p,View v,boolean ignoreNext,boolean preserveHistory);

	View exit(Player p);

	/**
	 * Open a new {@link View}, appending it to the head of the Player's {@link ViewHistory}.
	 *
	 * @param p The Player whose View to set
	 * @param v The View to set
	 * @return True if the Player's View was set, false otherwise.
	 * @see #setView(Player,View)
	 */
	boolean descendView(Player p,View v);

	/**
	 * Opens the superview of the player's current View if there is one.
	 *
	 * @return True if the player's View had a superview and it was opened.
	 */
	boolean openSuperview(Player p,boolean ignoreNext);

	ViewHistory getViewHistory(UUID uuid);

	View getPreviousView(Player p);

	boolean closeView(Player p);

	void closeAll();
}
