import lejos.hardware.Button;

/**
 * This class is to be run as a thread to accompany a Lego EV3 brick which
 * utilizes an ultrasound sensor and only turns in right angles. It generates a
 * map by the dimensions provided using BOX_LENGTH and BOX_WIDTH with
 * NUMBER_OF_ROWS and NUMBER_OF_COLUMNS which are all integer values and
 * represent exactly what they say they represent. The interface allows other
 * classes to see how many times each space has been seen, to see what still
 * needs to be explored, and the probability that each space on the grid is
 * occupied by an obstacle.
 * 
 * @author Joshua Roberts and jinwei.zhang
 *
 */
public class Map {
	
	public static final int NUMBER_OF_ROWS = 8;
	public static final int NUMBER_OF_COLUMNS = 7;
	// the length of the map is 195 cm
	// the width of the map is 156
	public static final float MAP_LENGTH = 195;
	public static final float MAP_WIDTH = 155;

	public static final float BOX_LENGTH = MAP_LENGTH / (NUMBER_OF_ROWS - 2);
	public static final float BOX_WIDTH = MAP_WIDTH / (NUMBER_OF_COLUMNS - 2);

	public static final int HEADING_NORTH = 0;
	public static final int HEADING_WEST = -90;
	public static final int HEADING_EAST = 90;
	public static final int HEADING_SOUTH = -180;
	public static final int MAX_VIEWING_DISTANCE = 200;

	private Cell[][] mapProbability;
	private int[][] mapSpaceBeenScanned;
	private int[][] mapSpaceOccupied;
	private int[][] mapSpaceBeenTo;



	private int currentXCoordinate;
	private int currentYCoordinate;

	private int endLocationX;
	private int endLocationY;

	private boolean wholeMapResult;





	/**
	 * Initialise the map and the sensor
	 * 
	 * @param ir
	 *            An EV3 Ultrasonic Sensor which has already been initialised
	 * @param opp
	 *            the Odometry Pose Provider which will be used by the pilot as well
	 *            as to get the location and bearing of the robot
	 */
	public Map() {
		currentXCoordinate = 1;
		currentYCoordinate = 1;
		endLocationX = -1;
		endLocationY = -1;
		wholeMapResult = false;

		mapProbability = new Cell[NUMBER_OF_COLUMNS][NUMBER_OF_ROWS];
		mapSpaceBeenScanned = new int[NUMBER_OF_COLUMNS][NUMBER_OF_ROWS];
		mapSpaceOccupied = new int[NUMBER_OF_COLUMNS][NUMBER_OF_ROWS];
		mapSpaceBeenTo = new int[NUMBER_OF_COLUMNS][NUMBER_OF_ROWS];
		// Initialise the 2D arrays
		for (int a = 0; a < NUMBER_OF_COLUMNS; a++) {
			for (int b = 0; b < NUMBER_OF_ROWS; b++) {
				mapProbability[a][b] = new Cell(a, b);
				mapProbability[a][b].setCellProbability(0);

				mapSpaceBeenScanned[a][b] = 0;
				mapSpaceOccupied[a][b] = 0;
				mapSpaceBeenTo[a][b] = 0;
				//start point
				mapSpaceBeenTo[1][1] = 1;
				// update the wall into 1 which means all the walls are
				// occupied.
				if (a == 0 || a == NUMBER_OF_COLUMNS - 1) {
					mapSpaceOccupied[a][b] = 1;
					mapSpaceBeenScanned[a][b] = 1;
					mapProbability[a][b].setCellProbability(1);
					mapSpaceBeenTo[a][b] = 1;
				}
				if (b == 0 || b == NUMBER_OF_ROWS - 1) {
					mapSpaceOccupied[a][b] = 1;
					mapSpaceBeenScanned[a][b] = 1;
					mapProbability[a][b].setCellProbability(1);
					mapSpaceBeenTo[a][b] = 1;
				}

			}
		}

		// update all the neighbours
		//fixed add neighbour problem due to different coordinate
		for (int a = 0; a < NUMBER_OF_COLUMNS; a++) {
			for (int b = 0; b < NUMBER_OF_ROWS; b++) {
				mapProbability[a][b].addNeighbours(mapProbability, a, b, NUMBER_OF_ROWS - 1, NUMBER_OF_COLUMNS - 1);

			}
		}

		// updateMapProbability(currentXCoordinate, currentYCoordinate);

		// Initialise the corners of the map since they cannot be visited by the robot
		// mapSpaceSeenCounts[0][0] = 1;
		// mapSpaceSeenCounts[0][NUMBER_OF_ROWS - 1] = 1;
		// mapSpaceSeenCounts[NUMBER_OF_COLUMNS - 1][0] = 1;
		// mapSpaceSeenCounts[NUMBER_OF_COLUMNS - 1][NUMBER_OF_ROWS - 1] = 1;

		// updateMapProbability(0, 0);
		// updateMapProbability(0, NUMBER_OF_ROWS - 1);
		// updateMapProbability(NUMBER_OF_COLUMNS - 1, 0);
		// updateMapProbability(NUMBER_OF_COLUMNS - 1, NUMBER_OF_ROWS - 1);
	}

	public int[][] getMapSpaceOccupied() {
		return this.mapSpaceOccupied;
	}

	/**
	 * @return A 2D array stating how many times each space in the map grid has been
	 *         scanned
	 */
	public int[][] getMapSpaceBeenScanned() {
		return this.mapSpaceBeenScanned;
	}

	// setter getter for the current location
	public void setCurrentPositionX(int i) {
		currentXCoordinate = i;
	}

	public void setCurrentPositionY(int j) {
		currentYCoordinate = j;
	}

	public int getCurrentPositionX() {
		return this.currentXCoordinate;
	}

	public int getCurrentPositionY() {
		return this.currentYCoordinate;
	}

	/**
	 * Store the coordinates of the end point on the map
	 * 
	 * @param x
	 *            The x coordinate
	 * @param y
	 *            The y coordinate
	 */
	public void setEndLocation(int x, int y) {
		this.endLocationX = x;
		this.endLocationY = y;
	}

	/**
	 * @return The int for the end location on the grid X and Y
	 */
	public int getEndLocationX() {
		return this.endLocationX;
	}

	public int getEndLocationY() {
		return this.endLocationY;
	}
	
	public boolean isWholeMapResult() {
		return this.wholeMapResult;
	}

	public void setWholeMapResult(boolean wholeMapResult) {
		this.wholeMapResult = wholeMapResult;
	}
	
	public int[][] getMapSpaceBeenTo() {
		return mapSpaceBeenTo;
	}

	public void setMapSpaceBeenTo(int[][] mapSpaceBeenTo) {
		this.mapSpaceBeenTo = mapSpaceBeenTo;
	}
	

	/**
	 * Update the probability that a space on the grid is occupied by an obstacle
	 * 
	 * @param x
	 *            The x coordinate
	 * @param y
	 *            The y coordinate
	 */
	private void updateCellProbability(int x, int y) {
		if (mapSpaceBeenScanned[x][y] != 0) {
			mapProbability[x][y].setCellProbability(((float) mapSpaceBeenScanned[x][y] + (float) mapSpaceOccupied[x][y])
					/ (2 * (float) mapSpaceBeenScanned[x][y]));
		} else {
			mapProbability[x][y].setCellProbability(0);
		}

	}

	/**
	 * 
	 * update the whole Probability array.
	 * 
	 * 
	 */
	private void updateWholeProbability() {
		boolean result = true;
		// i means COLUMNS j means rows;
		for (int i = 1; i <= NUMBER_OF_COLUMNS - 2; i++) {
			for (int j = 1; j <= NUMBER_OF_ROWS - 2; j++) {
				this.updateCellProbability(i, j);
				//check if there is any space that hasn't been to
				if(this.mapSpaceBeenScanned[i][j] == 0) {
					//once found has been scanned is o set the return value to be false
					System.out.println("not sc (x,y) "+ i + j);
					result = false;
				}
			}
		}
		this.setWholeMapResult(result);
	}

	/**
	 * @return The map probability of occupation for each space in the grid in the
	 *         form of a 2D array
	 */
	public Cell[][] getProbabilityMap() {
		return mapProbability;
	}

	/**
	 * Update the current coordinates under the assumption that the robot has moved
	 * one unit in the direction of the heading
	 * 
	 * @param heading
	 *            The direction of travel in rightangles i.e. can either be 0, 90,
	 *            -90 or 180
	 */
	int count = 0;
	public void move(int heading) {
		switch (heading) {
		case (HEADING_NORTH):
			currentYCoordinate++;
			// update the occupied matrix.
			mapSpaceOccupied[currentXCoordinate][currentYCoordinate] -= 1;
			mapSpaceBeenTo[currentXCoordinate][currentYCoordinate] += 1;
			mapSpaceBeenScanned[currentXCoordinate][currentYCoordinate] += 1;
			updateWholeProbability();
			break;
		case (HEADING_WEST):
			currentXCoordinate--;
			mapSpaceOccupied[currentXCoordinate][currentYCoordinate] -= 1;
			mapSpaceBeenTo[currentXCoordinate][currentYCoordinate] += 1;
			mapSpaceBeenScanned[currentXCoordinate][currentYCoordinate] += 1;
			updateWholeProbability();
			break;
		case (HEADING_EAST):
			currentXCoordinate++;
		try{
			mapSpaceOccupied[currentXCoordinate][currentYCoordinate] -= 1;
			mapSpaceBeenTo[currentXCoordinate][currentYCoordinate] += 1;
			mapSpaceBeenScanned[currentXCoordinate][currentYCoordinate] += 1;
			updateWholeProbability();
			
		}catch(Exception e ){
			
			System.out.println("shit" + count++);
			
		}
			break;
		case (HEADING_SOUTH):
		case (-HEADING_SOUTH):
			currentYCoordinate--;
			mapSpaceOccupied[currentXCoordinate][currentYCoordinate] -= 1;
			mapSpaceBeenTo[currentXCoordinate][currentYCoordinate] += 1;
			mapSpaceBeenScanned[currentXCoordinate][currentYCoordinate] += 1;
			updateWholeProbability();
			break;
		default:
			System.out.println("wrong! Map.move()");
		}
	
	}

	/**
	 * Using the given heading and the three distances recorded, update the cells
	 * visible at rightangles to the robot
	 * 
	 * @param heading
	 *            The current direction of travel only in rightangles
	 * @param distanceFront
	 *            The distance recorded in the same direction as the heading
	 * @param distanceLeft
	 *            The distance recorded left of the direction of the heading from
	 *            the robot
	 * @param distanceRight
	 *            The distance recorded right of the direction of the heading from
	 *            the robot
	 */
	public void scanCells(int heading, float distanceFront, float distanceLeft, float distanceRight) {
		if(distanceFront <= MAX_VIEWING_DISTANCE && distanceLeft <= MAX_VIEWING_DISTANCE && distanceRight<= MAX_VIEWING_DISTANCE) {
		switch (heading) {
		case (HEADING_NORTH):
			// For looking straight ahead
			updateNorth(distanceFront);

			// For looking left
			updateWest(distanceLeft);

			// For looking right
			updateEast(distanceRight);

			// update the probability
			updateWholeProbability();
			break;
		case (HEADING_WEST):
			// For looking straight ahead
			updateWest(distanceFront);

			// For looking left
			updateSouth(distanceLeft);

			// ForLookingRight
			updateNorth(distanceRight);

			// update the probability
			updateWholeProbability();
			break;
		case (HEADING_EAST):
			// For looking straight ahead
			updateEast(distanceFront);

			// For looking left
			updateNorth(distanceLeft);

			// For looking right;
			updateSouth(distanceRight);

			// update the probability
			updateWholeProbability();
			break;
			//odometery sometimes gives minus 180 and plus 180
		case (HEADING_SOUTH):
		case(-HEADING_SOUTH):
			// For looking straight ahead
			updateSouth(distanceFront);

			// For looking left
			updateEast(distanceLeft);

			// For looking right
			updateWest(distanceRight);

			// update the probability
			updateWholeProbability();

			break;
		default:
			System.out.println("scanCells fail");
			break;
		}
		}

	}

	

	/**
	 * Update the cells up the map from the robot
	 * 
	 * @param distance
	 *            The distance that has been recorded
	 */
	public void updateNorth(float distance) {

		boolean foundEnd = false;
		int cellNo = 1;
		while (!foundEnd) {
		   
			if (distance <= BOX_LENGTH * (cellNo)) {
				// the location of the obstacle in the m=north 
//				System.out.println("x: "+getCurrentPositionX());
//				System.out.println("y: "+getCurrentPositionY());
//		        Button.waitForAnyPress();
				this.mapSpaceBeenScanned[getCurrentPositionX()][getCurrentPositionY() + cellNo] += 1;
				this.mapSpaceOccupied[getCurrentPositionX()][getCurrentPositionY() + cellNo] += 1;

				// update the cells been scanned and occupied.
				for (int j = getCurrentPositionY(); j <= getCurrentPositionY() + cellNo - 1; j++) {
					this.mapSpaceBeenScanned[getCurrentPositionX()][j] += 1;
					this.mapSpaceOccupied[getCurrentPositionX()][j] -= 1;

				}
				foundEnd = true;
			} else {
				//some times the robot scanned the wall it will be out of bound.
				cellNo++;
				if(cellNo + this.getCurrentPositionY() > Map.NUMBER_OF_ROWS - 2  ) {
					foundEnd = true;
					break;
				}
			}

		}
	}

	/**
	 * Update the cells down the map from the robot
	 * 
	 * @param distance
	 *            The distance that has been recorded by the robot
	 */
	public void updateSouth(float distance) {

		boolean foundEnd = false;
		int cellNo = 1;
		while (!foundEnd) {
			if (distance <= BOX_LENGTH * (cellNo)) {
				// record the obstacle obstacle.
				this.mapSpaceBeenScanned[getCurrentPositionX()][getCurrentPositionY() - cellNo] += 1;
				this.mapSpaceOccupied[getCurrentPositionX()][getCurrentPositionY() - cellNo] += 1;
				// update the cells been scanned and occupied including the current cell. except
				// the obstacle
				for (int j = getCurrentPositionY(); j >= getCurrentPositionY() - cellNo + 1; j--) {
					this.mapSpaceBeenScanned[getCurrentPositionX()][j] += 1;
					this.mapSpaceOccupied[getCurrentPositionX()][j] -= 1;
				}
				foundEnd = true;
			} else {
				cellNo++;
				if(cellNo + this.getCurrentPositionY() > Map.NUMBER_OF_ROWS - 2|| getCurrentPositionY() - cellNo <0) {
					foundEnd = true;
					break;
				}
			}

		}
	}

	/**
	 * Update the cells left of the robot on the map
	 * 
	 * @param distance
	 *            The distance that the robot has recorded
	 */
	public void updateWest(double distance) {
		boolean foundEnd = false;
		int cellNo = 1;
		while (!foundEnd) {
			if (distance <= BOX_WIDTH * (cellNo)) {
				try {
				this.mapSpaceBeenScanned[getCurrentPositionX() - cellNo][getCurrentPositionY()] += 1;
				this.mapSpaceOccupied[getCurrentPositionX() - cellNo][getCurrentPositionY()] += 1;
				// update the cells been scanned and occupied. except the obstacle
				for (int i = getCurrentPositionX(); i >= getCurrentPositionX() - cellNo + 1; i--) {
					this.mapSpaceBeenScanned[i][getCurrentPositionY()] += 1;
					this.mapSpaceOccupied[i][getCurrentPositionY()] -= 1;
				}
				foundEnd = true;
				}catch(Exception e) {
					System.out.println("fuck" + (getCurrentPositionX() - cellNo));
					System.out.println("shit" + cellNo);
					Button.waitForAnyPress();
				}
			} else {
				cellNo++;
				if((cellNo + this.getCurrentPositionX() > Map.NUMBER_OF_COLUMNS -2) || getCurrentPositionX() - cellNo < 0) {
					foundEnd = true;
					break;
				}
			}

		}
	}

	/**
	 * Update the cells right of the robot on the map
	 * 
	 * @param distance
	 *            The distance the robot has recorded
	 */
	public void updateEast(double distance) {
		boolean foundEnd = false;
		int cellNo = 1;
		while (!foundEnd) {
			if (distance <= BOX_WIDTH * (cellNo)) {
				try {
					this.mapSpaceBeenScanned[getCurrentPositionX() + cellNo][getCurrentPositionY()] += 1;
					this.mapSpaceOccupied[getCurrentPositionX() + cellNo][getCurrentPositionY()] += 1;
				}catch(ArrayIndexOutOfBoundsException aioobe) {
					System.out.println("Fucked " + cellNo);
					Button.waitForAnyPress();
					
				}
				// update the cells been scanned and occupied. except the obstacle
				for (int i = getCurrentPositionX(); i <= getCurrentPositionX() + cellNo - 1; i++) {
					this.mapSpaceBeenScanned[i][getCurrentPositionY()] += 1;
					this.mapSpaceOccupied[i][getCurrentPositionY()] -= 1;
				}
				foundEnd = true;
			} else {
				cellNo++;
				if(cellNo + this.getCurrentPositionX() > Map.NUMBER_OF_COLUMNS - 2) {
					foundEnd = true;
					break;
				}
			}

		}
	}

}