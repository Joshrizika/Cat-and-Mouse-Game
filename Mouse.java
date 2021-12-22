import java.util.*;

public class Mouse extends Creature {
    private int timeAlive;
    private boolean alive;
    public Mouse(int r, int c, City cty, Random rnd){
        super(r, c, cty, rnd);
        this.lab = LAB_BLUE;
        alive = true;
        timeAlive = 0;
    }

    @Override
    public void maybeTurn(){
        if(rand.nextInt(5) == 0){
            changeDirection();
        }
        return;
    }

    @Override
    public void takeAction(){
        if (timeAlive == 20){
            Mouse babyMouse = new Mouse(this.getRow(), this.getCol(), this.city, rand);
            city.queueAddCreature(babyMouse);
        }
        this.maybeTurn();
        return;
    }

    @Override
    public boolean die(){
        
        if(timeAlive == 100){
            return true;
        }
        else if (!alive){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void step(){
        
        city.removeFromHashmap(this);
        
        timeAlive++;

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
