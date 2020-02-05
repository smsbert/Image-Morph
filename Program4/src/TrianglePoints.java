import javax.swing.*;
import java.awt.*;

public class TrianglePoints extends JPanel {

    private int x1, y1, x2, y2, x3, y3;
    int[] xcoords, ycoords;

    public TrianglePoints(int x, int y, int xp, int yp, int xpp, int ypp)
    {
        x1 = x;
        y1 = y;
        x2 = xp;
        y2 = yp;
        x3 = xpp;
        y3 = ypp;

        xcoords = new int[3];
        xcoords[0] = x1;
        xcoords[1] = x2;
        xcoords[2] = x3;

        ycoords = new int[3];
        ycoords[0] = y1;
        ycoords[1] = y2;
        ycoords[2] = y3;
    }

    public void fillTriangle(Graphics g)
    {
        g.setColor(Color.RED);
        g.drawPolygon(xcoords, ycoords, 3);
        //g.fillPolygon(xcoords, ycoords, 3);
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);

    }

    public int[] getXcoords() {
        return xcoords;
    }

    public int getX1(){return x1;}

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public int getX3() {
        return x3;
    }

    public void setX3(int x3) {
        this.x3 = x3;
    }

    public int getY3() {
        return y3;
    }

    public void setY3(int y3) {
        this.y3 = y3;
    }

    public int[] getYcoords() {
        return ycoords;
    }

}
