package us.blockbox.uilib.api;

public interface PageChangerFactory{
	PageChanger createNext(int page);
	PageChanger createPrevious(int page);
}