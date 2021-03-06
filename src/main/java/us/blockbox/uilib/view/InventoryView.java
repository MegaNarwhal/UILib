package us.blockbox.uilib.view;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import us.blockbox.uilib.api.*;
import us.blockbox.uilib.api.util.ItemBuilder;
import us.blockbox.uilib.component.PageChangerImpl;
import us.blockbox.uilib.component.PageChangerImplFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An array of Items to be represented as ItemStacks inside a Bukkit Inventory.
 *
 * @since 1.0
 */
public class InventoryView implements View{
	private final String name;
	private final Component[] components;

	public InventoryView(String name,Component[] components){
		Validate.notNull(name);
		Validate.notNull(components);
		this.name = name;
		if((components.length % 9) != 0){
			throw new IllegalArgumentException("Component array length must be a multiple of 9");
		}
		this.components = Arrays.copyOf(components,components.length);
	}

	@Override
	public Component set(int i,Component component){
		Component old = components[i];
		components[i] = component;
		return old;
	}

	/**
	 * @since 0.0.2
	 */
	public static View createPaginated(String name,Component[] components,int pageRows){
		return createPaginated(name,components,pageRows,new ItemBuilder(new ItemStack(Material.ARROW)).name(ChatColor.GREEN + "Previous Page").build(),new ItemBuilder(new ItemStack(Material.ARROW)).name(ChatColor.GREEN + "Next Page").build());
	}

	public static View createPaginated(String name,Component[] components,int pageRows,ItemStack prevSelector,ItemStack nextSelector){
		Consumer<Player> onClick = new Consumer<Player>(){
			@Override
			public void accept(Player player){
				player.playSound(player.getLocation(),Sound.UI_BUTTON_CLICK,4,1);
			}
		};
		PageChangerFactory pageChangerFactory = new PageChangerImplFactory(prevSelector,nextSelector,onClick);
		ViewPaginator paginator = new ViewPaginatorImpl(name,components,pageRows,pageChangerFactory);
		return paginator.paginate().get(0);
	}

	@Deprecated
	public static View createPaginatedOld(String name,Component[] components,int pageRows,ItemStack prevSelector,ItemStack nextSelector){
		int pageSize = pageRows * 9;
		int pages = (int)Math.ceil(components.length / ((double)pageSize));
		if(pages == 1){
			return InventoryView.create(name,components);
		}
		int pageNum = 0;
		List<View> views = new ArrayList<>(pages);
		int isrc = 0;
		Consumer<Player> onClick = new Consumer<Player>(){
			@Override
			public void accept(Player player){
				player.playSound(player.getLocation(),Sound.UI_BUTTON_CLICK,4,1);
			}
		};
		while(true){
			Component[] page = new Component[pageSize];
			int i = 0;
			while(isrc < components.length && i < pageSize){ //todo page numbers for prev/next buttons?
				Component c;
				int currentPage = pageNum + 1;
				if(currentPage > 1 && i == 0){//if this isn't the first page, insert a link to previous page as first item.
//					System.out.println("Inserting previous page link");
					c = new PageChangerImpl("Previous Page","pageprev",null,prevSelector,onClick);
				}else if(currentPage < pages && i == pageSize - 1){//if this isn't the last page, insert a link to next page as the last item.
//					System.out.println("Inserting next page link");
					c = new PageChangerImpl("Next Page","pagenext",null,nextSelector,onClick);
				}else{
					c = components[isrc];
					isrc++;
				}
				page[i] = c;
				i++;
			}
			//once the page is full, add the view to the list
			View v = new InventoryView(name,page);
			views.add(v);
			if(isrc >= components.length){
//				System.out.println("Finished adding all items to view.");
				break;
			}
			pageNum++;
		}
		//for each view, check for page buttons at the first and last items and set their view links
		View viewPrev = null;
		for(View view : views){
			Component thisFirst = view.getItem(0);
			if(thisFirst instanceof PageChanger){
//				System.out.println("Setting prev link from " + thisFirst + " to " + viewPrev);
				((PageChanger)thisFirst).setLink(viewPrev);
			}
			if(viewPrev != null){
				Component prevLast = viewPrev.getItem(viewPrev.size() - 1);
				if(prevLast instanceof PageChanger){
//					System.out.println("Setting next link from " + prevLast + " to " + view);
					((PageChanger)prevLast).setLink(view);
				}
			}
			viewPrev = view;
		}
		//return first view
		return views.get(0);
	}

	public static View create(String name,Component[] components){
		int size = InventoryView.roundUpToNine(components.length);
		Component[] v = new Component[size];
		System.arraycopy(components,0,v,0,components.length);
		return new InventoryView(name,v);
	}

	public static View createCentered(String name,Component[] components){ //todo spread out even number of components less than 8
		int size = InventoryView.roundUpToNine(components.length);
		int eachSide = (size - components.length) / 2;
//		System.out.println("Size: " + size + ", Empty slots on each side: " + eachSide);
		Component[] v = new Component[size];
		System.arraycopy(components,0,v,eachSide,components.length);
		return new InventoryView(name,v);
	}

	public static View padVertically(String name,int paddingRows,Component[] components){
		int paddingTop = paddingRows * 9;
		Component[] v = new Component[components.length + (paddingTop * 2)];
		System.arraycopy(components,0,v,paddingTop,components.length);
		return new InventoryView(name,v);

	}

	@Override
	public String getName(){
		return name;
	}

	/**
	 * @param index The index of the component to retrieve.
	 * @return The {@link Component} at the given index.
	 * @throws IndexOutOfBoundsException If the given index is greater than or equal to {@link #size()}
	 */
	@Override
	public Component getItem(int index){
		return components[index];
	}

	@Override
	public Component getItem(ItemStack stack){
		for(Component it : components){
			if(it.getItemStack().equals(stack)){
				return it;
			}
		}
		return null;
	}

	@Override
	public ItemStack[] asContents(){
		ItemStack[] is = new ItemStack[components.length];
		int i = 0;
		for(Component it : components){
			is[i] = (it == null) ? null : it.getItemStack().clone();
			i++;
		}
		return is;
	}

	@Override
	public Inventory asInventory(){
		Inventory inv = Bukkit.createInventory(null,size(),name);
		inv.setContents(asContents());
		return inv;
	}

	@Override
	public int size(){
		return components.length;
	}

	private static int roundUpToNine(int length){
		return (int)Math.ceil((double)length / 9D) * 9;
	}

	@Override
	public boolean equals(Object o){
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		InventoryView that = (InventoryView)o;

		if(!name.equals(that.name)) return false;
		// Probably incorrect - comparing Object[] arrays with Arrays.equals
		return Arrays.equals(components,that.components);
	}

	@Override
	public int hashCode(){
		int result = name.hashCode();
		result = 31 * result + Arrays.hashCode(components);
		return result;
	}
}