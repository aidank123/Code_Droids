package rushbot_1;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotType;

public class DesignSchool extends RobotPlayer {

    public static void runDesignSchool() throws GameActionException {

        //step 1 set hq location
        if(hq_location == null){
            Communications.getHQLocation();
            setEnemy_hq_location();
        }

        for (Direction dir : directions) {
            if (tryBuild(RobotType.LANDSCAPER, dir)) {
                numLandscapers++;

            }
        }
    }
}
