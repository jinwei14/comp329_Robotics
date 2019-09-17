

//import lejos.hardware.ev3.LocalEV3;
//import lejos.hardware.lcd.GraphicsLCD;
//
///**
// * This class is to be run as a thread to show the current state of the map in
// * the form of a grid of rectangles. If the rectangle is filled then it means the
// * robot has seen an obstacle there more times than not
// */
//public class PilotMonitor extends Thread {
//
//	private int delay;
//	public PilotRobot robot;
//	public Map wholeMap;
//
//	GraphicsLCD lcd = LocalEV3.get().getGraphicsLCD();
//
//	// Make the monitor a daemon and set
//	// the robot it monitors and the delay
//	public PilotMonitor(PilotRobot r, int d, Map mp) {
//		this.setDaemon(true);
//		delay = d;
//		robot = r;
//		wholeMap = mp;
//	}
//
//	/**
//	 * Main run method which shows the grid on the monitor to say where the robot has seen
//	 * and where the obstacles have been seen also the current location will be drawn on the map
//	 */
//	public void run() {
//		while (true) {
//			lcd.clear();
//			for (int i = 1; i <= Map.NUMBER_OF_COLUMNS - 2; i++) {
//				// position and weigth and height
//				for (int j = 1; j <= Map.NUMBER_OF_ROWS - 2; j++) {
//					lcd.drawRect((Consts.cellSize * (i)), ((Consts.cellSize * (Map.NUMBER_OF_ROWS - 2)) 
//							- Consts.cellSize * j), Consts.cellSize, Consts.cellSize);
//					// if the probability of the obstacle greater than 0.7
//					if (wholeMap.getProbabilityMap()[i][j].getCellProbability() >= (float) Consts.probBound) {
//						lcd.fillRect((Consts.cellSize * (i)), ((Consts.cellSize * (Map.NUMBER_OF_ROWS - 2)) 
//								- Consts.cellSize * j), Consts.cellSize, Consts.cellSize);
//					}
//					
//					//fill in the space has not been scanned on the screen
//					if (wholeMap.getMapSpaceBeenScanned()[i][j] == 0) {
//						lcd.drawChar('?',(Consts.cellSize * (i)), ((Consts.cellSize * (Map.NUMBER_OF_ROWS - 2)) 
//								- Consts.cellSize * j), GraphicsLCD.VCENTER);
//					}
//                     
//					//put an X to represent the current location
//					lcd.drawChar('X', Consts.cellSize * wholeMap.getCurrentPositionX(),
//							(20 * (Map.NUMBER_OF_ROWS - 2)) - Consts.cellSize * wholeMap.getCurrentPositionY(),
//							GraphicsLCD.VCENTER);
//					//put a character 'E' to represent the end location once it has been scanned
//					if (wholeMap.getEndLocationX() != -1 && wholeMap.getEndLocationY() != -1) {
//						lcd.drawChar('E', Consts.cellSize * wholeMap.getEndLocationX(),
//								(Consts.cellSize * (Map.NUMBER_OF_ROWS - 2)) 
//								- Consts.cellSize * wholeMap.getCurrentPositionY(), GraphicsLCD.VCENTER);
//					}
//				}
//
//			}
//			try {
//				sleep(delay);
//			} catch (Exception e) {
//				e.printStackTrace();
//				;
//			}
//		}
//	}
//
//}

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

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;

public class PilotMonitor extends Thread {

	private int delay;
	//public PilotRobot robot;
	private String msg;
	private int width;
	private int height;
	PilotRobot me;
    GraphicsLCD lcd = LocalEV3.get().getGraphicsLCD();
	
    // Make the monitor a daemon and set
    // the robot it monitors and the delay
    public PilotMonitor( int d,PilotRobot me){
    	this.setDaemon(true);
    	delay = d;
    //	robot = r;
    	msg = "";
    	width = lcd.getWidth()/6;
    	height = lcd.getHeight()/6;
    	this.me = me;
   // 	length = lcd.getHeight()/6;
    	
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
    public void run(){
    	// The decimalformat here is used to round the number to three significant digits
		DecimalFormat df = new DecimalFormat("####0.000");
           
    	while(true){
			lcd.clear();
			for (int i = 0; i <= 5; i++) {
				// position and weigth and height
				for (int j = 0; j <= 5; j++) {
					lcd.drawRect((width * i), (height * j), width, height);
					// if the probability of the obstacle greater than 0.7
				}

				}
			
			//obstacles
						lcd.fillRect((width * (3-1)), (height * (2-1)), width, height);
						lcd.fillRect((width * (5-1)), (height * (2-1)), width, height);
						lcd.fillRect((width * (2-1)), (height * (3-1)), width, height);
						lcd.fillRect((width * (5-1)), (height * (5-1)), width, height);
						lcd.fillRect((width * (6-1)), (height * (5-1)), width, height);
						lcd.fillRect((width * (1-1)), (height * (6-1)), width, height);


//						/*
//						 * 	public static final Location V1 = new Location(1, 1);
//	public static final Location V2 = new Location(5, 1);
//	public static final Location V3 = new Location(3, 3);
//	public static final Location V4 = new Location(4, 4);
//	public static final Location V5 = new Location(3, 5);
//						 * 
//						 * 
//						 * 
//						 * */
						lcd.drawChar('V', width * (1-1), height * (1-1), GraphicsLCD.VCENTER);
						lcd.drawChar('V', width * (5-1), height * (1-1), GraphicsLCD.VCENTER);
						lcd.drawChar('V', width * (3-1), height * (3-1), GraphicsLCD.VCENTER);
						lcd.drawChar('V', width * (4-1), height * (4-1), GraphicsLCD.VCENTER);
						lcd.drawChar('V', width * (3-1), height * (5-1), GraphicsLCD.VCENTER);
//						//obstacles victim
			
			lcd.drawChar('X', width * (me.getCurrentX()-1), height * (me.getCurrentX()-1), GraphicsLCD.VCENTER);
			
			try {
				sleep(delay);
			} catch (Exception e) {
				e.printStackTrace();
				;
			}
		}
    }

}