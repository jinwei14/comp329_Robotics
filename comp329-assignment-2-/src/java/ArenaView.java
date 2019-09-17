import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import jason.environment.grid.GridWorldView;

public class ArenaView extends GridWorldView {

	public ArenaView(ArenaModel model) {
		super(model, "Assignment world", 800);
		defaultFont = new Font("Arial", Font.BOLD, 18); // change default font
		setVisible(true);
		repaint();
	}

	/** draw application objects */
	@Override
	public void draw(Graphics g, int x, int y, int object) {

		switch (object) {
		case Const.ObstacleCode:
			drawObstacle(g, x, y);
			break;
		}
	}
	@Override
    public void drawAgent(Graphics g, int x, int y, Color c, int id) {
        String label = "S"+(id+1);
        c = Color.blue;
       
        if (id == 0) {
            c = Color.yellow;
        }
        super.drawAgent(g, x, y, c, -1);
       // g.fillRect(x * cellSizeW , y * cellSizeH , cellSizeW , cellSizeH ); 
        g.setColor(Color.MAGENTA);
        g.fillArc(x * cellSizeW , y * cellSizeH , cellSizeW , cellSizeH, 350, 20);
        g.setColor(Color.BLUE);
       
        if (id == 0) {
            g.setColor(Color.BLUE);
        } else {
            g.setColor(Color.white);                
        }
        super.drawString(g, x, y, defaultFont, label);
    }

	public void drawObstacle(Graphics g, int x, int y) {
		super.drawObstacle(g, x, y);
		// set up the color of the obstacle
		if (x == 0 || x == Const.GrideWidth-1 || y == 0 || y == Const.GrideHeight -1) {
			g.setColor(Color.white);
			drawString(g, x, y, defaultFont, "Wall");
		} else {
			g.setColor(Color.white);
			drawString(g, x, y, defaultFont, "Obstacle");
			
		}
	}
}
