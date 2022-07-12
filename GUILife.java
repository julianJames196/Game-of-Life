//package uk.ac.cam.jpj36.oop.tick5;
import javax.swing.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import javax.swing.border.*;
import javax.swing.event.*;

public class GUILife extends JFrame {
	private World world; //Current world being shown
	private ArrayList<World> cachedWorlds = new ArrayList<>();
	PatternStore store;
	GamePanel gamePanel;
	private boolean playing;
	java.util.Timer timer = new java.util.Timer();
	JButton playButton;

	public GUILife(PatternStore ps) {
	  	super("Game of Life");
	  	store=ps;
	  	setDefaultCloseOperation(EXIT_ON_CLOSE);
	  	setSize(1024,768);

	  	add(createPatternsPanel(), BorderLayout.WEST);
	  	add(createControlPanel(), BorderLayout.SOUTH);
	  	add(createGamePanel(), BorderLayout.CENTER);
	}

	private void addBorder(JComponent component, String title) {
	  	Border etch = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	  	Border tb = BorderFactory.createTitledBorder(etch,title);
	  	component.setBorder(tb);
	}
	
	private JPanel createGamePanel() {
  		gamePanel = new GamePanel();
  		addBorder(gamePanel,"Game Panel");
  		/*try {
  			PatternStore tempV = new PatternStore("https://www.cl.cam.ac.uk/teaching/1819/OOProg/ticks/life.txt");
  			World tempWorld = new ArrayWorld(tempV.getPatternsNameSorted().get(5));
  			gamePanel.display( tempWorld );
  		} catch (Exception e){
  			System.out.println(e.getMessage());
  		}*/
  		return gamePanel;
	}

	private JPanel createPatternsPanel() {
	  	JPanel patt = new JPanel();
	  	addBorder(patt,"Patterns");
	  	JList patternsJList = new JList( store.getPatternsNameSorted().toArray() );
	  	patternsJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	patternsJList.addListSelectionListener(new ListSelectionListener() {
    		public void valueChanged(ListSelectionEvent e) {
			    JList<Pattern> list = (JList<Pattern>) e.getSource();
			    Pattern p = list.getSelectedValue();
			    cachedWorlds.clear();
			    try {
			    	if ((p.getWidth()*p.getHeight()) < 64) {
				    	world = new PackedWorld(p);
					    cachedWorlds.add(0, copyWorld(true) );
					    world = cachedWorlds.get(0);
				    } else {
				    	world = new ArrayWorld(p);
					    cachedWorlds.add(0, copyWorld(true) );
					    world = cachedWorlds.get(0);
				    }
				    gamePanel.display(world);
			    } catch (Exception exc){System.out.println(exc.getMessage());}
			    timer.cancel();
				playing=false;
				playButton.setText("Play");
		  	} });
    	JScrollPane scrollPane = new JScrollPane(patternsJList);
	  	scrollPane.setPreferredSize( new Dimension(this.getWidth()/4, this.getHeight()-150) );
    	patt.add(scrollPane);
	  	return patt;
	}

	private JPanel createControlPanel() {
	  	JPanel ctrl =  new JPanel();
	  	addBorder(ctrl,"Controls");
	  	ctrl.setLayout(new GridLayout(1,3,0,0));

	  	JButton backBut = new JButton("< Back");
	  	backBut.addActionListener(e -> moveBack());

	  	JButton forBut = new JButton("Forward >");
	  	forBut.addActionListener(e -> moveForward());

	  	playButton = new JButton("Play");
	  	playButton.addActionListener(e -> runOrPause());
	  	
	  	ctrl.add(backBut);
	  	ctrl.add(playButton);
	  	ctrl.add(forBut);
	  	return ctrl;
	}
	
    public static void main(String[] args) {
    	try {
    		PatternStore temp = new PatternStore("https://www.cl.cam.ac.uk/teaching/1819/OOProg/ticks/life.txt");
    		GUILife gui = new GUILife(temp);
        	gui.setVisible(true);
    	} catch(Exception e){
    		System.out.println(e.getMessage());
    	}
    }

    private void moveForward(){
    	/*timer.cancel();
		playing=false;
		playButton.setText("Play");*/
	    if (world == null) {
	        System.out.println("Please select a pattern to play (l to list):");
	    } else {
	   		int nextWorldCount = world.getGenerationCount()+1;
            if (nextWorldCount == cachedWorlds.size()) {
                World newWorld = copyWorld(true);
                newWorld.nextGeneration();
                cachedWorlds.add(newWorld);
            }
        	world = cachedWorlds.get(nextWorldCount);
        	gamePanel.display(world);
	    }
    }

    private void runOrPause() {
		if (playing) {
		    timer.cancel();
		    playing=false;
		    playButton.setText("Play");
		}
		else {
		    playing=true;
		    playButton.setText("Stop");
		    timer = new java.util.Timer(true);
		    timer.scheduleAtFixedRate(new TimerTask() {
		      @Override
		      public void run() {
		        moveForward();
		      }
		    }, 0, 500);
		}
	}

    private void moveBack(){
    	timer.cancel();
		playing=false;
		playButton.setText("Play");
    	int toChoose = Math.max( world.getGenerationCount()-1 , 0);
		world = cachedWorlds.get( toChoose );
		gamePanel.display(world);
    }

    private World copyWorld(boolean useCloning) {
		if (!useCloning){
			if (world instanceof PackedWorld){
				return new PackedWorld( (PackedWorld)world );
			} else {
				return new ArrayWorld((ArrayWorld)world);
			}	
		} else {
			try {
				return (World)world.clone();
			} catch(CloneNotSupportedException c){
				System.out.println(c.getMessage());
				return world;
			}
		}
	}
}