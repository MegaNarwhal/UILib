package us.blockbox.uilib.viewmanager;

import us.blockbox.uilib.api.View;
import us.blockbox.uilib.api.ViewHistoryMutable;

import java.util.ArrayList;
import java.util.List;

public class ViewHistoryMutableImpl implements ViewHistoryMutable{
	private final List<View> views;

	public ViewHistoryMutableImpl(){
		views = new ArrayList<>();
	}

	public ViewHistoryMutableImpl(List<View> views){
		this.views = views;
	}

	@Override
	public View current(){
		if(views.isEmpty()){
			return null;
		}
		return views.get(size() - 1);
	}

	@Override
	public void add(View v){
		views.add(v);
//		System.out.println("New history size: " + size());
	}

	@Override
	public View back(){
		if(views.isEmpty()){
			return null;
		}
		return views.remove(size() - 1);
	}

	@Override
	public View setCurrent(View view){
		if(views.isEmpty()){
			views.add(view);
			return null;
		}
		return views.set(size() - 1,view);
	}

	@Override
	public View getPrevious(){
		int index = size() - 2;
		if(index > -1){
			return views.get(index);
		}
		return null;
	}

	@Override
	public int size(){
		return views.size();
	}

	private boolean indexValid(int index){
		return index > -1 && index < size();
	}
}
