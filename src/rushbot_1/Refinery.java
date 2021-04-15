package rushbot_1;

import battlecode.common.GameActionException;

public class Refinery extends RobotPlayer {

    static void runRefinery() throws GameActionException {

        //step 1 set hq location
        if(hq_location == null){
            Communications.getHQLocation();
            setEnemy_hq_location();
        }

        // System.out.println("Pollution: " + rc.sensePollution(rc.getLocation()));
    }
}
