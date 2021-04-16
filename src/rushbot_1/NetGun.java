package rushbot_1;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotType;

public class NetGun extends RobotPlayer {

    public static void runNetGun() throws GameActionException {

        //step 1 set hq location
        if(hq_location == null){
            Communications.getHQLocation();
            setEnemy_hq_location();
        } else if(enemy_hq_location == null){
            Communications.getEnemyHQLocation();
        }

        for (Direction dir : directions) {
            if (tryBuild(RobotType.NET_GUN, dir)) {

            }
        }
    }
}
