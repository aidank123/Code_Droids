package rushbot_1;

import battlecode.common.*;

public class NetGun extends RobotPlayer {

    public static void runNetGun() throws GameActionException {
        Team enemy = rc.getTeam().opponent();

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
            //I think NetGun is built by miners

        }
        //RobotInfo[] robots = rc.senseNearbyRobots(GameConstants.NET_GUN_SHOOT_RADIUS_SQUARED, enemy);
        //for(RobotInfo robot: robots){
            //if(robot.type == RobotType.DELIVERY_DRONE){
                //rc.shootUnit(robot.getID());
            //}
        //}
        //I think this would be the method to sense and shoot robots
        }
    }

