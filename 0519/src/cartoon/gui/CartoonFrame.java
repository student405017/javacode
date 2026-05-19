package cartoon.gui;

import cartoon.io.ImageLoader;
import cartoon.model.CartoonImage;
import cartoon.model.CartoonSettings;
import cartoon.processing.CartoonRenderer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;

public class CartoonFrame extends JFrame {
    private final ImageLoader imageLoader = new ImageLoader();
    private final CartoonRenderer renderer = new CartoonRenderer();

    private final ImagePanel originalPanel = new ImagePanel("Open an image");
    private final ImagePanel previewPanel = new ImagePanel("Cartoon preview");
    private final JLabel statusLabel = new JLabel("Ready");
    private final JButton saveButton = new JButton("Save PNG");
    private final JSlider blurSlider = new JSlider(0, 6, 2);
    private final JSlider levelSlider = new JSlider(2, 12, 6);
    private final JSlider edgeSlider = new JSlider(0, 100, 45);
    private final JLabel blurValue = new JLabel();
    private final JLabel levelValue = new JLabel();
    private final JLabel edgeValue = new JLabel();
    private final Timer renderTimer;

    private BufferedImage originalImage;
    private CartoonImage currentCartoon;
    private File currentFile;
    private int renderVersion;

    public CartoonFrame() {
        super("Traditional Image Processing Cartoon Style App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(980, 640));
        setSize(1180, 760);
        setLocationRelativeTo(null);

        renderTimer = new Timer(180, event -> renderPreview());
        renderTimer.setRepeats(false);

        setContentPane(buildContent());
        bindActions();
        updateSliderLabels();
        loadSampleIfPresent();
    }

    private JPanel buildContent() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                wrapImagePanel("Original", originalPanel),
                wrapImagePanel("Cartoon", previewPanel));
        splitPane.setResizeWeight(0.5);
        splitPane.setContinuousLayout(true);

        root.add(buildToolbar(), BorderLayout.NORTH);
        root.add(splitPane, BorderLayout.CENTER);
        root.add(statusLabel, BorderLayout.SOUTH);
        return root;
    }

    private JPanel buildToolbar() {
        JButton openButton = new JButton("Open Image");
        openButton.setName("openButton");
        saveButton.setEnabled(false);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttons.add(openButton);
        buttons.add(saveButton);

        JPanel sliders = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 8, 0, 0);
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addSlider(sliders, gbc, "Blur", blurSlider, blurValue, 0);
        addSlider(sliders, gbc, "Colors", levelSlider, levelValue, 2);
        addSlider(sliders, gbc, "Edge detail", edgeSlider, edgeValue, 4);

        JPanel toolbar = new JPanel(new BorderLayout(12, 0));
        toolbar.add(buttons, BorderLayout.WEST);
        toolbar.add(sliders, BorderLayout.CENTER);

        openButton.addActionListener(event -> openImage());
        saveButton.addActionListener(event -> saveImage());
        return toolbar;
    }

    private void addSlider(JPanel panel, GridBagConstraints gbc, String label, JSlider slider, JLabel valueLabel, int column) {
        slider.setPaintTicks(false);
        slider.setSnapToTicks(false);

        gbc.gridx = column;
        gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = column + 1;
        gbc.weightx = 1;
        panel.add(slider, gbc);

        gbc.gridx = column + 2;
        gbc.weightx = 0;
        valueLabel.setPreferredSize(new Dimension(34, 22));
        panel.add(valueLabel, gbc);
    }

    private JPanel wrapImagePanel(String title, ImagePanel panel) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(BorderFactory.createTitledBorder(title));
        wrapper.add(panel, BorderLayout.CENTER);
        return wrapper;
    }

    private void bindActions() {
        blurSlider.addChangeListener(event -> queuePreview());
        levelSlider.addChangeListener(event -> queuePreview());
        edgeSlider.addChangeListener(event -> queuePreview());
    }

    private void openImage() {
        JFileChooser chooser = new JFileChooser(new File(".").getAbsoluteFile());
        chooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "bmp", "gif"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            loadImage(chooser.getSelectedFile());
        }
    }

    private void saveImage() {
        if (currentCartoon == null) {
            return;
        }

        JFileChooser chooser = new JFileChooser(new File(".").getAbsoluteFile());
        chooser.setSelectedFile(defaultOutputFile());
        chooser.setFileFilter(new FileNameExtensionFilter("PNG image", "png"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File output = ensurePngExtension(chooser.getSelectedFile());
        try {
            ImageIO.write(currentCartoon.image(), "png", output);
            statusLabel.setText("Saved " + output.getName());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Save failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private File defaultOutputFile() {
        String baseName = "cartoon-output";
        if (currentFile != null) {
            String name = currentFile.getName();
            int dot = name.lastIndexOf('.');
            baseName = dot > 0 ? name.substring(0, dot) + "-cartoon" : name + "-cartoon";
        }
        return new File(baseName + ".png");
    }

    private File ensurePngExtension(File file) {
        String name = file.getName().toLowerCase();
        if (name.endsWith(".png")) {
            return file;
        }
        return new File(file.getParentFile(), file.getName() + ".png");
    }

    private void loadSampleIfPresent() {
        File sample = new File("image.jpg");
        if (sample.isFile()) {
            loadImage(sample);
        }
    }

    private void loadImage(File file) {
        try {
            originalImage = imageLoader.load(file);
            currentFile = file;
            currentCartoon = null;
            originalPanel.setImage(originalImage);
            previewPanel.setImage(null);
            saveButton.setEnabled(false);
            setTitle("Traditional Cartoon App - " + file.getName());
            statusLabel.setText("Loaded " + file.getName());
            queuePreview();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Open failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void queuePreview() {
        updateSliderLabels();
        if (originalImage != null) {
            renderTimer.restart();
        }
    }

    private void updateSliderLabels() {
        blurValue.setText(Integer.toString(blurSlider.getValue()));
        levelValue.setText(Integer.toString(levelSlider.getValue()));
        edgeValue.setText(Integer.toString(edgeSlider.getValue()));
    }

    private CartoonSettings currentSettings() {
        return new CartoonSettings(blurSlider.getValue(), levelSlider.getValue(), edgeSlider.getValue());
    }

    private void renderPreview() {
        if (originalImage == null) {
            return;
        }

        int version = ++renderVersion;
        CartoonSettings settings = currentSettings();
        statusLabel.setText("Rendering...");

        SwingWorker<CartoonImage, Void> worker = new SwingWorker<>() {
            @Override
            protected CartoonImage doInBackground() {
                return renderer.render(originalImage, settings);
            }

            @Override
            protected void done() {
                if (version != renderVersion) {
                    return;
                }
                try {
                    currentCartoon = get();
                    previewPanel.setImage(currentCartoon.image());
                    saveButton.setEnabled(true);
                    statusLabel.setText("Preview updated");
                } catch (Exception ex) {
                    statusLabel.setText("Render failed");
                    JOptionPane.showMessageDialog(CartoonFrame.this, ex.getMessage(), "Render failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }
}
