package rushbot_1;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;

public class Pathing extends RobotPlayer {

    //pathing attempts to use a simulated annealing algorithm that increases temperature, and therefore how random
    //of a move is chosen, based upon how long a robot is stuck trying to move to a location.

    //heating and cooling goes between .8 and .9 using a heating factor of 1.01 and cooling of .99.

    //The decision (should)) also be based on a number of factors in
    //each of the neighboring tiles ie. visited, pollution levels.

    static double temperature = .85;
    static double heating_factor = 1.01;
    static double cooling_factor = .99;

    static Direction[] cool;
    static Direction[] getting_warmer;
    static Direction[] hot;
    static Direction[] spicy;

    //index of current best move
    static int index;

    public static boolean sickPathing(Direction dir) throws GameActionException {

        if (temperature < .75) {
            temperature = .75;
        } else if (temperature > .95) {
            temperature = .95;
        }
        System.out.println("Current pathing temperature: " + temperature);
        //Direction[] ice_cold = {dir};
        Direction ice_cold = dir;
        cool = new Direction[]{dir, dir.rotateLeft(), dir.rotateRight()};
        //Direction[] cool = new Direction[3];
        getting_warmer = new Direction[]{dir, dir.rotateLeft(), dir.rotateRight(), dir.rotateLeft().rotateLeft(), dir.rotateRight().rotateRight()};
        // Direction[] getting_warmer = new Direction[5];
        hot = new Direction[]{dir, dir.rotateLeft(), dir.rotateRight(), dir.rotateLeft().rotateLeft(), dir.rotateRight().rotateRight(), dir.opposite().rotateRight(), dir.opposite().rotateLeft()};
        //Direction[] hot = new Direction[7];
        spicy = new Direction[]{dir, dir.rotateLeft(), dir.rotateRight(), dir.rotateLeft().rotateLeft(), dir.rotateRight().rotateRight(), dir.opposite().rotateRight(), dir.opposite().rotateLeft(), dir.opposite()};
        //Direction[] spicy = new Direction[8];


        if (temperature <= .82) {
            if (tryMove(ice_cold)) {
                temperature *= cooling_factor;
                return true;
            } else {
                temperature *= heating_factor;
                return false;
            }
        } else if (temperature > .82 && temperature <= .84) {
            for (Direction d : cool) {

                while (cool.length != 0) {
                    d = returnMovestoTry(cool);

                    if (tryMove(d)) {

                        temperature *= cooling_factor;
                        return true;
                    } else {
                        cool = changeMoveList(cool,d);
                    }
                }

            }
            temperature *= heating_factor;
            return false;
        } else if (temperature > .84 && temperature <= .86) {
            for (Direction d : getting_warmer) {
                while (getting_warmer.length != 0) {
                    d = returnMovestoTry(getting_warmer);
                    getting_warmer = changeMoveList(getting_warmer,d);
                    System.out.println("Warmer " + d);
                    if (tryMove(d)) {

                        temperature *= cooling_factor;
                        return true;
                    }
                }
            }
            temperature *= heating_factor;
            return false;
        } else if (temperature > .86 && temperature <= .88) {
            for (Direction d : hot) {
                while (hot.length != 0) {
                    d = returnMovestoTry(hot);
                    hot = changeMoveList(hot,d);
                    if (tryMove(d)) {

                        temperature *= cooling_factor;
                        return true;
                    }
                }
            }
            temperature *= heating_factor;
            return false;
        } else if (temperature >= .88) {
            for (Direction d : spicy) {
                while (spicy.length != 0) {
                    d = returnMovestoTry(spicy);
                    spicy = changeMoveList(spicy,d);
                    if (tryMove(d)) {

                        temperature *= cooling_factor;
                        return true;
                    }
                }
            }
            temperature *= heating_factor;
            return false;
        }

        return false;
    }

    public static boolean sickPathing(MapLocation m) throws GameActionException {

        return sickPathing(curr_loc.directionTo(m));
    }

    public static double analyzeSpot(Direction d) throws GameActionException {

        int num_times_visited = 0;
        int pollution_level = 0;
        double return_value;

        MapLocation m = rc.adjacentLocation(d);

        if(visited.containsKey(m)) {
            num_times_visited = visited.get(m);
        }
        if(rc.canSenseLocation(m)) {
            pollution_level = rc.sensePollution(m);
        }

        System.out.println("This spot: " + m + " was visited: " + num_times_visited);
        return (pollution_level + num_times_visited * 4)/2;
    }

    public static Direction returnMovestoTry(Direction[] input) throws GameActionException {

        Direction[] new_list = new Direction[input.length - 1];
        double lowest_cost = 99999;
        Direction bestMove = input[0];
        int i = 0;
        index = 0;

        for (Direction d : input) {
            if (analyzeSpot(d) < lowest_cost) {
                lowest_cost = analyzeSpot(d);
                bestMove = d;
                index = i;
            }
            i++;
        }

        return bestMove;

    }

    public static Direction[] changeMoveList(Direction[] input, Direction bestMove){

        Direction[] new_list = new Direction[input.length - 1];
        int split = 0;
        for (int j = 0; j < input.length; j++) {

            //if the index of input is not equal to the best move, it removes it from the moves list
            if (j != index) {
                new_list[j - split] = input[j];
            } else {
                split = 1;
            }

        }

        return new_list;
    }
}
