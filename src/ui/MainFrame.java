package ui;

import algorithms.BubbleSort;
import algorithms.InsertionSort;
import algorithms.QuickSort;
import algorithms.SortAlgorithm;
import io.FileIO;
import model.SortState;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

// Главный контроллер графического интерфейса
public class MainFrame extends JFrame {

    private VisualizerPanel visualizerPanel;
    private JTextArea logsArea;
    private JComboBox<String> algoBox;
    private int[] currentArray = new int[0];
    private JSlider speedSlider;
    private JCheckBox showValuesCheck;
    
    private JTextField inputField;
    
    private List<SortState> simulationStates;
    private int currentStateIndex = 0;
    private Timer timer;

    public MainFrame() {
        setTitle("Визуализатор алгоритмов сортировки (Релиз)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 245, 245));

        JPanel topPanel = createTopPanel();
        
        visualizerPanel = new VisualizerPanel();
        
        // Интеграция JScrollPane для поддержки больших массивов
        JScrollPane scrollPane = new JScrollPane(visualizerPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        JPanel rightPanel = createRightPanel();
        JPanel bottomPanel = createBottomPanel();

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        
        timer = new Timer(500, e -> stepForward());
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setOpaque(false);

        JButton btnStart = new JButton("Старт");
        JButton btnPause = new JButton("Пауза");
        
        algoBox = new JComboBox<>(new String[]{"Пузырек", "Вставками", "Быстрая сортировка"});

        JButton btnPrev = new JButton("◄");
        JButton btnNext = new JButton("►");

        showValuesCheck = new JCheckBox("Показывать значения");
        showValuesCheck.setOpaque(false);

        JLabel speedLabel = new JLabel("Скорость:");
        speedSlider = new JSlider(1, 100, 50);
        speedSlider.setOpaque(false);

        btnStart.addActionListener(e -> startSimulation());
        btnPause.addActionListener(e -> pauseSimulation());
        btnPrev.addActionListener(e -> stepBackward());
        btnNext.addActionListener(e -> stepForward());
        
        showValuesCheck.addActionListener(e -> visualizerPanel.setShowValues(showValuesCheck.isSelected()));

        speedSlider.addChangeListener(e -> {
            int delay = 1000 - (speedSlider.getValue() * 9);
            timer.setDelay(Math.max(10, delay));
        });

        panel.add(btnStart);
        panel.add(btnPause);
        panel.add(algoBox);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(btnPrev);
        panel.add(btnNext);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(showValuesCheck);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(speedLabel);
        panel.add(speedSlider);

        return panel;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        // Панель динамического изменения массива
        JPanel editPanel = new JPanel();
        editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));
        editPanel.setBorder(BorderFactory.createTitledBorder("Редактор массива"));
        editPanel.setOpaque(false);

        inputField = new JTextField();
        inputField.setMaximumSize(new Dimension(120, 30));
        
        JButton btnAdd = new JButton("Добавить");
        btnAdd.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton btnRemove = new JButton("Удалить последнее");
        btnRemove.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton btnClear = new JButton("Очистить");
        btnClear.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnAdd.addActionListener(e -> addElement());
        btnRemove.addActionListener(e -> removeLastElement());
        btnClear.addActionListener(e -> clearArray());

        editPanel.add(inputField);
        editPanel.add(Box.createVerticalStrut(5));
        editPanel.add(btnAdd);
        editPanel.add(Box.createVerticalStrut(5));
        editPanel.add(btnRemove);
        editPanel.add(Box.createVerticalStrut(5));
        editPanel.add(btnClear);

        JButton btnFromFile = new JButton("Загрузить (File)");
        btnFromFile.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnFromFile.addActionListener(e -> loadFile());

        JButton btnSave = new JButton("Сохранить (Save)");
        btnSave.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSave.addActionListener(e -> saveResults());

        JButton btnDevs = new JButton("О разработчиках");
        btnDevs.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDevs.addActionListener(e -> showDevsDialog());

        panel.add(editPanel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(btnFromFile);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnSave);
        panel.add(Box.createVerticalGlue());
        panel.add(btnDevs);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        TitledBorder titledBorder = BorderFactory.createTitledBorder("Логи выполнения");
        titledBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.setBorder(titledBorder);

        logsArea = new JTextArea(6, 50);
        logsArea.setEditable(false);
        logsArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        logsArea.setText("Система готова к работе.\n");
        
        JScrollPane scrollPane = new JScrollPane(logsArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Блок операций изменения массива
    private void addElement() {
        try {
            int val = Integer.parseInt(inputField.getText().trim());
            int[] newArray = new int[currentArray.length + 1];
            System.arraycopy(currentArray, 0, newArray, 0, currentArray.length);
            newArray[currentArray.length] = val;
            currentArray = newArray;
            
            inputField.setText("");
            resetSimulation();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Пожалуйста, введите корректное целое число.", 
                    "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeLastElement() {
        if (currentArray.length > 0) {
            int[] newArray = new int[currentArray.length - 1];
            System.arraycopy(currentArray, 0, newArray, 0, currentArray.length - 1);
            currentArray = newArray;
            resetSimulation();
        }
    }

    private void clearArray() {
        currentArray = new int[0];
        resetSimulation();
    }

    // Сброс состояний алгоритма при любом изменении исходных данных
    private void resetSimulation() {
        if (timer != null) timer.stop();
        simulationStates = null;
        currentStateIndex = 0;
        visualizerPanel.setState(new SortState(currentArray, List.of(), List.of(), "Массив изменен вручную."));
        logsArea.setText("Массив изменен. Вы можете запустить сортировку.\n");
    }

    private void loadFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                currentArray = FileIO.readArrayFromFile(fileChooser.getSelectedFile());
                resetSimulation();
                logsArea.setText("Успешная загрузка файла: " + fileChooser.getSelectedFile().getName() + "\n");
            } catch (Exception ex) {
                // Вывод диалогового окна с ошибкой
                JOptionPane.showMessageDialog(this, "Ошибка чтения файла: " + ex.getMessage(), 
                        "Критическая ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveResults() {
        if (simulationStates == null || simulationStates.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Нет данных для сохранения. Сначала завершите сортировку.", 
                    "Предупреждение", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                FileIO.saveResultToFile(fileChooser.getSelectedFile(), simulationStates);
                JOptionPane.showMessageDialog(this, "Результаты успешно сохранены.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка при сохранении: " + ex.getMessage(), 
                        "Ошибка записи", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void prepareSimulation() {
        if (currentArray.length == 0) {
            JOptionPane.showMessageDialog(this, "Массив пуст. Добавьте элементы перед сортировкой.", 
                    "Предупреждение", JOptionPane.WARNING_MESSAGE);
            return;
        }

        SortAlgorithm algorithm;
        String selectedAlgo = (String) algoBox.getSelectedItem();

        if ("Вставками".equals(selectedAlgo)) {
            algorithm = new InsertionSort();
        } else if ("Быстрая сортировка".equals(selectedAlgo)) {
            algorithm = new QuickSort();
        } else {
            algorithm = new BubbleSort();
        }

        int[] arrayCopy = currentArray.clone();
        simulationStates = algorithm.sort(arrayCopy);
        currentStateIndex = 0;
        logsArea.setText("Подготовка завершена. Выбран алгоритм: " + selectedAlgo + "\n");
    }

    private void startSimulation() {
        if (simulationStates == null) {
            prepareSimulation();
        }
        if (simulationStates != null) {
            int delay = 1000 - (speedSlider.getValue() * 9);
            timer.setDelay(Math.max(10, delay));
            timer.start();
        }
    }

    private void pauseSimulation() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }

    private void stepForward() {
        if (simulationStates == null) {
            prepareSimulation();
        }
        if (simulationStates != null && currentStateIndex < simulationStates.size() - 1) {
            currentStateIndex++;
            updateVisuals();
        } else {
            pauseSimulation();
        }
    }

    private void stepBackward() {
        pauseSimulation();
        if (simulationStates != null && currentStateIndex > 0) {
            currentStateIndex--;
            updateVisuals();
        }
    }

    private void updateVisuals() {
        SortState currentState = simulationStates.get(currentStateIndex);
        visualizerPanel.setState(currentState);
        logsArea.append(currentState.getLogMessage() + "\n");
        logsArea.setCaretPosition(logsArea.getDocument().getLength()); 
    }

    private void showDevsDialog() {
        DevsDialog dialog = new DevsDialog(this);
        dialog.setVisible(true);
    }
}