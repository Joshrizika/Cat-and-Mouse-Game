import java.util.*;

public class Thanos extends Creature{
    private int timeAlive;
    private boolean alive;
    private int numInfinityStones = 0;
    private boolean step = false;
    public Thanos(int r, int c, City cty, Random rnd){
        super(r, c, cty, rnd);
        this.lab = LAB_MAGENTA;
        timeAlive = 0;
        alive = true;

    }
    @Override
    public void maybeTurn(){
        if(rand.nextInt(10) == 0){
            changeDirection();
        }
        return;
    }

    public void goToCenter(){
        if (this.getRow() > 40){
            changeToDirection(0);
            return;
        }
        else if (this.getRow() < 40){
            changeToDirection(2);
            return;
        }
        if (this.getRow() == 40 && this.getCol() < 40){
            changeToDirection(1);
            return;
        }
        else if (this.getRow() == 40 && this.getCol() > 40){
            changeToDirection(3);
            return;
        }
    }
    @Override
    public void takeAction(){
        List<Creature> creatureList = city.getCreaturesAtLocation(getGridPoint());
        for (Creature c: creatureList){
            // System.out.println(c);
            if(c.lab == LAB_PINK || c.lab == LAB_ORANGE || c.lab == LAB_GREEN){
                c.killedOrLived();
                city.queueRmCreature(c);
                numInfinityStones++;
                this.timeAlive = 0;
            }
        }
        maybeChase();
        if (numInfinityStones == 6){
            goToCenter();
            if (this.getRow() == 40 && this.getCol() == 40){
                Snap();
                city.queueAddCreature(new InfinityStone(rand.nextInt(City.MAX_COL), rand.nextInt(City.MAX_ROW), city, rand, Creature.LAB_PINK));
                city.queueAddCreature(new InfinityStone(rand.nextInt(City.MAX_COL), rand.nextInt(City.MAX_ROW), city, rand, Creature.LAB_ORANGE));
                city.queueAddCreature(new InfinityStone(rand.nextInt(City.MAX_COL), rand.nextInt(City.MAX_ROW), city, rand, Creature.LAB_GREEN));
                city.queueAddCreature(new InfinityStone(rand.nextInt(City.MAX_COL), rand.nextInt(City.MAX_ROW), city, rand, Creature.LAB_PINK));
                city.queueAddCreature(new InfinityStone(rand.nextInt(City.MAX_COL), rand.nextInt(City.MAX_ROW), city, rand, Creature.LAB_ORANGE));
                city.queueAddCreature(new InfinityStone(rand.nextInt(City.MAX_COL), rand.nextInt(City.MAX_ROW), city, rand, Creature.LAB_GREEN));
            }
        }
        return;
    }

    public void Snap(){
        List<Creature> creatureList = city.getCreatures();
        for (Creature c: creatureList){
            int liveOrDie = rand.nextInt(2);
            if (liveOrDie == 0 && c.lab != LAB_MAGENTA){
                c.killedOrLived();
                c.lab = LAB_GRAY;
                city.queueRmCreature(c);
            }
            else{
                continue;
            }
        }
        numInfinityStones = 0;
        return;
    }

    public void maybeChase(){
        List<Creature> creatureList = city.getCreatures();
        Creature closestCreature = null;
        int closestValue = 160;
        for (Creature c: creatureList){
            // System.out.println(this.dist(c));
            if (this.dist(c) <= closestValue && (c.lab == LAB_PINK || c.lab == LAB_ORANGE || c.lab == LAB_GREEN)){
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
                // System.out.println("Accessed NorthWest");
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
        timeAlive++;
        return false;
    }
    @Override
    public void step(){
        step = !step;
        if (step){
            return;
        }
        
        city.removeFromHashmap(this);
        
        timeAlive++;
        // System.out.println(timeAlive);

        int row = this.getRow();
        int col = this.getCol();
        
        if (getDirection() == 0){
            if(row == 0){
                row = City.MAX_ROW+1;
            }
            setGridPoint(row-1, col);
        }
        if (getDirection() == 1){
            if(col == City.MAX_COL-1){
                col = -1;
            }
            setGridPoint(row, col+1);
        }
        if (getDirection() == 2){
            if(row == City.MAX_ROW-1){
                row = -1;
            }
            setGridPoint(row+1, col);
        }
        if (getDirection() == 3){
            if(col == 0){
                col = City.MAX_COL+1;
            }
            setGridPoint(row, col-1);
        }

        city.addToHashmap(this);
        return;
    }
    @Override
    public void killedOrLived(){
        alive = false;
    }
}
