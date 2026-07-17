package algorithms;

import model.SortState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Реализация сортировки пузырьком
public class BubbleSort implements SortAlgorithm {

    @Override
    public List<SortState> sort(int[] array) {
        List<SortState> states = new ArrayList<>();
        List<Integer> sortedIndices = new ArrayList<>();
        
        states.add(new SortState(array, new ArrayList<>(), sortedIndices, "Начало сортировки пузырьком."));

        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                states.add(new SortState(array, Arrays.asList(j, j + 1), sortedIndices, 
                        "Сравнение: " + array[j] + " и " + array[j + 1]));

                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                    
                    states.add(new SortState(array, Arrays.asList(j, j + 1), sortedIndices, 
                            "Обмен: " + array[j] + " и " + array[j + 1]));
                }
            }
            sortedIndices.add(n - i - 1);
            states.add(new SortState(array, new ArrayList<>(), sortedIndices, "Элемент " + array[n - i - 1] + " на позиции."));
        }
        if (n > 0) sortedIndices.add(0);
        states.add(new SortState(array, new ArrayList<>(), sortedIndices, "Сортировка завершена."));

        return states;
    }
}