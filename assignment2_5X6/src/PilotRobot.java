

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.text.StyleConstants.ColorConstants;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
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

/**
 * This is the main class to control the movement of the EV3 robot and take
 * recordings from the sensors
 */
public class PilotRobot {
	// instance the component of the robot
	private EV3TouchSensor leftBump, rightBump;
	private EV3IRSensor irSensor;
	private EV3ColorSensor cSensor;
	private SampleProvider leftSP, rightSP, distSP, colourSP;
	private float[] leftSample, rightSample, distSample, colourSample;
	private MovePilot pilot;
	private EV3UltrasonicSensor uSensor;
	private OdometryPoseProvider opp;
	private EV3LargeRegulatedMotor leftMotor;
	private EV3LargeRegulatedMotor rightMotor;
	private EV3MediumRegulatedMotor sensorMotor;

	private int[] headingNow;
	private int currentX;
	private int currentY;

	private boolean startFindingVictim;
	private LinkedList <String>restVictimNo;

	/**
	 * Initialise the pilot robot instance and set up the sensors, motors etc
	 * 
	 * @param wholeMap
	 *            The instance of Map
	 */
	public PilotRobot() {
		Brick myEV3 = BrickFinder.getDefault();
		// set up the component of the EV3 brick sensor and bumper
		/*
		 * 
		 * 
		 */
		leftBump = new EV3TouchSensor(myEV3.getPort("S1"));
		rightBump = new EV3TouchSensor(myEV3.getPort("S4"));
		uSensor = new EV3UltrasonicSensor(myEV3.getPort("S3"));
		cSensor = new EV3ColorSensor(myEV3.getPort("S2"));

		leftSP = leftBump.getTouchMode();
		rightSP = rightBump.getTouchMode();
		distSP = uSensor.getDistanceMode();
		colourSP = cSensor.getRGBMode();

		leftSample = new float[leftSP.sampleSize()]; // Size is 1
		rightSample = new float[rightSP.sampleSize()]; // Size is 1
		distSample = new float[distSP.sampleSize()]; // Size is 1
		colourSample = new float[colourSP.sampleSize()]; // Size is 3
		// set up the EV3 motor to the port
		/*
		 * 
		 * 
		 */
		sensorMotor = new EV3MediumRegulatedMotor(myEV3.getPort("C"));
		leftMotor = new EV3LargeRegulatedMotor(myEV3.getPort("B"));
		rightMotor = new EV3LargeRegulatedMotor(myEV3.getPort("D"));

		/*
		 * 
		 * 
		 */
		Wheel leftWheel = WheeledChassis.modelWheel(leftMotor, 4.32).offset(-5.5);
		Wheel rightWheel = WheeledChassis.modelWheel(rightMotor, 4.32).offset(5.5);
		Chassis myChassis = new WheeledChassis(new Wheel[] { leftWheel, rightWheel }, WheeledChassis.TYPE_DIFFERENTIAL);

		pilot = new MovePilot(myChassis);
		opp = new OdometryPoseProvider(pilot);
		// initialise the distance to 0 the the distanceFront to false
		/*
		 * 
		 */
		this.startFindingVictim = false;
		restVictimNo = new LinkedList<String>();
		
		this.restVictimNo.add("blue");
		this.restVictimNo.add("green");
		this.restVictimNo.add("red");
		
		this.currentX = -1;
		this.currentY = -1;
		sensorMotor.setSpeed(200);
		// set up the speed of the motor and the turning speed.
		// getPilot().setAngularAcceleration(200);
		getPilot().setAngularSpeed(50);

		getPilot().setLinearSpeed(8);
		// important
		getPilot().setLinearAcceleration(20);
	}

	public int getRestVictimNo() {
		return restVictimNo.size();
	}

	public void decreaseRestVictimNo(String color) {
		this.restVictimNo.remove(color);
	}

	/**
	 * getter for pilot
	 */
	public MovePilot getPilot() {
		return pilot;
	}

	/**
	 * getter for Odometry
	 */
	public OdometryPoseProvider getOdometry() {
		return this.opp;
	}

	public boolean isStartFindingVictim() {
		return startFindingVictim;
	}

	public void setStartFindingVictim(boolean startFindingVictim) {
		this.startFindingVictim = startFindingVictim;
	}

	/**
	 * @return the current heading mention heading need to keep fixing Front = 0
	 *         Left = -90 Right = 90 Back = 180 / -180
	 */
	public int[] getHeadingNow() {
		return headingNow;
	}

	public void setHeadingNow(int[] newHeading) {
		this.headingNow = newHeading;
	}

	public int getCurrentX() {
		return currentX;
	}

	public void setCurrentX(int currentX) {
		this.currentX = currentX;
	}

	public int getCurrentY() {
		return currentY;
	}

	public void setCurrentY(int currentY) {
		this.currentY = currentY;
	}

	/**
	 * Check if the blue paper been scanned if so record the current location
	 * 
	 */

	public boolean checkColorPaperBlue() {
		// data like 0.15 and 0.03 only used here. they are used as the RGB mode color
		// samplr to detect the
		// blue colour
		if (getColour()[0] < 0.1 && getColour()[1] < 0.2 && getColour()[1] > 0.1 && getColour()[2] > 0.1) {
			// Sound.buzz();
			// Sound.buzz();
			// once the blue card is been found print out the blue sign.
			// System.out.println("blue!!");
			return true;
		}
		return false;
	}

	public boolean checkColorPaperRed() {
		// data like 0.15 and 0.03 only used here. they are used as the RGB mode color
		// samplr to detect the
		// blue colour
		if (getColour()[0] > 0.1 && getColour()[1] < 0.03 && getColour()[2] < 0.03) {
			// Sound.buzz();
			// Sound.buzz();
			// Sound.buzz();
			// once the blue card is been found print out the blue sign.
			// System.out.println("blue!!");
			return true;
		}
		return false;
	}

	public boolean checkColorPaperGreen() {
		// data like 0.15 and 0.03 only used here. they are used as the RGB mode color
		// samplr to detect the
		// blue colour
		if (getColour()[0] < 0.1 && getColour()[1] > 0.1 && getColour()[2] < 0.1) {
			// Sound.buzz();

			// once the blue card is been found print out the blue sign.
			// System.out.println("blue!!");
			return true;
		}
		return false;
	}

	public boolean checkBlackTap() {
		// data like 0.15 and 0.03 only used here. they are used as the RGB mode color
		// samplr to detect the
		// blue colour
		if (getColour()[0] < 0.04 && getColour()[1] < 0.04 && getColour()[2] < 0.04) {
			// Sound.buzz();

			// once the blue card is been found print out the blue sign.
			// System.out.println("blue!!");
			return true;
		}
		return false;
	}

	public String getCellType() {
		// and get colour information
		int counter = 0;
		String colorInfo = null;
		boolean isColorRed = checkColorPaperRed();
		boolean isColorBlue = checkColorPaperBlue();
		boolean isColorGreen = checkColorPaperGreen();

		if (isColorRed) {
			counter++;
			colorInfo = "red";

		}
		if (isColorBlue) {

			counter++;
			colorInfo = "blue";
		}

		if (isColorGreen) {

			counter++;
			colorInfo = "green";
		}
		if (!isColorRed && !isColorBlue && !isColorGreen) {
			counter++;
			colorInfo = "nonVictim";
		}

		// if the robot return more than one colour parameter
		if (counter > 1 || counter == 0) {
			System.out.println("color sensor parameter wrong!!!! check !!");
			Button.waitForAnyPress();
			System.exit(0);
		}

		return colorInfo;

	}

	// calculate the "absolute right" distance
	public float getDistanceNew() {
		float disTrue = 0;
		boolean run = true;
		float dis1 = 0, dis2 = 0, dis3 = 0;
		// take 3 times scan and then make sure they are not 0 or Nan
		// if all have value make sure they are as close to each other as possible then
		// calculate the mean value
		while (run) {
			try {
				// fire 3 times and wait soem times in between
				dis1 = getDistance();
				Thread.sleep(10);
				dis2 = getDistance();
				Thread.sleep(10);
				dis3 = getDistance();
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// if(!Float.isNaN(dis)&& dis != 0) {
			// disArray.add(dis);
			// if(disArray.size()>4 && Math.abs(disArray.get(0) - disArray.get(1))>0.1)
			// }
			if (dis1 != 0 && !Float.isNaN(dis1) && dis2 != 0 && !Float.isNaN(dis2) && dis3 != 0 && !Float.isNaN(dis3)
					) {

				// ensure we get near the same distance 3 times
				// and all of them are not o nor NaN (infinite)
				if (Math.abs(dis1 - dis2) < 0.001 && Math.abs(dis3 - dis2) < 0.001 && Math.abs(dis1 - dis3) < 0.001) {

					disTrue = (dis1 + dis2 + dis3) / 3;
					run = false;
				}

			}
		}
		return disTrue;
	}

	public void rotateDirection(int[] disAround1) {
		if (disAround1[0] == 0) {

		} else if (disAround1[1] == 0) {
			getPilot().rotate(-90);
		} else if (disAround1[2] == 0) {
			getPilot().rotate(-180);
		} else if (disAround1[3] == 0) {
			getPilot().rotate(90);
		}
	}

	// instead of heard code use vector to turn
	public void moveToNext(int x, int y) {
		int currentX = this.getCurrentX();
		int currentY = this.getCurrentY();
		int[] heading = this.getHeadingNow();
		int[] movingVector = { x - currentX, y - currentY };
		this.setHeadingNow(movingVector);

		// |u||2 = u12 + u22.
		int num = (heading[0] * movingVector[0] + heading[1] * movingVector[1]);
		double den = 1;
		double cos = num / den;
		double angle = Math.toDegrees((Math.acos(cos)));

		int cross = heading[0] * movingVector[1] - heading[1] * movingVector[0];
		// a×b = x1y2 - x2y1
		// rotate this angle
		if (angle == 90) {
			if (cross > 0) {
				// right turn 90
				getPilot().rotate(90);
			} else {
				// left turn 90
				getPilot().rotate(-90);
			}
		} else if (angle == 180) {
			getPilot().rotate(180);
		}

		moveTilBlack();

		if (x > currentX) {
			this.setCurrentX(currentX + 1);
		} else if (x < currentX) {
			this.setCurrentX(currentX - 1);
		}

		if (y > currentY) {
			this.setCurrentY(currentY + 1);
		} else if (y < currentY) {
			this.setCurrentY(currentY - 1);
		}

		getDistance3Direct();
	}

	public void moveTilBlack() {

		getPilot().forward();
		boolean flag = true;
		while (flag) {
			if (checkBlackTap()) {
				// if this function succeed then jump out the
				// loop
				flag = false;
				// once found the black tape stop immediately
				System.out.println("black tape");
				getPilot().stop();

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		// travel a certain distance
		getPilot().travel(10);
	}

	public int haveObstacle(float dis) {
		int hasObstacle;
		if (dis < 20) {
			// have something in the front
			hasObstacle = 1;
		} else {
			hasObstacle = 0;
		}

		return hasObstacle;

	}

	public int[] getDistance4Direct() {

		int[] aroundInfo = new int[4];
		// get the distance in the front -----------
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		float front = getDistanceNew();
		// have something in the front
		aroundInfo[0] = haveObstacle(front);

		// left rotate sensor 90 and get the distance----------
		sensorMotor.rotate(90);
		float left = getDistanceNew();
		aroundInfo[1] = haveObstacle(left);

		// right rotate sensor 180 degrees and get the right distance--------
		sensorMotor.rotate(-180);
		float right = getDistanceNew();
		aroundInfo[3] = haveObstacle(right);

		// rotate right to get the back distance-----------
		getPilot().rotate(90);
		float back = getDistanceNew();
		aroundInfo[2] = haveObstacle(back);

		// rotate back to the start position
		sensorMotor.rotate(90);
		// rotate back to the origin position----------
		getPilot().rotate(-90);

		return aroundInfo;

	}

	public int[] getDistance3Direct() {
		getPilot().travel(-5);
		int[] aroundInfo = new int[4];
		// get the distance in the front -----------
		float front = getDistanceNew();
		// have something in the front
		aroundInfo[0] = haveObstacle(front);

		// left rotate sensor 90 and get the distance----------
		sensorMotor.rotate(90);
		float left = getDistanceNew();
		aroundInfo[1] = haveObstacle(left);

		// right rotate sensor 180 degrees and get the right distance--------
		sensorMotor.rotate(-180);
		float right = getDistanceNew();
		aroundInfo[3] = haveObstacle(right);

		aroundInfo[2] = 0;
		sensorMotor.rotate(90);

		getPilot().travel(5);

		calibrateAngle(aroundInfo);
		calibrateDistance(aroundInfo);
		return aroundInfo;

	}

	public void calibrateAngle(int[] around) {
		if (around[1] == 1) {
			// sensor rotate to the left
			sensorMotor.rotate(90);
			double distance1 = getDistanceNew();
			getPilot().travel(-5);
			double distance2 = getDistanceNew();
			getPilot().travel(5);
			sensorMotor.rotate(-90);
			double distance3 = Math.abs(distance1 - distance2);
			double angle = Math.atan(distance3 / 5) * 180 / Math.PI;
			if (distance1 < distance2) {
				// rotate to the right
				getPilot().rotate(angle);
			} else {
				getPilot().rotate(-angle);
			}
		} else if (around[3] == 1) {
			sensorMotor.rotate(-90);
			double distance1 = getDistanceNew();
			getPilot().travel(-5);
			double distance2 = getDistanceNew();
			getPilot().travel(5);
			sensorMotor.rotate(90);
			double distance3 = Math.abs(distance1 - distance2);
			double angle = Math.atan(distance3 / 5) * 180 / Math.PI;
			if (distance1 < distance2) {
				// rotate to the left
				getPilot().rotate(-angle);
			} else {
				getPilot().rotate(angle);
			}
		}
	}

	public void calibrateDistance(int[] around) {

		if (around[1] == 1) {
			// sensor rotate to the left
			sensorMotor.rotate(90);
			double distance = getDistanceNew();
			sensorMotor.rotate(-90);

			double diff = Math.abs(12 - distance);

			if (distance > 9.5 && distance < 13) {
				// do nothing
			} else if (distance < 12) {
				// turn right 30
				
				getPilot().travel(-Math.sqrt(3) * diff);
				getPilot().rotate(30);
				
				getPilot().travel(2 * diff);
				
				getPilot().rotate(-30);
			} else {
				// turn left 30
				getPilot().travel(-Math.sqrt(3) * diff);
				getPilot().rotate(-30);
				getPilot().travel(2 * diff);
				getPilot().rotate(30);
			
				
				
			}

		} else if (around[3] == 1) {
			// sensor rotate to the left
			sensorMotor.rotate(-90);
			double distance = getDistanceNew();
			sensorMotor.rotate(90);

			double diff = Math.abs(12 - distance);

			if (distance > 9.5 && distance < 13) {
				// do nothing
			} else if (distance < 12) {
				
				getPilot().travel(-Math.sqrt(3) * diff);
				// turn left 30
				getPilot().rotate(-30);
				getPilot().travel(2 * diff);
				getPilot().rotate(30);
				
			} else {
				getPilot().travel(-Math.sqrt(3) * diff);
				getPilot().rotate(30);
				getPilot().travel(2 * diff);
				getPilot().rotate(-30);
				
			}

		}

	}

	/**
	 * Leave the robot in a safe, consistent state in which all sensors and motors
	 * are closed
	 */
	public void closeRobot() {
		leftBump.close();
		rightBump.close();
		irSensor.close();
		cSensor.close();
	}

	/**
	 * @return Whether or not the left bumper has been pressed
	 */
	public boolean isLeftBumpPressed() {
		leftSP.fetchSample(leftSample, 0);
		return (leftSample[0] == 1.0);
	}

	/**
	 * @return Whether or not the right bumper has been pressed
	 */
	public boolean isRightBumpPressed() {
		rightSP.fetchSample(rightSample, 0);
		return (rightSample[0] == 1.0);
	}

	/**
	 * @return The distance in front of the distance sensor
	 */
	public float getDistance() {
		distSP.fetchSample(distSample, 0);
		return distSample[0] * 100;
	}

	/**
	 * @return An array of 3 values that describe the colour under the colour sensor
	 *         of the robot
	 */
	public float[] getColour() {
		colourSP.fetchSample(colourSample, 0);
		return colourSample; // return array of 3 colours
	}

}
