package us.blockbox.uilib;

import us.blockbox.uilib.view.View;

import java.util.ArrayList;
import java.util.List;

public class ViewHistory{
	private final List<View> views;

	public ViewHistory(){
		views = new ArrayList<>();
	}

	public ViewHistory(List<View> views){
		this.views = views;
	}

	public View current(){
		if(views.isEmpty()){
			return null;
		}
		return views.get(size() - 1);
	}

	public void add(View v){
		views.add(v);
//		System.out.println("New history size: " + size());
	}

	public View back(){
		if(views.isEmpty()){
			return null;
		}
		views.remove(size() - 1);
		return current();
	}

	public View getPrevious(){
		int index = size() - 2;
		if(index > -1){
			return views.get(index);
		}
		return null;
	}

	public int size(){
		return views.size();
	}

	private boolean indexValid(int index){
		return index > -1 && index < size();
	}
}
