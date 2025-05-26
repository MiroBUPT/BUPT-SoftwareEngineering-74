package boundary;


import javax.swing.*;
        import java.awt.*;
        import java.awt.Color;


/**
 * Abstract base class for all panels in the application.
 * This class provides common functionality for panel appearance and behavior.
 */
abstract class Panel extends JPanel {
    /** The color used for the panel's border */
    private java.awt.Color borderColor;
    /** The color used for the panel's background */
    private java.awt.Color fillColor;

    /**
     * Constructs a new Panel with specified border and background colors.
     * @param borderColor The color to use for the panel's border
     * @param fillColor The color to use for the panel's background
     */
    public Panel(java.awt.Color borderColor, java.awt.Color fillColor) {
        this.borderColor = borderColor;
        this.fillColor = fillColor;
        this.setBorder(BorderFactory.createLineBorder(borderColor));
        this.setBackground(fillColor);
    }

    /**
     * Gets the border color of this panel.
     * @return The color used for the panel's border
     */
    public java.awt.Color getBorderColor() {
        return borderColor;
    }

    /**
     * Gets the fill color of this panel.
     * @return The color used for the panel's background
     */
    public java.awt.Color getFillColor() {
        return fillColor;
    }

    /**
     * Initializes the panel.
     * This method should be implemented by subclasses to set up the panel's components.
     */
    public abstract void init(); // 初始化面板

    /**
     * Updates the panel's data.
     * This method should be implemented by subclasses to refresh the panel's content.
     */
    public abstract void updateData(); // 更新面板数据
}