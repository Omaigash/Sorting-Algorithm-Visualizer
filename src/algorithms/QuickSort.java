package algorithms;

import model.SortState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Реализация быстрой сортировки
public class QuickSort implements SortAlgorithm {

    @Override
    public List<SortState> sort(int[] array) {
        List<SortState> states = new ArrayList<>();
        List<Integer> sortedIndices = new ArrayList<>();
        
        states.add(new SortState(array, new ArrayList<>(), sortedIndices, "Начало быстрой сортировки."));
        quickSort(array, 0, array.length - 1, states, sortedIndices);
        
        for (int k = 0; k < array.length; k++) {
            if (!sortedIndices.contains(k)) sortedIndices.add(k);
        }
        states.add(new SortState(array, new ArrayList<>(), sortedIndices, "Сортировка завершена."));
        
        return states;
    }

    private void quickSort(int[] array, int low, int high, List<SortState> states, List<Integer> sortedIndices) {
        if (low < high) {
            int pi = partition(array, low, high, states, sortedIndices);
            sortedIndices.add(pi);
            states.add(new SortState(array, new ArrayList<>(), sortedIndices, "Опорный элемент на позиции."));

            quickSort(array, low, pi - 1, states, sortedIndices);
            quickSort(array, pi + 1, high, states, sortedIndices);
        } else if (low == high) {
            sortedIndices.add(low);
        }
    }

    private int partition(int[] array, int low, int high, List<SortState> states, List<Integer> sortedIndices) {
        int pivot = array[high];
        int i = (low - 1);
        
        states.add(new SortState(array, Arrays.asList(high), sortedIndices, "Опорный элемент: " + pivot));

        for (int j = low; j < high; j++) {
            states.add(new SortState(array, Arrays.asList(j, high), sortedIndices, "Сравнение " + array[j] + " с " + pivot));
            
            if (array[j] < pivot) {
                i++;
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                states.add(new SortState(array, Arrays.asList(i, j), sortedIndices, "Обмен " + array[i] + " и " + array[j]));
            }
        }

        int temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;
        states.add(new SortState(array, Arrays.asList(i + 1, high), sortedIndices, "Смещение опорного элемента."));

        return i + 1;
    }
}