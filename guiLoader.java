import javax.management.modelmbean.ModelMBeanInfoSupport;
import javax.swing.*;
public class guiLoader extends JFrame //this guiLoader handles all of the gui creation of the guis to keep things organised
{
    public static void mainProgramGUI() 
    {
        mainProgram gui = new mainProgram();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setSize((600),(600)); 
        gui.setLocationRelativeTo(null);
        gui.setVisible(true);
        gui.setResizable(false); 
        gui.setTitle("Festival Scheduler");
    }

    public static void progLoadupGUI()
    {
        progLoadup gui = new progLoadup();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setSize((600),(600)); 
        gui.setLocationRelativeTo(null);
        gui.setVisible(true);
        gui.setResizable(false); 
        gui.setTitle("Festival Scheduler Start");
    }
}
