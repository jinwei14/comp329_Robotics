
import java.io.IOException;


//EV329Lego
public class Main {

	public static void main(String[] args) throws IOException {
		
		PilotRobot me  =new PilotRobot();
		
		PilotMonitor myMonitor = new PilotMonitor(400,me);
		//myMonitor.start();
		
		Movement start =new Movement(me);
		start.statMain();
	}

}
