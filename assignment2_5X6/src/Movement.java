

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import lejos.hardware.Button;
import lejos.hardware.Sound;

public class Movement {

	private final int port = 1234;
	private PilotRobot me;
	private ServerSocket server;
	private Socket client;
	private DataOutputStream dOut;
	private DataInputStream dIn;
	private String msg;

	/**
	 * Sets everything up and gets it all going i.e. the robot and its behaviours
	 * and the map as well as the monitor
	 * 
	 * @param args
	 * @throws IOException
	 */

	public Movement(PilotRobot me) {
		super();
		try {
			this.me = me;

			server = new ServerSocket(port);
			// Scanner reader = new Scanner(System.in); // Reading from System.in
			System.out.println("Awaiting client..");
			client = server.accept();
			System.out.println("CONNECTED");
			dOut = new DataOutputStream(client.getOutputStream());
			dIn = new DataInputStream(client.getInputStream());

			msg = "";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void statMain() throws IOException {

		// String msg = reader.nextLine();

		// Start the Pilot Monitor
		System.out.println("start of the program");
		// wait the user to start
		Sound.beep();

		// should return this data back and the colour back
		int disAround1[] = me.getDistance4Direct();
		String colorFirst = me.getCellType();
		System.out.print("Distance");
		Sound.beepSequenceUp();
		System.out
				.print(disAround1[0] + "" + disAround1[1] + "" + disAround1[2] + "" + disAround1[3] + "" + colorFirst);

		writeToPC(disAround1[0] + "" + disAround1[1] + "" + disAround1[2] + "" + disAround1[3] + colorFirst);
		msg = readFromPC();

		if (msg.equals("positionLock")) {

			writeToPC("GiveMePosition");

			msg = readFromPC();
			setLocation(msg);

			writeToPC("GiveMeHeading");

			msg = readFromPC();
			setHeading(msg);

			writeToPC("HeadingSet");

			me.setStartFindingVictim(true);
			me.rotateDirection(disAround1);

		} else {
			// rotate direction automatically
			me.rotateDirection(disAround1);
		}

		// second round
		while (me.isStartFindingVictim() == false) {

			// go forward to check the black tape
			// this function will immediate return
			me.moveTilBlack();

			int distanceAll[] = me.getDistance3Direct();

			String colorCurrent = me.getCellType();
			System.out.print(colorCurrent);

			writeToPC(distanceAll[0] + "" + distanceAll[1] + "" + distanceAll[2] + "" + distanceAll[3] + colorCurrent);
			// send back info and receive info in here

			msg = readFromPC();

			// When determine the robot's position, robot will receive "positionLock"
			if (msg.equals("positionLock")) {
				// set the current location

				writeToPC("GiveMePosition");

				msg = readFromPC();
				setLocation(msg);

				writeToPC("GiveMeHeading");

				msg = readFromPC();

				setHeading(msg);

				writeToPC("HeadingSet");

				me.setStartFindingVictim(true);

				me.rotateDirection(distanceAll);

			} else {
				me.rotateDirection(distanceAll);
			}

		}

		// start find victim based on your current location and
		while (me.getRestVictimNo() > 0) {
			// receive next x and y
			// moveToNext(int x, int y)

			// System.out.println("Heading: " + me.getHeadingNow()[0] + "," +
			// me.getHeadingNow()[1]);

			// msg = null;
			// msg = dIn.readUTF();
			msg = readFromPC();
			String[] ns = msg.split(",");
			System.out.println("From PC: " + msg);
			if(Integer.parseInt(ns[0])==me.getCurrentX()&&Integer.parseInt(ns[1])==me.getCurrentY()) {
				
			}else {
				me.moveToNext(Integer.parseInt(ns[0]), Integer.parseInt(ns[1]));
			}
			String color = me.getCellType();
			switch (color) {
			case "red":
				System.out.println("red");

				writeToPC("red");
				me.decreaseRestVictimNo("red");
				break;
			case "blue":
				System.out.println("blue");

				writeToPC("blue");
				me.decreaseRestVictimNo("blue");
				break;
			case "green":
				System.out.println("green");

				writeToPC("green");
				me.decreaseRestVictimNo("green");
				break;

			case "nonVictim":

				writeToPC("nonVictim");

			default:
				System.out.println("wrong in color sensing");

			}

		}

		Sound.beepSequenceUp();
		Sound.beepSequence();
		System.out.println("finished");
		Button.waitForAnyPress();
		server.close();

	}

	private void writeToPC(String str) throws IOException {
		dOut.writeUTF(str);
		dOut.flush();
	}

	private String readFromPC() throws IOException {
		String str = null;
		str = dIn.readUTF();
		System.out.println("From PC:" + str);
		return str;
	}

	private void setHeading(String msg) {
		int[] heading = new int[2];
		String[] ns = msg.split(",");
		heading[0] = Integer.parseInt(ns[0]);
		heading[1] = Integer.parseInt(ns[1]);
		me.setHeadingNow(heading);
	}

	private void setLocation(String msg) {
		me.setCurrentX(msg.charAt(0) - '0');
		me.setCurrentY(msg.charAt(1) - '0');
	}

}
