import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class testCases {
	@Before
	public void setUp() {
		
	}

	@Test
	public void testCellEqual() {
		Cell one = new Cell(1,4);
		Cell two = new Cell(6,6);
		one.setDistanceAround(10,50,56,70); 
		two.setDistanceAround(70, 50, 56, 10);
		assertEquals(one.equalCell(two), true);
	}
	


}
