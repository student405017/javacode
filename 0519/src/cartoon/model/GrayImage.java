package cartoon.model;

public class GrayImage {
    private final int width;
    private final int height;
    private final double[] pixels;

    public GrayImage(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new double[width * height];
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public double get(int x, int y) {
        return pixels[y * width + x];
    }

    public double sample(int x, int y) {
        int clampedX = Math.max(0, Math.min(width - 1, x));
        int clampedY = Math.max(0, Math.min(height - 1, y));
        return get(clampedX, clampedY);
    }

    public void set(int x, int y, double value) {
        pixels[y * width + x] = value;
    }
}
