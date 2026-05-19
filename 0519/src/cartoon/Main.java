package cartoon;

import cartoon.gui.CartoonFrame;
import cartoon.io.ImageLoader;
import cartoon.model.CartoonImage;
import cartoon.model.CartoonSettings;
import cartoon.processing.CartoonRenderer;

import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        if (args.length >= 2) {
            runBatchExport(args);
            return;
        }

        if (GraphicsEnvironment.isHeadless()) {
            System.err.println("A display is required for the Swing app.");
            System.err.println("Batch mode: java -cp out cartoon.Main input.jpg output.png [blurRadius] [colorLevels] [edgeDetail]");
            return;
        }

        SwingUtilities.invokeLater(() -> {
            try {
                CartoonFrame frame = new CartoonFrame();
                frame.setVisible(true);
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Cartoon App", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private static void runBatchExport(String[] args) {
        File inputFile = new File(args[0]);
        File outputFile = new File(args[1]);
        int blurRadius = parseOrDefault(args, 2, 2);
        int colorLevels = parseOrDefault(args, 3, 6);
        int edgeDetail = parseOrDefault(args, 4, 45);

        try {
            BufferedImage input = new ImageLoader().load(inputFile);
            CartoonSettings settings = new CartoonSettings(blurRadius, colorLevels, edgeDetail);
            CartoonImage cartoon = new CartoonRenderer().render(input, settings);
            ImageIO.write(cartoon.image(), "png", outputFile);
            System.out.println("Saved cartoon image to " + outputFile.getAbsolutePath());
        } catch (IOException ex) {
            System.err.println("Could not export image: " + ex.getMessage());
        }
    }

    private static int parseOrDefault(String[] args, int index, int defaultValue) {
        if (index >= args.length) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(args[index]);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }
}
