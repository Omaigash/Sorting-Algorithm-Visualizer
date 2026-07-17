package ui;

import javax.swing.*;
import java.awt.*;

// Класс формирования модального окна разработчиков
public class DevsDialog extends JDialog {

    public DevsDialog(JFrame parent) {
        super(parent, "О разработчиках", true);
        setSize(320, 200);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Визуализатор алгоритмов v1.0");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel groupLabel = new JLabel("Группа: 4341");
        groupLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel dev1Label = new JLabel("Минебаев А.М. - UI/UX");
        dev1Label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel dev2Label = new JLabel("Пылаев С.Д. - Алгоритмы и состояния");
        dev2Label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel dev3Label = new JLabel("Омеляш Е.В. - Файловая система I/O");
        dev3Label.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(groupLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(dev1Label);
        panel.add(dev2Label);
        panel.add(dev3Label);

        add(panel);
    }
}
