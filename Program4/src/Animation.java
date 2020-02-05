import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Animation extends JPanel {

    Boolean testing;
    int tweenframes;
    ArrayList<TrianglePoints> start, end, current;
    TrianglePoints begtri, desttri, currtri;

    int currentstep;
    public Animation(ArrayList<TrianglePoints> starting, ArrayList<TrianglePoints> ending) {
        start = starting;
        end = ending;
        currentstep = 0;

        current = new ArrayList<>();

        testing = true;
        //current = start;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (currentstep == 0)
        {
            for (int i = 0; i < end.size(); i++) {
                start.get(i).fillTriangle(g);
            }
        }
        else {
            for (int i = 0; i < end.size(); i++) {
                current.get(i).fillTriangle(g);
            }
        }
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
        //clear out current triangles
        current.clear();
        //do this for every triangle in list
        for (int k = 0; k < end.size(); k++) {
            begtri = start.get(k);
            desttri = end.get(k);

            int[] begx = begtri.getXcoords();
            int[] begy = begtri.getYcoords();
            int[] finalx = desttri.getXcoords();
            int[] finaly = desttri.getYcoords();

            float[] currx = new float[3];
            float[] curry = new float[3];
            float[] xdiff = new float[3];
            float[] ydiff = new float[3];
            //determine distances
            for (int i = 0; i < 3; i++) {
                xdiff[i] = (finalx[i]-begx[i]);
                ydiff[i] = (finaly[i]-begy[i]);

            }

            //make new triangle
            for (int j = 0; j < 3; j++) {
                currx[j] = begx[j] + xdiff[j] * currentstep/tweenframes;
                curry[j] = begy[j] + ydiff[j] * currentstep/tweenframes;
            }
            //add triangle to current triangle list
            currtri = new TrianglePoints((int) currx[0], (int) curry[0], (int) currx[1], (int) curry[1], (int) currx[2], (int) curry[2]);

            current.add(currtri);
            repaint();

        }
    }
}
