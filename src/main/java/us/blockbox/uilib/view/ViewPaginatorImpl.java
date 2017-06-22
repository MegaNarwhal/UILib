package us.blockbox.uilib.view;

import us.blockbox.uilib.api.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewPaginatorImpl implements ViewPaginator{
	private final String name;
	private final Component[] components;
	private final int pageRows;
	private PageChangerFactory pageChangerFactory;

	public ViewPaginatorImpl(String name,Component[] components,int pageRows,PageChangerFactory pageChangerFactory){
		this.name = name;
		this.components = components;
		this.pageRows = pageRows;
		this.pageChangerFactory = pageChangerFactory;
	}

	@Override
	public List<View> paginate(){
		int pageSize = pageRows * 9;
		int pages = (int)Math.ceil(components.length / ((double)pageSize));
		if(pages == 1){
			return Collections.singletonList(InventoryView.create(name,components));
		}
		int pageNum = 0;
		List<View> views = new ArrayList<>(pages);
		int isrc = 0;
		while(true){
			Component[] page = new Component[pageSize];
			int i = 0;
			while(isrc < components.length && i < pageSize){ //todo page numbers for prev/next buttons?
				Component c;
				int currentPage = pageNum + 1;
				if(currentPage > 1 && i == 0){//if this isn't the first page, insert a link to previous page as first item.
//					System.out.println("Inserting previous page link");
					c = pageChangerFactory.createPrevious(currentPage);
				}else if(currentPage < pages && i == pageSize - 1){//if this isn't the last page, insert a link to next page as the last item.
//					System.out.println("Inserting next page link");
					c = pageChangerFactory.createNext(currentPage);
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
		return views;
	}
}
