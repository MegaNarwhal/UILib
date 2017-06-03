import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.junit.Test;
import us.blockbox.uilib.ViewManager;
import us.blockbox.uilib.component.Category;
import us.blockbox.uilib.component.Component;
import us.blockbox.uilib.view.InventoryView;
import us.blockbox.uilib.view.View;

import java.util.UUID;

import static org.junit.Assert.*;

public class CategoryTest{
	@Test
	public void testCategory(){
		ViewManager viewManager = new MockViewManager();
		Component[] components = new Component[0];
		View subView = InventoryView.create("Subview",components);
		Category c = new MockCategoryImpl(viewManager,"Test","test",null,new ItemStack(Material.GOLD_INGOT),subView);
		UUID uuid = UUID.randomUUID();
		Player p = new MockPlayer(uuid);
		assertNotNull(viewManager);
		View current = viewManager.getView(p);
		assertNull(current);
		assertTrue(c.select(p,ClickType.LEFT));
		View category = viewManager.getView(p);
		assertEquals(subView,category);
	}
}