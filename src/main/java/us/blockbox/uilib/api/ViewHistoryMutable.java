package us.blockbox.uilib.api;

/**
 * A {@link ViewHistory} that can be modified.
 */
public interface ViewHistoryMutable extends ViewHistory{
	/**
	 * Adds a {@link View} to the head of this ViewHistory.
	 *
	 * @param v The view to add.
	 */
	void add(View v);

	/**
	 * Removes the {@link View} at the head of this ViewHistory.
	 *
	 * @return The removed {@link View}, or null if the ViewHistory was empty
	 */
	View back();

	/**
	 * Overwrites the {@link View} at the head of this ViewHistory. This does not affect what the viewer sees. If the
	 * history is empty, this is the same as {@link ViewHistoryMutable#add(View)}.
	 *
	 * @param view The new current {@link View}
	 * @return The {@link View} that was at the head of this ViewHistory, or null if it was empty.
	 */
	View setCurrent(View view);
}
