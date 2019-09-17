
// PillotMonitor.java
// 
// Based on the RobotMonitor class, this displays the robot
// state on the LCD screen; however, it works with the PilotRobot
// class that exploits a MovePilot to control the Robot.
//
// Terry Payne
// 8th October 2017
//

import java.text.DecimalFormat;

import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;

public class PilotMonitor extends Thread {

	private int delay;
	public PilotRobot robot;
	private String msg;
	public Map wholeMap;

	GraphicsLCD lcd = LocalEV3.get().getGraphicsLCD();

	// Make the monitor a daemon and set
	// the robot it monitors and the delay
	public PilotMonitor(PilotRobot r, int d, Map mp) {
		this.setDaemon(true);
		delay = d;
		robot = r;
		msg = "";
		wholeMap = mp;
	}

	// Allow extra messages to be displayed
	public void resetMessage() {
		this.setMessage("");
	}

	// Clear the message that is displayed
	public void setMessage(String str) {
		msg = str;
	}

	// The monitor writes various bits of robot state to the screen, then
	// sleeps.
	public void run() {
		// The decimal format here is used to round the number to three significant
		// digits
		DecimalFormat df = new DecimalFormat("####0.000");

		while (true) {
			lcd.clear();
//			 lcd.setFont(Font.getDefaultFont());
//			 lcd.drawString("Robot Killer", lcd.getWidth() / 2, 0, GraphicsLCD.HCENTER);
//			 lcd.setFont(Font.getSmallFont());
//			
//			 lcd.drawString("LBump: " + robot.isLeftBumpPressed(), 0, 20, 0);
//			 lcd.drawString("RBump: " + robot.isRightBumpPressed(), 0, 30, 0);
//			 lcd.drawString("Dist: " + robot.getDistance(), 0, 40, 0);
//			 lcd.drawString("Colour: [" + df.format(robot.getColour()[0]) + " " +
//			 df.format(robot.getColour()[1]) + " "
//			 + df.format(robot.getColour()[2]) + "]", 0, 50, 0);
//			
//			 // Note that the following exploit additional information available from the
//			 // MovePilot. This could be extended to include speed, angular velocity, pose
//			 // etc.
//			 lcd.drawString("Motion: " + robot.getPilot().isMoving(), 0, 60, 0);
//			 lcd.drawString(" type: " + robot.getPilot().getMovement().getMoveType(), 0,
//			 70, 0);
//			 
////			 if(robot.getColour()[0] < 0.1 && robot.getColour()[1] < 0.1 && robot.getColour()[2] > 0.175 ) {
//			 if(robot.getColour()[0] > 0.015 && robot.getColour()[0] < 0.03 && robot.getColour()[1] < 0.15 && robot.getColour()[1] > 0.07 && robot.getColour()[2] > 0.07) {
//			 lcd.drawString("Paper scanned " , 0, 80, 0);
//			 }else {
//				 lcd.drawString("Paper not scanned " , 0, 80, 0);
//			 }
//			 lcd.drawString("heading " + robot.getHeadingNow(), 0, 90, 0);
//			 lcd.drawString(msg, 0, 100, 0);
			 //lcd.draw;
			// x asix is column
			// y is row

			// due to different coorennate system
			/*
			 * X ----> monitor | | |Y
			 */
			for (int i = 1; i <= Map.NUMBER_OF_COLUMNS - 2; i++) {
				// position and weigth and height
				for (int j = 1; j <= Map.NUMBER_OF_ROWS - 2; j++) {
					lcd.drawRect((20 * (i)), ((20 * 6) - 20 * j), 20, 20);
					// if the probability of the obstacle greater than 0.7
					if (wholeMap.getProbabilityMap()[i][j].getCellProbability() >= (float) 0.8) {
						lcd.fillRect((20 * (i)), ((20 * 6) - 20 * j), 20, 20);
					}

					lcd.drawChar('X', 20 * wholeMap.getCurrentPositionX(),
							(20 * 6) - 20 * wholeMap.getCurrentPositionY(), GraphicsLCD.VCENTER);
					if (wholeMap.getEndLocationX() != -1 && wholeMap.getEndLocationY() != -1) {
						lcd.drawChar('?', 20 * wholeMap.getEndLocationX(),
								(20 * 6) - 20 * wholeMap.getCurrentPositionY(), GraphicsLCD.VCENTER);
						// lcd.drawString(String.valueOf(wholeMap.getProbabilityMap()[i][j].getCellProbability())
						// , (20*(i)), ((20*6)-20*j), GraphicsLCD.VCENTER);
					}
				}

			}
			lcd.drawString("heading　 " + robot.getHeadingNow(), 70, 80, 0);
			// lcd.drawString("right "+robot.getDistanceRight(), 70, 95, 0);
			// lcd.drawString("front "+robot.getDistanceFront(), 70, 115, 0);
			// lcd.drawString("X= "+wholeMap.getCurrentPositionX(), 0, 80, 0);
			// lcd.drawString("Y= "+wholeMap.getCurrentPositionY(), 0, 100, 0);

			try {
				sleep(delay);
			} catch (Exception e) {
				// We have no exception handling
				;
			}
		}
	}

}