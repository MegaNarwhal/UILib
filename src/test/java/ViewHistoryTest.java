import org.junit.Test;
import us.blockbox.uilib.api.Component;
import us.blockbox.uilib.api.View;
import us.blockbox.uilib.api.ViewHistoryMutable;
import us.blockbox.uilib.view.InventoryView;
import us.blockbox.uilib.viewmanager.ViewHistoryMutableImpl;

import static org.junit.Assert.*;

public class ViewHistoryTest{
	@Test
	public void testViewHistory(){
		ViewHistoryMutable h = new ViewHistoryMutableImpl();
		Component[] components = new Component[9];
		View first = new InventoryView("First",components);
		View second = new InventoryView("Second",components);
		View third = new InventoryView("Third",components);
		h.add(first);
		h.add(second);
		h.add(third);
		assertEquals(3,h.size());//3 elements
		assertSame(second,h.getPrevious());//second is before third
		assertSame(third,h.back());//removed third
		assertEquals(2,h.size());//removed one, 2 left
		assertSame(first,h.getPrevious());//before second is first
		assertSame(second,h.back());//going back to second
		assertSame(first,h.back());//going back to first
		assertNull(h.back());//nothing left
		assertEquals(0,h.size());//empty
		assertNull(h.setCurrent(second));//is empty so not replacing anything
		assertSame(second,h.setCurrent(third));
	}
}