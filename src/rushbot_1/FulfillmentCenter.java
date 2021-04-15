package rushbot_1;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotType;

public class FulfillmentCenter extends RobotPlayer {

    public static void runFulfillmentCenter() throws GameActionException {

        //step 1 set hq location
        if(hq_location == null){
            Communications.getHQLocation();
            setEnemy_hq_location();
        }

        for (Direction dir : directions)
            if (tryBuild(RobotType.DELIVERY_DRONE, dir)) {
                numDrones++;
            }

    }

}
