package rushbot_1;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;

public class Landscaper extends RobotPlayer {

    public static void runLandscaper() throws GameActionException {
        if(rc.getDirtCarrying() == 0){
            tryDig();
        }
        if(hq_location != null){
            for(Direction dir : directions){
                MapLocation tileToCheck = hq_location.add(dir);
                if(rc.getLocation().distanceSquaredTo(tileToCheck) < 4 && rc.canDepositDirt(rc.getLocation().directionTo(tileToCheck))){
                    rc.depositDirt(rc.getLocation().directionTo(tileToCheck));
                    System.out.println("building a wall");
                }
            }
        }
        tryMove(randomDirection());


    }
}
