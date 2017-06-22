package us.blockbox.uilib.api;

/**
 * A history of the {@link View}s through which a viewer has navigated to reach their current {@link View}.
 */
public interface ViewHistory{
	/**
	 * @return The current {@link View}
	 */
	View current();

	/**
	 * Get the {@link View} immediately before the current {@link View} without modifying this ViewHistory.
	 * @return The previous {@link View}
	 */
	View getPrevious();

	/**
	 * @return The number of {@link View} elements in this ViewHistory
	 */
	int size();
}
