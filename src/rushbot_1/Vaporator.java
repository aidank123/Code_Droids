package rushbot_1;

import battlecode.common.GameActionException;

public class Vaporator extends RobotPlayer {

    public static void runVaporator() throws GameActionException {


        //step 1 set hq location
        if(hq_location == null){
            Communications.getHQLocation();
        } else if(enemy_hq_location == null){
            Communications.getEnemyHQLocation();
        }
        //CHECKS EVERY 20 ROUNDS
        if(rc.getRoundNum() % 20 == 6) {
            Communications.receiveCommands();
            //Communications.updateUnitCounts(20);
        }

    }

}
