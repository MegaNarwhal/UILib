import org.junit.Test;
import us.blockbox.uilib.ViewHistoryMutable;
import us.blockbox.uilib.ViewHistoryMutableImpl;
import us.blockbox.uilib.component.Component;
import us.blockbox.uilib.view.InventoryView;
import us.blockbox.uilib.view.View;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
		assertEquals(3,h.size());
		assertEquals(second,h.getPrevious());
		assertEquals(third,h.back());
		assertEquals(2,h.size());
		assertEquals(first,h.getPrevious());
		assertEquals(second,h.back());
		assertEquals(first,h.back());
		assertNull(h.back());
		assertEquals(0,h.size());
		assertNull(h.setCurrent(second));
		assertEquals(second,h.setCurrent(third));
	}
}
