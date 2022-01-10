package out_of_memory.Terkep;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import static out_of_memory.Terkep.TerkepPanel.MAP_SIZE;

public class PathFinder {
    private int x;
    private int y;
    private double f;
    private int g;
    private double h;
    protected Color type;
    private PathFinder [][] grid = new PathFinder[MAP_SIZE][MAP_SIZE];
    
    private ArrayList<PathFinder> neighbors;
    private PathFinder previous;
    private ArrayList<PathFinder> openSet = new ArrayList<>();
    private ArrayList<PathFinder> closedSet = new ArrayList<>();
    
    
    
    PathFinder (int x, int y) { //Spot
        this.x = x;
        this.y = y;
        this.f = 0;
        this.g = 0;
        this.h = 0;
        this.type = Color.DARK_GRAY;
        this.neighbors = new ArrayList<>();
    }
    public int getX () {
        return this.x;
    }
    public int getY () {
        return this.y;
    }
    public void setGrid (Color[][] map) {
        try {
            FileReader reader = new FileReader("Map.txt");
            int data;
            for (int y = 0; y<MAP_SIZE;y++) {
                for (int x = 0; x<MAP_SIZE;x++) {
                    data = reader.read();
                    grid[x][y].type = ConvertCharToColor((char)data);
                }
            }
            reader.close();
            for (int y = 0; y<MAP_SIZE;y++) {
                for (int x = 0; x<MAP_SIZE;x++) {
                    grid[x][y].addNeighbors(grid);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void createMyList(PathFinder start) {
        openSet.add(start);
    }
    private double dist (
        double x1, 
        double y1, 
        double x2, 
        double y2) {       
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }
    private double heuristic(PathFinder a, PathFinder b) {
        return dist(a.x,a.y,b.x,b.y);
    }
    
    private void addNeighbors(PathFinder[][] grid) {
        int x = this.x;
        int y = this.y;
        
        if (grid[x][y].type == Color.DARK_GRAY && x < MAP_SIZE-1) this.neighbors.add(grid[x + 1][y]);
        if (grid[x][y].type == Color.DARK_GRAY && x > 0) this.neighbors.add(grid[x - 1][y]);
        if (grid[x][y].type == Color.DARK_GRAY && y < MAP_SIZE-1) this.neighbors.add(grid[x][y + 1]);
        if (grid[x][y].type == Color.DARK_GRAY && y > 0) this.neighbors.add(grid[x][y - 1]);
    }
    
    public ArrayList<PathFinder> Finder(PathFinder start, PathFinder end) {
        this.createMyList(start);
        PathFinder current = start;
        
        if (openSet.size() > 0) {
            
            int winner = 0;
            for (int i = 0; i < openSet.size();i++) {
                if (openSet.get(i).f < openSet.get(winner).f) winner = i;
            }
            current = openSet.get(winner);
            
            
            if (current == end) { // Ha megérkeztünk uticélunkhoz akkor..
                
                System.out.println("Done!");
            }
            // Ha nem értünk célba akkor vizsgáltá teszi az adott mezőt.
            openSet.remove(current);
            closedSet.add(current);
            
            //Lehet itt kellene megkeresni a szomszédos mezőket.
           
            ArrayList<PathFinder> szomszedok = current.neighbors;
            int tempG = 0;
            for (int i = 0; i < szomszedok.size();i++) {         
               PathFinder szomszed = szomszedok.get(i);
               if (!closedSet.contains(szomszed)) { // Ha a szomszédos terület még nem bejárt akkor..
                   tempG = current.g + 1;
                   
                   if (tempG < szomszed.g) { // Ha a már bejárt mezőt meg tudjuk közelíteni rövidebb úton, akkor..
                       szomszed.g = tempG;
                   } 
                } else { // Ha még nem jártuk be akkor most bejárjuk
                   szomszed.g = tempG;
                   openSet.add(szomszed);
                }
               
                szomszed.h = heuristic(szomszed, end);
                szomszed.f = szomszed.g + szomszed.h;
            }
            
        } else {
            
            
            //no solution
        }
        //Az útvonal kirajzolása
        ArrayList<PathFinder> path = new ArrayList<>();
                PathFinder temp = current;
                path.add(temp);
                while (previous != null) {
                    path.add(previous);
                    temp = previous;
                }
        
        return path;
        
    }
    
    public Color ConvertCharToColor(char ch){
        
        switch (ch)
        {
            case '0':
                return Color.DARK_GRAY;
            case 'b':
                return Color.BLUE;
            case 'g':
                return Color.GREEN;
            case 'r':
                return Color.RED;
        }
        
        return Color.BLUE;
    }
}
