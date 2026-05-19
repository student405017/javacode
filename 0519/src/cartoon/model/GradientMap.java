package cartoon.model;

public class GradientMap {
    private final int width;
    private final int height;
    private final double[] values;

    public GradientMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.values = new double[width * height];
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public double get(int x, int y) {
        return values[y * width + x];
    }

    public void set(int x, int y, double value) {
        values[y * width + x] = value;
    }
}
