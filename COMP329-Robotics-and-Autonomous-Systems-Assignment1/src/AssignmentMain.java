

import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
//303 yes
//304 shit
//305 OK
//306 ok
//307 ok !!!!
//308 ok
//309 ok !
//312 no bluetooth
public class AssignmentMain {

	public static void main(String[] args) { 
		Map wholeMap = new Map();
		PilotRobot me = new PilotRobot(wholeMap);
		
		PilotMonitor myMonitor = new PilotMonitor(me, 400, wholeMap);	
		// Set up the behaviours for the Arbitrator and construct it.
		Behavior b1 = new DriveForward(me,wholeMap);		
		Behavior b2 = new AvoidObstacle(me, wholeMap);
		Behavior b3 = new NavigateBack(me,wholeMap);
		Behavior b4 = new StopProgram();
		Behavior [] bArray = {b1, b2, b3, b4};
		Arbitrator arby = new Arbitrator(bArray);

		// Note that in the Arbritrator constructor, a message is sent
		// to stdout.  The following prints eight black lines to clear
		// the message from the screen
        for (int i=0; i<8; i++)
        	System.out.println("");

        // Start the Pilot Monitor
		myMonitor.start();

		// Tell the user to start
		myMonitor.setMessage("Press a key to start");				
        Button.waitForAnyPress();
        //update the distance around before any behaviour take control
        Motor.A.rotate(90);
		me.setDistanceLeft(me.getDistance()*100);
		//motor turn right 180
		Motor.A.rotate(-180);
		me.setDistanceRight(me.getDistance()*100);
		Motor.A.rotate(90);
	    me.setDistanceFront(me.getDistance()*100);
//		System.out.print("distanceFront: " + me.getDistanceFront());
//		System.out.print("distanceLeft: " + me.getDistanceLeft());
//		System.out.print("distanceRight: " + me.getDistanceRight());
		wholeMap.scanCells(Map.HEADING_NORTH, me.getDistanceFront(), me.getDistanceLeft(), me.getDistanceRight());

        //new AvoidObstacle(me, wholeMap).updateDistanceAround();       
        // Start the Arbitrator
		arby.go();
		arby.stop();
	}

}