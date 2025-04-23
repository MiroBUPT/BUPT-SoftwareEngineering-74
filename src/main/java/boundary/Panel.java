package boundary;


import javax.swing.*;
        import java.awt.*;
        import java.awt.Color;


abstract class Panel extends JPanel {
    private java.awt.Color borderColor;
    private java.awt.Color fillColor;

    public Panel(java.awt.Color borderColor, java.awt.Color fillColor) {
        this.borderColor = borderColor;
        this.fillColor = fillColor;
        this.setBorder(BorderFactory.createLineBorder(borderColor));
        this.setBackground(fillColor);
    }

    public java.awt.Color getBorderColor() {
        return borderColor;
    }

    public java.awt.Color getFillColor() {
        return fillColor;
    }

    public abstract void init(); // 初始化面板
    public abstract void updateData(); // 更新面板数据
}