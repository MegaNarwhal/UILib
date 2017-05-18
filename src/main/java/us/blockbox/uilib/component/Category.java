package us.blockbox.uilib.component;

import us.blockbox.uilib.view.View;

/**
 * A Component that belongs to a View and may link to a subview.
 * @since 1.0
 */
public interface Category extends Component{
	View getSubview();
}
