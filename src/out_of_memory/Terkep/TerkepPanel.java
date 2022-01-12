
package out_of_memory.Terkep;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TerkepPanel extends JPanel implements ActionListener {
    
    
    static final int SCREEN_WIDTH = 1000;
    static final int SCREEN_HEIGHT = 1000;
    static int UNIT_SIZE = 25;
    static int MAP_UNITS = (SCREEN_WIDTH / UNIT_SIZE) - 1;
    static final int DELAY = 150;
    static final int MAP_SIZE = 100;
    Color[][] X_Y = new Color[MAP_SIZE][MAP_SIZE];
    boolean running = false;
    Color c = Color.BLUE;
    char charSzin;
    int forX = 0, forY = 0;
    boolean gridToggle = false;
    boolean followToggle = false;
    boolean editorToggle = false;
    Timer timer;
    boolean celbaert = true;
    int selected = 0;
    int index = 25;
    
    ArrayList<Jarmu> J = new ArrayList<>();
    
    
    TerkepPanel(){
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addMouseListener(new MyMouseAdapter());
        this.addKeyListener(new MyKeyAdapter());
        FileReader();   
        startMap();
        ujJarmu(5,4);
    }
    
    public void startMap(){
        timer = new Timer(DELAY,this);
        timer.start();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void ujJarmu(int x, int y) {
        Jarmu j = new Jarmu(x, y);
        J.add(j);
        
    }
    public ArrayList<PathFinder> ujPath (Jarmu j) {
        int x = j.carPosX;
        int y = j.carPosY;
        int newX;
        int newY;
        do {
            newX = new Random().nextInt(99 - 0 + 1) + 0;
            newY = new Random().nextInt(99 - 0 + 1) + 0;
        } while (X_Y[newX][newY] != Color.DARK_GRAY);
        PathFinder end = new PathFinder(newX,newY);
        PathFinder start = new PathFinder(x,y);
        start.setGrid(X_Y);
        j.path = new ArrayList<PathFinder>();
        j.path.add(start);
        return j.path.get(0).Finder(start, end);
        
    }
    public void MoveByGPS (Jarmu j){
                int dist = j.distance;
                if (j.carPosX - j.path.get(dist).getX() == 1) j.direction = 'L';
                else if (j.carPosX - j.path.get(dist).getX() == -1) j.direction = 'R';
                else if (j.carPosY - j.path.get(dist).getY() == 1) j.direction = 'U';
                else j.direction = 'D';
                j.carPosX = j.path.get(dist).getX();
                j.carPosY = j.path.get(dist).getY();
                PathFinder spot = j.path.get(0).previous;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running){
            for (int carNumber = 0; carNumber < J.size(); carNumber++)
            {
                //Move();
                int dist = J.get(carNumber).distance;
                if (J.get(carNumber).path != null) MoveByGPS(J.get(carNumber));
                
                if (dist > 0) { J.get(carNumber).distance--; MoveByGPS(J.get(carNumber)) ;}
                else {
                    J.get(carNumber).path = ujPath(J.get(carNumber));
                    J.get(carNumber).distance = J.get(carNumber).path.size() - 1;
                }
            }
            
        }
        repaint(); 
    }
    public void draw(Graphics g) {
        
        //KEZDŐ GRID
        for(int i = 0; i<MAP_UNITS;i++){
            //g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
            //g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
        }
        
        //TÖMBBŐL KIFESTETT NÉGYZETEK
        for (int x = forX; x<MAP_SIZE-1;x++) {
            for (int y = forY; y<MAP_SIZE-1;y++) {
                g.setColor(X_Y[x][y]);
                if (c != Color.BLACK) {
                    if (gridToggle) g.fillRect((x-forX)*UNIT_SIZE, (y-forY)*UNIT_SIZE, UNIT_SIZE-1, UNIT_SIZE-1);
                    else g.fillRect((x-forX)*UNIT_SIZE, (y-forY)*UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                }
                g.setColor(Color.BLACK); //Ezzel váltja vissza feketére
            }
        }
        
        //Útvonal kirajzolása
        
            
            if (J.get(selected).path != null) {
                for (int carNumber = 0; carNumber < J.size(); carNumber++) {
                    ArrayList<PathFinder> p = J.get(carNumber).path;
                    g.setColor(Color.RED);
                    for (int i = 0; i < p.size();i++) {
                    //g.fillRect((p.get(i).getX() - forX) * UNIT_SIZE, (p.get(i).getY() - forY) * UNIT_SIZE, 10, UNIT_SIZE);
                    }
                    g.fillRect((p.get(0).getX() - forX) * UNIT_SIZE, (p.get(0).getY() - forY) * UNIT_SIZE, 10, UNIT_SIZE);
                }
                
            }
            
        
        
        
        
        //Egérrel történő színezés
        if (!editorToggle) {
            
            g.setColor(Color.RED);
            
            for (int i = 0; i < J.size(); i++) {
                int drawPosX = J.get(i).carPosX - forX;
                int drawPosY = J.get(i).carPosY - forY;
                
                switch (J.get(i).direction) {
                case 'L': 
                    g.fillRect(drawPosX*UNIT_SIZE,drawPosY*UNIT_SIZE, UNIT_SIZE, UNIT_SIZE-UNIT_SIZE/2);
                    break;
                case 'R':
                    g.fillRect(drawPosX*UNIT_SIZE,drawPosY*UNIT_SIZE+UNIT_SIZE/2, UNIT_SIZE, UNIT_SIZE-UNIT_SIZE/2);
                    break;
                case 'U': 
                    g.fillRect(drawPosX*UNIT_SIZE+UNIT_SIZE/2,drawPosY*UNIT_SIZE, UNIT_SIZE-UNIT_SIZE/2, UNIT_SIZE);
                    break;
                case 'D': 
                    g.fillRect(drawPosX*UNIT_SIZE,drawPosY*UNIT_SIZE, UNIT_SIZE-UNIT_SIZE/2, UNIT_SIZE);
                    break;
            }
            }
            
        }
        else g.setColor(c);
    }
    
    
    public boolean checkDirection(char d) {
        
        int x = J.get(selected).carPosX, y = J.get(selected).carPosY;
        
        switch (d) {
            case 'U':
                if (X_Y[x][y-1] == Color.DARK_GRAY) return true;
                break;
            case 'D':
                if (X_Y[x][y+1] == Color.DARK_GRAY) return true;
                break;
            case 'L':
                if (X_Y[x-1][y] == Color.DARK_GRAY) return true;
                break;
            case 'R':
                if (X_Y[x+1][y] == Color.DARK_GRAY) return true;
                break;
        }
        return false;
    }
    public void Move() {
        int s = selected;
        for (selected = 0; selected < J.size();selected++) {
            char direction = J.get(selected).direction;
            int x = J.get(selected).carPosX, y = J.get(selected).carPosY;

            switch (direction) {
                case 'L':
                    if (checkDirection('L')) {
                        if(forX != 0 && followToggle) { forX--; x--; }
                        else  x--;
                    }
                    else if (checkDirection('U')) direction = 'U';
                    else if (checkDirection('D')) direction = 'D';
                    else direction = 'R';
                    break;
                case 'R':
                    if (checkDirection('R')) {
                        if(forX != MAP_SIZE-MAP_UNITS && followToggle) { forX++; x++; }
                        else x++;
                    }
                    else if (checkDirection('U')) direction = 'U';
                    else if (checkDirection('D')) direction = 'D';
                    else direction = 'L';
                    break;
                case 'U':
                    if (checkDirection('U')) {
                        if(forY != 0 && followToggle) { forY--; y--; }
                        else y--;
                    }
                    else if (checkDirection('R')) direction = 'R';
                    else if (checkDirection('L')) direction = 'L';              
                    else direction = 'D';
                    break;
                case 'D':
                    if (checkDirection('D')) {
                        if(forY != MAP_SIZE-MAP_UNITS && followToggle) { forY++; y++; }
                        else y++;
                    }
                    else if (checkDirection('R')) direction = 'R';
                    else if (checkDirection('L')) direction = 'L';
                    else direction = 'U';
                    break;
            }
            J.get(selected).carPosX = x;
            J.get(selected).carPosY = y;
            J.get(selected).direction = direction;
        }
        selected = s;
        
    }
      
    
    
    
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int x = J.get(selected).carPosX;
            int y = J.get(selected).carPosY;
            
            switch(e.getKeyCode()) {
                
                // Kapcsolok
                case KeyEvent.VK_G:
                    if (!gridToggle) gridToggle = true;
                    else gridToggle = false;
                    repaint();
                    break;
                case KeyEvent.VK_SPACE:
                    if (!followToggle) followToggle = true;
                    else followToggle = false;
                    break;
                case KeyEvent.VK_R:
                    if (!running) running = true;
                    else running = false;
                    break;
                
                // ZOOM
                case KeyEvent.VK_ADD:
                    UNIT_SIZE++;
                    MAP_UNITS = (SCREEN_WIDTH / UNIT_SIZE) - 1;
                    repaint();
                    break;
                    
                case KeyEvent.VK_MINUS:
                    if (MAP_UNITS < MAP_SIZE) {
                        UNIT_SIZE--;
                        MAP_UNITS = (SCREEN_WIDTH / UNIT_SIZE) - 1;
                    }
                    repaint();
                    break;
                    
                    
                // MAP MOZGATÁS
                case KeyEvent.VK_UP:
                    if(forY != 0) {
                        forY--;
                        //for (int i = 0; i < J.size();i++) J.get(i).drawPosY++;
                    }
                    repaint();
                    break;
                    
                case KeyEvent.VK_RIGHT:
                    if(forX != MAP_SIZE-MAP_UNITS) {
                        forX++;
                        //for (int i = 0; i < J.size();i++) J.get(i).drawPosX--;
                    }
                    repaint();
                    break;
                
                case KeyEvent.VK_LEFT:
                    if(forX != 0) {
                        forX--;
                        //for (int i = 0; i < J.size();i++) J.get(i).drawPosX++;
                    }
                    repaint();
                    break;
                
                case KeyEvent.VK_DOWN:
                    if(forY != MAP_SIZE-MAP_UNITS) {
                        forY++;
                        //for (int i = 0; i < J.size();i++) J.get(i).drawPosY--;                      
                    }
                    repaint();
                    break;
                // MENTÉS - BETÖLTÉS
                case KeyEvent.VK_S:
                    if (editorToggle)
                    {
                        FileWriter();
                        System.out.println("Saved!");
                    }
                    
                    break;
                    
                case KeyEvent.VK_L:
                    FileReader();
                    repaint();
                    System.out.println("Loaded!");
                    break;
                // SZIN KIVALASZTAS
                case KeyEvent.VK_0:
                    c = Color.DARK_GRAY;
                    charSzin = '0';
                    System.out.println("Selected color: DARK_GRAY");
                    break;
                
                case KeyEvent.VK_1:
                    c = Color.BLUE;
                    charSzin = 'b';
                    System.out.println("Selected color: BLUE");
                    break;
                    
                case KeyEvent.VK_2:
                    c = Color.GREEN;
                    charSzin = 'g';
                    System.out.println("Selected color: GREEN");
                    break;
                    
                case KeyEvent.VK_3:
                    c = Color.RED;
                    charSzin = 'r';
                    System.out.println("Selected color: RED");
                    break;
                // IRÁNYITAS
                case KeyEvent.VK_NUMPAD6:
                    if (X_Y[x+1][y] == Color.DARK_GRAY) {
                        if(forX != MAP_SIZE-MAP_UNITS && followToggle && !running) { forX++; x++; }
                        else x++;
                        J.get(selected).direction = 'R';
                        J.get(selected).carPosX = x;
                        repaint();
                    }
                    break;
                case KeyEvent.VK_NUMPAD4:
                    if (X_Y[x-1][y] == Color.DARK_GRAY) {
                        if(forX != 0 && followToggle && !running) { forX--; x--; }
                        else x--;     
                        J.get(selected).direction = 'L';
                        J.get(selected).carPosX = x;
                        repaint();
                    }
                    break;
                case KeyEvent.VK_NUMPAD5:
                    if (X_Y[x][y+1] == Color.DARK_GRAY) {
                        if(forY != MAP_SIZE-MAP_UNITS && followToggle && !running) { forY++; y++; }
                        else y++;
                        J.get(selected).direction = 'D';
                        J.get(selected).carPosY = y;
                        repaint();
                    }
                    break;
                case KeyEvent.VK_NUMPAD8:
                    if (X_Y[x][y-1] == Color.DARK_GRAY) {
                        if(forY != 0 && followToggle && !running) { forY--; y--; }
                        else y--;
                        J.get(selected).direction = 'U';
                        J.get(selected).carPosY = y;
                        repaint();
                    }
                    break;   
                case KeyEvent.VK_NUMPAD7:
                    if (selected != 0) selected--;
                    break;
                case KeyEvent.VK_NUMPAD9:
                    if (selected != J.size()-1) selected++;
                    break;
            }
            
            
            repaint();
        }
    }
    public class MyMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
        int drawPosX = e.getX() / UNIT_SIZE;
        int drawPosY = e.getY() / UNIT_SIZE; 
        if (editorToggle) X_Y[drawPosX+forX][drawPosY+forY] = c;
        int carPosX = drawPosX + forX;
        int carPosY = drawPosY + forY;
        if (X_Y[carPosX][carPosY] == Color.DARK_GRAY) ujJarmu(carPosX,carPosY);
        repaint();
        }
    }
    
    public void FileWriter() {
        try {
            FileWriter writer = new FileWriter("Map.txt");
            for (int y = 0; y<MAP_SIZE-1;y++) {
                for (int x = 0; x<MAP_SIZE-1;x++) {
                    writer.write(ConvertColorToChar(X_Y[x][y]));
                }
                writer.write("\n");
            }
            
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void FileReader() {
        try {
            FileReader reader = new FileReader("Map.txt");
            int data;
            for (int y = 0; y<MAP_SIZE;y++) {
                for (int x = 0; x<MAP_SIZE;x++) {
                    data = reader.read();
                    X_Y[x][y] = ConvertCharToColor((char)data);
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    
    public char ConvertColorToChar(Color c) {
        if (c == Color.DARK_GRAY) return '0';
        else if (c == Color.BLUE) return 'b';
        else if (c == Color.GREEN) return 'g';
        else if (c == Color.RED) return 'r';
        else return 'a';
    }
      
}
