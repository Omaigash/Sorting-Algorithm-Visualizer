package algorithms;

import model.SortState;
import java.util.List;

// Базовый интерфейс алгоритмов сортировки
public interface SortAlgorithm {
    List<SortState> sort(int[] array);
}