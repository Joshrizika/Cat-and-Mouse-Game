import java.util.*;

public class InfinityStone extends Creature{
    private boolean alive;
    public InfinityStone(int r, int c, City cty, Random rnd, char lab){
        super(r, c, cty, rnd);
        this.lab = lab;
        alive = true;

    }
    @Override
    public void maybeTurn(){
        return;
    }
    @Override
    public void takeAction(){
        return;
    }
    @Override
    public boolean die(){
        if (!alive){
            return true;
        }
        else{
            return false;
        }
    }
    @Override
    public void step(){
        return;
    }
    @Override
    public void killedOrLived(){
        alive = false;
    }
}
