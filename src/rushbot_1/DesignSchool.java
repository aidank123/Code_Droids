package rushbot_1;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotType;

public class DesignSchool extends RobotPlayer {

    public static void runDesignSchool() throws GameActionException {

        //step 1 set hq location
//        if(hq_location == null){
//            Communications.getHQLocation();
//        }else if(enemy_hq_location == null){
//            Communications.getEnemyHQLocation();
//        }
        //CHECKS EVERY 20 ROUNDS
        if(rc.getRoundNum() % 20 == 2) {
            Communications.receiveCommands();
            //Communications.updateUnitCounts(20);
        }

        if(CURRENT_HQ_COMMAND == EARLY_GAME_RUSH){
        for (Direction dir : directions) {
            if (rc.canBuildRobot((RobotType.LANDSCAPER),dir) && rc.getTeamSoup() >= (RobotType.LANDSCAPER.cost + message_cost) && numLandscapers <= 6) {
                if (tryBuild(RobotType.LANDSCAPER, dir)) {
                Communications.sendLandscaperCreation(rc.adjacentLocation(dir));
                numLandscapers++;
            }
            }
        }
    } else if(CURRENT_HQ_COMMAND == DEFEND_AND_BUILD){


        }
    }
}
