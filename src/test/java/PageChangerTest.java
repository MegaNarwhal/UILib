import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.junit.Test;
import us.blockbox.uilib.PageChangerFactory;
import us.blockbox.uilib.ViewManager;
import us.blockbox.uilib.ViewPaginatorImpl;
import us.blockbox.uilib.component.Component;
import us.blockbox.uilib.component.FillerItem;
import us.blockbox.uilib.component.PageChanger;
import us.blockbox.uilib.view.InventoryView;
import us.blockbox.uilib.view.View;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.Assert.*;

public class PageChangerTest{
	/**
	 * Test pagination of a View under a superview.
	 */
	@Test
	public void testSubviewPagination(){
		int expectedPages = 5;
		int expectedTraversals = (expectedPages * 2) - 2;
		int componentSize = (expectedPages) * 9;
		Component[] components = new Component[componentSize];
		ItemStack stack = new ItemStack(Material.DIRT);
		for(int i = 0; i < components.length; i++){
			components[i] = new FillerItem("dummy","dummy",null,stack);
		}
		View superView = InventoryView.create("Superview",components);
		UUID uuid = UUID.randomUUID();
		Player p = new MockPlayer(uuid);
		final ViewManager viewManager = new MockViewManager();
		viewManager.setView(p,superView);
		Material pageChangerType = Material.STONE;
		final ItemStack selector = new ItemStack(pageChangerType);
		PageChangerFactory pageChangerFactory = new PageChangerFactory(){
			@Override
			public PageChanger createNext(int page){
				return new MockPageChangerImpl(viewManager,"Previous","previous",null,selector,null);
			}

			@Override
			public PageChanger createPrevious(int page){
				return new MockPageChangerImpl(viewManager,"Next","next",null,selector,null);
			}
		};
		View paginated = new ViewPaginatorImpl("Test View",components,1,pageChangerFactory).paginate().get(0);
		viewManager.descendView(p,paginated);
		View view = viewManager.getView(p);
		assertNotNull(view);
		assertEquals(view,paginated);
		Collection<View> visited = new HashSet<>(expectedPages);
		visited.add(view);
		int traversals = 0;
		//go forward through pages
		while(true){
			boolean foundSomething = false;
			for(int i = 0; i < view.size(); i++){
				Component item = view.getItem(i);
				if(item instanceof PageChanger){
					View link = ((PageChanger)item).getLink();
					assertNotNull(link);
					if(!visited.contains(link)){
						assertTrue(item.select(p,ClickType.LEFT));
						visited.add(link);
						view = viewManager.getView(p);
						traversals++;
						foundSomething = true;
						break;
					}
				}
			}
			if(!foundSomething){
				break;
			}
		}
		int pages = visited.size();
		//go back through pages
		visited.clear();
		visited.add(view);
		while(true){
			boolean foundSomething = false;
			for(int i = 0; i < view.size(); i++){
				Component item = view.getItem(i);
				if(item instanceof PageChanger){
					View link = ((PageChanger)item).getLink();
					if(!visited.contains(link)){
						assertTrue(item.select(p,ClickType.LEFT));
						visited.add(link);
						view = viewManager.getView(p);
						traversals++;
						foundSomething = true;
						break;
					}
				}
			}
			if(!foundSomething){
				break;
			}
		}
		assertEquals(expectedPages,pages); //correct number
		assertEquals(pages,visited.size()); //same number both ways
		assertEquals(expectedTraversals,traversals);
		assertTrue(viewManager.openSuperview(p,false));
		View backToSuper = viewManager.getView(p);
		assertEquals(superView,backToSuper);
	}
}
