package boundary;

import javax.swing.border.Border;
import java.awt.*;

/**
 * Custom border implementation that creates an elliptical border around components.
 * This class implements the Border interface to provide a rounded, elliptical border
 * with customizable color and thickness.
 */
public class EllipseBorder implements Border {
    /** The color of the border */
    private Color color;
    /** The thickness of the border in pixels */
    private int thickness;

    /**
     * Constructs a new EllipseBorder with specified color and thickness.
     * @param color The color to use for the border
     * @param thickness The thickness of the border in pixels
     */
    public EllipseBorder(Color color, int thickness) {
        this.color = color;
        this.thickness = thickness;
    }

    /**
     * Paints the elliptical border around the specified component.
     * @param c The component to paint the border around
     * @param g The graphics context to use for painting
     * @param x The x coordinate of the border
     * @param y The y coordinate of the border
     * @param width The width of the border
     * @param height The height of the border
     */
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(thickness));
        g2d.drawOval(x + thickness / 2, y + thickness / 2, width - thickness, height - thickness);
    }

    /**
     * Gets the insets of the border.
     * @param c The component the border is being painted on
     * @return The insets of the border
     */
    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(thickness, thickness, thickness, thickness);
    }

    /**
     * Determines if the border is opaque.
     * @return false, as this border is not opaque
     */
    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}