package cartoon.processing;

import cartoon.model.CartoonSettings;
import cartoon.model.EdgeMap;
import cartoon.model.GradientMap;
import cartoon.model.GrayImage;

public class EdgeDetector {
    private final AdaptiveThreshold threshold = new AdaptiveThreshold();

    public EdgeMap detect(GrayImage gray, CartoonSettings settings) {
        GradientMap gradient = sobel(gray);
        return threshold.apply(gradient, 8, settings.edgeDetail());
    }

    private GradientMap sobel(GrayImage gray) {
        int width = gray.width();
        int height = gray.height();
        GradientMap gradient = new GradientMap(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double topLeft = gray.sample(x - 1, y - 1);
                double top = gray.sample(x, y - 1);
                double topRight = gray.sample(x + 1, y - 1);
                double left = gray.sample(x - 1, y);
                double right = gray.sample(x + 1, y);
                double bottomLeft = gray.sample(x - 1, y + 1);
                double bottom = gray.sample(x, y + 1);
                double bottomRight = gray.sample(x + 1, y + 1);

                double gx = -topLeft + topRight - 2.0 * left + 2.0 * right - bottomLeft + bottomRight;
                double gy = topLeft + 2.0 * top + topRight - bottomLeft - 2.0 * bottom - bottomRight;
                gradient.set(x, y, Math.hypot(gx, gy));
            }
        }
        return gradient;
    }
}
