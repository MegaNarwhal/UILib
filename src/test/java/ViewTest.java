import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.junit.Test;
import us.blockbox.uilib.PageChangerFactory;
import us.blockbox.uilib.ViewManager;
import us.blockbox.uilib.ViewPaginatorImpl;
import us.blockbox.uilib.component.Category;
import us.blockbox.uilib.component.Component;
import us.blockbox.uilib.component.FillerItem;
import us.blockbox.uilib.component.PageChanger;
import us.blockbox.uilib.view.InventoryView;
import us.blockbox.uilib.view.View;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;

public class ViewTest{
	@Test
	public void testGetViewers(){
		ViewManager viewManager = new MockViewManager();
		List<Player> players = MockPlayer.getRandom(3);
		Player p1 = players.get(0);
		Player p2 = players.get(1);
		Player p3 = players.get(2);
		View view = InventoryView.create("Test",new Component[9]);
		View view2 = InventoryView.create("Test",new Component[10]);
		viewManager.setView(p1,view);
		viewManager.setView(p2,view);
		viewManager.setView(p3,view2);
		Set<UUID> viewersView = viewManager.getViewers(view);
		if(!(viewersView.size() == 2 & viewersView.contains(p1.getUniqueId()) && viewersView.contains(p2.getUniqueId()))){
			fail("Incorrect viewers");
		}
		Set<UUID> viewersName = viewManager.getViewers(view.getName());
		if(viewersName.size() != 3){
			fail("Incorrect viewers");
		}
	}

	@Test
	public void testPagination(){
		Component[] components = new Component[27];
		ItemStack stack = new ItemStack(Material.DIRT);
		for(int i = 0; i < components.length; i++){
			components[i] = new FillerItem("dummy","dummy",null,stack);
		}
		Material pageChangerType = Material.STONE;
		final ItemStack selector = new ItemStack(pageChangerType);
		final ViewManager viewManager = new MockViewManager();
		PageChangerFactory pageChangerFactory = new PageChangerFactory(){
			@Override
			public PageChanger createNext(int page){
				return new MockPageChangerImpl(viewManager,"Previous","previous",null,selector,null);
			}

			@Override
			public PageChanger createPrevious(int page){
				return new MockPageChangerImpl(viewManager,"Next","next",null,selector,null);
			}
		};//todo create class
		View v = new ViewPaginatorImpl("Test View",components,1,pageChangerFactory).paginate().get(0);
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
		assertEquals(2,page);
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
		View sub = InventoryView.create("Subview",subc);
		Category cat = new MockCategoryImpl(viewManager,"Sub","sub",null,new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)5),sub);
		Component[] superc = new Component[]{filler,cat,filler};
		View superv = InventoryView.create("Superview",superc);
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
				assertEquals(1,i);
				View subview = ((Category)item).getSubview();
				viewManager.descendView(p,subview);
				View newCurrent = viewManager.getView(uuid);
				if(newCurrent != subview){
					fail("Subview is not the expected subview!");
				}
				assertTrue(viewManager.openSuperview(p,false));
				View backToSuper = viewManager.getView(uuid);
				if(backToSuper != superv){
					fail("Superview from history is not the same as original superview!");
				}
			}
		}
	}
}