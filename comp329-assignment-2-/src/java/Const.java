import jason.asSyntax.Literal;
import jason.asSyntax.Term;

//variables and literals 
public final class Const {
	// port number
	public static final int PORT = 1234;
	// IP address
	// String ip = "10.0.1.1";
	public static final String IP = "0.0.0.0"; // BT;

	public static final int GrideWidth = 9; // grid size 7+2
	public static final int GrideHeight = 8; // grid size 6+2
	public static final int ObstacleCode = 8; // obstacle code in grid model

	public static final Term moveTerm = Literal.parseLiteral("next(slot)");
	public static final Literal v1 = Literal.parseLiteral("victim(r1)");
	public static final Literal v2 = Literal.parseLiteral("victim(r2)");
}
