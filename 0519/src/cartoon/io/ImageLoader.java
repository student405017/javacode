package cartoon.io;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageLoader {
    public BufferedImage load(File file) throws IOException {
        if (file == null || !file.isFile()) {
            throw new IOException("Image file not found.");
        }

        BufferedImage image = ImageIO.read(file);
        if (image == null) {
            throw new IOException("Unsupported or unreadable image: " + file.getName());
        }
        return toArgb(image);
    }

    private BufferedImage toArgb(BufferedImage source) {
        BufferedImage converted = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = converted.createGraphics();
        graphics.drawImage(source, 0, 0, null);
        graphics.dispose();
        return converted;
    }
}
