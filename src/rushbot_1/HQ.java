package rushbot_1;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotType;

public class HQ extends RobotPlayer {

    public static void runHQ() throws GameActionException {

        System.out.println("Number of miners: " + numMiners);
        System.out.println("Number of DesignSchools: " + numDesignSchools);

        if (rc.getRoundNum() == 1) {
            //System.out.println(rc.);
            Communications.sendHQLocation(rc.getLocation());

        }
        else if (numMiners < 5) {
            for (Direction dir : directions) {
                if (tryBuild(RobotType.MINER, dir)) {
                    numMiners++;
                }

            }
        }
    }
}
