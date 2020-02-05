import java.awt.*;
import java.util.ArrayList;

public class GridPoint {

    int coordx, coordy;
    Color fill;

    ArrayList<GridPoint> neighbors;
    Polygon neighborhood;
    public GridPoint(){};

    public GridPoint(int x, int y)
    {
        coordx = x;
        coordy = y;
        fill = Color.WHITE;
        neighbors = new ArrayList<>();

    }

    public void addNeighbor(GridPoint n)
    {
        neighbors.add(n);
    }

    public void addNeighborLine(Graphics g)
    {
        for(int i = 0; i< neighbors.size(); i++)
        {
            g.setColor(Color.WHITE);
            g.drawLine(coordx, coordy, (neighbors.get(i).getCoordx()),(neighbors.get(i).getCoordy()));
        }
    }

    public void createNeighborhood()
    {
        int xneigh[] = new int[neighbors.size()];
        int yneigh[] = new int[neighbors.size()];
        for(int i = 0; i< neighbors.size(); i++)
        {
            xneigh[i] = neighbors.get(i).getCoordx();
            yneigh[i] = neighbors.get(i).getCoordy();
        }

        neighborhood = new Polygon(xneigh,yneigh,neighbors.size());
    }

    //setters
    public void setCoordx(int x){coordx = x; }
    public void setCoordy(int y){coordy = y; }

    //getters
    public int getCoordx(){return coordx;}
    public int getCoordy(){return coordy;}
    public GridPoint getNeighbor(int n) {return neighbors.get(n);}

    public Polygon getNeighborhood() {
        return neighborhood;
    }
}
