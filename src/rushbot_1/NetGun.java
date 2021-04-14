package rushbot_1;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotType;

public class NetGun extends RobotPlayer {

    public static void runNetGun() throws GameActionException {
        for (Direction dir : directions) {
            if (tryBuild(RobotType.NET_GUN, dir)) {

            }
        }
    }
}
