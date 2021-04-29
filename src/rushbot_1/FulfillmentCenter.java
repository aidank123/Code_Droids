package rushbot_1;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotType;

public class FulfillmentCenter extends RobotPlayer {

    public static void runFulfillmentCenter() throws GameActionException {


        //step 1 set hq location
        if(hq_location == null){
            Communications.getHQLocation();
        } else if(enemy_hq_location == null){
            Communications.getEnemyHQLocation();
        }
        //CHECKS FOR COMMANDS AND UPDATES EVERY 20 ROUNDS
        if(rc.getRoundNum() % 20 == 2) {
            Communications.receiveCommands();
            Communications.receiveUpdates();
        }

        if(CURRENT_HQ_COMMAND == EARLY_GAME_RUSH){
            if(numDrones < 5){
                for (Direction dir : directions)
                    if (rc.canBuildRobot((RobotType.DELIVERY_DRONE),dir) && rc.getTeamSoup() >= (RobotType.DELIVERY_DRONE.cost + message_cost)) {
                        if (tryBuild(RobotType.DELIVERY_DRONE, dir)) {
                            Communications.sendDroneCreation(rc.adjacentLocation(dir));
                        }
            }
        }

            }

    }

}
