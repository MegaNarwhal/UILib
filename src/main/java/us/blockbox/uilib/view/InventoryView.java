package us.blockbox.uilib.view;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import us.blockbox.uilib.component.IComponent;

import java.util.Arrays;

/**
 * An array of Items to be represented as ItemStacks inside a Bukkit Inventory.
 *
 * @since 1.0
 */
public class InventoryView implements View{
	private final String name;
	private final IComponent[] components;
	private View viewSuper;

	public InventoryView(String name){
		this(name,new IComponent[9]);
	}

	public InventoryView(String name,IComponent[] components){
		this(name,components,null);
	}

	public InventoryView(String name,IComponent[] components,View viewSuper){
		this.name = name;
		if(components == null){
			throw new IllegalArgumentException("Component array cannot be null");
		}
		if((components.length % 9) != 0){ //todo maybe auto-pad arrays that aren't a multiple of 9
			throw new IllegalArgumentException("Component array length must be a multiple of 9");
		}
		this.components = Arrays.copyOf(components,components.length);
		this.viewSuper = viewSuper;
	}

	public static View create(String name,IComponent[] components){
		int size = InventoryView.roundUpToNine(components.length);
		IComponent[] v = new IComponent[size];
		System.arraycopy(components,0,v,0,components.length);
		return new InventoryView(name,v);
	}

	public static View createCentered(String name,IComponent[] components){ //todo spread out even number of components less than 8
		int size = InventoryView.roundUpToNine(components.length);
		int eachSide = (size - components.length) / 2;
//		System.out.println("Size: " + size + ", Empty slots on each side: " + eachSide);
		IComponent[] v = new IComponent[size];
		System.arraycopy(components,0,v,eachSide,components.length);
		return new InventoryView(name,v);
	}

	public static View padVertically(String name,int paddingRows,IComponent[] components){
		int paddingTop = paddingRows * 9;
		IComponent[] v = new IComponent[components.length + (paddingTop * 2)];
		System.arraycopy(components,0,v,paddingTop,components.length);
		return new InventoryView(name,v);

	}

	@Override
	public String getName(){
		return name;
	}

	/**
	 * @param index The index of the component to retrieve.
	 * @return The {@link IComponent} at the given index.
	 * @throws IndexOutOfBoundsException If the given index is greater than or equal to {@link #size()}
	 */
	@Override
	public IComponent getItem(int index){
		return components[index];
	}

	@Override
	public IComponent getItem(ItemStack stack){
		for(IComponent it : components){
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
		for(IComponent it : components){
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

	public static class Builder implements ViewBuilder{
		private String name;
		private IComponent[] components;
		private View superview;

		@Override
		public ViewBuilder setName(String name){
			this.name = name;
			return this;
		}

		@Override
		public ViewBuilder setComponents(IComponent[] components){
			this.components = components;
			return this;
		}

		@Override
		public ViewBuilder setSuperview(View superview){
			this.superview = superview;
			return this;
		}

		@Override
		public View build(){
			return new InventoryView(name,components,superview);
		}
	}
}