import java.awt.*; //only necessary imports which allow for GUIS, GUI events, GUI objects and random numbers
import java.awt.event.*;
import java.time.temporal.TemporalAdjuster;

import javax.swing.*;
import java.util.*;
public class mainProgram extends JFrame
{
    String[] colNames = {"Act Name", "Prefered Start Time", "Actual Start Time", "Priority"};
    String[][] orderedData = new String[20][4];
    String[][] rawData = new String[20][4];

    JTable table = new JTable(orderedData, colNames);

    JLabel actNameLabel = new JLabel("Act Name");
    JLabel prefStartLabel = new JLabel("Start Time");
    JLabel priorityLabel = new JLabel("Priority");
    
    JTextArea actNameText = new JTextArea();
    JTextArea prefStartText = new JTextArea();
    JTextArea priorityText = new JTextArea();

    JButton inputDataButto = new JButton();

    String userName, userTime, userPrio;

    public mainProgram()
    {
        setLayout(new FlowLayout());

        add(actNameLabel);
        add(prefStartLabel);
        add(priorityLabel);

        add(actNameText);
        add(prefStartText);
        add(priorityText);
    }

    Object[][] orderData(Object[][] normalData)
    {
        return normalData;
    }

    int indexOfChange(Object[][] orderedData)
    {
        return 0;
    }

    int indexOfHighestPriority(String[][] orderlyData)
    {
        int highPrioIndex = 0;
        int highPrioVal = Integer.parseInt(orderlyData[0][3]);
        for(int i = 1; i < orderlyData.length; i++)
        {
            if(highPrioVal > Integer.parseInt(orderlyData[i][3]))
            {
                highPrioIndex = i;
                highPrioVal = Integer.parseInt(orderlyData[i][3]);
            }
        }
        return highPrioIndex;
    }

    boolean validateEvent(String[] newEvent)
    {
        int tempIntHours, tempIntMinutes;
        if(userName != null)
        {
            try
            {
                tempIntHours = Integer.parseInt(userTime.substring(0, 2));
                tempIntMinutes = Integer.parseInt(userTime.substring(3, 5));
            }
            catch(Exception e)
            {
                return false;
            }
            if(userTime.substring(2, 3).equals(":"))
            {
                if(tempIntHours > 24 || tempIntHours < 0)
                {
                    return false;
                }
                else if(tempIntMinutes > 60 || tempIntMinutes < 0)
                {
                    return false;
                }
            }
        }
        return true;
    }

    Object[][] gapData(String[][] orderlyData)
    {
        String[][] tempData = new String[(orderlyData.length * 2) - 1][4];
        int highPrioIndex = indexOfHighestPriority(orderlyData);
        int tempIndex = highPrioIndex;
        for(int i = (highPrioIndex * 2) - 2; i > 0; i = i - 2)
        {
            tempData[i + 1][0] = "Break";
            tempData[i + 1][1] = "N/A";
            tempData[i + 1][2] = changeTime(orderlyData[tempIndex][2], (highPrioIndex - tempIndex) * -10);
            tempData[i + 1][3] = "0";
            tempData[i] = orderlyData[tempIndex];
            tempData[i][2] = changeTime(orderlyData[tempIndex - 1][2], (highPrioIndex - tempIndex) * -10);
            tempIndex = tempIndex - 1;
        }
        tempIndex = highPrioIndex;
        for(int i = 0; i < (highPrioIndex * 2) + 2; i++)
        {
            tempData[i - 1][0] = "Break";
            tempData[i - 1][1] = "N/A";
            tempData[i - 1][2] = changeTime(orderlyData[tempIndex][2], (tempIndex - highPrioIndex) * 10);
            tempData[i - 1][3] = "0";
            tempData[i] = orderlyData[tempIndex];
            tempData[i][2] = changeTime(orderlyData[tempIndex + 1][2], (tempIndex - highPrioIndex) * 10);
            tempIndex = tempIndex + 1;
        }
        return tempData;
    }

    String changeTime(String initTime, int timeChange)
    {
        int oldHours = Integer.parseInt(initTime.substring(0, 1));
        int oldMinutes = Integer.parseInt(initTime.substring(3, 4));
        int minutes = timeChange % 60;
        int hours = (timeChange - minutes) / 60;
        if((oldMinutes + minutes) > 60)
        {
            minutes = minutes - 60;
            hours = hours + 1;
        }
        else if((oldMinutes + minutes) < 0)
        {
            minutes = minutes - 40;
            hours = hours - 1;
        }
        if(hours > 24)
        {
            hours = hours - 24;
        }
        return(Integer.toString(oldHours + hours) + ":" + Integer.toString(oldMinutes + minutes));
    }
    public static void main(String[] args)
    {
        guiLoader.mainProgramGUI();
    }
}
