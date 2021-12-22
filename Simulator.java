import java.util.Random;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Simulator {
	
    public static void main(String[] args) {

        //This is the arguments for running your program so you can test with different settings
        final String USAGE = "java Simulator numMice numCats numZombieCats rounds [randSeed] [--DEBUG]";
        //note that [ ] is to indicate an optional argument. You do not include [ ] when using this argument.
        //When using the --DEBUG flag, you must set a random seed.
        
        boolean DEBUG = false;
        
        //parse arguments
        if(args.length < 4){
            System.out.println("ERROR: missing arguments");
            System.out.println(USAGE);
            System.exit(1);
        }
        int numMice = Integer.parseInt(args[0]);
        int numCats = Integer.parseInt(args[1]);
        int numZombieCats = Integer.parseInt(args[2]);
        int rounds = Integer.parseInt(args[3]);

        Random rand;
        if(args.length > 4)
            rand = new Random(Integer.parseInt(args[4]));
        else
            rand = new Random(100);

        if(args.length > 5 && args[5].equals("--DEBUG")){
            DEBUG=true;
        }

        //Initialize a city with mice, cats, and zombie cats
        City city= new City(rand,numMice,numCats,numZombieCats);
        //Note for Level 4 you may need to change your constructors arguments.
        
        int count = 0;

        city.queueAddCreature(new Thanos(rand.nextInt(City.MAX_COL), rand.nextInt(City.MAX_ROW), city, rand));

        city.queueAddCreature(new InfinityStone(rand.nextInt(City.MAX_COL), rand.nextInt(City.MAX_ROW), city, rand, Creature.LAB_PINK));
        city.queueAddCreature(new InfinityStone(rand.nextInt(City.MAX_COL), rand.nextInt(City.MAX_ROW), city, rand, Creature.LAB_ORANGE));
        city.queueAddCreature(new InfinityStone(rand.nextInt(City.MAX_COL), rand.nextInt(City.MAX_ROW), city, rand, Creature.LAB_GREEN));
        city.queueAddCreature(new InfinityStone(rand.nextInt(City.MAX_COL), rand.nextInt(City.MAX_ROW), city, rand, Creature.LAB_PINK));
        city.queueAddCreature(new InfinityStone(rand.nextInt(City.MAX_COL), rand.nextInt(City.MAX_ROW), city, rand, Creature.LAB_ORANGE));
        city.queueAddCreature(new InfinityStone(rand.nextInt(City.MAX_COL), rand.nextInt(City.MAX_ROW), city, rand, Creature.LAB_GREEN));

        while (count < rounds) {
            count++;

            if (count%100 == 0){
                city.queueAddCreature(new Mouse(rand.nextInt(City.MAX_COL), rand.nextInt(City.MAX_ROW), city, rand));
            }

            if (count%25 == 0){
                city.queueAddCreature(new Cat(rand.nextInt(City.MAX_COL), rand.nextInt(City.MAX_ROW), city, rand));
            }

            //TODO: You'll eventually need to complete functionality
            //for adding mice and cats per round, but start small and
            //build up.
            
            city.simulate();

            System.out.println("done "+count);
            System.out.flush();

            if(DEBUG){
                System.err.print("Enter anything to continue: ");
                try{
                    (new BufferedReader(new InputStreamReader(System.in))).readLine();
                }catch(Exception e){
                    System.exit(1);
                }
            }
            
        }
    }
}
