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
        this.type = null;
        this.neighbors = new ArrayList<>();
        this.previous = null;
    }
    public void setX_Y(int _x, int _y) {
        this.x = _x;
        this.y = _y;
    }
    public int getX () {
        return this.x;
    }
    public int getY () {
        return this.y;
    }
    public void setGrid (Color[][] map) {
        //System.out.println("teszt");
        try {
            FileReader reader = new FileReader("Map.txt");
            int data;
            for (int y = 0; y<MAP_SIZE;y++) {
                for (int x = 0; x<MAP_SIZE;x++) {
                    data = reader.read();
                    PathFinder t = new PathFinder(x,y);
                    grid[x][y] = t;
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
        
        if (x < MAP_SIZE-1) if (grid[x + 1][y].type == Color.DARK_GRAY) this.neighbors.add(grid[x + 1][y]);
        if (x > 0) if (grid[x - 1][y].type == Color.DARK_GRAY) this.neighbors.add(grid[x - 1][y]);
        if (y < MAP_SIZE-1) if (grid[x][y + 1].type == Color.DARK_GRAY) this.neighbors.add(grid[x][y + 1]);
        if (y > 0) if (grid[x][y - 1].type == Color.DARK_GRAY) this.neighbors.add(grid[x][y - 1]);
    }
    
    public ArrayList<PathFinder> Finder(PathFinder start, PathFinder end) {
        this.createMyList(start);
        this.addNeighbors(grid);
        PathFinder current = start;
        do 
        {
            if (openSet.size() > 0) {
            
                int winner = 0;
                for (int i = 0; i < openSet.size();i++) {
                    if (openSet.get(i).f < openSet.get(winner).f) winner = i;
                }
                current = openSet.get(winner);
                
                if (current.x == end.getX() && current.y == end.getY()) { // Ha megérkeztünk uticélunkhoz akkor..
                    ArrayList<PathFinder> path = new ArrayList<>();
                    PathFinder temp = current;
                    path.add(temp);

                    while (temp.previous != null) {
                        path.add(temp.previous);
                        temp = temp.previous;
                    }
                    System.out.println(path.size());
                    System.out.println("Done!");
                    
                    return path;
                    
                    
                }
                // Ha nem értünk célba akkor vizsgáltá teszi az adott mezőt.
                openSet.remove(current);
                closedSet.add(current);

                ArrayList<PathFinder> szomszedok = current.neighbors;
                int tempG = 0;
                for (int i = 0; i < szomszedok.size();i++) {
                    PathFinder szomszed = szomszedok.get(i);
                    if (!closedSet.contains(szomszed)) { // Ha a szomszédos terület még nem bejárt akkor..
                       tempG = current.g + 1;
                       szomszed.previous = current;
                        if (tempG < szomszed.g) {
                            szomszed.g = tempG;
                        }
                        else { // Következő lehetőség(ek)
                        szomszed.g = tempG;
                        openSet.add(szomszed);
                        } 
                    }

                    szomszed.h = heuristic(szomszed, end);
                    szomszed.f = szomszed.g + szomszed.h;
                    
                    
                    //System.out.println("f: "+szomszed.f+"g: "+szomszed.g+"h: "+szomszed.h);
                }
            
            } else {


                //no solution
            }
        }
        while (openSet.size() > 0);
        
        //Az útvonal kirajzolása
        
        
        return openSet;
        
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
