import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayDeque;
import java.util.Queue;
import javax.imageio.ImageIO;

public class MultiThresholdSegmentation {
    private static final int T1 = 85;
    private static final int T2 = 150;
    private static final int[] DX = {1, -1, 0, 0};
    private static final int[] DY = {0, 0, 1, -1};
    private static final Color[] CLASS_COLORS = {
            new Color(49, 130, 189),
            new Color(65, 171, 93),
            new Color(244, 180, 0)
    };

    public static void main(String[] args) {
        try {
            File input = args.length > 0
                    ? new File(args[0])
                    : new File("article-5e2002f2a1ffb.jpg");

            BufferedImage image = ImageIO.read(input);
            if (image == null) {
                throw new IllegalArgumentException("Cannot read image: " + input.getAbsolutePath());
            }

            int width = image.getWidth();
            int height = image.getHeight();
            int[][] classes = new int[height][width];
            BufferedImage thresholdResult = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Color color = new Color(image.getRGB(x, y));
                    int gray = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                    int classId = getClassId(gray);

                    classes[y][x] = classId;
                    thresholdResult.setRGB(x, y, getThresholdColor(classId).getRGB());
                }
            }

            BufferedImage bfsResult = labelConnectedComponentsWithBfs(classes, width, height);

            BufferedImage compareResult = createCompareImage(thresholdResult, bfsResult, width, height);

            ImageIO.write(thresholdResult, "jpg", new File("segmented_threshold_color.jpg"));
            ImageIO.write(bfsResult, "jpg", new File("segmented_bfs.jpg"));
            ImageIO.write(compareResult, "jpg", new File("segmented_compare.jpg"));
            System.out.println("Done. Output: segmented_threshold_color.jpg, segmented_bfs.jpg, segmented_compare.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getClassId(int gray) {
        if (gray < T1) {
            return 0;
        }
        if (gray < T2) {
            return 1;
        }
        return 2;
    }

    private static Color getThresholdColor(int classId) {
        return CLASS_COLORS[classId];
    }

    private static BufferedImage labelConnectedComponentsWithBfs(int[][] classes, int width, int height) {
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        boolean[][] visited = new boolean[height][width];
        int componentId = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (!visited[y][x]) {
                    componentId++;
                    bfs(classes, visited, result, x, y, componentId, width, height);
                }
            }
        }

        System.out.println("BFS connected components: " + componentId);
        return result;
    }

    private static void bfs(
            int[][] classes,
            boolean[][] visited,
            BufferedImage result,
            int startX,
            int startY,
            int componentId,
            int width,
            int height) {
        int classId = classes[startY][startX];
        int rgb = getComponentColor(componentId, classId).getRGB();
        Queue<int[]> queue = new ArrayDeque<>();

        visited[startY][startX] = true;
        queue.add(new int[]{startX, startY});

        while (!queue.isEmpty()) {
            int[] point = queue.poll();
            int x = point[0];
            int y = point[1];
            result.setRGB(x, y, rgb);

            for (int i = 0; i < DX.length; i++) {
                int nx = x + DX[i];
                int ny = y + DY[i];

                if (isInside(nx, ny, width, height)
                        && !visited[ny][nx]
                        && classes[ny][nx] == classId) {
                    visited[ny][nx] = true;
                    queue.add(new int[]{nx, ny});
                }
            }
        }
    }

    private static boolean isInside(int x, int y, int width, int height) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    private static Color getComponentColor(int componentId, int classId) {
        float hue = (float) ((componentId * 0.61803398875 + classId * 0.13) % 1.0);
        float saturation = classId == 1 ? 0.55f : 0.85f;
        float brightness = classId == 0 ? 0.65f : 0.95f;
        return Color.getHSBColor(hue, saturation, brightness);
    }

    private static BufferedImage createCompareImage(
            BufferedImage thresholdResult,
            BufferedImage bfsResult,
            int width,
            int height) {
        BufferedImage compare = new BufferedImage(width * 2, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                compare.setRGB(x, y, thresholdResult.getRGB(x, y));
                compare.setRGB(x + width, y, bfsResult.getRGB(x, y));
            }
        }

        return compare;
    }
}
