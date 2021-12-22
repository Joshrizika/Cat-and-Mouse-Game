import java.util.*;

public class Cat extends Creature {
    private int timeAlive = 0;
    private boolean alive;
    public Cat(int r, int c, City cty, Random rnd){
        super(r, c, cty, rnd);
        this.lab = LAB_YELLOW;
        alive = true;
        timeAlive = 0;

    }

    @Override
    public void maybeTurn(){
        if(rand.nextInt(20) == 0){
            changeDirection();
        }
        return;
    }

    @Override
    public void takeAction(){
        List<Creature> creatureList = city.getCreaturesAtLocation(getGridPoint());
        for (Creature c: creatureList){
            System.out.println(c);
            if(c.lab == LAB_BLUE){
                c.killedOrLived();
                city.queueRmCreature(c);
                this.timeAlive = 0;
            }
        }
        maybeChase();
        if (lab == LAB_YELLOW){
            maybeTurn();
        }
        return;
    }

    public void maybeChase(){
        List<Creature> creatureList = city.getCreatures();
        this.lab = LAB_YELLOW;
        Creature closestCreature = null;
        int closestValue = 20;
        for (Creature c: creatureList){
            // System.out.println(this.dist(c));
            if (this.dist(c) <= closestValue && c.lab == LAB_BLUE){
                this.lab = LAB_CYAN;
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
            // System.out.println("North: " + north + " East: " + east + " South: " + south + " West: " + west);

            if (north && !east && !west){
                // System.out.println("Accessed North");
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
                // System.out.println("Accessed East");
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
                // System.out.println("Accessed South");
                changeToDirection(2);
            }

            if (south && west){
                System.out.println("Accessed Southwest");
                if (destination.row - this.getRow() > this.getCol() - destination.col){
                    changeToDirection(2);
                }
                if (destination.row - this.getRow() < this.getCol() - destination.col){
                    changeToDirection(3);
                }
            }

            if (west && !north && !south){
                // System.out.println("Accessed West");
                changeToDirection(3);
            }

            if (west && north){
                System.out.println("Accessed NorthWest");
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
    public boolean die(){
        this.timeAlive++;
        if(this.timeAlive == 100){
            ZombieCat NewZC = new ZombieCat(this.getRow(), this.getCol(), this.city, rand);
            city.queueAddCreature(NewZC);
            return true;
        }
        else if(!alive){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void step(){

        city.removeFromHashmap(this);

        int row = getRow();
        int col = getCol();
        
        if (getDirection() == 0){
            if(row == 0 || row == 1){
                row = row + 80;
            }
            setGridPoint(row-2, col);
        }
        if (getDirection() == 1){
            if(col == 79 || col == 78){
                col = col - 80;
            }
            setGridPoint(row, col+2);
        }
        if (getDirection() == 2){
            if(row == 79 || row == 78){
                row = row - 80;
            }
            setGridPoint(row+2, col);
        }
        if (getDirection() == 3){
            if(col == 0 || col == 1){
                col = col + 80;
            }
            setGridPoint(row, col-2);
        }
        city.addToHashmap(this);
        return;
    }

    @Override
    public void killedOrLived(){
        alive = false;
    }
    
}
