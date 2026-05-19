package cartoon.processing;

import cartoon.model.GrayImage;

import java.awt.image.BufferedImage;

public class GaussianBlur {
    public BufferedImage apply(BufferedImage source, int radius) {
        if (radius <= 0) {
            return copy(source);
        }

        double[] kernel = kernel(radius);
        int width = source.getWidth();
        int height = source.getHeight();
        int[] input = source.getRGB(0, 0, width, height, null, 0, width);
        int[] horizontal = new int[input.length];
        int[] output = new int[input.length];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                horizontal[y * width + x] = blurPixel(input, width, height, x, y, kernel, true);
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                output[y * width + x] = blurPixel(horizontal, width, height, x, y, kernel, false);
            }
        }

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, width, height, output, 0, width);
        return image;
    }

    public GrayImage apply(GrayImage source, int radius) {
        if (radius <= 0) {
            return copy(source);
        }

        double[] kernel = kernel(radius);
        int width = source.width();
        int height = source.height();
        GrayImage horizontal = new GrayImage(width, height);
        GrayImage output = new GrayImage(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double value = 0.0;
                for (int k = -radius; k <= radius; k++) {
                    value += source.sample(x + k, y) * kernel[k + radius];
                }
                horizontal.set(x, y, value);
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double value = 0.0;
                for (int k = -radius; k <= radius; k++) {
                    value += horizontal.sample(x, y + k) * kernel[k + radius];
                }
                output.set(x, y, value);
            }
        }
        return output;
    }

    private int blurPixel(int[] pixels, int width, int height, int x, int y, double[] kernel, boolean horizontal) {
        int radius = kernel.length / 2;
        double alpha = 0.0;
        double red = 0.0;
        double green = 0.0;
        double blue = 0.0;

        for (int k = -radius; k <= radius; k++) {
            int sampleX = horizontal ? clamp(x + k, 0, width - 1) : x;
            int sampleY = horizontal ? y : clamp(y + k, 0, height - 1);
            int argb = pixels[sampleY * width + sampleX];
            double weight = kernel[k + radius];
            alpha += ((argb >>> 24) & 0xFF) * weight;
            red += ((argb >>> 16) & 0xFF) * weight;
            green += ((argb >>> 8) & 0xFF) * weight;
            blue += (argb & 0xFF) * weight;
        }

        return (clamp((int) Math.round(alpha), 0, 255) << 24)
                | (clamp((int) Math.round(red), 0, 255) << 16)
                | (clamp((int) Math.round(green), 0, 255) << 8)
                | clamp((int) Math.round(blue), 0, 255);
    }

    private double[] kernel(int radius) {
        double sigma = Math.max(0.8, radius / 2.0);
        double[] kernel = new double[radius * 2 + 1];
        double sum = 0.0;

        for (int i = -radius; i <= radius; i++) {
            double value = Math.exp(-(i * i) / (2.0 * sigma * sigma));
            kernel[i + radius] = value;
            sum += value;
        }

        for (int i = 0; i < kernel.length; i++) {
            kernel[i] /= sum;
        }
        return kernel;
    }

    private BufferedImage copy(BufferedImage source) {
        BufferedImage image = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, source.getWidth(), source.getHeight(),
                source.getRGB(0, 0, source.getWidth(), source.getHeight(), null, 0, source.getWidth()),
                0, source.getWidth());
        return image;
    }

    private GrayImage copy(GrayImage source) {
        GrayImage image = new GrayImage(source.width(), source.height());
        for (int y = 0; y < source.height(); y++) {
            for (int x = 0; x < source.width(); x++) {
                image.set(x, y, source.get(x, y));
            }
        }
        return image;
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
