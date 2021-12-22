import java.util.*;

public class ZombieCat extends Cat {
    private boolean alive;
    private int timeAlive = 0;
    public ZombieCat(int r, int c, City cty, Random rnd){
        super(r, c, cty, rnd);
        this.lab = LAB_RED;
        alive = true;
        timeAlive = 0;
    }

    @Override
    public void takeAction(){
        List<Creature> creatureList = city.getCreaturesAtLocation(getGridPoint());
        for (Creature c: creatureList){
            if(c.lab == LAB_BLUE || c.lab == LAB_YELLOW || c.lab == LAB_CYAN){
                c.killedOrLived();
                city.queueRmCreature(c);
                if (c.lab == LAB_YELLOW || c.lab == LAB_CYAN){
                    ZombieCat NewZC = new ZombieCat(this.getRow(), this.getCol(), this.city, rand);
                    city.queueAddCreature(NewZC);
                }
                this.timeAlive = 0;
            }
        }
        maybeChase();
        if (lab == LAB_RED){
            maybeTurn();
        }
        return;
    }

    public void maybeChase(){
        List<Creature> creatureList = city.getCreatures();
        this.lab = LAB_RED;
        Creature closestCreature = null;
        int closestValue = 40;
        for (Creature c: creatureList){
            if ((this.dist(c) <= closestValue && c.lab == LAB_BLUE) || (this.dist(c) <= closestValue && c.lab == LAB_YELLOW) || (this.dist(c) <= closestValue && c.lab == LAB_CYAN)){
                this.lab = LAB_BLACK;
                closestValue = this.dist(c);
                closestCreature = c;
            }
        }
        if (closestCreature != null){
            GridPoint destination = closestCreature.getGridPoint();
            boolean north = false;
            boolean east = false;
            boolean south = false;
            boolean west = false;

            if(destination.row < this.getRow()){
                north = true;
            }
            else if(destination.col > this.getCol()){
                east = true;
            }
            else if(destination.row > this.getRow()){
                south = true;
            }
            else if(destination.col < this.getCol()){
                west = true;
            }

            if (north && !east && !west){
                changeToDirection(0);
            }

            if (north && east){
                if (this.getRow() - destination.row > destination.col - this.getCol()){
                    changeToDirection(0);
                }
                if (this.getRow() - destination.row < destination.col - this.getCol()){
                    changeToDirection(1);
                }
            }

            if (east && !north && !south){
                changeToDirection(1);
            }

            if (east && south){
                if (destination.col - this.getCol() > destination.row - this.getRow()){
                    changeToDirection(1);
                }
                if (destination.col - this.getCol() < destination.row - this.getRow()){
                    changeToDirection(2);
                }
            }

            if (south && !east && !west){
                changeToDirection(2);
            }

            if (south && west){
                if (destination.row - this.getRow() > this.getCol() - destination.col){
                    changeToDirection(2);
                }
                if (destination.row - this.getRow() < this.getCol() - destination.col){
                    changeToDirection(3);
                }
            }

            if (west && !north && !south){
                changeToDirection(3);
            }

            if (west && north){
                if (this.getCol() - destination.col > this.getRow() - destination.row){
                    changeToDirection(3);
                }
                if (this.getCol() - destination.col < this.getRow() - destination.row){
                    changeToDirection(0);
                }
            }
        }
        return;
    }

    @Override  
    public void step(){

        city.removeFromHashmap(this);

        int row = getRow();
        int col = getCol();
        
        if (getDirection() == 0){
            if(row == 0 || row == 1 || row == 3){
                row = row + 80;
            }
            setGridPoint(row-3, col);
        }
        if (getDirection() == 1){
            if(col == 79 || col == 78 || col == 77){
                col = col - 80;
            }
            setGridPoint(row, col+3);
        }
        if (getDirection() == 2){
            if(row == 79 || row == 78 || row == 77){
                row = row - 80;
            }
            setGridPoint(row+3, col);
        }
        if (getDirection() == 3){
            if(col == 0 || col == 1 || col == 2){
                col = col + 80;
            }
            setGridPoint(row, col-3);
        }
        city.addToHashmap(this);
        return;
    }
    
    @Override
    public boolean die(){
        this.timeAlive++;
        if(this.timeAlive == 200){
            return true;
        }
        else if(!alive){
            return true;
        }
        else{
            return false;
        }
    }
}
