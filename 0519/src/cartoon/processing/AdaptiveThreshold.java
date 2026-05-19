package cartoon.processing;

import cartoon.model.EdgeMap;
import cartoon.model.GradientMap;

public class AdaptiveThreshold {
    public EdgeMap apply(GradientMap gradient, int radius, int edgeDetail) {
        int width = gradient.width();
        int height = gradient.height();
        double[] integral = buildIntegralImage(gradient);
        EdgeMap edgeMap = new EdgeMap(width, height);
        double offset = 65.0 - edgeDetail;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double localMean = mean(integral, width, height, x, y, radius);
                double threshold = Math.max(18.0, localMean + offset);
                edgeMap.setEdge(x, y, gradient.get(x, y) > threshold);
            }
        }
        return edgeMap;
    }

    private double[] buildIntegralImage(GradientMap gradient) {
        int width = gradient.width();
        int height = gradient.height();
        double[] integral = new double[(width + 1) * (height + 1)];

        for (int y = 1; y <= height; y++) {
            double rowSum = 0.0;
            for (int x = 1; x <= width; x++) {
                rowSum += gradient.get(x - 1, y - 1);
                int index = y * (width + 1) + x;
                integral[index] = integral[index - (width + 1)] + rowSum;
            }
        }
        return integral;
    }

    private double mean(double[] integral, int width, int height, int centerX, int centerY, int radius) {
        int left = Math.max(0, centerX - radius);
        int right = Math.min(width - 1, centerX + radius);
        int top = Math.max(0, centerY - radius);
        int bottom = Math.min(height - 1, centerY + radius);
        int stride = width + 1;

        int x1 = left;
        int x2 = right + 1;
        int y1 = top;
        int y2 = bottom + 1;
        double sum = integral[y2 * stride + x2]
                - integral[y1 * stride + x2]
                - integral[y2 * stride + x1]
                + integral[y1 * stride + x1];
        int area = (right - left + 1) * (bottom - top + 1);
        return sum / area;
    }
}
