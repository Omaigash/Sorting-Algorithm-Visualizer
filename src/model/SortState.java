package model;

import java.util.ArrayList;
import java.util.List;

// Класс хранения снимка состояния массива
public class SortState {
    
    private final int[] arraySnapshot;
    private final List<Integer> comparingIndices;
    private final List<Integer> sortedIndices;
    private final String logMessage;

    public SortState(int[] array, List<Integer> comparingIndices, List<Integer> sortedIndices, String logMessage) {
        this.arraySnapshot = array.clone();
        this.comparingIndices = new ArrayList<>(comparingIndices);
        this.sortedIndices = new ArrayList<>(sortedIndices);
        this.logMessage = logMessage;
    }

    public int[] getArraySnapshot() {
        return arraySnapshot.clone();
    }

    public List<Integer> getComparingIndices() {
        return new ArrayList<>(comparingIndices);
    }

    public List<Integer> getSortedIndices() {
        return new ArrayList<>(sortedIndices);
    }

    public String getLogMessage() {
        return logMessage;
    }
}