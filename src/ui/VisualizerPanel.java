package ui;

import model.SortState;
import javax.swing.*;
import java.awt.*;

// Панель отрисовки массива с поддержкой отрицательных чисел и оси
public class VisualizerPanel extends JPanel {

    private SortState currentState;
    private boolean showValues = false;

    public VisualizerPanel() {
        setBackground(Color.WHITE);
    }

    public void setShowValues(boolean showValues) {
        this.showValues = showValues;
        repaint();
    }

    // Обновление состояния и перерасчет ширины для JScrollPane
    public void setState(SortState state) {
        this.currentState = state;
        
        if (state != null) {
            int count = state.getArraySnapshot().length;
            int minBarWidth = 40;
            int requiredWidth = count * minBarWidth + 40;
            
            // Установка предпочтительного размера панели для активации горизонтальной прокрутки
            if (getParent() != null && requiredWidth > getParent().getWidth()) {
                setPreferredSize(new Dimension(requiredWidth, getHeight()));
            } else if (getParent() != null) {
                setPreferredSize(new Dimension(getParent().getWidth(), getHeight()));
            }
            revalidate();
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (currentState == null || currentState.getArraySnapshot().length == 0) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int verticalPadding = 40;
        int horizontalPadding = 20;
        
        int[] data = currentState.getArraySnapshot();
        int elementsCount = data.length;
        
        int barWidth = (width - horizontalPadding * 2) / elementsCount;
        if (barWidth > 100) barWidth = 100; // Ограничение максимальной ширины

        // Поиск минимального и максимального значения
        int maxValue = 0;
        int minValue = 0;
        for (int value : data) {
            if (value > maxValue) maxValue = value;
            if (value < minValue) minValue = value;
        }

        int range = maxValue - minValue;
        if (range == 0) range = 1;

        // Вычисление положения нулевой оси
        int drawAreaHeight = height - verticalPadding * 2;
        double scale = (double) drawAreaHeight / range;
        int yZero = verticalPadding + (int) (maxValue * scale);

        // Отрисовка нулевой оси
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawLine(horizontalPadding, yZero, width - horizontalPadding, yZero);

        // Отрисовка столбцов
        for (int i = 0; i < elementsCount; i++) {
            int val = data[i];
            int barHeight = (int) (Math.abs(val) * scale);
            
            // Определение координаты Y (вверх или вниз от нулевой оси)
            int y = val >= 0 ? yZero - barHeight : yZero;
            int x = horizontalPadding + i * barWidth;
            int currentBarWidth = Math.max(5, barWidth - 4);

            // Цветовая индикация состояния
            if (currentState.getComparingIndices().contains(i)) {
                g2d.setColor(new Color(255, 99, 71)); 
            } else if (currentState.getSortedIndices().contains(i)) {
                g2d.setColor(new Color(60, 179, 113)); 
            } else {
                g2d.setColor(new Color(135, 206, 250)); 
            }

            g2d.fillRect(x, y, currentBarWidth, barHeight);

            g2d.setColor(new Color(0, 0, 139));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(x, y, currentBarWidth, barHeight);

            // Отрисовка числовых значений
            if (showValues) {
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
                String text = String.valueOf(val);
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(text);
                
                int textX = x + (currentBarWidth - textWidth) / 2;
                int textY = val >= 0 ? y - 5 : y + barHeight + fm.getAscent() + 2;
                
                g2d.drawString(text, textX, textY);
            }
        }
    }
}