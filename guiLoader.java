import javax.swing.*;
public class guiLoader extends JFrame //this guiLoader handles all of the gui creation of the guis to keep things organised
{
    public static void mainGUI(int guess, int charac) //guess numbers and character number need to be passed so the size of the window can accomodate a smaller/larger board
    {
        mainPage gui = new mainPage();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setSize(((charac * 2) * 120),((guess + 1) * 120)); //the size of the page adjusts with the number of guesses and characters
        gui.setLocationRelativeTo(null);
        gui.setVisible(true);
        gui.setResizable(false); //if the user could resize the page it could make some buttons or labels disappear and make the page look messy
        gui.setTitle("Colourdle"); //the nickname for this game as a play on "Worlde" and "Colour"
    }
    public static void victoryGUI()
    {
        victory gui = new victory();
        gui.setSize(300, 100);
        gui.setLocationRelativeTo(null);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setTitle("Victory");
    }
    public static void gameSetupGUI()
    {
        gameSetup gui = new gameSetup();
        gui.setSize(300, 300);
        gui.setLocationRelativeTo(null);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setTitle("Colourdle Setup");
    }
}