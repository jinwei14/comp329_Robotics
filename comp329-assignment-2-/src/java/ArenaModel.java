import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;

public class ArenaModel extends GridWorldModel{

	public ArenaModel() {
		super(Const.GrideWidth, Const.GrideHeight, 2);
		 try {
             setAgPos(0, 1, 1);        
//             Location r2Loc = new Location(Const.GrideWidth/2, Const.GrideHeight/2);
//             setAgPos(1, r2Loc);
         } catch (Exception e) {
             e.printStackTrace();
         }
		 addWall();
		 addObstacle();
		 initMap();	
	}
	
	private void addWall() { 
		//fill in the first row and the last row as wall GrideWidth = 9
		for (int i = 0; i< Const.GrideWidth  ;i++) {
			this.add(Const.ObstacleCode, i, 0);
			this.add(Const.ObstacleCode, i, Const.GrideHeight -1);
		}
		
		//fill in the first column and the last column as wall GrideHeight = 8
		for (int j = 0; j< Const.GrideHeight  ;j++) {
			this.add(Const.ObstacleCode, 0, j);
			this.add(Const.ObstacleCode, Const.GrideWidth-1, j);
		}
		
	}
	
	private void addObstacle() {
		this.add(Const.ObstacleCode, 5,1);
		this.add(Const.ObstacleCode, 3,3);
		this.add(Const.ObstacleCode, 2,6);		
	}
	
	 void moveSlot() throws Exception {
 		
         Location r1 = getAgPos(0);
         r1.x++;
         if (r1.x == getWidth()) {
             r1.x = 0;
             r1.y++;
         }
         // finished searching the whole grid
         if (r1.y == getHeight()) {
             return;
         }
         setAgPos(0, r1);
        //setAgPos(1, getAgPos(1)); // just to draw it in the view

     }
	 
	 private void initMap() {
		 
		 
	 }

}
