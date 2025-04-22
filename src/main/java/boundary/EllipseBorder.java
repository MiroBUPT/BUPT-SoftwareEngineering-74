package main.java.boundary;

import javax.swing.border.Border;
import java.awt.*;

// 自定义椭圆形边框类
public class EllipseBorder implements Border {
    private Color color;
    private int thickness;

    // 构造函数，接收边框颜色和厚度
    public EllipseBorder(Color color, int thickness) {
        this.color = color;
        this.thickness = thickness;
    }

    // 绘制边框的方法
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(thickness));
        g2d.drawOval(x + thickness / 2, y + thickness / 2, width - thickness, height - thickness);
    }

    // 获取边框的内边距
    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(thickness, thickness, thickness, thickness);
    }

    // 判断边框是否不透明
    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}