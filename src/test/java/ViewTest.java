import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.junit.Assert;
import org.junit.Test;
import us.blockbox.uilib.ViewManager;
import us.blockbox.uilib.component.*;
import us.blockbox.uilib.view.InventoryView;
import us.blockbox.uilib.view.View;

import java.util.UUID;

import static org.junit.Assert.fail;

public class ViewTest{
	@Test
	public void testPagination() throws Exception{
		Component[] components = new Component[27];
		ItemStack stack = new ItemStack(Material.DIRT);
		for(int i = 0; i < components.length; i++){
			components[i] = new FillerItem("dummy","dummy",null,stack);
		}
		Material pageChangerType = Material.STONE;
		ItemStack selector = new ItemStack(pageChangerType);
		View v = InventoryView.createPaginated("Test View",components,1,selector,selector);
		int page = 0;
		while(true){
			int index = v.size() - 1;
			Component c = v.getItem(index);
			if(c instanceof PageChanger){
				if(c.getItemStack().getType() != pageChangerType){
					fail("Page changer is wrong type. Index: " + index);
				}
				View link = ((PageChanger)c).getLink();
				if(link == null){
					fail("Page link leads to null View! Index: " + index);
				}
				v = link;
				page++;
			}else{
				break;
			}
		}
		if(page != 2){
			fail("Incorrect page count!");
		}
	}

	@Test
	public void testSubview(){
		ViewManager viewManager = new MockViewManager();
		Component filler = FillerItem.create(new ItemStack(Material.GOLD_BLOCK));
		Component[] subc = new Component[23];
		FillerItem fillerItem = FillerItem.create(new ItemStack(Material.GOLD_SWORD));
		for(int i = 0; i < subc.length; i++){
			subc[i] = fillerItem;
		}
		View sub = InventoryView.create("Paginated View",subc);
		Category cat = new CategoryImpl("Sub","sub",null,new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)5),sub);
		Component[] superc = new Component[]{filler,cat,filler};
		View superv = InventoryView.create("Super",superc);
		UUID uuid = UUID.randomUUID();
		Player p = new MockPlayer(uuid);
		viewManager.setView(p,superv);
		View view = viewManager.getView(uuid);
		if(view != superv){
			fail("Incorrect view in ViewManager!");
		}
		int size = view.size();
		for(int i = 0; i < size; i++){
			Component item = view.getItem(i);
			if(item instanceof Category){
				if(i == 1){
					View subview = ((Category)item).getSubview();
					viewManager.descendView(p,subview);
					View newCurrent = viewManager.getView(uuid);
					if(newCurrent != subview){
						fail("Subview is not the expected subview!");
					}
					Assert.assertTrue(viewManager.openSuperview(p,false));
					View backToSuper = viewManager.getView(uuid);
					if(backToSuper != superv){
						fail("Superview from history is not the same as original superview!");
					}
				}else{
					fail("Category where there shouldn't be one!");
				}
			}
		}
	}

}