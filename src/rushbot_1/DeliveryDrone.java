package rushbot_1;

import battlecode.common.*;

public class DeliveryDrone extends RobotPlayer {

    public static void runDeliveryDrone() throws GameActionException {
//        Team enemy = rc.getTeam().opponent();
//        if (!rc.isCurrentlyHoldingUnit()) {
//            // See if there are any enemy robots within capturing range
//            RobotInfo[] robots = rc.senseNearbyRobots(GameConstants.DELIVERY_DRONE_PICKUP_RADIUS_SQUARED, enemy);
//
//            if (robots.length > 0) {
//                // Pick up a first robot within range
//                rc.pickUpUnit(robots[0].getID());
//                System.out.println("I picked up " + robots[0].getID() + "!");
//            }
//        } else {
//            // No close robots, so search for robots within sight radius
//            tryMove(randomDirection());
//        }

        MapLocation cowloc = null;
        Team enemy = rc.getTeam().opponent();
        if (!rc.isCurrentlyHoldingUnit()) {
            // See if there are any enemy robots within capturing range
            RobotInfo[] robots = rc.senseNearbyRobots(GameConstants.DELIVERY_DRONE_PICKUP_RADIUS_SQUARED, enemy);
            if (robots.length > 0) {
                for (RobotInfo robot : robots) {
                    //check all in radius and check for any cows
                    if (robot.type == RobotType.COW) {
                        //if robot in list is a cow then mark location
                        cowloc = robot.location;
                    }
                }
                //make direction to cow so possible to move there
                Direction directions_to_cow = rc.getLocation().directionTo(cowloc);
                tryMove(directions_to_cow);
                // Pick up a first robot within range
                rc.pickUpUnit(robots[0].getID());
            }
        } else {
            // No close robots, so search for robots within sight radius
            tryMove(randomDirection());
        }
    }
}
