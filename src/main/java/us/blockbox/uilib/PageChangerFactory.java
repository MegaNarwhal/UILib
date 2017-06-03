package us.blockbox.uilib;

import us.blockbox.uilib.component.PageChanger;

public interface PageChangerFactory{
	PageChanger createNext(int page);
	PageChanger createPrevious(int page);
}