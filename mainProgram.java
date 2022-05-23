import java.awt.*; //only necessary imports which allow for GUIS, GUI events, GUI objects and random numbers
import java.awt.event.*;
import java.time.temporal.TemporalAdjuster;

import javax.swing.*;
import java.util.*;
public class mainProgram extends JFrame
{
    String[] colNames = {"Act Name", "Length", "Start Time", "Priority"};
    //String[][] orderedData = new String[20][4]; //{name, length, start time (in minutes), priority};
    String[][] rawData = new String[20][4]; //{name, length, blank/start time, priority}

    JTable table = new JTable(rawData, colNames);

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
        int currentPrio = getIndexOfHighestPriority(rawData)[1];
        String currentEarliestTime = (rawData[getIndexOfHighestPriority(rawData)[0]][2]);
        String currentLatestTime = Integer.toString(Integer.parseInt(rawData[getIndexOfHighestPriority(rawData)[0]][2]) + Integer.parseInt(rawData[getIndexOfHighestPriority(rawData)[0]][1]));
        int numPrioSearched = 1;
        int[] earlierPriorities = new int[20];
        int[] laterPriorities = new int[20];
        int highPrioFinalPos = 0;
        int currentLength, numEarlyPrio = 0, numLatePrio = 0;
        while(numPrioSearched != rawData.length)
        {
            if(getIndexOfPriority(Integer.toString(currentPrio + 1)) != -1)
            {
                if(furtherTime(rawData[getIndexOfHighestPriority(rawData)[0]][2], currentEarliestTime, currentLatestTime))
                {
                    laterPriorities[numLatePrio] = getIndexOfPriority(Integer.toString(currentPrio + 1));
                    currentLatestTime = Integer.toString(Integer.parseInt(currentLatestTime) + Integer.parseInt(rawData[getIndexOfPriority(Integer.toString(currentPrio + 1))][1]));
                    numLatePrio++;
                }
                else
                {
                    earlierPriorities[numEarlyPrio] = getIndexOfPriority(Integer.toString(currentPrio + 1));
                    currentEarliestTime = Integer.toString(Integer.parseInt(currentEarliestTime) - Integer.parseInt(rawData[getIndexOfPriority(Integer.toString(currentPrio + 1))][1]));
                    numEarlyPrio++;
                }
                numPrioSearched++;
                currentLength = Integer.parseInt(rawData[getIndexOfPriority(Integer.toString(currentPrio + 1))][1]);
            }
            currentPrio++;
        }
        currentEarliestTime = (rawData[getIndexOfHighestPriority(rawData)[0]][2]);
        currentLatestTime = Integer.toString(Integer.parseInt(rawData[getIndexOfHighestPriority(rawData)[0]][2]) + Integer.parseInt(rawData[getIndexOfHighestPriority(rawData)[0]][1]));
        String[][] updatedData = new String[numEarlyPrio + numLatePrio + 1][4];
        updatedData[getIndexOfHighestPriority(rawData)[0]] = rawData[getIndexOfHighestPriority(rawData)[0]];
        for(int i = numEarlyPrio; i > 0; i--)
        {
            updatedData[i - 1] = rawData[earlierPriorities[numEarlyPrio - i]];
            updatedData[i - 1][2] = Integer.toString(Integer.parseInt(currentEarliestTime) - Integer.parseInt(rawData[earlierPriorities[numEarlyPrio - i]][1]));
            currentEarliestTime = updatedData[i - 1][2];
        }
        for(int j = 0; j < numLatePrio; j++)
        {
            updatedData[j + getIndexOfHighestPriority(rawData)[0] + 1] = rawData[laterPriorities[j]];
            updatedData[j + getIndexOfHighestPriority(rawData)[0] + 1][2] = currentLatestTime;
            currentLatestTime = Integer.toString(Integer.parseInt(currentLatestTime) + Integer.parseInt(updatedData[j + getIndexOfHighestPriority(rawData)[0] + 1][1]));
        }
        return(gapData(updatedData));
    }

    boolean furtherTime(String highPrioStart, String earliest, String latest)
    {
        int EarlyDiffFromEnd = ((Integer.parseInt(latest.substring(0,2)) * 60) + Integer.parseInt(latest.substring(3,5))) - ((Integer.parseInt(highPrioStart.substring(0,2)) * 60) + Integer.parseInt(highPrioStart.substring(3,5)));
        int EarlyDiffFromStart = ((Integer.parseInt(highPrioStart.substring(0,2)) * 60) + Integer.parseInt(highPrioStart.substring(3,5))) - ((Integer.parseInt(earliest.substring(0,2)) * 60) + Integer.parseInt(earliest.substring(3,5)));
        if(EarlyDiffFromEnd <= EarlyDiffFromStart)
        {
            return true;
        }
        return false;
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
        if(userName != null)
        {
            try
            {
                Integer.parseInt(userLength);
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

    String[][] gapData(String[][] orderlyData)
    {
        String[][] tempData = new String[(orderlyData.length * 2) - 1][4];
        int highPrioIndex = getIndexOfHighestPriority(orderlyData)[0];
        int tempIndex = highPrioIndex;
        for(int i = (highPrioIndex * 2) - 2; i > 0; i = i - 2)
        {
            tempData[i + 1][0] = "Break";
            tempData[i + 1][1] = "10";
            tempData[i + 1][2] = Integer.toString(Integer.parseInt(orderlyData[tempIndex][2]) + (((highPrioIndex - tempIndex) - 1) * -10));
            tempData[i + 1][3] = "0";
            tempData[i] = orderlyData[tempIndex];
            tempData[i][2] = Integer.toString(Integer.parseInt(orderlyData[tempIndex + 1][2]) + ((tempIndex - highPrioIndex) * -10));
            tempIndex = tempIndex - 1;
        }
        tempIndex = highPrioIndex;
        for(int i = 0; i < (highPrioIndex * 2) + 2; i++)
        {
            tempData[i - 1][0] = "Break";
            tempData[i - 1][1] = "10";
            tempData[i - 1][2] = Integer.toString(Integer.parseInt(orderlyData[tempIndex][2]) + (((tempIndex - highPrioIndex) - 1) * 10));
            tempData[i - 1][3] = "0";
            tempData[i] = orderlyData[tempIndex];
            tempData[i][2] = Integer.toString(Integer.parseInt(orderlyData[tempIndex + 1][2]) + ((tempIndex - highPrioIndex) * 10));
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
