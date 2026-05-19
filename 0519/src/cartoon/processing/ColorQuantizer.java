package cartoon.processing;

import java.awt.image.BufferedImage;

public class ColorQuantizer {
    public BufferedImage quantize(BufferedImage source, int levels) {
        int width = source.getWidth();
        int height = source.getHeight();
        int[] pixels = source.getRGB(0, 0, width, height, null, 0, width);
        int[] output = new int[pixels.length];
        double step = 255.0 / Math.max(1, levels - 1);

        for (int i = 0; i < pixels.length; i++) {
            int argb = pixels[i];
            int alpha = (argb >>> 24) & 0xFF;
            int red = quantizeChannel((argb >>> 16) & 0xFF, step);
            int green = quantizeChannel((argb >>> 8) & 0xFF, step);
            int blue = quantizeChannel(argb & 0xFF, step);
            output[i] = (alpha << 24) | (red << 16) | (green << 8) | blue;
        }

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, width, height, output, 0, width);
        return image;
    }

    private int quantizeChannel(int value, double step) {
        return clamp((int) Math.round(Math.round(value / step) * step));
    }

    private int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }
}
