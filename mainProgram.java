import java.awt.*; //only necessary imports which allow for GUIS, GUI events, GUI objects and random numbers
import java.awt.event.*;
import java.nio.file.attribute.UserPrincipal;

import javax.sql.RowSetMetaData;
import javax.swing.*;
import javax.swing.table.TableModel;

import java.util.*;
public class mainProgram extends JFrame
{
    String[] colNames = {"Act Name", "Length", "Start Time", "Priority"};
    String[][] rawData = new String[40][4]; //{name, length, blank/start time, priority}
    String[][] completeData = new String[40][4];

    int rawDataLength = 0;

    JLabel actNameLabel = new JLabel("Act Name:");
    JLabel actLenLabel = new JLabel("Length:");
    JLabel priorityLabel = new JLabel("Priority:");
    
    JTextArea actNameText = new JTextArea(1, 20);
    JTextArea actLenText = new JTextArea(1, 3);
    JTextArea priorityText = new JTextArea(1, 2);

    JButton inputDataButton = new JButton("Insert Data");

    String userName, userLength, userPrio;

    int highPrioIndex;

    JTable table = new JTable(completeData, colNames);

    public mainProgram()
    {
        setLayout(new FlowLayout());

        rawData[0][0] = "Test";
        rawData[0][1] = "20";
        rawData[0][2] = "780";
        rawData[0][3] = "1";

        completeData[0][0] = "Test";
        completeData[0][1] = "20";
        completeData[0][2] = "780";
        completeData[0][3] = "1";

        completeData = rawData;
        completeData[0][0] = "Test";

        add(actNameLabel);
        add(actNameText);

        add(actLenLabel);
        add(actLenText);

        add(priorityLabel);
        add(priorityText);
        
        add(inputDataButton);
        inputDataButton.addActionListener( actionEvent -> {
            String[] tempArray = {actNameText.getText(), actLenText.getText(), "", priorityText.getText()};
            if(validateEvent(tempArray))
            {
                rawData[rawDataLength] = tempArray;
                System.out.println("rawdatalength: " + rawDataLength);
                System.out.println(rawData[rawDataLength][0]);
                System.out.println(rawData[rawDataLength][1]);
                System.out.println(rawData[rawDataLength][2]);
                System.out.println(rawData[rawDataLength][3]);
                rawDataLength++;
                completeData = orderData(rawData);
                System.out.println("rawdatalength: " + rawDataLength);
                System.out.println(completeData[rawDataLength-1][0]);
                System.out.println(completeData[rawDataLength-1][1]);
                System.out.println(completeData[rawDataLength-1][2]);
                System.out.println(completeData[rawDataLength-1][3]);
                System.out.println("Attemping repaint");
                table.repaint();
            }
        });

        /*rawData[0][0] = "Test";
        rawData[0][1] = "20";
        rawData[0][2] = "780";
        rawData[0][3] = "1";

        completeData = rawData;
        completeData[0][0] = "Test";*/
        System.out.println("complete data [0][2]: " + completeData[0][2]);
        highPrioIndex = 0;
        rawDataLength++;

        table.setAutoCreateRowSorter(true);
        table.repaint();;

        add(new JScrollPane(table));
    }

    String[][] orderData(String[][] rawData)
    {
        int currentPrio = getIndexOfHighestPriority(rawData)[1];
        System.out.println(highPrioIndex);
        System.out.println("high prio val" + getIndexOfHighestPriority(rawData)[1]);
        System.out.println(rawData[highPrioIndex][0]);
        System.out.println(rawData[highPrioIndex][1]);
        System.out.println(rawData[highPrioIndex][2]);
        System.out.println(rawData[highPrioIndex][3]);
        String currentEarliestTime = (rawData[highPrioIndex][2]);
        String currentLatestTime = Integer.toString(Integer.parseInt(rawData[highPrioIndex][2]) + Integer.parseInt(rawData[highPrioIndex][1]));
        int numPrioSearched = 1;
        int[] earlierPriorities = new int[20];
        int[] laterPriorities = new int[20];
        int highPrioFinalPos = 0;
        int currentLength, numEarlyPrio = 0, numLatePrio = 0;
        System.out.println("raw data length: " + rawDataLength);
        while(numPrioSearched != rawDataLength)
        {
            //System.out.println(numPrioSearched + ", " + rawDataLength);
            //System.out.println("Integer.toString(currentPrio + 1: " + Integer.toString(currentPrio + 1));
            if(getIndexOfPriority(Integer.toString(currentPrio + 1)) != -1)
            {
                if(furtherTime(rawData[highPrioIndex][2], currentEarliestTime, currentLatestTime))
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
                System.out.println("Found a new priority");
                numPrioSearched++;
                currentLength = Integer.parseInt(rawData[getIndexOfPriority(Integer.toString(currentPrio + 1))][1]);
            }
            else
            {
                //System.out.println("Did else");
            }
            currentPrio++;
        }
        currentEarliestTime = (rawData[highPrioIndex][2]);
        currentLatestTime = Integer.toString(Integer.parseInt(rawData[highPrioIndex][2]) + Integer.parseInt(rawData[highPrioIndex][1]));
        String[][] updatedData = new String[40][4];
        updatedData[highPrioIndex] = rawData[highPrioIndex];
        for(int i = numEarlyPrio; i > 0; i--)
        {
            updatedData[i - 1] = rawData[earlierPriorities[numEarlyPrio - i]];
            updatedData[i - 1][2] = Integer.toString(Integer.parseInt(currentEarliestTime) - Integer.parseInt(rawData[earlierPriorities[numEarlyPrio - i]][1]));
            currentEarliestTime = updatedData[i - 1][2];
        }
        for(int j = 0; j < numLatePrio; j++)
        {
            updatedData[j + highPrioIndex + 1] = rawData[laterPriorities[j]];
            updatedData[j + highPrioIndex + 1][2] = currentLatestTime;
            currentLatestTime = Integer.toString(Integer.parseInt(currentLatestTime) + Integer.parseInt(updatedData[j + highPrioIndex + 1][1]));
        }
        return(gapData(updatedData));
    }

    String minToTime(String minutes)
    {
        System.out.println("converting time");
        int intMin = Integer.parseInt(minutes); //add 24hr overflow
        String time = Integer.toString((intMin - (intMin % 60)) / 60) + ":" + Integer.toString(intMin % 60);
        System.out.println("time:" + time);
        return time;
    }

    boolean furtherTime(String highPrioStart, String earliest, String latest)
    {
        int EarlyDiffFromStart = Integer.parseInt(highPrioStart) - Integer.parseInt(earliest);
        int EarlyDiffFromEnd = Integer.parseInt(latest) - Integer.parseInt(highPrioStart);
        //int EarlyDiffFromEnd = ((Integer.parseInt(latest.substring(0,2)) * 60) + Integer.parseInt(latest.substring(3,5))) - ((Integer.parseInt(highPrioStart.substring(0,2)) * 60) + Integer.parseInt(highPrioStart.substring(3,5)));
        //int EarlyDiffFromStart = ((Integer.parseInt(highPrioStart.substring(0,2)) * 60) + Integer.parseInt(highPrioStart.substring(3,5))) - ((Integer.parseInt(earliest.substring(0,2)) * 60) + Integer.parseInt(earliest.substring(3,5)));
        if(EarlyDiffFromEnd <= EarlyDiffFromStart)
        {
            return true;
        }
        return false;
    }

    int getIndexOfPriority(String val)
    {
        //System.out.println(rawDataLength);
        for(int i = 0; i < rawDataLength; i++)
        {
            //System.out.println("i: " + i);
            //System.out.println(rawData[i][0]);
            //System.out.println(rawData[i][1]);
            //System.out.println(rawData[i][2]);
            //System.out.println(rawData[i][3]);
            if(rawData[i][3].equals(val))
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
        int highPrioVal = Integer.parseInt(rawData[0][3]);
        for(int i = 0; i < rawDataLength - 1; i++)
        {
            System.out.println("index func: " + i + ", " + rawDataLength);
            if(highPrioVal > Integer.parseInt(rawData[i][3]))
            {
                highPrioIndex = i;
                highPrioVal = Integer.parseInt(rawData[i][3]);
            }
        }
        int[] tempIntArray = {highPrioIndex, highPrioVal};
        return tempIntArray;
    }

    boolean validateEvent(String[] newEvent)
    {
        System.out.println("validating");
        System.out.println("name:" + actNameText.getText());
        System.out.println("length: " + actLenText.getText());
        System.out.println("priority: " + priorityText.getText());
        if(actNameText.getText().equals("") || actLenText.getText().equals("") || priorityText.getText().equals(""))
        {
            return false;
        }
        if(Integer.parseInt(priorityText.getText()) <= 0)
            {
                return false;
            }
            System.out.println("reached loop");
            for(int i = 0; i < rawDataLength + 1; i++)
            {
                System.out.println("userPrio");
                System.out.println(rawData[i][3]);
                if(priorityText.getText().equals(rawData[i][3]))
                {
                    return false;
                }
            }
            try
            {
                Integer.parseInt(actLenText.getText());
            }
            catch(Exception e)
            {
                return false;
            }
        System.out.println("returning true");
        return true;
    }

    String[][] gapData(String[][] orderlyData)
    {
        String[][] tempData = new String[40][4];
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
        for(int i = 0; i < (highPrioIndex * 2); i++)
        {
            tempData[highPrioIndex + i + 1][0] = "Break";
            tempData[highPrioIndex + i + 1][1] = "10";
            tempData[highPrioIndex + i + 1][2] = minToTime(Integer.toString(Integer.parseInt(orderlyData[tempIndex][2]) + (((tempIndex - highPrioIndex) - 1) * 10)));
            tempData[highPrioIndex + i + 1][3] = "0";
            tempData[i + 2] = orderlyData[tempIndex];
            tempData[i + 2][2] = minToTime(Integer.toString(Integer.parseInt(orderlyData[tempIndex][2]) + ((tempIndex - highPrioIndex) * 10)));
            tempIndex = tempIndex + 1;
        }
        System.out.println("tempdata length:" + tempData.length);
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
