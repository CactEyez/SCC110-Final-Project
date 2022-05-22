import java.awt.*; //only necessary imports which allow for GUIS, GUI events, GUI objects and random numbers
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
public class mainProgram extends JFrame
{
    String[] colNames = {"Act Name", "Prefered Start Time", "Actual Start Time", "Priority"};
    Object[][] orderedData = new Object[20][4];
    Object[][] rawData = new Object[20][4];
    
    JTable table = new JTable(orderedData, colNames);
    public mainProgram()
    {

    }

    Object[][] orderData(Object[][] normalData)
    {
        return normalData;
    }

    int indexOfChange(Object[][] orderedData)
    {
        return 0;
    }

    boolean validateEvent(Object[] newEvent)
    {
        return true;
    }

    Object[][] gapData(Object[][] orderedData)
    {
        return orderedData;
    }
}
