import java.util.ArrayList;
import java.util.Collections;

/**
 * Cell class is responsible for representing one unit of the main map.
 */
public class Cell {
	private ArrayList<Double> distanceAround;
	/**
	 * Cell position in the grid
	 */
	private int x;

	private int y;

	/**
	 * ----- Constructor ------
	 * 
	 * @param x
	 * @param y
	 */
	public Cell(int x, int y) {
		this.distanceAround = new ArrayList<Double>();
		this.x = x;
		this.y = y;

	}

	/**
	 * getter setter for the x y position
	 */
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}


	/**
	 * getter setter for the DistanceAround array.
	 * @return
	 */
	public ArrayList<Double> getDistanceAround() {
		return distanceAround;
	}

	public void setDistanceAround(double dis1, double dis2, double dis3, double dis4) {
		this.distanceAround.add(dis1);
		this.distanceAround.add(dis2);
		this.distanceAround.add(dis3);
		this.distanceAround.add(dis4);
	}

	
	/**
	 * @param cellOne 
	 * @param cellTwo
	 * @return
	 */
	public boolean equalCell(Cell cell) {
		boolean flag = false;
		ArrayList<Double> first = this.getDistanceAround();
		ArrayList<Double> second = cell.getDistanceAround();
		Collections.sort(first);
		Collections.sort(second);
		if (first.equals(second)) {
			flag = true;
		} else {
			flag = false;
		}
		return flag;
	}


}