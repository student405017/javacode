package cartoon.model;

public class EdgeMap {
    private final int width;
    private final int height;
    private final boolean[] edges;

    public EdgeMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.edges = new boolean[width * height];
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public boolean isEdge(int x, int y) {
        return edges[y * width + x];
    }

    public void setEdge(int x, int y, boolean edge) {
        edges[y * width + x] = edge;
    }
}
