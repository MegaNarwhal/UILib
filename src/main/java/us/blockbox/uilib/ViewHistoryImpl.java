package us.blockbox.uilib;

import us.blockbox.uilib.view.View;

class ViewHistoryImpl implements ViewHistory{
	private final ViewHistory history;

	public ViewHistoryImpl(ViewHistory history){
		this.history = history;
	}

	@Override
	public View current(){
		return history.current();
	}

	@Override
	public View getPrevious(){
		return history.getPrevious();
	}

	@Override
	public int size(){
		return history.size();
	}
}
