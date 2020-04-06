package bean;

import java.io.Serializable;
import java.util.ArrayList;

import Item.InfoBomb;
import Item.InfoItem;

public class ServerObject implements Serializable{
    /**
	 * 
	 */
	private boolean[][] breakable_walls ;
	//private ArrayList<Agent> listInfoAgents;
    private ArrayList<InfoItem> listInfoItems;
    private ArrayList<String> listInfoBombs;
	
	
	private static final long serialVersionUID = 1L;
	public ServerObject() {
		super();
		listInfoItems = new ArrayList<InfoItem>();
		listInfoBombs = new ArrayList<String>();
	}


    public boolean[][] getBreakable_walls() {
        return breakable_walls;
    }

    public void setBreakable_walls(boolean[][] breakable_walls) {
        this.breakable_walls = breakable_walls;
    }
    public ArrayList<InfoItem> getListInfoItems() {
        return listInfoItems;
    }

    public void setListInfoItems(ArrayList<InfoItem> listInfoItems) {
    	for(InfoItem s : listInfoItems) {
    		this.listInfoItems.add(s);
    	}
    }
    public ArrayList<String> getListInfoBombs() {
        return listInfoBombs;
    }

    public void setListInfoBombs(ArrayList<String> listInfoBombs) {
    	for(String s : listInfoBombs) {
    		this.listInfoBombs.add(s);
    	}
    }
    
/*
    public ArrayList<Agent> getListInfoAgents() {
        return listInfoAgents;
    }

    public void setListInfoAgents(ArrayList<Agent> listInfoAgents) {
        this.listInfoAgents = listInfoAgents;
    }
*/
    public void setInfoGame(boolean[][] breakable_walls, ArrayList<InfoItem> listInfoItems, ArrayList<String> listInfoBombs){
        setBreakable_walls(breakable_walls);
        //setListInfoAgents(listInfoAgents);
        setListInfoItems(listInfoItems);
        setListInfoBombs(listInfoBombs);
    }
}
