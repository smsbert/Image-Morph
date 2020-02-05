import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class View extends JFrame {
    JMenuBar menuBar = new JMenuBar(); // Main menu bar
    private JMenu optionsMenu, file; // stores the options for the program such as settings, reset, and morph controls
    private JMenuItem setup, reset, previewMorph, quit, preImage, postImage; // menu items to execute functions
    private BufferedImage image;   // the image

    private JPanel brightnessOptions;


    private int gridSize;

    private Font font = new Font("Arial", Font.PLAIN, 20); // Font for the labels in the program


    View() {
        super("Morph View");
        quit = new JMenuItem("Quit"); // Exits the program
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        quit.setFont(font);
        optionsMenu = new JMenu("Options"); // menu of options for the program such as settings, morph control, and reset
        optionsMenu.setFont(font);
        setup = new JMenuItem("Setup"); // opens popup menu to customize gridsize
        setGridSize(10);

        setup.setFont(font);
        reset = new JMenuItem("Reset"); // resets the grid to the default size and default position of control points

        reset.setFont(font);
        previewMorph = new JMenuItem("Morph Options"); // Menu item will execute a morph preview showing control point movement
        previewMorph.setFont(font);

        file = new JMenu("File");
        file.setFont(font);
        preImage = new JMenuItem("Open preImage");

        preImage.setFont(font);
        postImage = new JMenuItem("Open postImage");

        postImage.setFont(font);

        // Brightness

        brightnessOptions = new JPanel();
        brightnessOptions.setLayout(new GridLayout(2, 4, 1, 1));

        // Brightness
       /* preBrightness = new JSlider(JSlider.HORIZONTAL, 0, 100, 15);
        preBrightnessLabel = new JLabel("Pre Image Brightness: 15");
        preBrightnessLabel.setFont(font);

        postBrightness = new JSlider(JSlider.HORIZONTAL, 0, 100, 15);
        postBrightnessLabel = new JLabel("Post Image Brightness: 15");
        postBrightnessLabel.setFont(font);*/

       /* brightnessOptions = new JPanel();
        brightnessOptions.setLayout(new GridLayout(2, 2, 1, 1));
        brightnessOptions.add(preBrightnessLabel);
        brightnessOptions.add(preBrightness);
        brightnessOptions.add(postBrightnessLabel);
        brightnessOptions.add(postBrightness);*/

        // Add pre and post file image openers
        file.add(preImage);
        file.add(postImage);

        // Add setup, reset, and morph menu to the options menu
        optionsMenu.add(file);
        optionsMenu.add(setup);
        optionsMenu.add(reset);
        optionsMenu.add(previewMorph);
        // Add the options menu and the quit control to the menu bar
        menuBar.add(optionsMenu);
        menuBar.add(quit);

        // Set the programs menu bar to be or main menu
       // setLayout(new GridLayout(2, 4, 1, 1));
        setJMenuBar(menuBar);
        setSize(new Dimension(1500, 1250));
        setVisible(true);
        repaint();
        revalidate();
    }
    // This method reads an Image object from a file indicated by
    // the string provided as the parameter.  The image is converted
    // here to a BufferedImage object, and that new object is the returned
    // value of this method.
    // The mediatracker in this method can throw an exception
    public BufferedImage readImage(String file) {

        Image image = Toolkit.getDefaultToolkit().getImage(file);
        MediaTracker tracker = new MediaTracker(new Component() {
        });
        tracker.addImage(image, 0);
        try {
            tracker.waitForID(0);
        } catch (InterruptedException e) {
        }
        BufferedImage bim = new BufferedImage
                (image.getWidth(this), image.getHeight(this),
                        BufferedImage.TYPE_INT_RGB);
        Graphics2D big = bim.createGraphics();
        big.drawImage(image, 0, 0, this);
        return bim;
    }

    public void setBrightnessOptions(JPanel brightnessOptions) {
        this.brightnessOptions = brightnessOptions;
    }

    public JPanel getBrightnessOptions() {
        return brightnessOptions;
    }

    public JMenuItem getSetup() {
        return setup;
    }

    public JMenuItem getReset() {
        return reset;
    }

    public JMenuItem getPreviewMorph() {
        return previewMorph;
    }

    public JMenuItem getPreImage() {
        return preImage;
    }

    public JMenuItem getPostImage() {
        return postImage;
    }

    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

}
