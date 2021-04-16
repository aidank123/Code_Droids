package rushbot_1;

import battlecode.common.GameActionException;

public class Vaporator extends RobotPlayer {

    public static void runVaporator() throws GameActionException {

        //step 1 set hq location
        if(hq_location == null){
            Communications.getHQLocation();
            setEnemy_hq_location();
        } else if(enemy_hq_location == null){
            Communications.getEnemyHQLocation();
        }


    }

}
