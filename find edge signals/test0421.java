import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class test0421 {
    public static void main(String[] args) {
        // args[0]: input image
        // args[1]: horizontal edge output (Ix)
        // args[2]: vertical edge output (Iy)
        String inputPath = args.length > 0
                ? args[0]
                : "250px-Siberian_Tiger_by_Malene_Th.jpg";
        String horizontalOutputPath = args.length > 1
                ? args[1]
                : "horizontal_edge_signal.png";
        String verticalOutputPath = args.length > 2
                ? args[2]
                : "vertical_edge_signal.png";

        try {
            File inputFile = new File(inputPath);
            if (!inputFile.exists()) {
                System.out.println("Input image not found: " + inputPath);
                System.out.println(
                        "Usage: java test0421 <input-image> <horizontal-output> <vertical-output>");
                return;
            }

            BufferedImage inputImage = ImageIO.read(inputFile);
            if (inputImage == null) {
                System.out.println("Cannot read image: " + inputPath);
                return;
            }

            int[][] gray = toGrayMatrix(inputImage);
            BufferedImage horizontalImage = findHorizontalEdgeSignal(gray);
            BufferedImage verticalImage = findVerticalEdgeSignal(gray);

            ImageIO.write(horizontalImage, "png", new File(horizontalOutputPath));
            ImageIO.write(verticalImage, "png", new File(verticalOutputPath));

            System.out.println("Horizontal edge image saved to: " + horizontalOutputPath);
            System.out.println("Vertical edge image saved to: " + verticalOutputPath);
        } catch (IOException e) {
            System.out.println("Image processing failed: " + e.getMessage());
        }
    }

    private static int[][] toGrayMatrix(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] gray = new int[height][width];

        // Convert the color image to grayscale first.
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = rgb & 0xff;
                gray[y][x] = (r + g + b) / 3;
            }
        }

        return gray;
    }

    private static BufferedImage findHorizontalEdgeSignal(int[][] gray) {
        int height = gray.length;
        int width = gray[0].length;
        BufferedImage result = createGrayBaseImage(width, height);

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                // Ix(x, y) = (f(x + 1, y) - f(x - 1, y)) / 2
                int ix = (gray[y][x + 1] - gray[y][x - 1]) / 2;
                result.setRGB(x, y, toGrayRgb(clamp(128 + ix)));
            }
        }

        return result;
    }

    private static BufferedImage findVerticalEdgeSignal(int[][] gray) {
        int height = gray.length;
        int width = gray[0].length;
        BufferedImage result = createGrayBaseImage(width, height);

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                // Iy(x, y) = (f(x, y + 1) - f(x, y - 1)) / 2
                int iy = (gray[y + 1][x] - gray[y - 1][x]) / 2;
                result.setRGB(x, y, toGrayRgb(clamp(128 + iy)));
            }
        }

        return result;
    }

    private static BufferedImage createGrayBaseImage(int width, int height) {
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        // Fill with middle gray so positive and negative signals are both visible.
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                result.setRGB(x, y, toGrayRgb(128));
            }
        }

        return result;
    }

    private static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }

    private static int toGrayRgb(int gray) {
        return (gray << 16) | (gray << 8) | gray;
    }
}
