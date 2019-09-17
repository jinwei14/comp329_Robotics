
// DriveForward.java
// 
// An behaviour to drive the robot forward - this is the simplest 
// behaviour that we consider (and hence the takeControl() action 
// is true.  It exploits the PilotRobot class drive the robot 
// forward using a non-blocking call (i.e. it returns immediately) 
// so that if the Arbitrator asks a behaviour to be suppressed, it 
// can stop immediately.
//
// Terry Payne
// 8th October 2017
//5X4
//-90 robot will turn left
//-90 Motor will turn right
//303 no
//304 ok
//305 OK
//306 not ok
//307 ok !!!!
//309 not ok !
//312 no bluetooth
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.robotics.subsumption.Behavior;

/**
 * basic idea is to move the robot forward 25.4 cm and use the ultrasound sensor
 * to scan to the left and right direction. Once the obstacle is find in front
 * of the robot then scan the left and right direction. If the distance to the
 * left direction is much more bigger than the right direction then turn left,
 * vice verse. Once the robot decide to turn it need to figure out the cell it
 * is turning to has not been explored before. All movement are going to be
 * measured by centimetre.
 **/
//
public class DriveForward implements Behavior {
	// private int headingNow;
	public boolean suppressed;
	private PilotRobot me;
	private Map wholeMap;

	// Constructor - store a reference to the robot
	// this part need to change is the heading changed
	public DriveForward(PilotRobot robot, Map mp) {
		wholeMap = mp;
		me = robot;
		// wholeMap = new Map();
	}

	// When called, this should stop action()
	public void suppress() {
		suppressed = true;
	}

	/**
	 * 
	 * This returns true, so will always take control (if no higher priority
	 * behaviour also takes control). We could modify this to look for a "finish"
	 * variable so that if the robot should stop, then we could simply not take
	 * control. If no behaviour takes control, the Arbritrator will end.
	 * 
	 * 
	 * 
	 */
	public boolean takeControl() {
		// assume there is no obstacle in the front of the robot
		me.setObstacleFront(false);

		return true;
	}

	/**
	 * 
	 * This is our action function. This starts the motor running (which returns
	 * immediately). We then simply run until told to suppress our action, in which
	 * case we stop.
	 * 
	 * 
	 * 
	 */
	public void action() {
		// Allow this method to run
		// Button.waitForAnyPress();
		boolean shouldNotMove = false;
		boolean shouldRecordColor = false;
		suppressed = false;

		//if (1,1) has only been to once so that keep travelling
		if(wholeMap.getMapSpaceBeenTo()[1][1] <= 1) {
		   me.getPilot().travel(me.getMovedDistance(), true);
		 //if (1,1) has been to more than once so that the robot is in a deadlock
		}else {
			//move to the neighbour of place that has't been to and keep updating the map
			//this can deal with the deadlock
			//loop through this action until the Map is completed
			Sound.beep();
			System.out.print("need to work on");
			Button.waitForAnyPress();
			me.dealDeadlock();
			


		}
 
		// While we can run, yield the thread to let other threads run.
		// It is important that no action function blocks any other action.
		while (!suppressed && me.getPilot().isMoving()) {
			shouldRecordColor = me.checkColorPaper();
			shouldNotMove = me.checkStopFront();
			// if there is an obstacle in front of the robot stop the robot immediately
			Thread.yield();

		}
		// stop the pilot

		me.setHeading();
		me.getPilot().stop();
         
		// only set after each move have finished
		// if there obstacle found between each move then the heading will changed
		// however the slightly move before stop doesn't count
		if (!shouldNotMove) {
			wholeMap.move(me.getHeadingNow());
			if (shouldRecordColor) {

				wholeMap.setEndLocation(wholeMap.getCurrentPositionX(), wholeMap.getCurrentPositionY());
			}
			// when stop moving scan left and right and the front then calibrate the angle

			// update the distance around
			// turning is false because the robot has not turn
			me.updateDistanceAround(false);
			// update the scanned map here.
			wholeMap.scanCells(me.getHeadingNow(), me.getDistanceFront(), me.getDistanceLeft(), me.getDistanceRight());
			this.suppress();

			// Ensure that the motors have stopped.
			me.getPilot().stop();

		}

	}

}