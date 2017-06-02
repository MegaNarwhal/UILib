import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import us.blockbox.uilib.ViewManager;
import us.blockbox.uilib.ViewManagerFactory;
import us.blockbox.uilib.component.Category;
import us.blockbox.uilib.component.CategoryImpl;
import us.blockbox.uilib.component.Component;
import us.blockbox.uilib.view.InventoryView;
import us.blockbox.uilib.view.View;

import java.util.UUID;

public class CategoryTest{
	@Before
	public void setUp() throws Exception{
		System.out.println("CATEGORY SETUP");
		ViewManagerFactory.setInstance(new MockViewManager());
	}

	@After
	public void tearDown() throws Exception{
		System.out.println("CATEGORY TEARDOWN");
		ViewManagerFactory.setInstance(null);
	}

	@Test
	public void testCategory(){
		Component[] components = new Component[0];
		View subView = InventoryView.create("Subview",components);
		Category c = new CategoryImpl("Test","test",null,new ItemStack(Material.GOLD_INGOT),subView);
		UUID uuid = UUID.randomUUID();
		Player p = new MockPlayer(uuid);
		ViewManager viewManager = ViewManagerFactory.getInstance();
		Assert.assertNotNull(viewManager);
		View current = viewManager.getView(p);
		Assert.assertNull(current);
		Assert.assertTrue(c.select(p,ClickType.LEFT));
		View category = viewManager.getView(p);
		Assert.assertEquals(subView,category);
	}
}