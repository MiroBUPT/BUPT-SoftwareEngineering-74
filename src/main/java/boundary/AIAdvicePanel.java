package boundary;

/*public class AIAdvicePanel extends Panel {
    public AIAdvicePanel(Color border, Color fill) {
        super(border, fill);
    }

    @Override
    public void init() {
        System.out.println("AI建议面板初始化");
    }

    @Override
    public void updateData() {
        System.out.println("AI建议面板数据更新到最新数据");
    }
}*/

import javax.swing.*;
import java.awt.*;
import java.awt.Color;

public class AIAdvicePanel extends JPanel {
    public AIAdvicePanel(java.awt.Color borderColor, java.awt.Color fillColor) {
        setBorder(BorderFactory.createLineBorder(borderColor));
        setBackground(fillColor);
        setLayout(new BorderLayout());
        init();
    }

    private void init() {
        System.out.println("AI Advice 面板初始化");

        JLabel label = new JLabel("AI Advice");
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setHorizontalAlignment(JLabel.CENTER);
        add(label, BorderLayout.CENTER);
    }



    public static void main(String[] args) {
        JFrame frame = new JFrame("AI Advice Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new AIAdvicePanel(java.awt.Color.BLACK, Color.WHITE));
        frame.pack();
        frame.setSize(300, 200);
        frame.setVisible(true);
    }
}