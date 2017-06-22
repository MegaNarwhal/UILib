package us.blockbox.uilib.viewmanager;

import us.blockbox.uilib.api.View;
import us.blockbox.uilib.api.ViewHistory;

class ViewHistoryImpl implements ViewHistory{
	private final ViewHistory history;

	ViewHistoryImpl(ViewHistory history){
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
