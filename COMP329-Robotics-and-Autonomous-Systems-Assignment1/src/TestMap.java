import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.*;

/**
 * Testing class for testing A* search and Map class function
 * As we do not have enough time for testing code on the robot 
 * Writing a Junit test class is a good way to test the code without the robot
 * there shouldn not be loads of
 * @author jinwei.zhang
 */
public class TestMap {
	private Map mp;

	@Before
	public void setUp() {
		mp = new Map();
		mp.setEndLocation(4, 3);
	}

	@Test
	public void testEndPosition() {
//		mp.setEndLocation(mp.getCurrentPositionX(), mp.getCurrentPositionY());
//		assertEquals(mp.getEndLocationX(), 1);
	}

	@Test
	public void testPosition() {
//		// System.out.print(mp.getMapSpaceOccupied()[2][3]);
//		// i means column j means rows
//		System.out.println("map been occupied !!!!!!!");
//		int count = 0;
//		for (int i = 0; i < 8; i++) {
//			for (int j = 0; j < 8; j++) {
//				System.out.print(mp.getMapSpaceOccupied()[i][j]);
//				count++;
//			}
//			System.out.println("");
//		}
//
//		System.out.println("this is count cells " + count);
//		System.out.println("current X " + mp.getCurrentPositionX());
//		System.out.println("current Y " + mp.getCurrentPositionY());

	}

	@Test
	public void testHasScanned() {
		//System.out.println("map been scanned !!!!!!!");
		// System.out.print(mp.getMapSpaceOccupied()[2][3]);
		// i means rows j means column
//		for (int i = 0; i < 8; i++) {
//			for (int j = 0; j < 9; j++) {
//				System.out.print(mp.getMapSpaceBeenScanned()[i][j]);
//			}
//			System.out.println("");
//		}
//		System.out.println("********************");

	}

	@Test
	// test the current position after each movement.
	public void testMovement() {
		// let the robot move 6 times towards the heading 0 degree
//		for (int i = 0; i < 6; i++) {
//			mp.move(0);
//		}
//		// and move 3 times towards the East
//		for (int i = 0; i < 3; i++) {
//			mp.move(90);
//		}
//		mp.setCurrentPositionX(1);
//		mp.setCurrentPositionY(1);
//		mp.move(Map.HEADING_EAST);


		// mp.move(0);

		// System.out.println(mp.getCurrentPositionY());
	}

	@Test
	// test the current position after each movement.
	public void testProbability() {
		// scan first based on (1,1)
//		mp.scanCells(Map.HEADING_NORTH, 40, 20, 20);
//		System.out.println("testProbability！！！！！！");
//		for (int i = 0; i < Map.NUMBER_OF_COLUMNS; i++) {
//			for (int j = 0; j < Map.NUMBER_OF_ROWS; j++) {
//				System.out.print(mp.getProbabilityMap()[i][j] + " ");
//			}
//			System.out.println("");
//		}
//
//		 
//		
//		 System.out.println("X1 Y3  " + mp.getProbabilityMap()[1][3]);
//		 System.out.println("X1 Y3  " + mp.getMapSpaceOccupied()[1][3]);
	}

	@Test
	public void testUpdateNorth() {
//	    System.out.println("test update North!!!");
//		mp.setCurrentPositionX(1);
//		mp.setCurrentPositionY(6);
//		System.out.println("current location:" + mp.getCurrentPositionX() +" ," + mp.getCurrentPositionY());
//		System.out.println("box length:" + Map.BOX_LENGTH);
//		// assume the current location is (1,1)
//		// looking up and the update the map up cell, 40 means there is something 40 centremeters away
//		//about 2 cells
//		mp.updateNorth(10);
//		// i,j column rows
//		for (int i = 0; i < 7; i++) {
//			for (int j = 0; j < 8; j++) {
//				System.out.print(mp.getMapSpaceBeenScanned()[i][j]);
//				
//			}
//			System.out.println("");
//		}
//		System.out.println("result:" + mp.getMapSpaceBeenScanned()[3][5]);
//		
//		for (int i = 0; i < 7; i++) {
//			for (int j = 0; j < 8; j++) {
//				System.out.print(mp.getMapSpaceOccupied()[i][j]);
//				
//			}
//			System.out.println("");
//		}
//		System.out.println("result:" + mp.getMapSpaceOccupied()[3][4]);

	}

	@Test
	public void testUpdateSouth() {
//	    System.out.println("test update South!!!");
//		mp.setCurrentPositionX(3);
//		mp.setCurrentPositionY(3);
//		System.out.println("current location:" + mp.getCurrentPositionX() +" ," + mp.getCurrentPositionY());
//		System.out.println("box length:" + mp.BOX_LENGTH);
//		// assume the current location is (1,1)
//		// looking up and the update the map up cell, 40 means there is something 40 centremeters away
//		//about 2 cells
//		mp.updateSouth(40);
//		// i,j column rows
//		for (int i = 0; i < 8; i++) {
//			for (int j = 0; j < 9; j++) {
//				System.out.print(mp.getMapSpaceBeenScanned()[i][j]);
//				
//			}
//			System.out.println("");
//		}
//		System.out.println("result:" + mp.getMapSpaceBeenScanned()[3][2]);
//		
//		for (int i = 0; i < 8; i++) {
//			for (int j = 0; j < 9; j++) {
//				System.out.print(mp.getMapSpaceOccupied()[i][j]);
//				
//			}
//			System.out.println("");
//		}
//		System.out.println("result:" + mp.getMapSpaceOccupied()[3][2]);

	}
	
	@Test
	public void testUpdateEast() {
//	    System.out.println("test update South!!!");
//		mp.setCurrentPositionX(3);
//		mp.setCurrentPositionY(3);
//		System.out.println("current location:" + mp.getCurrentPositionX() +" ," + mp.getCurrentPositionY());
//		System.out.println("box width:" + mp.BOX_WIDTH);
//		// assume the current location is (1,1)
//		// looking up and the update the map up cell, 40 means there is something 40 centremeters away
//		//about 2 cells
//		for (int i = 0; i < 8; i++) {
//			for (int j = 0; j < 9; j++) {
//				System.out.print(mp.getMapSpaceBeenScanned()[i][j]);
//				
//			}
//			System.out.println("");
//		}
//		mp.updateEast(77);
//		// i,j column rows
//		for (int i = 0; i < 8; i++) {
//			for (int j = 0; j < 9; j++) {
//				System.out.print(mp.getMapSpaceBeenScanned()[i][j]);
//				
//			}
//			System.out.println("");
//		}
//		System.out.println("result:" + mp.getMapSpaceBeenScanned()[6][3]);
//		
//		for (int i = 0; i < 8; i++) {
//			for (int j = 0; j < 9; j++) {
//				System.out.print(mp.getMapSpaceOccupied()[i][j]);
//				
//			}
//			System.out.println("");
//		}
//		System.out.println("result:" + mp.getMapSpaceOccupied()[3][2]);
		
	}
	
	@Test
	public void testUpdateWest() {
//	    System.out.println("test update west!!!");
//		mp.setCurrentPositionX(3);
//		mp.setCurrentPositionY(3);
//		System.out.println("current location:" + mp.getCurrentPositionX() +" ," + mp.getCurrentPositionY());
//		System.out.println("box width:" + mp.BOX_WIDTH);
//		// assume the current location is (1,1)
//		// looking up and the update the map up cell, 40 means there is something 40 centremeters away
//		//about 2 cells
//		for (int i = 0; i < 8; i++) {
//			for (int j = 0; j < 9; j++) {
//				System.out.print(mp.getMapSpaceBeenScanned()[i][j]);
//				
//			}
//			System.out.println("");
//		}
//		mp.updateWest(51.6);
//		// i,j column rows
//		for (int i = 0; i < 8; i++) {
//			for (int j = 0; j < 9; j++) {
//				System.out.print(mp.getMapSpaceBeenScanned()[i][j]);
//				
//			}
//			System.out.println("");
//		}
//		System.out.println("result:" + mp.getMapSpaceBeenScanned()[6][3]);
//		
//		for (int i = 0; i < 8; i++) {
//			for (int j = 0; j < 9; j++) {
//				System.out.print(mp.getMapSpaceOccupied()[i][j]);
//				
//			}
//			System.out.println("");
//		}
//		System.out.println("result:" + mp.getMapSpaceOccupied()[3][2]);
		
	}
	
	@Test
	public void testScannCells() {
//		System.out.println("testScannCells");
//		System.out.println("current location:" + mp.getCurrentPositionX() +" ," + mp.getCurrentPositionY());
//		mp.setCurrentPositionX(3);
//		mp.setCurrentPositionY(5);
//		System.out.println("current location:" + mp.getCurrentPositionX() +" ," + mp.getCurrentPositionY());
//		// assume the current location is (3,5)
//		// looking up and the update the whole up cell
//		mp.updateDown(10);
//		int result = mp.getMapSpaceSeenCounts()[3][2];
//		System.out.println(result);
//		float prop = mp.getMap()[3][2];
//		System.out.println(prop);
//		// where I have not scanned yet
//		System.out.println(mp.getMap()[3][1]);

	}
	
	@Test
	public void testPathFindingWithoutObstacle() {
//		mp.scanCells(Map.HEADING_NORTH, 40, 20, 20);
//		System.out.println("testProbability！！！！！！");
//		for (int i = 0; i < Map.NUMBER_OF_COLUMNS; i++) {
//			for (int j = 0; j < Map.NUMBER_OF_ROWS; j++) {
//				System.out.print(mp.getProbabilityMap()[i][j].getCellProbability() + " ");
//			    
//			}
//			System.out.println("");
//		}
//
//		 System.out.println("X1 Y3  " + mp.getProbabilityMap()[1][3].getCellProbability());		
//		 Cell start = mp.getProbabilityMap()[1][1];
//		 Cell end = mp.getProbabilityMap()[4][6];
//		 
//		 PathFinder ph = new PathFinder();
//		 ph.openSet.add(start);
//		 
//		 ArrayList<Cell> path = ph.FindPath(mp.getProbabilityMap(), end,start);
//		 
//		 if(path == null)
//		 {
//			 System.out.println("no path");
//		 }else {
//			 for(Cell cell :path)
//			 {
//				 System.out.print("("+cell.getCellXPos() + " " + cell.getCellYPos()+")" );
//			 }
//		 }
//
//		 for(Cell cell :path)
//		 {
//			 System.out.print("("+cell.getCellXPos() + " " + cell.getCellYPos()+")" );
//		 }
		 
	}
	
	@Test
	public void testPathFindingWithObstacle() {
		System.out.println("testProbability！！！！！！");
		mp.getProbabilityMap()[1][4].setCellProbability((float)0.9);
		mp.getProbabilityMap()[2][4].setCellProbability((float)0.9);
		mp.getProbabilityMap()[3][4].setCellProbability((float)0.9);
		mp.getProbabilityMap()[4][4].setCellProbability((float)0.7);
		mp.getProbabilityMap()[4][3].setCellProbability((float)0.6);
		mp.getProbabilityMap()[4][2].setCellProbability((float)0.9);
		
		for (int i = 0; i < Map.NUMBER_OF_COLUMNS; i++) {
			for (int j = 0; j < Map.NUMBER_OF_ROWS; j++) {
				System.out.print(mp.getProbabilityMap()[i][j].getCellProbability() + " ");
			}
			System.out.println("");
		}
	

		 System.out.println("X1 Y3  " + mp.getProbabilityMap()[1][3].getCellProbability());
	
		 
			for (int i = 1; i <= Map.NUMBER_OF_COLUMNS-2; i++) {
				for (int j = 1; j <= Map.NUMBER_OF_ROWS-2; j++) 
				{
				mp.getProbabilityMap()[i][j].previous = null;
				}

			}
		 
		 Cell end = mp.getProbabilityMap()[4][6];
		 Cell start = mp.getProbabilityMap()[2][2];
		
		 
		 PathFinder ph = new PathFinder();
		 ph.openSet.add(start);
		 
		 ArrayList<Cell> path = ph.findPath(mp.getProbabilityMap(), end, start);
		 
		 if(path == null)
		 {
			 System.out.println("no path");
		 }else {
			 for(Cell cell :path)
			 {
				 System.out.print("("+cell.getCellXPos() + " " + cell.getCellYPos()+")" );
			 }
		 }

		 
		 
		 
//second
		 
			for (int i = 1; i <= Map.NUMBER_OF_COLUMNS-2; i++) {
				for (int j = 1; j <= Map.NUMBER_OF_ROWS-2; j++) 
				{
				mp.getProbabilityMap()[i][j].previous = null;
				}

			}
		 
		 
		 
		  end = mp.getProbabilityMap()[2][2];
		  

		  
		 
		  start = mp.getProbabilityMap()[3][3];
		
		 
		  ph = new PathFinder();

		  
		 ph.openSet.add(start);
		 
		  path = ph.findPath(mp.getProbabilityMap(), end, start);
		 
		 if(path == null)
		 {
			 System.out.println("no path");
		 }else {
			 for(Cell cell :path)
			 {
				 System.out.print("("+cell.getCellXPos() + " " + cell.getCellYPos()+")" );
			 }
		 }
	}
	
	@Test 
	public void testNeigbhour() {
//		for (int a = 0; a < Map.NUMBER_OF_COLUMNS; a++) {
//			for (int b = 0; b < Map.NUMBER_OF_ROWS; b++) {
//				
//				mp.getProbabilityMap()[a][b].setCellProbability(0);
//				mp.getProbabilityMap()[a][b].addNeighbours(mp.getProbabilityMap(), a, b, Map.NUMBER_OF_ROWS, Map.NUMBER_OF_COLUMNS);
//			}
//		}
		 Cell start = mp.getProbabilityMap()[1][6];
		 for(int i = 0; i < start.getNeighbours().size(); i++) {
			 System.out.println(start.getNeighbours().get(i).getCellXPos()+ " " +start.getNeighbours().get(i).getCellYPos());
		 }
		 
		 
	}
	
	

	
	
	
	

}
