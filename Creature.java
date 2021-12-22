import java.util.*;

public abstract class Creature {
  
    public final static int NORTH = 0;
    public final static int EAST = 1;
    public final static int SOUTH = 2;
    public final static int WEST = 3;
    public final static int NUM_DIRS = 4;
    public final static int[] DIRS = {NORTH,EAST,SOUTH,WEST};

    //The following are the color labels available to creatures
    public final static char LAB_BLACK='k';
    public final static char LAB_BLUE='b';
    public final static char LAB_RED='r';
    public final static char LAB_YELLOW='y';
    public final static char LAB_ORANGE='o';
    public final static char LAB_PINK='p';
    public final static char LAB_MAGENTA='m';
    public final static char LAB_CYAN='c';
    public final static char LAB_GREEN='g';
    public final static char LAB_GRAY='e';


    //current direction facing
    private int dir;

    //current point in grid
    private GridPoint point;

    //current color label for the point
    protected char lab;

    //random instance
    protected Random rand;

    //City in which this creature lives so that it can update it's
    //location and get other information it might need (like the
    //location of other creatures) when making decisions about which
    //direction to move and what not.
    protected City city;


    public Creature(int r, int c, City cty, Random rnd){
        point = new GridPoint(r,c);
        city = cty;
        rand = rnd;
        lab = LAB_GRAY;
        dir = rand.nextInt(NUM_DIRS);
    }
 
    public abstract void maybeTurn(); //randomnly turn if suppose to
    public abstract void takeAction(); //take an action based on a location in the city
    public abstract boolean die(); //return true if should die, false otherwise
    public abstract void step(); //take a step in the given direction in the city


    public void changeDirection(){
        dir = rand.nextInt(NUM_DIRS);
    }

    public void changeToDirection(int direction){
        dir = direction;
        return;
    }

    public int getDirection(){
        return dir;
    }

    public abstract void killedOrLived();

    public int getRow(){
        return point.row;
    }
    public int getCol(){
        return point.col;
    }
    public GridPoint getGridPoint(){
        return new GridPoint(point); //return a copy to preseve
                                     //encapsulation
    }
    public void setGridPoint(int r, int c){
        this.point = new GridPoint(r, c);
        return;
    }

    public int dist(Creature c){
        return point.dist(c.getGridPoint());
    }

    public String toString() {
        //output in col,row format or (x,y) format
        return ""+this.point.col+" "+this.point.row+" "+lab;
    }


}
