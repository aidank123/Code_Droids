package rushbot_1;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;

public class Landscaper extends RobotPlayer {

    public static void runLandscaper() throws GameActionException {



        //step 1 set hq location
        if(hq_location == null){
            Communications.getHQLocation();
        } else if(enemy_hq_location == null){
            Communications.getEnemyHQLocation();
        }
        //CHECKS EVERY OTHER ROUND ON ODD NUMBERS
        if(rc.getRoundNum() % 2 == 1) {
            Communications.receiveCommands();
            //Communications.updateUnitCounts(10);
        }
        //if not carrying dirt, pickup dirt

        if(rc.getDirtCarrying() == 0){
            tryDig();
        }

        if(hq_location != null){
            MapLocation bestPlaceToBuildWall = null;
            int lowestElevation = 9999;
            for(Direction dir : directions){
                MapLocation tileToCheck = hq_location.add(dir);
                if(curr_loc.distanceSquaredTo(tileToCheck) < 4
                        && rc.canDepositDirt(curr_loc.directionTo(tileToCheck))){
                    if(rc.senseElevation(tileToCheck) < lowestElevation) {
                        lowestElevation = rc.senseElevation(tileToCheck);
                        bestPlaceToBuildWall = tileToCheck;
                    }
                }
            }
            if(Math.random() < 0.5) {
                if (bestPlaceToBuildWall != null) {
                    rc.depositDirt(curr_loc.directionTo(bestPlaceToBuildWall));
                    System.out.println("building a wall");
                }
            }
        }
        if(hq_location != null){
            goTo(hq_location);
        }else{
            tryMove(randomDirection());
        }


    }
}
