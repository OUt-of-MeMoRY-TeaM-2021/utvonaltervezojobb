
package out_of_memory.Terkep;


import javax.swing.JFrame;

public class TerkepFrame extends JFrame {
    
   public TerkepFrame(){
        this.add(new TerkepPanel());
        this.setTitle("Terkep");
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
    

    
    

    

    
}
