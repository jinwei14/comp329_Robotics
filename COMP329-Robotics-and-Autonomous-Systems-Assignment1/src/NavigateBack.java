import java.util.ArrayList;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

public class NavigateBack implements Behavior {
	public boolean suppressed;
	private PilotRobot me;
	private MovePilot pilot;
	private Map wholeMap;
	private int xEnd;
	private int yEnd;

	// Constructor - the robot, and gets access to the pilot class
	// that is managed by the robot (this saves us calling
	// me.getPilot.somemethod() all of the while)
	public NavigateBack(PilotRobot robot, Map mp) {
		wholeMap = mp;
		me = robot;
		pilot = me.getPilot();
	}

	// When called, this should stop action()
	public void suppress() {
		suppressed = true;
	}

	// When called, determine if this behaviour should start
	public boolean takeControl() {
		if (wholeMap.isWholeMapResult() == true) {
			// found have has been generated and the end position has been recorded
			Sound.setVolume(100);
			Sound.buzz();
			Sound.buzz();
try {
	System.out.println("map! wait!");
	Thread.sleep(5000);
	
} catch (InterruptedException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
			pilot.stop();
			xEnd = wholeMap.getEndLocationX();
			yEnd = wholeMap.getEndLocationY();
			return true;
		}
		return false;
	}

	public void action() {
		// Allow this method to run
		suppressed = false;
		me.getPilot().stop();

		while (!suppressed) {
			// when the end position has been found and map has been generated correctly
			if (xEnd != -1 && yEnd != -1) {
				me.navigateToEndPoint(xEnd, yEnd);
			} else {
				while (xEnd == -1 && yEnd == -1) {
					// when map has been generated correctly but end point has not been found
					me.findEndPoint();
				}

			}
			Thread.yield(); // wait till turn is complete or suppressed is called
		}

		// Ensure that the motors have stopped.
		pilot.stop();
	}
//
//	//if map is completed and the robot knows the end point
//	public void navigateToEndPoint(int x, int y) {
//
//		if (x == wholeMap.getCurrentPositionX() && y == wholeMap.getCurrentPositionY()) {
//			// current point is just the end point accidently
//			me.getPilot().stop();
//			System.out.println("solved");
//			Button.waitForAnyPress();
//		} else {
//			// current position is the end position navigate back to the start
//			Cell end = wholeMap.getProbabilityMap()[wholeMap.getCurrentPositionX()][wholeMap.getCurrentPositionY()];
//			Cell start = wholeMap.getProbabilityMap()[x][y];
//			// create a pathFinder instance then produce an array of Cell (path)
//			PathFinder ph = new PathFinder();
//			ph.openSet.add(start);
//
//			// return the path from end to the start.
//			ArrayList<Cell> path = ph.FindPath(wholeMap.getProbabilityMap(), end, start);
//
//			// if there is no path then print out a no Path signal for debug
//			if (path == null) {
//				System.out.println("no path");
//			} else {
//				for (Cell cell : path) {
//					System.out.print("(" + cell.getCellXPos() + " " + cell.getCellYPos() + ")");
//					// wait the button press to navigate back.
//
//				}
//			}
//
//			for (int a = 1; a < path.size(); a++) {
//				this.moveToNeibourPoint(path.get(a).getCellXPos(), path.get(a).getCellYPos());
//			}
//			
//			System.out.println("navigation end");
//			Button.waitForAnyPress();
//			
//		}
//
//	}
//
//	/**
//	 * 
//	 * @param x
//	 *            x position of the next cell
//	 * @param y
//	 *            y position of the next cell
//	 */
//	public void moveToNeibourPoint(int x, int y) {
//		// means that next cell is on the East of the current cell
//		if (x - wholeMap.getCurrentPositionX() > 0) {
//
//			if (me.getHeadingNow() == Map.HEADING_NORTH) {
//				// turn right and move a width of box
//				me.getPilot().rotate(90);
//				// me.getPilot().travel(Map.BOX_WIDTH);
//
//			} else if (me.getHeadingNow() == Map.HEADING_SOUTH) {
//				// turn left and move box width
//				me.getPilot().rotate(-90);
//				// me.getPilot().travel(Map.BOX_WIDTH);
//			} else if (me.getHeadingNow() == Map.HEADING_WEST) {
//				me.getPilot().rotate(180);
//				// me.getPilot().travel(Map.BOX_WIDTH);
//			} else if (me.getHeadingNow() == Map.HEADING_EAST) {
//				// no need to turn
//				// me.getPilot().travel(Map.BOX_WIDTH);
//			} else {
//				System.out.println("NeibourPoint Fail!");
//			}
//			// means that next cell is on the West of the current cell
//		} else if (x - wholeMap.getCurrentPositionX() < 0) {
//			if (me.getHeadingNow() == Map.HEADING_NORTH) {
//				// turn left and move a width of box
//				me.getPilot().rotate(-90);
//				// me.getPilot().travel(Map.BOX_WIDTH);
//			} else if (me.getHeadingNow() == Map.HEADING_SOUTH) {
//				// turn left and move box width
//				me.getPilot().rotate(90);
//				// me.getPilot().travel(Map.BOX_WIDTH);
//			} else if (me.getHeadingNow() == Map.HEADING_WEST) {
//				// no need to turn
//				// me.getPilot().travel(Map.BOX_WIDTH);
//			} else if (me.getHeadingNow() == Map.HEADING_EAST) {
//				// turn to the west
//				me.getPilot().rotate(180);
//				// me.getPilot().travel(Map.BOX_WIDTH);
//			} else {
//				System.out.println("moveToPoint Fail!");
//			}
//
//			// next cell must on the south or north of the current cell
//		} else if (x - wholeMap.getCurrentPositionX() == 0) {
//
//			if (y - wholeMap.getCurrentPositionY() > 0) {
//				// the next cell is on the north of the current cell
//				if (me.getHeadingNow() == Map.HEADING_NORTH) {
//					// no need to turn
//					// me.getPilot().travel(Map.BOX_LENGTH);
//				} else if (me.getHeadingNow() == Map.HEADING_SOUTH) {
//					// turn around
//					me.getPilot().rotate(180);
//					// me.getPilot().travel(Map.BOX_LENGTH);
//				} else if (me.getHeadingNow() == Map.HEADING_WEST) {
//					// turn right (North) 90
//					me.getPilot().rotate(90);
//					// me.getPilot().travel(Map.BOX_LENGTH);
//				} else if (me.getHeadingNow() == Map.HEADING_EAST) {
//					// turn to left (South) 90
//					me.getPilot().rotate(-90);
//					// me.getPilot().travel(Map.BOX_LENGTH);
//				} else {
//					System.out.println("moveToPoint Fail!");
//				}
//			} else if (y - wholeMap.getCurrentPositionY() < 0) {
//				// the next cell is on the south of the current cell
//				if (me.getHeadingNow() == Map.HEADING_NORTH) {
//					// turn around 180
//					me.getPilot().rotate(180);
//					// me.getPilot().travel(Map.BOX_LENGTH);
//				} else if (me.getHeadingNow() == Map.HEADING_SOUTH) {
//					// no need to turn
//					// me.getPilot().travel(Map.BOX_LENGTH);
//				} else if (me.getHeadingNow() == Map.HEADING_WEST) {
//					// turn left (North) 90
//					me.getPilot().rotate(-90);
//					// me.getPilot().travel(Map.BOX_LENGTH);
//				} else if (me.getHeadingNow() == Map.HEADING_EAST) {
//					// turn right (South) 90
//					me.getPilot().rotate(90);
//					// me.getPilot().travel(Map.BOX_LENGTH);
//				} else {
//					System.out.println("moveToPoint Fail!");
//				}
//
//			}
//		}
//		// rounded the heading first
//		me.setHeading();
//		// move the distance depend on the heading.
//		me.getPilot().travel(me.getMovedDistance());
//		wholeMap.move(me.getHeadingNow());
//
//		if (me.checkColorPaper() == true) {
//			pilot.stop();
//			Button.waitForAnyPress();
//
//		}
//
//	}
//
//	/**
//	 * findEndPoint is a method called when when map has been generated correctly
//	 * but end point has not been found. Only thing need to do is to navigate to the
//	 * nearest point in order to find the end position Once the end location been
//	 * found then stop the robot
//	 */
//	public void findEndPoint() {
//		// loop through the mapSpaceBeen to and then find out a location that haven't
//		// been to.
//		// when blue paper has been scanned then stop instantly.
//		// while the end point is still not found
//		while (xEnd == -1 && yEnd == -1) {
//			for (int column = 1; column <= Map.NUMBER_OF_COLUMNS - 2; column++) {
//				for (int row = 1; row <= Map.NUMBER_OF_ROWS - 2; row++) {
//					// if there is no obstacle in this space and has not been to.
//					if (wholeMap.getProbabilityMap()[column][row].getCellProbability() < (float) 0.7
//							&& wholeMap.getMapSpaceBeenTo()[column][row] == 0) {
//
//						navigateToEndPoint(column, row);
//
//					}
//				}
//			}
//		}
//	}

}