
// Environment code for project assignment2

import jason.asSyntax.*;
import jason.asSyntax.parser.ParseException;
import jason.environment.*;
import jason.environment.grid.Location;

import java.util.logging.*;

public class AssEnv extends Environment {

	private ArenaModel model;
	private ArenaView view;
	private Logger logger = Logger.getLogger("assignment2." + AssEnv.class.getName());

	/** Called before the MAS execution with the args informed in .mas2j */
	@Override
	public void init(String[] args) {
		super.init(args);
		model = new ArenaModel();
		view = new ArenaView(model);
		model.setView(view);
		updatePercepts();
	}

	@Override
	public boolean executeAction(String agName, Structure action) {

		logger.info(agName + " doing: " + action);
		try {
			Thread.sleep(500);
			if (action.equals(Const.moveTerm)) {
				model.moveSlot();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		 updatePercepts();

	        try {
	            Thread.sleep(500);
	        } catch (Exception e) {
	        	
	        }
		informAgsEnvironmentChanged();
		return true; // the action was executed with success
	}

	/** Called before the end of MAS execution */
	@Override
	public void stop() {
		super.stop();
	}

	public ArenaModel getModel() {
		return model;
	}

	public void setModel(ArenaModel model) {
		this.model = model;
	}

	public ArenaView getView() {
		return view;
	}

	public void setView(ArenaView view) {
		this.view = view;
	}
	

	
	 /** creates the agents perception based on the MarsModel */
	//keep updating where the robot think it is. 
    void updatePercepts() {
        clearPercepts();
        
        Location r1Loc = model.getAgPos(0);
        //Location r2Loc = model.getAgPos(1);
        
        Literal pos1 = Literal.parseLiteral("pos(r1," + r1Loc.x + "," + r1Loc.y + ")");
       // Literal pos2 = Literal.parseLiteral("pos(r2," + r2Loc.x + "," + r2Loc.y + ")");

        addPercept(pos1);
        //addPercept(pos2);
        
        if (model.hasObject(Const.ObstacleCode, r1Loc)) {
            addPercept(Const.v1);
        }
//        if (model.hasObject(Const.ObstacleCode, r2Loc)) {
//            addPercept(Const.v2);
//        }
    }
}
