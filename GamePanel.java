//package uk.ac.cam.jpj36.oop.tick5;
import java.awt.Color;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
	private World world = null;

	@Override
	protected void paintComponent(java.awt.Graphics g) {
	    // Paint the background white
	    g.setColor(java.awt.Color.WHITE);
	    g.fillRect(0, 0, this.getWidth(), this.getHeight());

	    if (world != null){
		    /*g.setColor(Color.BLACK);
		    g.drawRect(200, 200, 30, 30);
		    g.setColor(Color.LIGHT_GRAY);
		    g.fillRect(140, 140, 30, 30);
		    g.fillRect(260, 140, 30, 30);
		    g.setColor(Color.BLACK);
		    g.drawLine(150, 300, 280, 300);
		    g.drawString("@@@", 135,120);
		    g.drawString("@@@", 255,120);*/

		    //Draw cells
		    int squareWidth = (int) Math.floor( this.getWidth() / world.getWidth() );
		    int squareHeight = (int) Math.floor( (this.getHeight() - 60 ) / world.getHeight() );
		    int squareSize = (squareWidth <= squareHeight) ? squareWidth : squareHeight;
		    
		    for (int i = 0; i < world.getWidth(); i++){
		    	for (int j = 0; j < world.getHeight(); j++){
		    		if (world.getCell(i, j)){
		    			g.setColor(Color.BLACK);
		    		} else {
		    			g.setColor(Color.WHITE);
		    		}

		    		g.drawRect(i*squareSize, j*squareSize, squareSize, squareSize);
		    		g.fillRect(i*squareSize, j*squareSize, squareSize, squareSize);
		    	}
		    } 
		    //Draw Grid lines
		    g.setColor(Color.LIGHT_GRAY);
		    for (int i = 0; i < world.getWidth()+1; i++){
		    	g.drawLine(i*squareSize, 0, i*squareSize, squareSize*world.getHeight() ); //Vertical
		    }
		    for (int j = 0; j < world.getHeight()+1; j++){
		    	g.drawLine(0, j*squareSize, squareSize*world.getWidth()   , j*squareSize); //Horizontal
		    }

		    //Draw Generation count
		    g.setColor(Color.BLACK);
		    g.drawString("Generation: " + world.getGenerationCount() , 30, this.getHeight() - 30);	
	    }
	}

	public void display(World w) {
	    world = w;
	    repaint();
	}
}