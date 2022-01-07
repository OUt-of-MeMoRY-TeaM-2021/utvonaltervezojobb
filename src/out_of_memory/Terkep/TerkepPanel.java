
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
import javax.swing.JPanel;
import javax.swing.Timer;

public class TerkepPanel extends JPanel implements ActionListener {
    
    static final int SCREEN_WIDTH = 1000;
    static final int SCREEN_HEIGHT = 1000;
    static int UNIT_SIZE = 25;
    static int MAP_UNITS = (SCREEN_WIDTH / UNIT_SIZE) - 1;
    static final int DELAY = 75;
    static final int MAP_SIZE = 100;
    Color[][] X_Y = new Color[MAP_SIZE][MAP_SIZE];
    boolean running = false;
    Color c = Color.BLACK;
    char charSzin;
    int MOUSE_X = 0, MOUSE_Y = 0;
    int forX = 0, forY = 0;
    int carPosX, carPosY;
    boolean gridToggle = false;
    boolean followToggle = false;
    boolean editorToggle = true;
    char direction = 'U';
    Timer timer;
    
    
    TerkepPanel(){
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addMouseListener(new MyMouseAdapter());
        this.addKeyListener(new MyKeyAdapter());
        //startMap();
    }
    /*
    public void startMap(){
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }*/
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g) {
        
        //KEZDŐ GRID
        for(int i = 0; i<MAP_UNITS;i++){
            g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
            g.drawLine(0, i*UNIT_SIZE, SCREEN_HEIGHT, i*UNIT_SIZE);
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
        
        
        
        //Egérrel történő színezés
        if (editorToggle) {
            g.setColor(Color.RED);
            switch (direction) {
                case 'L': 
                    g.fillRect(MOUSE_X*UNIT_SIZE,MOUSE_Y*UNIT_SIZE, UNIT_SIZE, UNIT_SIZE-UNIT_SIZE/2);
                    break;
                case 'R':
                    g.fillRect(MOUSE_X*UNIT_SIZE,MOUSE_Y*UNIT_SIZE+UNIT_SIZE/2, UNIT_SIZE, UNIT_SIZE-UNIT_SIZE/2);
                    break;
                case 'U': 
                    g.fillRect(MOUSE_X*UNIT_SIZE+UNIT_SIZE/2,MOUSE_Y*UNIT_SIZE, UNIT_SIZE-UNIT_SIZE/2, UNIT_SIZE);
                    break;
                case 'D': 
                    g.fillRect(MOUSE_X*UNIT_SIZE,MOUSE_Y*UNIT_SIZE, UNIT_SIZE-UNIT_SIZE/2, UNIT_SIZE);
                    break;
            }
        }
        else g.setColor(c);
    }
      
    
    @Override
    public void actionPerformed(ActionEvent e) {
        /*if (running){
            
        }
        repaint(); <<<<<<<<----------------------------Folyamatos_Szimulációért----------------------*/
    }
    
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
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
                    if(forY != 0) forY--;
                    repaint();
                    break;
                    
                case KeyEvent.VK_RIGHT:
                    if(forX != MAP_SIZE-MAP_UNITS) forX++;
                    repaint();
                    break;
                
                case KeyEvent.VK_LEFT:
                    if(forX != 0) forX--;
                    repaint();
                    break;
                
                case KeyEvent.VK_DOWN:
                    if(forY != MAP_SIZE-MAP_UNITS) forY++;
                    repaint();
                    break;
                // MENTÉS - BETÖLTÉS
                case KeyEvent.VK_S:
                    FileWriter();
                    System.out.println("Saved!");
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
                    if (X_Y[carPosX+1][carPosY] == Color.DARK_GRAY) {
                        if(forX != MAP_SIZE-MAP_UNITS && followToggle) { forX++; carPosX++; }
                        else { MOUSE_X++; carPosX++; }
                        direction = 'R';
                        repaint();
                    }
                    break;
                case KeyEvent.VK_NUMPAD4:
                    if (X_Y[carPosX-1][carPosY] == Color.DARK_GRAY) {
                        if(forX != 0 && followToggle) { forX--; carPosX--; }
                        else { MOUSE_X--; carPosX--; }     
                        direction = 'L';
                        repaint();
                    }
                    break;
                case KeyEvent.VK_NUMPAD5:
                    if (X_Y[carPosX][carPosY+1] == Color.DARK_GRAY) {
                        if(forY != MAP_SIZE-MAP_UNITS && followToggle) { forY++; carPosY++; }
                        else { MOUSE_Y++; carPosY++; }  
                        direction = 'D';
                        repaint();
                    }
                    break;
                case KeyEvent.VK_NUMPAD8:
                    if (X_Y[carPosX][carPosY-1] == Color.DARK_GRAY) {
                        if(forY != 0 && followToggle) { forY--; carPosY--; }
                        else { MOUSE_Y--; carPosY--; }
                        direction = 'U';
                        repaint();
                    }
                    break;
            }
        }
    }
    public class MyMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
        MOUSE_X = e.getX() / UNIT_SIZE;
        MOUSE_Y = e.getY() / UNIT_SIZE; 
        //System.out.println(e.getX()/25 + "," + e.getY()/25);
        if (!editorToggle) X_Y[MOUSE_X+forX][MOUSE_Y+forY] = c;
        carPosX = MOUSE_X;
        carPosY = MOUSE_Y;
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
                    //System.out.print((char)data);
                    
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
