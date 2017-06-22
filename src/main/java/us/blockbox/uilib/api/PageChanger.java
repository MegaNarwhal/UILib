package us.blockbox.uilib.api;

/**
 * A View that links to other Views. This differs from {@link Category} in that it does not descend, it simply divides a
 * {@link View} into multiple pages. This means a user can exit with a single action rather than being moved back in
 * their view history.
 */
public interface PageChanger extends Component{
	/**
	 * @return The {@link View} this PageChanger links to.
	 */
	View getLink();

	/**
	 * @param view The {@link View} this PageChanger should link to.
	 */
	void setLink(View view);
}