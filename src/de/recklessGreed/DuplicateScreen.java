package de.recklessGreed;

import javax.swing.*;

public class DuplicateScreen {
    private JPanel rootPane;
    private JTextField leftField;
    private JTextField rightField;
    private JLabel header;
    private JScrollPane leftScrollPane;
    private JScrollPane rightScrollPane;
    private JTextArea leftPane;
    private JTextArea rightPane;

    public DuplicateScreen(x68_File left, x68_File right, double cosDist) {
        JFrame frame = new JFrame("DuplicateScreen");
        frame.setSize(800,600);
        double perc = (cosDist -1) * -100;
        header.setText("CosineDistance: " + perc + " %");
        leftField.setText(left.getName());
        rightField.setText(right.getName());
        leftPane.setText(left.getText());
        rightPane.setText(right.getText());
        frame.setContentPane(rootPane);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
