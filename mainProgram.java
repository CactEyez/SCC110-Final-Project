import java.awt.*; //only necessary imports which allow for GUIS, GUI events, GUI objects and random numbers
import java.awt.event.*;
import java.nio.file.attribute.UserPrincipal;

import javax.sql.RowSetMetaData;
import javax.swing.*;
import javax.swing.table.TableModel;

import java.util.*;
import java.util.concurrent.CompletionException;

public class mainProgram extends JFrame {
    String[] colNames = { "Act Name", "Length", "Start Time", "Priority" };
    String[][] rawData = new String[40][4]; // {name, length, blank/start time, priority}
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

    public mainProgram() {
        setLayout(new FlowLayout());

        rawData[0][0] = "Test";
        rawData[0][1] = "20";
        rawData[0][2] = "780";
        rawData[0][3] = "1";

        completeData[0][0] = "Test";
        completeData[0][1] = "20";
        completeData[0][2] = "13:00";
        completeData[0][3] = "1";

        add(actNameLabel);
        add(actNameText);

        add(actLenLabel);
        add(actLenText);

        add(priorityLabel);
        add(priorityText);

        add(inputDataButton);
        inputDataButton.addActionListener(actionEvent -> {
            String[] tempArray = { actNameText.getText(), actLenText.getText(), "", priorityText.getText() };
            if (validateEvent(tempArray)) {

                    rawData[rawDataLength] = tempArray;
                rawDataLength++;
                String[][] tempOrderData = orderData();
                for (int i = 0; i < rawDataLength; i++) {
                    for (int j = 0; j < completeData[i].length; j++) {
                        completeData[i][j] = tempOrderData[i][j];
                    }
                }
                for(int k = 0; k < rawDataLength; k++)
                {
                    completeData[k][2] = minToTime(completeData[k][2]);
                }
                table.repaint();
            }
        });

        highPrioIndex = 0;
        rawDataLength++;

        table.setAutoCreateRowSorter(true);
        table.repaint();
        ;

        add(new JScrollPane(table));
    }

    String[][] orderData() {
        int highPrioIndex = getIndexOfHighestPriority(rawData)[0];
        int currentPrio = getIndexOfHighestPriority(rawData)[1];
        String currentEarliestTime = (rawData[highPrioIndex][2]);
        String currentLatestTime = Integer.toString(Integer.parseInt(rawData[highPrioIndex][2]) + Integer.parseInt(rawData[highPrioIndex][1]));
        int numPrioSearched = 1;
        int[] earlierPriorities = new int[20];
        int[] laterPriorities = new int[20];
        int highPrioFinalPos = 0;
        int currentLength, numEarlyPrio = 0, numLatePrio = 0;
        while (numPrioSearched != rawDataLength) {
            if (getIndexOfPriority(Integer.toString(currentPrio + 1)) != -1) {
                if (furtherTime(rawData[highPrioIndex][2], currentEarliestTime, currentLatestTime)) {
                    laterPriorities[numLatePrio] = getIndexOfPriority(Integer.toString(currentPrio + 1));
                    currentLatestTime = Integer.toString(Integer.parseInt(currentLatestTime) + Integer.parseInt(rawData[getIndexOfPriority(Integer.toString(currentPrio + 1))][1]));
                    numLatePrio++;
                } else {
                    earlierPriorities[numEarlyPrio] = getIndexOfPriority(Integer.toString(currentPrio + 1));
                    currentEarliestTime = Integer.toString(Integer.parseInt(currentEarliestTime) - Integer.parseInt(rawData[getIndexOfPriority(Integer.toString(currentPrio + 1))][1]));
                    numEarlyPrio++;
                }
                numPrioSearched++;
                currentLength = Integer.parseInt(rawData[getIndexOfPriority(Integer.toString(currentPrio + 1))][1]);
            }
            currentPrio++;
        }
        currentEarliestTime = Integer.toString(Integer.parseInt((rawData[highPrioIndex][2])) - 10);
        currentLatestTime = Integer.toString(Integer.parseInt(rawData[highPrioIndex][2]) + Integer.parseInt(rawData[highPrioIndex][1]) + 10);
        String[][] updatedData = new String[40][4];
        updatedData[numEarlyPrio] = rawData[highPrioIndex];
        highPrioIndex = highPrioIndex + numEarlyPrio;
        for (int i = numEarlyPrio; i > 0; i--) {
            updatedData[i - 1] = rawData[earlierPriorities[numEarlyPrio - i]];
            updatedData[i - 1][1] = Integer.toString(Integer.parseInt(rawData[earlierPriorities[numEarlyPrio - i]][1]));
            updatedData[i - 1][2] = Integer.toString(Integer.parseInt(currentEarliestTime)
                    - Integer.parseInt(rawData[earlierPriorities[numEarlyPrio - i]][1]));
            currentEarliestTime = updatedData[i - 1][2];
        }
        for (int j = 0; j < numLatePrio; j++) {
            updatedData[j + highPrioIndex + 1] = rawData[laterPriorities[j]];
            updatedData[j + highPrioIndex + 1][2] = currentLatestTime;
            currentLatestTime = Integer.toString(
                    Integer.parseInt(currentLatestTime) + Integer.parseInt(updatedData[j + highPrioIndex][1]));
        }
        //return (gapData(updatedData));
        return(updatedData);
    }

    String minToTime(String minutes) {
        System.out.println("converting time");
        String time;
        int intMin = Integer.parseInt(minutes); // add 24hr overflow
        System.out.println((intMin - (intMin % 60)) / 60);
        if(((intMin - (intMin % 60)) / 60) > 24)
        {
            time = Integer.toString(((intMin - (intMin % 60)) / 60) - 24) + ":" + Integer.toString(intMin % 60);
        }
        else{
            time = Integer.toString((intMin - (intMin % 60)) / 60) + ":" + Integer.toString(intMin % 60);
        }
        if ((intMin % 60) == 0) {
            time = time + "0";
        }
        System.out.println("time:" + time);
        return time;
    }

    boolean furtherTime(String highPrioStart, String earliest, String latest) {
        int EarlyDiffFromStart = Integer.parseInt(highPrioStart) - Integer.parseInt(earliest);
        int EarlyDiffFromEnd = Integer.parseInt(latest) - Integer.parseInt(highPrioStart);
        if (EarlyDiffFromEnd > EarlyDiffFromStart) {
            return false;
        }
        else{
            return true;
        }
    }

    int getIndexOfPriority(String val) {
        for (int i = 0; i < rawDataLength; i++) {
            if (rawData[i][3].equals(val)) {
                return i;
            }
        }
        return -1;
    }

    int getIndexOfChange(Object[][] orderedData) {
        return 0;
    }

    int[] getIndexOfHighestPriority(String[][] orderlyData) {
        int tempHighPrioIndex = 0;
        int highPrioVal = Integer.parseInt(orderlyData[0][3]);
        for (int i = 0; i < rawDataLength; i++) {
            if (highPrioVal > Integer.parseInt(orderlyData[i][3])) {
                tempHighPrioIndex = i;
                highPrioVal = Integer.parseInt(orderlyData[i][3]);
            }
        }
        int[] tempIntArray = { tempHighPrioIndex, highPrioVal };
        return tempIntArray;
    }

    boolean validateEvent(String[] newEvent) {
        if (actNameText.getText().equals("") || actLenText.getText().equals("") || priorityText.getText().equals("")) {
            return false;
        }
        if (Integer.parseInt(priorityText.getText()) <= 0) {
            return false;
        }
        for (int i = 0; i < rawDataLength + 1; i++) {
            if (priorityText.getText().equals(rawData[i][3])) {
                return false;
            }
        }
        try {
            Integer.parseInt(actLenText.getText());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    String[][] gapData(String[][] orderlyData) {
        String[][] tempData = new String[40][4];
        int highPrioIndex = getIndexOfHighestPriority(orderlyData)[0];
        int tempIndex = highPrioIndex;
        for (int i = (highPrioIndex * 2) - 2; i > 0; i = i - 2) {
            tempData[i + 1][0] = "Break";
            tempData[i + 1][1] = "10";
            tempData[i + 1][2] = Integer
                    .toString(Integer.parseInt(orderlyData[tempIndex][2]) + (((highPrioIndex - tempIndex) - 1) * -10));
            tempData[i + 1][3] = "0";
            tempData[i] = orderlyData[tempIndex];
            tempData[i][2] = Integer
                    .toString(Integer.parseInt(orderlyData[tempIndex + 1][2]) + ((tempIndex - highPrioIndex) * -10));
            tempIndex = tempIndex - 1;
        }
        tempIndex = highPrioIndex;
        for (int i = 0; i < ((highPrioIndex * 2) - 1); i++) {
            tempData[highPrioIndex + i + 1][0] = "Break";
            tempData[highPrioIndex + i + 1][1] = "10";
            tempData[highPrioIndex + i + 1][2] = minToTime(Integer
                    .toString(Integer.parseInt(orderlyData[tempIndex][2]) + (((tempIndex - highPrioIndex) - 1) * 10)));
            tempData[highPrioIndex + i + 1][3] = "0";
            tempData[i + 2] = orderlyData[tempIndex];
            tempData[i + 2][2] = minToTime(
                    Integer.toString(Integer.parseInt(orderlyData[tempIndex][2]) + ((tempIndex - highPrioIndex) * 10)));
            tempIndex = tempIndex + 1;
        }
        printArray(tempData);
        return tempData;
    }

    String changeTime(String initTime, int timeChange) {
        int oldHours = Integer.parseInt(initTime.substring(0, 1));
        int oldMinutes = Integer.parseInt(initTime.substring(3, 4));
        int minutes = timeChange % 60;
        int hours = (timeChange - minutes) / 60;
        if ((oldMinutes + minutes) > 60) {
            minutes = minutes - 60;
            hours = hours + 1;
        } else if ((oldMinutes + minutes) < 0) {
            minutes = minutes - 40;
            hours = hours - 1;
        }
        if (hours > 24) {
            hours = hours - 24;
        }
        if ((oldMinutes + minutes) == 0) {
            return (Integer.toString(oldHours + hours) + ":" + "00");
        }
        return (Integer.toString(oldHours + hours) + ":" + Integer.toString(oldMinutes + minutes));
    }

    void printArray(String[][] printy)
    {
        for(int i = 0; i < rawDataLength; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                System.out.println("String[" + i + "][" + j + "]: " + printy[i][j]);
            }
        }
    }

    public static void main(String[] args) {
        guiLoader.mainProgramGUI();
    }
}
