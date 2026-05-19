package cartoon.model;

import java.awt.image.BufferedImage;

public class CartoonImage {
    private final BufferedImage image;

    public CartoonImage(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage image() {
        return image;
    }
}
