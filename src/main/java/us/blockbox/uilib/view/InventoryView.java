package us.blockbox.uilib.view;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import us.blockbox.uilib.Consumer;
import us.blockbox.uilib.ItemBuilder;
import us.blockbox.uilib.component.Component;
import us.blockbox.uilib.component.PageChanger;
import us.blockbox.uilib.component.PageChangerImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An array of Items to be represented as ItemStacks inside a Bukkit Inventory.
 *
 * @since 1.0
 */
public class InventoryView implements View{//todo remove viewSuper
	private final String name;
	private final Component[] components;
	private View viewSuper;

	public InventoryView(String name){
		this(name,new Component[9]);
	}

	public InventoryView(String name,Component[] components){
		this(name,components,null);
	}

	public InventoryView(String name,Component[] components,View viewSuper){
		this.name = name;
		if(components == null){
			throw new IllegalArgumentException("Component array cannot be null");
		}
		if((components.length % 9) != 0){
			throw new IllegalArgumentException("Component array length must be a multiple of 9");
		}
		this.components = Arrays.copyOf(components,components.length);
		this.viewSuper = viewSuper;
	}

	/**
	 * @since 0.0.2
	 */
	public static View createPaginated(String name,Component[] components,int pageRows){ //todo
//		int roundedSize = InventoryView.roundUpToNine(components.length);
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
					c = new PageChangerImpl("Previous Page","pageprev",null,new ItemBuilder(new ItemStack(Material.ARROW)).name(ChatColor.GREEN + "Previous Page").build(),onClick);
				}else if(currentPage < pages && i == pageSize - 1){//if this isn't the last page, insert a link to next page as the last item.
//					System.out.println("Inserting next page link");
					c = new PageChangerImpl("Next Page","pagenext",null,new ItemBuilder(new ItemStack(Material.ARROW)).name(ChatColor.GREEN + "Next Page").build(),onClick);
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
//		System.out.println("Created paginated view with " + views.size() + " pages");
		return views.get(0);
	}

//		while(true){
//			int iSrc = 0;
//			int iDest = 0;
//			Component[] pageComponents = new Component[InventoryView.roundUpToNine(pageSize)];
//			if(pageNum > 0){
//				//insert pagechanger
//				iDest++;
//			}
//			if(pageNum < pages){
//				iDest++;
//			}
//			while(iSrc < pageSize && iDest < ){
//				Component c = components[iSrc];
//				pageComponents[iDest] = c;
//				iDest++;
//				iSrc++;
//			}
//			if(pageNum < pages){
//				//insert pagechanger
//			}
//			if(pageNum < pages){
//				pageNum++;
//			}else{
//				break;
//			}
//		}

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
		Inventory inv = Bukkit.createInventory(null,size(),getName());
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
}