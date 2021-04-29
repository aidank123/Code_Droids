package rushbot_1;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;

public class Pathing extends RobotPlayer{

    //pathing attempts to use a simulated annealing algorithm that increases temperature, and therefore how random
    //of a move is chosen, based upon how long a robot is stuck trying to move to a location.

    //heating and cooling goes between .8 and .9 using a heating factor of 1.01 and cooling of .99.

    //The decision (should)) also be based on a number of factors in
    //each of the neighboring tiles ie. visited, pollution levels.

    static double temperature = .85;
    static double heating_factor = 1.01;
    static double cooling_factor = .99;


    public static boolean sickPathing(Direction dir) throws GameActionException  {

        if(temperature < .75) {
            temperature = .75;
        }else if(temperature > .95){
            temperature = .95;
        }
        System.out.println("Current pathing temperature: " + temperature);
        Direction[] ice_cold = {dir};
        Direction[] cool = {dir, dir.rotateLeft(), dir.rotateRight()};
        Direction[] getting_warmer = {dir, dir.rotateLeft(), dir.rotateRight(), dir.rotateLeft().rotateLeft(),dir.rotateRight().rotateRight()};
        Direction[] hot = {dir, dir.rotateLeft(), dir.rotateRight(), dir.rotateLeft().rotateLeft(),dir.rotateRight().rotateRight(), dir.opposite().rotateRight(), dir.opposite().rotateLeft()};
        Direction[] spicy = {dir, dir.rotateLeft(), dir.rotateRight(), dir.rotateLeft().rotateLeft(),dir.rotateRight().rotateRight(), dir.opposite().rotateRight(), dir.opposite().rotateLeft(), dir.opposite()};

       if(temperature <= .82){

           for (Direction d : ice_cold){
               if(tryMove(d)){

                   temperature *= cooling_factor;
                   return true;
               }else{

               }
           }
           temperature *= heating_factor;
           return false;
       } else if(temperature > .82 && temperature <= .84){
           for (Direction d : cool){
               if(tryMove(d)){

                   temperature *= cooling_factor;
                   return true;
               }else{

               }
           }
           temperature *= heating_factor;
            return false;
       } else if(temperature > .84 && temperature <= .86){
           for (Direction d : getting_warmer){
               if(tryMove(d)){

                   temperature *= cooling_factor;
                   return true;
               }else{

               }
           }
           temperature *= heating_factor;
            return false;
       } else if(temperature > .86 && temperature <= .88){
           for (Direction d : hot){
               if(tryMove()){

                   temperature *= cooling_factor;
                   return true;
               }else{

               }
           }
            temperature *= heating_factor;
            return false;
       } else if(temperature >= .88){
           for (Direction d : spicy){
               if(tryMove(d)){
                   temperature *= cooling_factor;
                   return true;
               }else{

               }
           }
           temperature *= heating_factor;
            return false;
       }

    return false;
    }

    public static boolean sickPathing (MapLocation m) throws GameActionException {

        return sickPathing(curr_loc.directionTo(m));
    }
}
