package cartoon.processing;

import cartoon.model.CartoonImage;
import cartoon.model.CartoonSettings;
import cartoon.model.EdgeMap;
import cartoon.model.GrayImage;

import java.awt.image.BufferedImage;

public class CartoonRenderer {
    private final GaussianBlur blur = new GaussianBlur();
    private final GrayConverter grayConverter = new GrayConverter();
    private final EdgeDetector edgeDetector = new EdgeDetector();
    private final ColorQuantizer colorQuantizer = new ColorQuantizer();

    public CartoonImage render(BufferedImage input, CartoonSettings settings) {
        BufferedImage smoothed = blur.apply(input, settings.blurRadius());
        GrayImage gray = grayConverter.toGray(smoothed);
        EdgeMap edges = edgeDetector.detect(gray, settings);
        BufferedImage quantized = colorQuantizer.quantize(smoothed, settings.colorLevels());

        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < output.getHeight(); y++) {
            for (int x = 0; x < output.getWidth(); x++) {
                int rgb = edges.isEdge(x, y) ? 0xFF111111 : quantized.getRGB(x, y);
                output.setRGB(x, y, rgb);
            }
        }
        return new CartoonImage(output);
    }
}
