package us.blockbox.uilib.view;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import us.blockbox.uilib.component.Component;

public class ImmutableView implements View{
	private final View view;

	public ImmutableView(View view){
		this.view = view;
	}

	@Override
	public String getName(){
		return view.getName();
	}

	@Override
	public Component getItem(int index){
		return view.getItem(index);
	}

	@Override
	public Component getItem(ItemStack stack){
		return view.getItem(stack);
	}

	@Override
	public ItemStack[] asContents(){
		return view.asContents();
	}

	@Override
	public Inventory asInventory(){
		return view.asInventory();
	}

	@Override
	public int size(){
		return view.size();
	}

	@Override
	public Component set(int i,Component component) throws UnsupportedOperationException{
		throw new UnsupportedOperationException();
	}
}