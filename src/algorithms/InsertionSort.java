package algorithms;

import model.SortState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Реализация сортировки вставками
public class InsertionSort implements SortAlgorithm {

    @Override
    public List<SortState> sort(int[] array) {
        List<SortState> states = new ArrayList<>();
        List<Integer> sortedIndices = new ArrayList<>();
        
        states.add(new SortState(array, new ArrayList<>(), sortedIndices, "Начало сортировки вставками."));

        for (int i = 1; i < array.length; ++i) {
            int key = array[i];
            int j = i - 1;

            states.add(new SortState(array, Arrays.asList(i), sortedIndices, "Выбор " + key + " для вставки."));

            while (j >= 0 && array[j] > key) {
                states.add(new SortState(array, Arrays.asList(j, j + 1), sortedIndices, "Сдвиг " + array[j] + " вправо."));
                array[j + 1] = array[j];
                j = j - 1;
                states.add(new SortState(array, Arrays.asList(j + 1), sortedIndices, "Промежуточное состояние."));
            }
            array[j + 1] = key;
            states.add(new SortState(array, Arrays.asList(j + 1), sortedIndices, "Вставка " + key + " на позицию."));
        }
        
        for (int k = 0; k < array.length; k++) sortedIndices.add(k);
        states.add(new SortState(array, new ArrayList<>(), sortedIndices, "Сортировка завершена."));

        return states;
    }
}