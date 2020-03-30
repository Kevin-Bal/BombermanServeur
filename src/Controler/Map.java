package Controler;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

import java.util.ArrayList;

import Agent.ColorAgent;
import Agent.Agent;
import Agent.AgentAction;



/** 
 * Classe qui permet de charger une carte de Bomberman à partir d'un fichier de layout d'extension .lay
 * 
 * @author Kevin Balavoine, Victor Lelu--Ribaimont 
 * 
 */

public class Map implements Serializable {

	private static final long serialVersionUID = 1L;
	

	private String filename;
	private int size_x;
	private int size_y;
	
	private boolean walls[][];
	private boolean start_brokable_walls[][];

	private ArrayList<Agent> start_agents;
	

	public Map(String filename) throws Exception{
		
		this.filename = filename;
		
		try{
		
		InputStream flux =new FileInputStream(filename); 
		InputStreamReader lecture =new InputStreamReader(flux);
		BufferedReader tampon =new BufferedReader(lecture);
		
		String ligne;

		int nbX=0;
		int nbY=0;

		while ((ligne = tampon.readLine())!=null)
		{
			ligne = ligne.trim();
			if (nbX==0) {nbX = ligne.length();}
			else if (nbX != ligne.length()) throw new Exception("Toutes les lignes doivent avoir la même longueur");
			nbY++;
		}			
		tampon.close(); 
			
		size_x = nbX;
		size_y = nbY;
		
		walls = new boolean [size_x][size_y];
		start_brokable_walls  = new boolean [size_x][size_y];
			
		flux = new FileInputStream(filename); 
		lecture = new InputStreamReader(flux);
		tampon = new BufferedReader(lecture);
		int y=0;
	
		ColorAgent[] color = ColorAgent.values();
		int cpt_col = 0;
		
		start_agents = new ArrayList<Agent>();
		
		while ((ligne=tampon.readLine())!=null)
		{
			ligne=ligne.trim();

			for(int x=0;x<ligne.length();x++)
			{
				
				if (ligne.charAt(x)=='%') 
					walls[x][y]=true; 
					
				else walls[x][y]=false;
				
				if (ligne.charAt(x)=='$') 
					start_brokable_walls[x][y]=true; 
				else start_brokable_walls[x][y]=false;
				
				if (ligne.charAt(x)=='E' || ligne.charAt(x)=='V' || ligne.charAt(x)=='R') {
					start_agents.add(new Agent(x,y,AgentAction.STOP,ligne.charAt(x),ColorAgent.DEFAULT,false,false, null));
				}
				
				if (ligne.charAt(x)=='B') {
					ColorAgent col;
					if (cpt_col < color.length) col = color[cpt_col];
					else col = ColorAgent.DEFAULT;	
					start_agents.add(new Agent(x,y,AgentAction.STOP, ligne.charAt(x),col,false,false, null));
					cpt_col++;
				}
					
			}
			y++;
		}			
		tampon.close(); 
		
		//On verifie que le labyrinthe est clos			
		for(int x=0;x<size_x;x++) if (!walls[x][0]) throw new Exception("Mauvais format du fichier: la carte doit etre close");
		for(int x=0;x<size_x;x++) if (!walls[x][size_y-1]) throw new Exception("Mauvais format du fichier: la carte doit etre close");
		for(y=0;y<size_y;y++) if (!walls[0][y]) throw new Exception("Mauvais format du fichier: la carte doit etre close");
		for(y=0;y<size_y;y++) if (!walls[size_x-1][y]) throw new Exception("Mauvais format du fichier: la carte doit etre close");
		
		}catch (Exception e){
			System.out.println("Erreur : "+e.getMessage());
		}

		
	}
	

	
	public int getSizeX() {return(size_x);}
	public int getSizeY() {return(size_y);}
	


	
	public String getFilename(){
		return filename;
	}

	public boolean[][] getStart_brokable_walls() {
		return start_brokable_walls;
	}

	public boolean[][] get_walls() {
		return walls;
	}
	
	
	public ArrayList<Agent> getStart_agents() {
		return start_agents;
	}



	
}