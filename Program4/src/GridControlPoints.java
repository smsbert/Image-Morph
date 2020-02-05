import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;

public class GridControlPoints extends JPanel {


    int sizegrid;
    StartImage start, end;
    String startpic, endpic;
    int piclength, picwidth;
    View v;
    int currtween;
    int totaltween;
    BufferedImage startim, endim, image;
    BufferedImage currstart, currend;
    Animation animation;
    ActualMorph morphit;

    JSlider framesPerSecond, transitionTime;
    JLabel frameslabel, transitionTimeLabel;
    Boolean testing,animate,morph;
    int framespersec, totalsec, gridSize;

    int prevPostBrightness = 1, newPostBrightness, prevPreBrightness = 1, newPreBrightness,
            prevPostContrast = 1, newPostContrast, prevPreContrast = 1, newPreContrast;

    private JSlider preBrightness, postBrightness, preContrast,postContrast;
    private JButton preBrighten, preDim, postBrighten, postDim;
    private double preImageBrightness = 1, postImageBrightness = 1;
    private JLabel preBrightnessLabel, postBrightnessLabel, preContrastLabel, postContrastLabel;
    private JPanel brightnessOptions;
    private Font font = new Font("Arial", Font.PLAIN, 20); // Font for the labels in the program


    public GridControlPoints (int n, View v1)
    {
        //initialize variables
        v =v1;
        sizegrid = n;
        piclength = 500;
        picwidth = 500;
        startpic = "cat.jpg";
        framespersec = 30;

        totalsec = 2;

        currtween = 5000;
        totaltween = framespersec*totalsec;
        endpic = "tiger.jpg";

        //create JPanel
        //startim = v.getStartim();
        startim = readImage(startpic);

        currstart= new BufferedImage(startim.getWidth(), startim.getHeight(), startim.getType());
        Graphics g = currstart.getGraphics();
        g.drawImage(startim, 0, 0, null);
        g.dispose();
        start = new StartImage(n,startim);
        start.setPreferredSize(new Dimension(piclength, picwidth));

        //endim = v.getEndim();
        endim = readImage(endpic);
        currend= new BufferedImage(endim.getWidth(), endim.getHeight(), endim.getType());
        g = currend.getGraphics();
        g.drawImage(endim, 0, 0, null);
        g.dispose();



        end = new StartImage(n, currend);
        end.setPreferredSize(new Dimension(piclength, picwidth));


        //add to main Panel
        add(start, BorderLayout.EAST);
        add(end, BorderLayout.WEST);

        //paint
        start.repaint();
        end.repaint();

        //add dragging to pictures
        start.addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                start.startDrag(e.getX(), e.getY());
                int x = start.getDragrow();
                int y = start.getDragcol();
                end.setDragrow(x);
                end.setDragcol(y);
                end.repaint();
                end.validate();
                start.validate();
                repaint();
                validate();
            }
        });

        start.addMouseMotionListener(new MouseMotionAdapter()
        {
            public void mouseDragged(MouseEvent e)
            {
                start.doDrag(e.getX(), e.getY());

            }
        });

        end.addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                end.startDrag(e.getX(), e.getY());
                int x = end.getDragrow();
                int y = end.getDragcol();
                start.setDragrow(x);
                start.setDragcol(y);
                start.repaint();
                start.validate();
                end.validate();
                repaint();
                validate();
            }
        });
        end.addMouseMotionListener(new MouseMotionAdapter()
        {
            public void mouseDragged(MouseEvent e)
            {
                end.doDrag(e.getX(), e.getY());
            }
        });

        //get reset button
        JMenuItem reset = v.getReset();
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //preBrightness.setValue(5);
                //postBrightness.setValue(5);

                start.setup();
                startim = readImage(startpic);
                start.setBackground(startim);
                currstart = readImage(startpic);
                preImageBrightness = 1;
                start.repaint();
                start.revalidate();

                end.setup();
                endim = readImage(endpic);
                end.setBackground(endim);
                postImageBrightness = 1;
                currend = readImage(endpic);
                end.repaint();
                end.revalidate();
            }
        });

        //get setup button
        JMenuItem setup = v.getSetup();

        framesPerSecond = new JSlider(JSlider.HORIZONTAL, 10, 60, 30);
        frameslabel = new JLabel("Frames per second: 30");
        framesPerSecond.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                framesPerSecond.setValue(framesPerSecond.getValue());
                framespersec = framesPerSecond.getValue();
                frameslabel.setText("Frames per second: " + framesPerSecond.getValue());
            }
        });

        transitionTimeLabel = new JLabel("Transition Time: 10 seconds");
        transitionTime = new JSlider(JSlider.HORIZONTAL, 1 , 10, 10);
        transitionTime.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                transitionTime.setValue(transitionTime.getValue());
                transitionTimeLabel.setText("Transition Time: " + transitionTime.getValue() + " seconds");
                totalsec = transitionTime.getValue();
            }
        });

        preBrightnessLabel = new JLabel("Start Image Brightness:");
        preBrightnessLabel.setFont(font);
        preBrighten = new JButton("+");
        preBrighten.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                preImageBrightness = preImageBrightness + 0.2;
                RescaleOp rescaleBrightPostOp = new RescaleOp((float)preImageBrightness, 0, null);
                rescaleBrightPostOp.filter(startim,currstart);  // Source and destination are the same.
                start.setBackground(currstart);
                repaint();
            }
        });
        preBrighten.setFont(font);
        preDim = new JButton("-");
        preDim.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                preImageBrightness = preImageBrightness - 0.2;
                RescaleOp rescaleDimPostOp = new RescaleOp((float)preImageBrightness, 0, null);
                rescaleDimPostOp.filter(startim, currstart);  // Source and destination are the same.
                start.setBackground(currstart);
                repaint();
            }
        });
        preDim.setFont(font);

        postBrightnessLabel = new JLabel("End Image Brightness:");
        postBrightnessLabel.setFont(font);
        postBrighten = new JButton("+");
        postBrighten.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                postImageBrightness = postImageBrightness + 0.2;
                RescaleOp rescaleBrightPostOp = new RescaleOp((float)postImageBrightness, 0, null);
                rescaleBrightPostOp.filter(endim,currend);  // Source and destination are the same.
                end.setBackground(currend);
                repaint();
            }
        });
        postBrighten.setFont(font);
        postDim = new JButton("-");
        postDim.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                postImageBrightness = postImageBrightness - 0.2;
                RescaleOp rescaleDimPostOp = new RescaleOp((float)postImageBrightness, 0, null);
                rescaleDimPostOp.filter(endim, currend);  // Source and destination are the same.
                end.setBackground(currend);
                repaint();
            }
        });
        postDim.setFont(font);

        brightnessOptions = new JPanel();
        brightnessOptions.setLayout(new GridLayout(2,2, 1, 1));
        brightnessOptions.add(preBrightnessLabel);
        brightnessOptions.add(preBrighten);
        brightnessOptions.add(preDim);
        brightnessOptions.add(postBrightnessLabel);
        brightnessOptions.add(postBrighten);
        brightnessOptions.add(postDim);

        v.setBrightnessOptions(brightnessOptions);

        setup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetupOptions s = new SetupOptions(framesPerSecond,frameslabel, transitionTime, transitionTimeLabel, v);
                //dispose();
                totalsec = s.getTransitionTime();
               gridSize = s.getGridSize();
               v.setGridSize(gridSize);
            }
        });

        //get morph button
        JMenuItem previewMorph = v.getPreviewMorph();
        previewMorph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                MorphPreview s = new MorphPreview();
                JFrame frame = s.getShowMorph();

                //triangulate
                ArrayList<TrianglePoints> triangles = start.getTriangulation();
                ArrayList<TrianglePoints> ending = end.getTriangulation();
                animation = new Animation(triangles,ending);

                morphit = new ActualMorph(triangles, ending, currstart,currend);

                //DrawTriangle(start,end);
                //animation.setBackground(Color.RED);
                animation.setPreferredSize(new Dimension(500,500));
                frame.setSize(new Dimension(1000, 632));

                frame.add(animation, BorderLayout.WEST);
                frame.add(morphit, BorderLayout.CENTER);

                JButton previewmorph = s.getMorphPreview();
                previewmorph.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        currtween =0;
                        timer.stop();
                        timer.setDelay(1000/framespersec);
                        timer.start();
                        animate = true;
                    }
                });

                JButton actualmorph = s.getMorph();
                actualmorph.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        morphit.initializeMorph();
                        currtween = 0;
                        timer2.stop();
                        timer2.setDelay(1000/framespersec);
                        timer2.start();
                        testing = true;
                        morph = true;
                    }
                });
            }
        });
        JMenuItem preImage = v.getPreImage();
        preImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //startim = openFile();
                currstart = openFile();
                startim= new BufferedImage(currstart.getWidth(), currstart.getHeight(), currstart.getType());
                Graphics g = startim.getGraphics();
                g.drawImage(currstart, 0, 0, null);
                g.dispose();
                start.setBackground(currstart);
                start.repaint();
                start.revalidate();
            }
        });
        JMenuItem postImage = v.getPostImage();
        postImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endim =openFile();
                //currend = openFile();
                currend= new BufferedImage(endim.getWidth(), endim.getHeight(), endim.getType());
                Graphics g = currend.getGraphics();
                g.drawImage(endim, 0, 0, null);
                g.dispose();
                end.setBackground(endim);
                piclength = end.getWidth();
                picwidth = end.getHeight();
                end.repaint();
                end.revalidate();
            }
        });

        add(brightnessOptions);

    }



    // File opener function
    private BufferedImage openFile() {

        final JFileChooser fc = new JFileChooser(".");
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
                image = ImageIO.read(file);
            } catch (IOException e1) {
            };
        }
        return image;
    }
    //timer to show maze generation
    Timer timer = new Timer(1, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if(animate) {
                currtween++;
                totaltween = framespersec * totalsec;
                animation.showPreview(totaltween, currtween);
            }
            if(currtween >= totaltween)
            {
                animate = false;
            }
        }
    });

    Timer timer2 = new Timer(1, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if(morph)
            {
                currtween++;
                totaltween = framespersec*totalsec;
                //System.out.println(totaltween);
                morphit.showPreview(totaltween,currtween);
            }
            if(currtween >= totaltween)
                morph = false;

        }
    });



    public BufferedImage readImage (String file) {

        Image image = Toolkit.getDefaultToolkit().getImage(file);
        MediaTracker tracker = new MediaTracker (new Component () {});
        tracker.addImage(image, 0);
        try { tracker.waitForID (0); }
        catch (InterruptedException e) {}
        BufferedImage bim = new BufferedImage
                (image.getWidth(this), image.getHeight(this),
                        BufferedImage.TYPE_INT_RGB);
        Graphics2D big = bim.createGraphics();
        big.drawImage (image, 0, 0, this);
        return bim;
    }

    public JPanel getBrightnessOptions() {
        return brightnessOptions;
    }

    public void setStart(StartImage start) {
        this.start = start;
    }

    public void setEnd(StartImage end) {
        this.end = end;
    }

    public BufferedImage getStartim() {
        return startim;
    }

    public BufferedImage getEndim() {
        return endim;
    }


    public static void main(String[] argv) {
        View v = new View();
        v.setLayout(new BorderLayout());
        v.addWindowListener (
                new WindowAdapter () {
                    public void windowClosing ( WindowEvent e) {
                        System.exit(0);
                    }
                }
        );

        v.add(new GridControlPoints(v.getGridSize(), v), BorderLayout.NORTH);
        v.add(v.getBrightnessOptions(), BorderLayout.SOUTH);
        v.repaint();
        v.revalidate();
        v.pack();
    }

}
