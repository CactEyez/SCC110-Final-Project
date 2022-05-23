import java.awt.*; //only necessary imports which allow for GUIS, GUI events, GUI objects and random numbers
import java.awt.event.*;
import java.time.temporal.TemporalAdjuster;

import javax.swing.*;
import java.util.*;
public class mainProgram extends JFrame
{
    String[] colNames = {"Act Name", "Length", "Start Time", "Priority"};
    String[][] orderedData = new String[20][4]; //{name, length, start time, priority};
    String[][] rawData = new String[20][4]; //{name, length, blank, priority}

    JTable table = new JTable(orderedData, colNames);

    JLabel actNameLabel = new JLabel("Act Name");
    JLabel prefStartLabel = new JLabel("Length");
    JLabel priorityLabel = new JLabel("Priority");
    
    JTextArea actNameText = new JTextArea();
    JTextArea prefStartText = new JTextArea();
    JTextArea priorityText = new JTextArea();

    JButton inputDataButto = new JButton();

    String userName, userLength, userPrio;

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

    String[][] orderData(String[][] normalData)
    {
        String[][] updatedData = new String[20][4];
        int[] priorityTimeOrder = new int[0];
        int highestPriorityIndex = getIndexOfHighestPriority(rawData)[0];
        priorityTimeOrder[highestPriorityIndex] = 0;
        int nextPriority = -1;
        int prioritiesSearched = 1;
        int currentPriority = getIndexOfHighestPriority(rawData)[1];
        while(prioritiesSearched != rawData.length);
        {
            //priorityTimeOrder[getIndexOfPriority(Integer.toString(currentPriority - 1))];
            if(getIndexOfPriority(Integer.toString(currentPriority - 1)) != -1)
            {
                currentPriority = currentPriority - 1;
                //ADD CHECK TIME WHETHER BETTER BEFORE OR AFTER
                //IF HIGHER PUT ABOVE IN PRIORITY TIME ORDER
            }
        }
        return updatedData;
    }

    int getIndexOfPriority(String val)
    {
        for(int i = 0; i < rawData.length; i++)
        {
            if(rawData[i][2].equals(val))
            {
                return i;
            }
        }
        return -1;
    }

    int getIndexOfChange(Object[][] orderedData)
    {
        return 0;
    }

    int[] getIndexOfHighestPriority(String[][] orderlyData)
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
        int[] tempIntArray = {highPrioIndex, highPrioVal};
        return tempIntArray;
    }

    boolean validateEvent(String[] newEvent)
    {
        int tempLength;
        if(userName != null)
        {
            try
            {
                tempLength = Integer.parseInt(userLength);
            }
            catch(Exception e)
            {
                return false;
            }
            if(Integer.parseInt(userPrio) <= 0)
            {
                return false;
            }
        }
        return true;
    }

    Object[][] gapData(String[][] orderlyData)
    {
        String[][] tempData = new String[(orderlyData.length * 2) - 1][4];
        int highPrioIndex = getIndexOfHighestPriority(orderlyData)[0];
        int tempIndex = highPrioIndex;
        for(int i = (highPrioIndex * 2) - 2; i > 0; i = i - 2)
        {
            tempData[i + 1][0] = "Break";
            tempData[i + 1][1] = "10";
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
            tempData[i - 1][1] = "10";
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
