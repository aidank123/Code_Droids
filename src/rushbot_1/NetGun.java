package rushbot_1;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotType;

public class NetGun extends RobotPlayer {

    public static void runNetGun() throws GameActionException {


        //step 1 set hq location
        if(hq_location == null){
            Communications.getHQLocation();
        } else if(enemy_hq_location == null){
            Communications.getEnemyHQLocation();
        }
    //CHECKS EVERY 5 ROUNDS
        if(rc.getRoundNum() % 5 == 0) {
                //Communications.updateUnitCounts(5);
        }
        for (Direction dir : directions) {
            if (tryBuild(RobotType.NET_GUN, dir)) {

            }
        }
    }
}
