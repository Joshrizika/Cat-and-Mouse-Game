import java.util.*;

public class City{


    //Determine the City Grid based on the size of the Plotter
    public static final int WIDTH = 80;
    public static final int HEIGHT = 80;

    //Different names, same result
    public static final int MAX_COL = WIDTH;
    public static final int MAX_ROW = HEIGHT;

    
    // The Grid World for your reference
    //
    //        (x)
    //        columns
    //        0 1 2 3 4 5 ... WIDTH (MAX_COL)
    //       .----------------...
    // (y)r 0|           ,--column (x)
    //    o 1|      * (1,3) 
    //    w 2|         ^    
    //      3|         '-row (y)
    //      .|
    //      .|
    //      .|       
    //HEIGHT :
    //MAX_ROW:
    //


    //IMPORTANT! The grid world is a torus. Whenn a a point goes off
    //an edge, it wrapps around to the other side. So with a width of
    //of 80, a point at (79,5) would move to (0,5) next if it moved
    //one space down. Similarly, with a height of 80, a point
    //at (5,0) would move to (5,79) if it moved one space left.


    //-------------------------------------
    //The simulation's Data Structures
    //
    private List<Creature> creatures; //list of all creatues
    //Map of GridPoint to a list of cratures whose location is that grid point 
    private HashMap<GridPoint,List<Creature>> creatureGrid; 
    
    private Queue<Creature> rmQueue; //creatures that are staged for removal
    private Queue<Creature> addQueue; //creatures taht are staged to be added    

    //... YES! you must use all of these collections.
    //... YES! you may add others if you need, but you MUST use these too!
    

    //Random instance
    private Random rand;

    //Note, for Level 4, you may need to change this constructors arguments.
    public City(Random rand, int numMice, int numCats, int numZombieCats) {
        this.rand = rand;
        this.creatures = new LinkedList<Creature>();
        this.creatureGrid = new HashMap<GridPoint,List<Creature>>();
        this.rmQueue = new LinkedList<Creature>();
        this.addQueue = new LinkedList<Creature>();

        for (int i = 0; i < numMice; i++){
            this.queueAddCreature(new Mouse(rand.nextInt(City.MAX_ROW), rand.nextInt(City.MAX_COL), this, rand));
        }
        for (int i = 0; i < numCats; i++){
            this.queueAddCreature(new Cat(rand.nextInt(City.MAX_ROW), rand.nextInt(City.MAX_COL), this, rand));
        }
        for (int i = 0; i < numZombieCats; i++){
            this.queueAddCreature(new ZombieCat(rand.nextInt(City.MAX_ROW), rand.nextInt(City.MAX_COL), this, rand));
        }
        
    }



    //Return the current number of creatures in the simulation
    public int numCreatures(){
        return creatures.size();
    }

    
    // Because we'll be iterating of the Creature List we can't remove
    // items from the list until after that iteration is
    // complete. Instead, we will queue (or stage) removals and
    // additions.
    //
    // I gave yout the two methods for adding, but you'll need to
    // implementing the clearing.

    //stage a create to be removed
    public void queueRmCreature(Creature c){
        //DO NOT EDIT
        rmQueue.add(c);
    }

    //Stage a creature to be added
    public void queueAddCreature(Creature c){
        //DO NOT EDIT
        addQueue.add(c);
    }
    
    //Clear the queue of creatures staged for removal and addition
    public void clearQueue(){
        //TODO
        for (Creature c : addQueue){
            creatures.add(c);
            List<Creature> sharedCreatures = new LinkedList<Creature>();
            if (creatureGrid.get(c.getGridPoint()) == null){
                sharedCreatures.add(c);
            }
            else{
                sharedCreatures = creatureGrid.get(c.getGridPoint());
                sharedCreatures.add(c);
            }
            creatureGrid.put(c.getGridPoint(), sharedCreatures);
        }
        addQueue.clear();
        addQueue = new LinkedList<Creature>();
        Creature cur;
        Iterator<Creature> iterator = rmQueue.iterator();

        while(iterator.hasNext()){
            cur = (Creature)iterator.next();
            creatures.remove(cur);
            List<Creature> sharedCreatures = creatureGrid.get(cur.getGridPoint());
            if (sharedCreatures == null){
                continue;
            }
            sharedCreatures.remove(cur);
            creatureGrid.put(cur.getGridPoint(), sharedCreatures);
        }
        this.rmQueue.clear();
        // rmQueue = new LinkedList<Creature>();
        return;

        //Clear the queues by either adding creatures to the
        //simulation or removing them.

    }


    //TODO -- there are a number of other member methods you'll want
    //to write here to interact with creatures. This is a good thing
    //to think about when putting together your UML diagram

    public List<Creature> getCreaturesAtLocation(GridPoint g){
        return creatureGrid.get(g);
    }

    public List<Creature> getCreatures(){
        return creatures;
    }

    public void removeFromHashmap(Creature c){
        List<Creature> creatureList = this.getCreaturesAtLocation(c.getGridPoint());
        creatureList.remove(c);
        creatureGrid.replace(c.getGridPoint(), creatureList);
    }

    public void addToHashmap(Creature c){
        List<Creature> creatureList = new LinkedList<Creature>();
        if(creatureGrid.get(c.getGridPoint()) == null){
            creatureList.add(c);
        }
        else{
            creatureList = creatureGrid.get(c.getGridPoint());
            creatureList.add(c);
        }
        creatureGrid.put(c.getGridPoint(), creatureList);
    }
    

    // This is the simulate method that is called in Simulator.java
    // 
    //You need to realize in your Creature class (and decendents) this
    //functionality so that they work properly. Read through these
    //comments so it's clear you understand.
    public void simulate() {
        //DO NOT EDIT!
        
        //You get this one for free, but you need to review this to
        //understand how to implement your various creatures

        //First, for all creatures ...
        for(Creature c : creatures){
            //check to see if any creature should die
            if(c.die()){
                queueRmCreature(c); //stage that creature for removal
                continue;
            }
            
            //for all remaining creatures take a step
            //this could involve chasing another creature
            //or running away from a creature
            c.step();
        }

        //Clear queue of any removes or adds of creatures due to creature death
        clearQueue(); 

        
        //For every creature determine if an action should be taken
        // such as, procreating (mice), eating (cats, zombiecats), or
        // some new action that you'll add to the system.
        for(Creature c : creatures){
            c.takeAction(); 
        }

        //Clear queue of any removes or adds following actions, such
        //as a mouse that was eaten or a cat that was was removed due
        //to being turned into a zombie cat.
        clearQueue();

        //Output all the locations of the creatures.
        for(Creature c : creatures){
            System.out.println(c);
        }

    }
}
