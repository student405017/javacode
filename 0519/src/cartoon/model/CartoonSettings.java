package cartoon.model;

public class CartoonSettings {
    private final int blurRadius;
    private final int colorLevels;
    private final int edgeDetail;

    public CartoonSettings(int blurRadius, int colorLevels, int edgeDetail) {
        this.blurRadius = clamp(blurRadius, 0, 8);
        this.colorLevels = clamp(colorLevels, 2, 24);
        this.edgeDetail = clamp(edgeDetail, 0, 100);
    }

    public int blurRadius() {
        return blurRadius;
    }

    public int colorLevels() {
        return colorLevels;
    }

    public int edgeDetail() {
        return edgeDetail;
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
