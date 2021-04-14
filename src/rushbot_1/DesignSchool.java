package rushbot_1;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotType;

public class DesignSchool extends RobotPlayer {

    public static void runDesignSchool() throws GameActionException {

        if (!broadcastedCreation) {
            broadcastDesignSchoolCreation(rc.getLocation());
        }
        for (Direction dir : directions) {
            if (tryBuild(RobotType.LANDSCAPER, dir)) {
                numLandscapers++;

            }
        }
    }
}
