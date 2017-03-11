package us.blockbox.uilib.view;

import us.blockbox.uilib.component.IComponent;

public interface ViewBuilder{
	ViewBuilder setName(String name);
	ViewBuilder setComponents(IComponent[] components);
	ViewBuilder setSuperview(View superview);
	View build();
}
