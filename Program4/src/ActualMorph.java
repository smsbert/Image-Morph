import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ActualMorph extends JPanel {

    Boolean testing;
    int tweenframes;
    ArrayList<TrianglePoints> start, end, current, rev, previoustri;
    TrianglePoints begtri, desttri, currtri, revcurrtri, endBegTri, endDestTri;
    MorphTools morph;

    BufferedImage src;
    BufferedImage dest;
    BufferedImage curr;
    BufferedImage revCurr;
    BufferedImage previous;
    BufferedImage display;

    int currentstep;
    public ActualMorph(ArrayList<TrianglePoints> starting,
                       ArrayList<TrianglePoints> ending,
                       BufferedImage beg,
                       BufferedImage endall) {
        start = starting;
        end = ending;

        previoustri = new ArrayList<>();
        for(int i = 0; i < start.size(); i++)
            previoustri.add(start.get(i));
        src = beg;
        dest = endall;
        initializeMorph();
    }

    public void initializeMorph()
    {
        currentstep = 0;

        morph = new MorphTools();

        BufferedImage b = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
        Graphics g = b.getGraphics();
        g.drawImage(src, 0, 0, null);
        g.dispose();
        curr = b;

        BufferedImage e = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
        g = e.getGraphics();
        g.drawImage(src, 0, 0, null);
        g.dispose();
        display = e;

        BufferedImage a = new BufferedImage(dest.getWidth(), dest.getHeight(), dest.getType());
        g = a.getGraphics();
        g.drawImage(dest, 0, 0, null);
        g.dispose();
        revCurr = a;

        BufferedImage c = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
        g = c.getGraphics();
        g.drawImage(src, 0, 0, null);
        g.dispose();
        previous = c;

        //curr = readImage("stapler.jpg");
        current = new ArrayList<>();
        for(int i = 0; i < start.size(); i++)
            current.add(start.get(i));

        rev = new ArrayList<>();
        for(int i = 0; i < end.size(); i++)
            rev.add(end.get(i));

        testing = true;
        //current = start;
        repaint();
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);

            String name = "image" + currentstep + ".jpeg";
            File outputfile = new File(name);
            try {
                ImageIO.write(curr, "jpeg", outputfile);
            } catch (IOException e) {
                e.printStackTrace();
            }

        g.drawImage(curr,0,0,null);
    }

    public void showPreview(int steps, int currstep) {
        tweenframes=steps;
        if(currstep <= tweenframes) {
            currentstep = currstep;
            stepforPreview(currstep);
        }
    }

    public void stepforPreview(int currentstep)
    {
        previoustri.clear();
        for(int i = 0; i < current.size(); i++)
            previoustri.add(current.get(i));

        //clear out current triangles
        current.clear();
        rev.clear();

        //do this for every triangle in list
        for (int k = 0; k < start.size(); k++) {
            begtri = start.get(k);
            desttri = end.get(k);

            endBegTri = end.get(k);
            endDestTri = start.get(k);

            int[] begx = begtri.getXcoords();
            int[] begy = begtri.getYcoords();
            int[] finalx = desttri.getXcoords();
            int[] finaly = desttri.getYcoords();

            int[] endBegx = endBegTri.getXcoords();
            int[] endBegy = endBegTri.getYcoords();
            int[] endFinalx = endDestTri.getXcoords();
            int[] endFinaly = endDestTri.getYcoords();

            float[] currx = new float[3];
            float[] curry = new float[3];
            float[] xdiff = new float[3];
            float[] ydiff = new float[3];

            float[] revcurrx = new float[3];
            float[] revcurry = new float[3];
            float[] revxdiff = new float[3];
            float[] revydiff = new float[3];

            //determine distances
            for (int i = 0; i < 3; i++) {
                xdiff[i] = (finalx[i]-begx[i]);
                ydiff[i] = (finaly[i]-begy[i]);

                revxdiff[i] = (endFinalx[i] - endBegx[i]);
                revydiff[i] = (endFinaly[i] - endBegy[i]);
            }

            //make new triangle
            for (int j = 0; j < 3; j++) {
                currx[j] = begx[j] + xdiff[j] * currentstep/tweenframes;
                curry[j] = begy[j] + ydiff[j] * currentstep/tweenframes;

                revcurrx[j] = endBegx[j] + revxdiff[j] * (tweenframes - currentstep)/tweenframes;
                revcurry[j] = endBegy[j] + revydiff[j] * (tweenframes - currentstep)/tweenframes;
            }

            currtri = new TrianglePoints((int) currx[0], (int) curry[0], (int) currx[1], (int) curry[1], (int) currx[2], (int) curry[2]);
            revcurrtri = new TrianglePoints((int) revcurrx[0], (int)revcurry[0],(int) revcurrx[1], (int)revcurry[1], (int) revcurrx[2], (int)revcurry[2]);

            current.add(currtri);
            rev.add(revcurrtri);

            //morph.warpTriangle(previous, curr, rev.get(k), current.get(k), null, null );
            morph.warpTriangle(src, curr, start.get(k), current.get(k), null, null);
            morph.warpTriangle(dest, revCurr, end.get(k), rev.get(k),null,null);

            BufferedImage b = new BufferedImage(curr.getWidth(), curr.getHeight(), curr.getType());
            Graphics g = b.getGraphics();
            g.drawImage(curr, 0, 0, null);
            g.dispose();
            previous = b;
        }

        for(int i = 0; i< 500; i++ ) {
            for (int j = 0; j < 500; j++) {

                int intensitystart = curr.getRGB(i, j);
                Color startcolor = new Color(intensitystart);
                int red = startcolor.getRed();
                int blue = startcolor.getBlue();
                int green = startcolor.getGreen();

                int intensityend = revCurr.getRGB(i, j);
                Color endcolor = new Color(intensityend);
                int red2 = endcolor.getRed();
                int blue2 = endcolor.getBlue();
                int green2 = endcolor.getGreen();
                //difference
                int currred = (red*(tweenframes - currentstep) + red2*(currentstep))/(tweenframes);
                int currblue = (blue*(tweenframes - currentstep) + blue2*(currentstep))/(tweenframes);
                int currgreen = (green*(tweenframes - currentstep) + green2*(currentstep))/(tweenframes);

                Color currcolor = new Color(currred, currgreen, currblue);
                curr.setRGB(i, j, currcolor.getRGB());
                repaint();
            }
        }
    }

}
