package cartoon.processing;

import cartoon.model.GrayImage;

import java.awt.image.BufferedImage;

public class GrayConverter {
    public GrayImage toGray(BufferedImage source) {
        int width = source.getWidth();
        int height = source.getHeight();
        GrayImage gray = new GrayImage(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = source.getRGB(x, y);
                int red = (argb >>> 16) & 0xFF;
                int green = (argb >>> 8) & 0xFF;
                int blue = argb & 0xFF;
                double luminance = 0.299 * red + 0.587 * green + 0.114 * blue;
                gray.set(x, y, luminance);
            }
        }
        return gray;
    }
}
