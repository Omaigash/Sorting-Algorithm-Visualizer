package io;

import model.SortState;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

// Модуль работы с файловой системой
public class FileIO {

    // Чтение массива строго из первой строки файла
    public static int[] readArrayFromFile(File file) throws IOException {
        List<String> lines = Files.readAllLines(file.toPath());
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Файл пуст.");
        }

        String firstLine = lines.get(0).trim();
        if (firstLine.isEmpty()) {
            throw new IllegalArgumentException("Первая строка файла пуста.");
        }

        String[] tokens = firstLine.split("\\s+");
        List<Integer> numbers = new ArrayList<>();

        for (String token : tokens) {
            try {
                numbers.add(Integer.parseInt(token));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Обнаружен недопустимый символ: '" + token + "'");
            }
        }

        int[] result = new int[numbers.size()];
        for (int i = 0; i < numbers.size(); i++) {
            result[i] = numbers.get(i);
        }

        return result;
    }

    // Сохранение итогового результата и логов
    public static void saveResultToFile(File file, List<SortState> states) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            if (states == null || states.isEmpty()) {
                writer.write("");
                return;
            }

            SortState finalState = states.get(states.size() - 1);
            int[] finalArray = finalState.getArraySnapshot();
            
            // Запись финального массива в первую строку (для обратной совместимости)
            for (int val : finalArray) {
                writer.write(val + " ");
            }
            writer.write("\n\n=== Логи выполнения ===\n");
            
            int step = 1;
            for (SortState state : states) {
                writer.write("Шаг " + step++ + ": " + state.getLogMessage() + "\n");
            }
        }
    }
}