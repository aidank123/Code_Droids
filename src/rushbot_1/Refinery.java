package rushbot_1;

import battlecode.common.GameActionException;

public class Refinery extends RobotPlayer {

    static void runRefinery() throws GameActionException {


        //step 1 set hq location
        if(hq_location == null){
            Communications.getHQLocation();
        } else if(enemy_hq_location == null){
            Communications.getEnemyHQLocation();
        }

    //CHECKS EVERY 20 ROUNDS
        if(rc.getRoundNum() % 20 == 4) {
            Communications.receiveCommands();
            //Communications.updateUnitCounts(20);
        }
        // System.out.println("Pollution: " + rc.sensePollution(rc.getLocation()));
    }
}
