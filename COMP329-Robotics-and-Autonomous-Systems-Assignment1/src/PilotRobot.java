
// PilotRobot.java
// 
// Based on the SimpleRobot class, this provides access to the
// sensors, and constructs a MovePilot to control the robot.
//
// Terry Payne
// 8th October 2017

import java.util.ArrayList;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.Motor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Pose;

public class PilotRobot {
	private EV3TouchSensor leftBump, rightBump;
	private EV3IRSensor irSensor;
	private EV3ColorSensor cSensor;
	private SampleProvider leftSP, rightSP, distSP, colourSP;
	private float[] leftSample, rightSample, distSample, colourSample;
	private MovePilot pilot;
	// private NXTUltrasonicSensor uSensor;
	private EV3UltrasonicSensor uSensor;
	private OdometryPoseProvider opp;

	private float leftDifference;
	private float distanceFront;
	private float distanceLeft;
	private float distanceRight;
	private boolean obstacleFront;
	private int headingNow;
	private Map wholeMap;

	public PilotRobot(Map wholeMap) {
		this.wholeMap = wholeMap;
		Brick myEV3 = BrickFinder.getDefault();

		leftBump = new EV3TouchSensor(myEV3.getPort("S2"));
		rightBump = new EV3TouchSensor(myEV3.getPort("S1"));
		// irSensor = new EV3IRSensor(myEV3.getPort("S3"));
		// uSensor = new NXTUltrasonicSensor(myEV3.getPort("S3"));
		uSensor = new EV3UltrasonicSensor(myEV3.getPort("S3"));
		cSensor = new EV3ColorSensor(myEV3.getPort("S4"));

		leftSP = leftBump.getTouchMode();
		rightSP = rightBump.getTouchMode();
		// distSP = irSensor.getDistanceMode(); // effective range of the sensor in
		// Distance mode is about 5 to 50 centimeters
		distSP = uSensor.getDistanceMode();
		colourSP = cSensor.getRGBMode();

		leftSample = new float[leftSP.sampleSize()]; // Size is 1
		rightSample = new float[rightSP.sampleSize()]; // Size is 1
		distSample = new float[distSP.sampleSize()]; // Size is 1
		colourSample = new float[colourSP.sampleSize()]; // Size is 3

		// Set up the wheels by specifying the diameter of the
		// left (and right) wheels in centimeters, i.e. 3.25 cm
		// the offset number is the distance between the center
		// of wheel to the center of robot, i.e. half of track width
		// NOTE: this may require some trial and error to get right!!!
		Wheel leftWheel = WheeledChassis.modelWheel(Motor.B, 3.25).offset(-9.0);
		Wheel rightWheel = WheeledChassis.modelWheel(Motor.C, 3.25).offset(9.0);
		Chassis myChassis = new WheeledChassis(new Wheel[] { leftWheel, rightWheel }, WheeledChassis.TYPE_DIFFERENTIAL);

		pilot = new MovePilot(myChassis);
		opp = new OdometryPoseProvider(pilot);
		this.distanceFront = 0;
		this.distanceLeft = 0;
		this.distanceRight = 0;
		this.obstacleFront = false;
		this.leftDifference = 0;
		// set the different speed of the motor and Pilot.
		Motor.A.setSpeed(200);
		// getPilot().setLinearAcceleration(10);
		getPilot().setLinearSpeed(10);
		getPilot().setAngularAcceleration(150);
		getPilot().setAngularSpeed(30);
	}

	public MovePilot getPilot() {
		return pilot;
	}

	public OdometryPoseProvider getOdometry() {
		return this.opp;
	}

	public float getLeftDifference() {
		return leftDifference;
	}

	public void setLeftDifference(float leftDifference) {
		this.leftDifference = leftDifference;
	}

	public float getDistanceFront() {
		return distanceFront;
	}

	public void setDistanceFront(float distanceFront) {
		this.distanceFront = distanceFront;
	}

	public float getDistanceLeft() {
		return distanceLeft;
	}

	public void setDistanceLeft(float distanceLeft) {
		this.distanceLeft = distanceLeft;
	}

	public float getDistanceRight() {
		return distanceRight;
	}

	public void setDistanceRight(float distanceRight) {
		this.distanceRight = distanceRight;
	}

	public boolean getObstacleFront() {
		return obstacleFront;
	}

	public void setObstacleFront(boolean obstacleFront) {
		this.obstacleFront = obstacleFront;
	}

	/**
	 * get the current heading front = 0 left =
	 * 
	 */
	public int getHeadingNow() {
		return headingNow;
	}

	/**
	 * calibrate the current heading front = 0 left =
	 * 
	 */
	public void setHeading() {
		// reset the heading only this way works setHeading Not working

		float headingRubbish = getOdometry().getPose().getHeading();
		headingNow = (Math.round(headingRubbish / 90)) * 90;
		// reset
		// System.out.println("headingRub "+ headingNow);
		// keep updating the heading of the odometry
		Pose ps = new Pose(0, 0, headingNow);
		getOdometry().setPose(ps);

	}

	/**
	 * check if the blue paper been scanned if so record the current location
	 * 
	 * 
	 * 
	 * 
	 */
	public boolean checkColorPaper() {
		// once the blue colour has been scanned quit the program
		// me.getColour() == Colour.blue ????
		if (getColour()[0] > 0.015 && getColour()[0] < 0.03 && getColour()[1] < 0.15 && getColour()[1] > 0.07
				&& getColour()[2] > 0.07) {
			// me.getPilot().stop();
			Sound.buzz();
			System.out.println("blue!!");

			return true;
		}

		return false;
	}

	/**
	 * check if the is something in the front or not
	 * 
	 * 
	 * 
	 * 
	 */
	public boolean checkStopFront() {
		boolean flag = false;
		if (getDistance() * 100 <= 4) {
			// stop the pilot and update the distance of front left and right
			getPilot().travel(-3);
			getPilot().stop();
			setObstacleFront(true);
			// make some move there is an obstacle in the front
			// Thread.yield();
			flag = true;

		}
		return flag;

	}

	/**
	 * calibrate the angle depend on the difference to the left hand side.
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	public void calibrateAngle() {
		setLeftDifference(getDistance() * 100 - getDistanceLeft());

		// System.out.println(leftDifference);
		// cacluate the radians
		double radians = (getLeftDifference()) / getMovedDistance();
		// width of the box if the distance is too big, then there must be an obstacle
		// which has
		// been scanned then ignore this difference;
		if (getLeftDifference() >= Map.BOX_LENGTH) {
			// System.out.println("11");
			;
			// if the angle is inside this range then do the calibration. between the width
			// of box and 0.5 cm
		} else if ((getLeftDifference() < Map.BOX_WIDTH / 2 && getLeftDifference() >= 0.5)
				|| (getLeftDifference() >= -Map.BOX_WIDTH / 2 && getLeftDifference() <= -0.5)) {
			// turn the arcSin Degree;

			double turnDegree = Math.toDegrees(Math.atan(radians));
			// me.getPilot().travel(-40);
			getPilot().rotate(-(turnDegree + 2));
			// System.out.println("22");
			// if the angle is too small then there is no need to calibrate the angle
		} else {
			System.out.println("33");
			;
		}

	}

	/**
	 * 
	 * get the moving distance depend on where the robot is facing.
	 * 
	 * 
	 * 
	 * 
	 */
	public float getMovedDistance() {
		float distance = 0;
		switch (getHeadingNow()) {
		case (Map.HEADING_NORTH):
		case (Map.HEADING_SOUTH):
			// some times gives -180 some times gives 180
		case (-Map.HEADING_SOUTH):
			distance = Map.BOX_LENGTH;
			break;
		case (Map.HEADING_WEST):
		case (Map.HEADING_EAST):
			distance = Map.BOX_WIDTH;
			break;
		default:
			System.out.println("wrong!getMovedDistance" + getHeadingNow());

		}

		return distance;
	}

	public void updateDistanceAround(boolean turn) {

		// round the heading that odometry give us
		setHeading();
		getPilot().stop();
		Motor.A.rotate(90);
		if (turn == true) {
			// do nothing
			;

		} else {
			// when turning is false do the calibration
			// calibrate the angle only depend on the difference of the left hand side.
			calibrateAngle();

		}
		// calibrate the angle only depend on the difference of the left hand side.
		setDistanceLeft(getDistance() * 100);
		// update here
		// motor turn right 180
		Motor.A.rotate(-180);
		setDistanceRight(getDistance() * 100);
		Motor.A.rotate(90);
		setDistanceFront(getDistance() * 100);
		wholeMap.scanCells(getHeadingNow(), getDistanceFront(), getDistanceLeft(), getDistanceRight());

		// round the heading afterwards
		setHeading();
	}

	/**
	 * 
	 * make some move when obstacle is found in the front.
	 * 
	 * 
	 */
	public void obstacleFindFrontAction() {
		// calibration when meet obstacle.

		while (getObstacleFront()) {
			// when robot is surrounded with obstacles
			if (getDistanceRight() + getDistanceLeft() <= Map.BOX_LENGTH) {
				
				/*
				 * check where I have been to and the clear way point and make the turn.
				 *
				 */
				// turn right 90
				getPilot().rotate(180, false);
				getPilot().travel(-15);
				// reset the heading. round the heading.

				setObstacleFront(false);

				break;
			} else if (getDistanceRight() + getDistanceLeft() > Map.BOX_LENGTH) {

				// need some changes.
				/**
				 * 
				 * 
				 * 
				 */
				if (getDistanceLeft() > getDistanceRight()) {
					// turn left and update the heading and the distance around
					getPilot().rotate(-90, false);

					setObstacleFront(false);
					break;
				} else {
					// turn right and update the heading and the distance around
					getPilot().rotate(90, false);

					setObstacleFront(false);
					break;
				}

			}
		}

		this.setHeading();
		getPilot().travel(15);
		getPilot().rotate(17);
		getPilot().travel(-15 / Math.cos(Math.toRadians(13)));
		getPilot().rotate(-17);
		getPilot().travel(-10);
		this.setHeading();
		// the robot has turned so that turning is true
		updateDistanceAround(true);
		this.setHeading();

	}

	// if map is completed and the robot knows the end point
	public void navigateToEndPoint(int x, int y) {

		if (x == wholeMap.getCurrentPositionX() && y == wholeMap.getCurrentPositionY()) {
			// current point is just the end point accidently
			getPilot().stop();
			System.out.println("solved");
			Button.waitForAnyPress();
		} else {
			// update the previous as null so that it can execute the pathfind
			for (int i = 1; i <= Map.NUMBER_OF_COLUMNS - 2; i++) {
				for (int j = 1; j <= Map.NUMBER_OF_ROWS - 2; j++) {
					wholeMap.getProbabilityMap()[i][j].previous = null;
				}

			}
			// current position is the end position navigate back to the start
			Cell end = wholeMap.getProbabilityMap()[wholeMap.getCurrentPositionX()][wholeMap.getCurrentPositionY()];
			Cell start = wholeMap.getProbabilityMap()[x][y];
			// create a pathFinder instance then produce an array of Cell (path)
			PathFinder ph = new PathFinder();
			ph.openSet.add(start);

			// return the path from end to the start.
			ArrayList<Cell> path = ph.findPath(wholeMap.getProbabilityMap(), end, start);

			// if there is no path then print out a no Path signal for debug
			if (path == null) {
				System.out.println("no path");
			} else {
				for (Cell cell : path) {
					System.out.print("(" + cell.getCellXPos() + " " + cell.getCellYPos() + ")");
					// wait the button press to navigate back.

				}
			}

			for (int a = 1; a < path.size(); a++) {
				this.moveToNeibourPoint(path.get(a).getCellXPos(), path.get(a).getCellYPos());
			}

			System.out.println("navigation end");

		}

	}

	/**
	 * 
	 * @param x
	 *            x position of the next cell
	 * @param y
	 *            y position of the next cell nned
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 *            update !!!!!!!!!!!!
	 * 
	 *            update distance around!
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	public void moveToNeibourPoint(int x, int y) {
		// means that next cell is on the East of the current cell
		this.setHeading();
		if (x - wholeMap.getCurrentPositionX() > 0) {

			if (getHeadingNow() == Map.HEADING_NORTH) {
				// turn right and move a width of box
				getPilot().rotate(90);
				updateDistanceAround(true);
				// me.getPilot().travel(Map.BOX_WIDTH);

			} else if (getHeadingNow() == Map.HEADING_SOUTH || getHeadingNow() == -Map.HEADING_SOUTH) {
				// turn left and move box width
				getPilot().rotate(-90);
				updateDistanceAround(true);
				// me.getPilot().travel(Map.BOX_WIDTH);
			} else if (getHeadingNow() == Map.HEADING_WEST) {
				getPilot().rotate(180);
				getPilot().travel(-10);
				updateDistanceAround(true);
				// me.getPilot().travel(Map.BOX_WIDTH);
			} else if (getHeadingNow() == Map.HEADING_EAST) {
				// no need to turn
				updateDistanceAround(false);
				// me.getPilot().travel(Map.BOX_WIDTH);
			} else {
				System.out.println("NeibourPoint Fail!");
			}
			// means that next cell is on the West of the current cell
		} else if (x - wholeMap.getCurrentPositionX() < 0) {
			if (getHeadingNow() == Map.HEADING_NORTH) {
				// turn left and move a width of box
				getPilot().rotate(-90);
				updateDistanceAround(true);
				// me.getPilot().travel(Map.BOX_WIDTH);
			} else if (getHeadingNow() == Map.HEADING_SOUTH || getHeadingNow() == -Map.HEADING_SOUTH) {
				// turn left and move box width
				getPilot().rotate(90);
				updateDistanceAround(true);
				// me.getPilot().travel(Map.BOX_WIDTH);
			} else if (getHeadingNow() == Map.HEADING_WEST) {
				// no need to turn
				updateDistanceAround(false);
				// me.getPilot().travel(Map.BOX_WIDTH);
			} else if (getHeadingNow() == Map.HEADING_EAST) {
				// turn to the west
				getPilot().rotate(180);
				getPilot().travel(-10);
				updateDistanceAround(true);
				// me.getPilot().travel(Map.BOX_WIDTH);
			} else {
				System.out.println("moveToPoint Fail!");
			}

			// next cell must on the south or north of the current cell
		} else if (x - wholeMap.getCurrentPositionX() == 0) {

			if (y - wholeMap.getCurrentPositionY() > 0) {
				// the next cell is on the north of the current cell
				if (getHeadingNow() == Map.HEADING_NORTH) {
					// no need to turn
					updateDistanceAround(false);
					// me.getPilot().travel(Map.BOX_LENGTH);
				} else if (getHeadingNow() == Map.HEADING_SOUTH || getHeadingNow() == -Map.HEADING_SOUTH) {
					// turn around
					getPilot().rotate(180);
					getPilot().travel(-10);
					updateDistanceAround(true);
					// me.getPilot().travel(Map.BOX_LENGTH);
				} else if (getHeadingNow() == Map.HEADING_WEST) {
					// turn right (North) 90
					getPilot().rotate(90);
					updateDistanceAround(true);
					// me.getPilot().travel(Map.BOX_LENGTH);
				} else if (getHeadingNow() == Map.HEADING_EAST) {
					// turn to left (South) 90
					getPilot().rotate(-90);
					updateDistanceAround(true);
					// me.getPilot().travel(Map.BOX_LENGTH);
				} else {
					System.out.println("moveToPoint Fail!");
				}
			} else if (y - wholeMap.getCurrentPositionY() < 0) {
				// the next cell is on the south of the current cell
				if (getHeadingNow() == Map.HEADING_NORTH) {
					// turn around 180
					getPilot().rotate(180);
					getPilot().travel(-10);
					updateDistanceAround(true);
					// me.getPilot().travel(Map.BOX_LENGTH);
				} else if (getHeadingNow() == Map.HEADING_SOUTH || getHeadingNow() == -Map.HEADING_SOUTH) {
					// no need to turn
					updateDistanceAround(false);
					// me.getPilot().travel(Map.BOX_LENGTH);
				} else if (getHeadingNow() == Map.HEADING_WEST) {
					// turn left (North) 90
					getPilot().rotate(-90);
					updateDistanceAround(true);
					// me.getPilot().travel(Map.BOX_LENGTH);
				} else if (getHeadingNow() == Map.HEADING_EAST) {
					// turn right (South) 90
					getPilot().rotate(90);
					updateDistanceAround(true);
					// me.getPilot().travel(Map.BOX_LENGTH);
				} else {
					System.out.println("moveToPoint Fail!");
				}

			}
		}
		// rounded the heading first
		setHeading();
		// move the distance depend on the heading.
		getPilot().travel(getMovedDistance());
		updateDistanceAround(false);
		wholeMap.move(getHeadingNow());

		// if we have the map of the arena
		if (wholeMap.isWholeMapResult() == true) {
			if (checkColorPaper() == true) {
				pilot.stop();
				wholeMap.setEndLocation(wholeMap.getCurrentPositionX(), wholeMap.getCurrentPositionY());
				System.out.println("done !");
				Button.waitForAnyPress();
			}
			// if we do not have the map of the arena
		} else {
			if (checkColorPaper() == true) {
				wholeMap.setEndLocation(wholeMap.getCurrentPositionX(), wholeMap.getCurrentPositionY());

			}
		}

	}

	/**
	 * findEndPoint is a method called when when map has been generated correctly
	 * but end point has not been found. Only thing need to do is to navigate to the
	 * nearest point in order to find the end position Once the end location been
	 * found then stop the robot
	 */
	public void findEndPoint() {
		// loop through the mapSpaceBeen to and then find out a location that haven't
		// been to and navigate to the neighbour of that point
		// when blue paper has been scanned then stop instantly.
		// while the end point is still not found
		while (wholeMap.getEndLocationX() == -1 && wholeMap.getEndLocationY() == -1) {
			for (int column = 1; column <= Map.NUMBER_OF_COLUMNS - 2; column++) {
				for (int row = 1; row <= Map.NUMBER_OF_ROWS - 2; row++) {
					// if there is no obstacle in this space and has not been to.
					if (wholeMap.getProbabilityMap()[column][row].getCellProbability() < (float) 0.8
							&& wholeMap.getMapSpaceBeenTo()[column][row] == 0) {
						System.out.print("start navigate (xy) " + column + "," + row);
						Button.waitForAnyPress();

						navigateToEndPoint(column, row);

					}
				}
			}
		}
	}

	public void dealDeadlock() {
		// loop through the mapSpaceBeen to and then find out a location that haven't
		// been to.
		// when blue paper has been scanned then stop instantly.
		// while the end point is still not found
		while (wholeMap.isWholeMapResult() == false) {
			for (int column = 1; column <= Map.NUMBER_OF_COLUMNS - 2; column++) {
				for (int row = 1; row <= Map.NUMBER_OF_ROWS - 2; row++) {
					// if there is no obstacle in this space and has not been to.
					if (wholeMap.getProbabilityMap()[column][row].getCellProbability() < (float) 0.8
							&& wholeMap.getMapSpaceBeenTo()[column][row] == 0) {

						for (int i = 0; i < wholeMap.getProbabilityMap()[column][row].getNeighbours().size(); i++) {
							if (wholeMap.getProbabilityMap()[column][row].getNeighbours().get(i)
									.getCellProbability() < (float) 0.8) {
								navigateToEndPoint(
										wholeMap.getProbabilityMap()[column][row].getNeighbours().get(i).getCellXPos(),
										wholeMap.getProbabilityMap()[column][row].getNeighbours().get(i).getCellYPos());

								if (wholeMap.isWholeMapResult() == true) {
									break;
								}
							}

						}

					}
				}

			}
		}
	}

	public void closeRobot() {
		leftBump.close();
		rightBump.close();
		irSensor.close();
		cSensor.close();
	}

	public boolean isLeftBumpPressed() {
		leftSP.fetchSample(leftSample, 0);
		return (leftSample[0] == 1.0);
	}

	public boolean isRightBumpPressed() {
		rightSP.fetchSample(rightSample, 0);
		return (rightSample[0] == 1.0);
	}

	public float getDistance() {
		distSP.fetchSample(distSample, 0);
		return distSample[0];
	}

	public float[] getColour() {
		colourSP.fetchSample(colourSample, 0);
		return colourSample; // return array of 3 colours
	}

}