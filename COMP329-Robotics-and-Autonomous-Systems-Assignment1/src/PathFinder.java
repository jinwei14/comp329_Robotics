import java.util.ArrayList;

public class PathFinder {
	
	// The set of currently discovered nodes that are not evaluated yet.
    // Initially, only the start node is known.
	public  ArrayList<Cell> openSet  = new ArrayList<Cell>();
	
	// The set of nodes already evaluated
	public  ArrayList<Cell> closedSet= new ArrayList<Cell>();
	
	public ArrayList<Cell> path = new ArrayList<Cell>();
	
	public ArrayList<Cell> findPath(Cell[][] map,Cell targetCell,Cell startCell)
	{
		boolean pathWasFound = false;
		
		while(!pathWasFound){
			
			if(!openSet.isEmpty())
			{
				int winner = 0;
		    	
		    		for(int i = 0; i<openSet.size();i++)
		    		{
		    			if( openSet.get(i).getF()< openSet.get(winner).getF())
		    			{
		    				winner = i;
		    			}
		    		}
		    		
		    		Cell current = openSet.get(winner);
		    	   
		    		if(current == targetCell)
		    		{
		    			Cell temp = current;
		    			path.add(temp);
		    		
		    			while(temp.getPrevious()!=null)
		    			{
		    				if(temp.getPrevious() !=null)
		    				{
		    					path.add(temp.getPrevious());
		    					temp = temp.getPrevious();
		    				}
		    				
		    				System.out.println("While" + temp.getCellXPos() + " " + temp.getCellYPos() );
		    			}
		    		
		    			System.out.println("DONE");
		    			pathWasFound = true;
		    			return path;
		    	  }
		    	  
		    	  openSet.remove(current);
		    	  closedSet.add(current);
		    	   
		    	  ArrayList<Cell> neighbours = current.getNeighbours();
		    	   
		    	  for(int i = 0; i<neighbours.size();i++)
		    	  {
		    		  Cell neighbour = neighbours.get(i);
		    		  
		    		  // if(closedSet.contains(neighbour) == false && neighbour.getCellProbability() < 0.7)
		    		  if(closedSet.contains(neighbour) == false  && Float.compare(neighbour.getCellProbability(), 0.8f) < 0)/*&& neighbour.getCellProbability() < 0.7*/
		    		  {
		    			  int tempG = current.getG() +1;
		    		   
		    			  if(openSet.contains(neighbour))
		    			  {
		    				  if(tempG < neighbour.getG())
		    				  {
		    					  neighbour.setG(tempG);
		    				  }
		    			   
		    			  }else
		    			  {
		    				  neighbour.setG(tempG);
		    				  openSet.add(neighbour);
		    			  }
		    		   
		    		   neighbour.setH(heuristic(neighbour,targetCell));
		    		   neighbour.setF(neighbour.getG()+neighbour.getH());
		    		   neighbour.setPrevious(current);
		    		  }
		    	   }
		    	   
		    	  
		    }else{
		    	System.out.println("No solution");
		    	pathWasFound = true;
		    	return null;
		    }
		    		
		}
	
		return null;
	}
	
	// Uses the Manhattan distance
	private int heuristic(Cell a, Cell b)
	{
		return Math.abs(a.getCellXPos()-b.getCellXPos()) + Math.abs(a.getCellYPos()-b.getCellYPos());
	}
}