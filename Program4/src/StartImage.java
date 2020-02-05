import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class StartImage extends JPanel {

    BufferedImage background;
    int sizegrid;
    GridPoint[][] grid;

    //draggable handles
    Rectangle handles[][];

    ArrayList<TrianglePoints> triangulation;
    int piclength, picwidth;

    int dragrow, dragcol;

    public StartImage(int n, BufferedImage pic) {

        //initialize variables
        sizegrid = n;
        piclength = 500;
        picwidth = 500;
        handles = new Rectangle[n][n];
        triangulation = new ArrayList<>();
        //set background pic

        background = pic;
        setup();
    }
    public void setup()
    {
        //make points
        //make grid of points
        int x, y;
        grid = new GridPoint[sizegrid][sizegrid];
        for(int i = 0; i< sizegrid; i++)
        {
            for(int j = 0; j< sizegrid; j++)
            {
                x = (j+1) * piclength/(sizegrid+1);
                y = (i+1) * picwidth/(sizegrid+1);
                grid[i][j] = new GridPoint(x,y);

                //draw handle at that point
                handles[i][j] = new Rectangle(x-5, y-5, 10, 10);

                //add all neighbors

            }
        }
        for(int i = 0; i< sizegrid; i++)
        {
            for(int j = 0; j< sizegrid; j++)
            {
                //find points on wall related to point
                GridPoint northwest = new GridPoint(j*piclength/(sizegrid + 1),(i)*picwidth/(sizegrid+1));
                GridPoint west = new GridPoint(j*piclength/(sizegrid + 1), (i+1)*picwidth/(sizegrid+1));
                GridPoint north = new GridPoint((j+1)*piclength/(sizegrid+1), (i)*picwidth/(sizegrid + 1));
                GridPoint northeast = new GridPoint((j+2)*piclength/(sizegrid+1), (i)*picwidth/(sizegrid+1));
                GridPoint southeast = new GridPoint((j+2)*piclength/(sizegrid+1), (i+2)*picwidth/(sizegrid+1));
                GridPoint south = new GridPoint((j+1)*piclength/(sizegrid+1), (i+2)*picwidth/(sizegrid+1));
                GridPoint southwest = new GridPoint((j)*piclength/(sizegrid+1), (i+2)*picwidth/(sizegrid+1));
                GridPoint east = new GridPoint((j+2)*piclength/(sizegrid+1), (i+1)*picwidth/(sizegrid+1));

                //if not in first row or column draw to N, W, NW neighbors
                if(i != 0 && (j != 0))
                {
                    //get x and y of north, west, northwest neighbors
                    grid[i][j].addNeighbor(grid[i][j-1]); //west
                    grid[i][j].addNeighbor(grid[i-1][j-1]); //northwest
                    grid[i][j].addNeighbor(grid[i-1][j]); //north
                    //grid[i][j].addNeighbor()
                    if(i != sizegrid-1 && j !=sizegrid-1)
                    {
                        grid[i][j].addNeighbor(grid[i][j+1]);
                        grid[i][j].addNeighbor(grid[i+1][j+1]);
                        grid[i][j].addNeighbor(grid[i+1][j]);
                    }
                }

                //if is in first column of grid, neighbor north and to wall
                else if(j == 0)
                {
                    grid[i][j].addNeighbor(west);
                    grid[i][j].addNeighbor(northwest);

                    if(i == 0)
                    {
                        grid[i][j].addNeighbor(north);
                    }
                    else
                    {
                        //north neighbor
                        grid[i][j].addNeighbor(grid[i - 1][j]);
                    }
                    if(i != sizegrid-1 ) {
                        grid[i][j].addNeighbor(grid[i][j + 1]);
                        grid[i][j].addNeighbor(grid[i + 1][j + 1]);
                        grid[i][j].addNeighbor(grid[i + 1][j]);
                    }
                }

                //if its in the first row, draw to west neighbor and to wall
                else if(i == 0) {
                    grid[i][j].addNeighbor(grid[i][j - 1]); //west
                    grid[i][j].addNeighbor(northwest);
                    grid[i][j].addNeighbor(north);
                    if (j != sizegrid - 1) {
                        grid[i][j].addNeighbor(grid[i][j + 1]);
                        grid[i][j].addNeighbor(grid[i + 1][j + 1]);
                        grid[i][j].addNeighbor(grid[i + 1][j]);
                    }
                }

                //draw extra lines connect east and south to wall
                if(j == sizegrid - 1)
                {
                    if(i == 0)
                    {
                        grid[i][j].addNeighbor(northeast);
                    }
                    grid[i][j].addNeighbor(east);
                    grid[i][j].addNeighbor(southeast);
                    if(i!=sizegrid-1)
                    grid[i][j].addNeighbor(grid[i+1][j]);
                }

                if(i == sizegrid - 1)
                {
                    if(j != sizegrid -1 )
                        grid[i][j].addNeighbor(grid[i][j+1]);

                    grid[i][j].addNeighbor(southeast);
                    grid[i][j].addNeighbor(south);
                    if(j == 0)
                    {
                        grid[i][j].addNeighbor(southwest);
                    }
                }
            }
        }
    }

    public void paint(Graphics g)
    {
        super.paint(g);

        //put picture in background
        g.drawImage(background, 0, 0, this);

        //reset triangulation
        triangulation.clear();

        //draw handles and lines
        for(int i = 0; i < sizegrid; i++)
        {
            for(int j = 0; j< sizegrid; j++)
            {
                //draw handles
                g.setColor(Color.WHITE);
                ((Graphics2D)g).fill(handles[i][j]);
                grid[i][j].addNeighborLine(g);
            }
        }

        //color drag handle special
        if(dragrow > -1 && dragcol >-1)
        {
            g.setColor(Color.RED);
            ((Graphics2D)g).fill(handles[dragrow][dragcol]);
        }

        //testing triangles
        makeTriangles();
    }

    public void makeTriangles()
    {
       //first for every draggable point, draw triangles to N/NW
        for(int i = 0; i< sizegrid; i++)
        {
            for(int j = 0; j< sizegrid; j++)
            {
                //get W, NW, N neighbors
                GridPoint west, northwest, north;
                west = grid[i][j].getNeighbor(0);
                northwest = grid[i][j].getNeighbor(1);
                north = grid[i][j].getNeighbor(2);

                //make traingles
                TrianglePoints tri1 = new TrianglePoints(
                        grid[i][j].getCoordx(), grid[i][j].getCoordy(),
                        west.getCoordx(), west.getCoordy(),
                        northwest.getCoordx(), northwest.getCoordy());

                TrianglePoints tri2 = new TrianglePoints(
                        grid[i][j].getCoordx(), grid[i][j].getCoordy(),
                        northwest.getCoordx(), northwest.getCoordy(),
                        north.getCoordx(), north.getCoordy());

                triangulation.add(tri1);
                triangulation.add(tri2);
            }
        }

        //for points in last column make triangles to north, northeast
        int j = sizegrid-1;
        for(int i = 0; i < sizegrid; i++)
        {
            GridPoint north, northeast, east;
            north = grid[i][j].getNeighbor(2);
            northeast = grid[i][j].getNeighbor(3);
            east = grid[i][j].getNeighbor(4);

            //make traingles
            TrianglePoints tri1 = new TrianglePoints(
                    grid[i][j].getCoordx(), grid[i][j].getCoordy(),
                    north.getCoordx(), north.getCoordy(),
                    northeast.getCoordx(), northeast.getCoordy());

            TrianglePoints tri2 = new TrianglePoints(
                    grid[i][j].getCoordx(), grid[i][j].getCoordy(),
                    northeast.getCoordx(), northeast.getCoordy(),
                    east.getCoordx(), east.getCoordy());
            //special case first one
            if(i == 0)
            {
                GridPoint b = grid[i][j].getNeighbor(4);
                GridPoint c = grid[i][j].getNeighbor(5);
                TrianglePoints tri3 = new TrianglePoints(
                        grid[i][j].getCoordx(), grid[i][j].getCoordy(),
                        b.getCoordx(), b.getCoordy(),
                        c.getCoordx(), c.getCoordy());

                triangulation.add(tri3);

            }
            triangulation.add(tri1);
            triangulation.add(tri2);
        }


        //for points in last column make triangles to north, northeast
        int i = sizegrid-1;
        for(j = 0; j < sizegrid; j++)
        {
            GridPoint north, northeast, east;
            north = grid[i][j].getNeighbor(3);
            northeast = grid[i][j].getNeighbor(4);
            east = grid[i][j].getNeighbor(5);

            //make traingles
            TrianglePoints tri1 = new TrianglePoints(
                    grid[i][j].getCoordx(), grid[i][j].getCoordy(),
                    north.getCoordx(), north.getCoordy(),
                    northeast.getCoordx(), northeast.getCoordy());

            TrianglePoints tri2 = new TrianglePoints(
                    grid[i][j].getCoordx(), grid[i][j].getCoordy(),
                    northeast.getCoordx(), northeast.getCoordy(),
                    east.getCoordx(), east.getCoordy());
            //special case first one
            if(j == 0)
            {
                GridPoint a = grid[i][j].getNeighbor(5);
                GridPoint b = grid[i][j].getNeighbor(6);
                GridPoint c = grid[i][j].getNeighbor(0);
                TrianglePoints tri3 = new TrianglePoints(
                        grid[i][j].getCoordx(), grid[i][j].getCoordy(),
                        b.getCoordx(), b.getCoordy(),
                        c.getCoordx(), c.getCoordy());
                TrianglePoints tri4 = new TrianglePoints(
                        grid[i][j].getCoordx(), grid[i][j].getCoordy(),
                        b.getCoordx(), b.getCoordy(),
                        a.getCoordx(), a.getCoordy());
                triangulation.add(tri3);
                triangulation.add(tri4);
            }
            if(j == sizegrid-1)
            {
                GridPoint b = grid[i][j].getNeighbor(6);
                GridPoint c = grid[i][j].getNeighbor(5);
                TrianglePoints tri3 = new TrianglePoints(
                        grid[i][j].getCoordx(), grid[i][j].getCoordy(),
                        b.getCoordx(), b.getCoordy(),
                        c.getCoordx(), c.getCoordy());
                triangulation.add(tri3);
            }
            triangulation.add(tri1);
            triangulation.add(tri2);
        }
    }

    public void startDrag(int x, int y)
    {
			/* find which handle if any is trying to be dragged */
        for (int i = 0; i < sizegrid; i++) {
            for(int j = 0; j< sizegrid; j++) {
                if (handles[i][j].contains(x, y)) {
                    dragrow = i;
                    dragcol = j;
                    return;
                }
            }
        }
    }

    /* move the handle and repaint */
    public void doDrag(int x, int y) {
        /* only if a handle is being dragged */
        if (dragrow > -1 && dragcol > -1) {
            //only if it is within neighborhood
            grid[dragrow][dragcol].createNeighborhood();

            Polygon region = grid[dragrow][dragcol].getNeighborhood();

            if (region.contains(x, y)) {
                grid[dragrow][dragcol].setCoordx(x);
                grid[dragrow][dragcol].setCoordy(y);

                handles[dragrow][dragcol].setRect(x - 5, y - 5, 10, 10);
                repaint();

            }
        }
    }
    public int getDragrow(){return dragrow;}
    public int getDragcol(){return dragcol;}

    public ArrayList<TrianglePoints> getTriangulation() {
        return triangulation;
    }

    public void setDragrow(int x){dragrow = x;}
    public void setDragcol(int y){dragcol = y;}

    public void setBackground(BufferedImage background) {
        this.background = background;
    }


}
