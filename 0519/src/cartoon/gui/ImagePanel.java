package cartoon.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
    private final String emptyText;
    private BufferedImage image;

    public ImagePanel(String emptyText) {
        this.emptyText = emptyText;
        setBackground(new Color(36, 39, 43));
        setPreferredSize(new Dimension(460, 420));
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        if (image == null) {
            drawEmptyState(g);
        } else {
            drawImage(g);
        }
        g.dispose();
    }

    private void drawImage(Graphics2D g) {
        int width = getWidth();
        int height = getHeight();
        double scale = Math.min(width / (double) image.getWidth(), height / (double) image.getHeight());
        int drawWidth = Math.max(1, (int) Math.round(image.getWidth() * scale));
        int drawHeight = Math.max(1, (int) Math.round(image.getHeight() * scale));
        int x = (width - drawWidth) / 2;
        int y = (height - drawHeight) / 2;
        g.drawImage(image, x, y, drawWidth, drawHeight, null);
    }

    private void drawEmptyState(Graphics2D g) {
        g.setColor(new Color(215, 218, 222));
        FontMetrics metrics = g.getFontMetrics();
        int x = (getWidth() - metrics.stringWidth(emptyText)) / 2;
        int y = (getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();
        g.drawString(emptyText, Math.max(12, x), y);
    }
}
