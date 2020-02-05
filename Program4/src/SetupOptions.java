import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// Opens the popup menu to customize the grid
public class SetupOptions extends JFrame {

    private JFrame settings;
    private JSlider gridSize;
    private JLabel gridSizeLabel;
    private JButton done;
    private Font font = new Font("Arial", Font.PLAIN, 20); //Font for text
    private JLabel framesPerSecondLabel;
    private JSlider framesPerSecond;
    private JLabel transitionTimeLabel;
    private JSlider transitionTime;

    public SetupOptions(JSlider speedslider, JLabel speedlabel, JSlider timeslider, JLabel timelabel, View oldV) {

        settings = new JFrame("Settings"); // opens a popup for the setup options
        settings.setLayout(new GridLayout(4, 2, 2, 2));
        gridSizeLabel = new JLabel("Grid Size: 10 x 10");
        gridSizeLabel.setFont(font);
        gridSize = new JSlider(JSlider.HORIZONTAL, 5, 20, 10);
        gridSize.addChangeListener(new ChangeListener() { // allows the user to customize the grid size
            @Override
            public void stateChanged(ChangeEvent e) {
                //call a change speed timer to switch it when it gets switched
                gridSize.setValue(gridSize.getValue());
                gridSizeLabel.setText("Grid Size: " + gridSize.getValue() + " x " + gridSize.getValue());
            }
        });
        framesPerSecondLabel = speedlabel;
        framesPerSecond = speedslider;
        framesPerSecondLabel.setFont(font);

        transitionTime = timeslider;
        transitionTimeLabel = timelabel;
        transitionTimeLabel.setFont(font);
        done = new JButton("Done");
        done.addActionListener(new ActionListener() { // closes the popup menu
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.dispose();
                oldV.dispose();
                View v = new View();
                v.setLayout(new BorderLayout());
                v.addWindowListener (
                        new WindowAdapter() {
                            public void windowClosing ( WindowEvent e) {
                                System.exit(0);
                            }
                        }
                );
                v.add(new GridControlPoints(gridSize.getValue(), v), BorderLayout.NORTH);
                v.add(v.getBrightnessOptions(), BorderLayout.SOUTH);
                v.repaint();
                v.revalidate();
                v.pack();
            }
        });
        done.setFont(font);
        // ADD everything to the popup menu display
        settings.add(gridSizeLabel);
        settings.add(gridSize);
        settings.add(framesPerSecondLabel);
        settings.add(framesPerSecond);
        settings.add(transitionTimeLabel);
        settings.add(transitionTime);
        settings.add(done);
        settings.setVisible(true);
        settings.pack();
    }

    public int getGridSize() {
        return gridSize.getValue();
    }

    public int getTransitionTime() {
        return transitionTime.getValue();
    }
}