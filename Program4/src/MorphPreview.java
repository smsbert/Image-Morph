import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MorphPreview extends JFrame {
    private JFrame showMorph;
    private JButton morph, morphPreview, done;
    private JPanel  buttonPanel;
    private Font font = new Font("Arial", Font.PLAIN, 20); //Font for text

    MorphPreview() {

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,3,0,0));
        buttonPanel.setPreferredSize(new Dimension(500,100));
        showMorph = new JFrame("Morph");
        showMorph.setLayout(new BorderLayout());
        showMorph.setPreferredSize(new Dimension(500, 600));

        morph = new JButton("Morph");
        morph.setFont(font);

        morphPreview = new JButton("Morph Preview");

        done = new JButton("Done");
        done.addActionListener(new ActionListener() { // closes the popup menu
            @Override
            public void actionPerformed(ActionEvent e) {
                showMorph.dispose();
            }
        });
        done.setFont(font);

        morphPreview.setFont(font);
        buttonPanel.add(morph);
        buttonPanel.add(morphPreview);
        buttonPanel.add(done);
        showMorph.add(buttonPanel, BorderLayout.SOUTH);
        showMorph.setVisible(true);
        showMorph.pack();
    }
    public JFrame getShowMorph(){return showMorph;}

    public JButton getMorph() {
        return morph;
    }

    public JButton getMorphPreview() {
        return morphPreview;
    }
}
