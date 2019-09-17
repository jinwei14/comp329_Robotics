//
//// BackUp.java
//// 
//// An behaviour to back up the robot if the touch sensors detect
//// an obstacle.  It exploits the PilotRobot class to make sure that
//// calls to the motors are non-blocking (i.e. they return immediately)
//// so that if the Arbitrator asks a behaviour to be suppressed, it can
//// stop immediately.
////
//// Terry Payne
//// 8th October 2017
////
//
//import lejos.hardware.Button;
//import lejos.robotics.navigation.MovePilot;
//import lejos.robotics.subsumption.Behavior;
//
//public class BackUp implements Behavior {
//	public boolean suppressed;
//	private PilotRobot me;
//	private Map wholeMap;
//	private MovePilot pilot;
//
//	// Constructor - the robot, and gets access to the pilot class
//	// that is managed by the robot (this saves us calling
//	// me.getPilot.somemethod() all of the while)
//	public BackUp(PilotRobot robot, Map mp) {
//		wholeMap = mp;
//		me = robot;
//		pilot = me.getPilot();
//	}
//
//	// When called, this should stop action()
//	public void suppress() {
//		suppressed = true;
//	}
//
//	// When called, determine if this behaviour should start
//	public boolean takeControl() {
//		if ((me.isLeftBumpPressed() || me.isRightBumpPressed()) && !Button.ESCAPE.isDown()) {
//
//
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	// This is our action function. All calls to the motors should be
//	// non blocking, so that they can be stopped if suppress is true.
//	// If a call is made to move a specific distance or rotate a specific
//	// angle etc, then it should return immediately, and monitored until
//	// it has completed. The code below illustrates this, but waiting
//	// until the robot stops moving. An OdometryPoseProvider could also
//	// be used for this.
//
//	public void action() {
//		// Allow this method to run
//		suppressed = false;
//
//		// Reverse for 20cm, and have the thread yield until this is
//		// complete (i.e. the robot stops) or if suppressed is true.
//		pilot.travel(-20, true);
//		while (pilot.isMoving() && !suppressed) {
//			Thread.yield(); // wait till turn is complete or suppressed is called
//
//		}
//		// Rotate for 45 degrees, and have the thread yield until this is
//		// complete (i.e. the robot stops) or if suppressed is true. Note
//		// that we can check suppressed to see if it is even worth doing.
//		// There are more elegant ways of doing this!!!
//		if (!suppressed)
//			pilot.rotate(-90, true);
//		while (pilot.isMoving() && !suppressed) {
//			Thread.yield(); // wait till turn is complete or suppressed is called
//		}
//
//		// Ensure that the motors have stopped.
//		pilot.stop();
//	}
//}