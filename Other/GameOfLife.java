//package uk.ac.cam.jpj36.oop.tick5;
import java.util.*;
import java.io.*;
import java.net.*;

public class GameOfLife {
	private World world; //Current world being shown
	private ArrayList<World> cachedWorlds = new ArrayList<>();
	PatternStore store;
   
	public GameOfLife(PatternStore w) {
	    store = w;
	}

	public void print() {
	    System.out.println("- " + world.getGenerationCount() );
	    for (int row = 0; row < world.getHeight() ; row++) { 
		    for (int col = 0; col < world.getWidth() ; col++) {
		        System.out.print( world.getCell(col, row) ? "#" : "_");
		    }
	      	System.out.println();
	    }
	}

	public void play() throws IOException {   
		String response="";
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		    
		System.out.println("Please select a pattern to play (l to list):");
		while (!response.equals("q")) {
		    response = in.readLine();
		    System.out.println(response);

		    if (response.equals("f")) {
		    	//Moves forward 1 generation
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
                	print();
			    }
		    }

		    else if (response.equals("l")) {
		    	//LISTS all the patterns
		      	List<Pattern> names = store.getPatternsNameSorted();
		      	int i = 0;
		      	for (Pattern p : names) {
		        	System.out.println(i+" "+p.getName()+"  ("+p.getAuthor()+")");
		       	 	i++;
		      	}
		    }
		    else if (response.startsWith("p")) {
		    	//PLAYS one of the patterns
		        List<Pattern> names = store.getPatternsNameSorted();
		        int patternNumber = Integer.parseInt( response.split(" ")[1] );
		        Pattern toUse = names.get(patternNumber);
		        if (toUse.getWidth() * toUse.getHeight() < 64){
		        	try {
		        	    world = new PackedWorld(toUse);
		        		cachedWorlds.add(0, copyWorld(true) );
		        		world = cachedWorlds.get(0);
		        	} catch (Exception e){
		        		System.out.println(e.getMessage());
		        	}
		        } else {
		      		try {
		      		    world = new ArrayWorld(toUse);
		        		cachedWorlds.add(0, copyWorld(true) );
		        		world = cachedWorlds.get(0);
		        	} catch (Exception e){
		        		System.out.println(e.getMessage());
		        	}
		        }
		        print();
		    }
		    
		    else if (response.startsWith("b")){
		    	int toChoose = Math.max( world.getGenerationCount()-1 , 0);
		    	world = cachedWorlds.get( toChoose );
		    	print();
		    }
		}
	}

	public static void main(String args[]) throws IOException {
		if (args.length!=1) {
		    System.out.println("Usage: java GameOfLife <path/url to store>");
		    return;
		}
		  
		try {
		    PatternStore ps = new PatternStore(args[0]);
		    GameOfLife gol = new GameOfLife(ps);    
		    gol.play();
		}
		catch (IOException ioe) {
		    System.out.println("Failed to load pattern store");
		}
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